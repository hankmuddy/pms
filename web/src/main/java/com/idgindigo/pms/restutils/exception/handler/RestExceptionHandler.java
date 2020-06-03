package com.idgindigo.pms.restutils.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.idgindigo.pms.channel.wubook.WubookException;
import com.idgindigo.pms.restutils.exception.ApproveException;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.restutils.exception.RoomNotAvailableException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.extranet.MailService;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.BatchUpdateException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author vomel
 * @since 29.10.13 13:00
 */
@ControllerAdvice
public class RestExceptionHandler {
    @Inject
    private MailService mailService;
    public static final String UNIQUE_CONSTRAINT_CODE = "23505";
    public static final String FOREIGN_KEY_INSERT_UPDATE_CONSTRAINT_CODE = "23503";
    public static final String FOREIGN_KEY_DELETE_CONSTRAINT_CODE = "23504";
    public static final String CHECK_CONSTRAINT = "23514";
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
    public static final String LEFT_PART = "Key [(]";
    private static final Pattern LEFT = Pattern.compile(LEFT_PART);
    public static final String RIGHT_PART = "[)]=";
    private static final Pattern RIGHT = Pattern.compile(RIGHT_PART);
    private static final Pattern POSTGRES_MESSAGE = Pattern.compile(".*" + LEFT_PART + ".*" + RIGHT_PART + ".*");

    private static final Map<String, String> messageToCode = new HashMap<String, String>() {{
        put("may not be null", RestFriendlyException.MAY_NOT_BE_NULL);
    }};

    @ExceptionHandler(RestFriendlyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ExceptionMessage> handleRestFriendlyException(RestFriendlyException ex) {
        return Collections.singletonList(new ExceptionMessage(ex.getMessage(), ex.getSource()));
    }

    @ExceptionHandler(ApproveException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ExceptionMessage> handleApproveException(ApproveException ex) {
        List<ExceptionMessage> messages = new ArrayList<>(ex.getSources().size());
        for (String source : ex.getSources()) {
            messages.add(new ExceptionMessage(ApproveException.MESSAGE, source));
        }
        return messages;
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ExceptionMessage> handleBookingException(RoomNotAvailableException ex) {
        return Collections.singletonList(new ExceptionMessage(RoomNotAvailableException.CODE, "room"));

    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEntityNotFoundException() {
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ExceptionMessage> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if (e.getCause().getCause() instanceof BatchUpdateException) {
            BatchUpdateException cause = (BatchUpdateException) e.getCause().getCause();
            String sqlState = cause.getSQLState();
            switch (sqlState) {
                case UNIQUE_CONSTRAINT_CODE:
                    return tryParseBatchUpdateException(cause);
                case FOREIGN_KEY_INSERT_UPDATE_CONSTRAINT_CODE:
                    return Collections.singletonList(new ExceptionMessage(RestFriendlyException.ENTITY_IS_USED));
                case CHECK_CONSTRAINT:
                    return Collections.singletonList(new ExceptionMessage(RestFriendlyException.INTEGRITY_VIOLATION));
            }
        }
        if (e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
            SQLIntegrityConstraintViolationException cause = (SQLIntegrityConstraintViolationException) e.getCause().getCause();
            if (cause.getSQLState().equals(FOREIGN_KEY_DELETE_CONSTRAINT_CODE)) {
                return Collections.singletonList(new ExceptionMessage(RestFriendlyException.ENTITY_IS_USED));
            }
        }
        return Collections.emptyList();
    }

    private List<ExceptionMessage> tryParseBatchUpdateException(BatchUpdateException cause) {
        String parseErrorMessage = "Couldn't parse UniqueConstraintViolation from {}";
        if (!(cause.getNextException() instanceof PSQLException)) {
            logger.error(parseErrorMessage, cause);
            return Collections.emptyList();
        }

        PSQLException sqlCause = (PSQLException) cause.getNextException();
        ServerErrorMessage message = sqlCause.getServerErrorMessage();
        if (!POSTGRES_MESSAGE.matcher(message.getDetail()).matches()) {
            logger.error(parseErrorMessage, cause);
            return Collections.emptyList();
        }
        String source = RIGHT.split(LEFT.split(message.getDetail())[1])[0];
        return Collections.singletonList(new ExceptionMessage(RestFriendlyException.DUPLICATE_ENTRY, source));
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ExceptionMessage> handleValidationException(RuntimeException ex) {
        List<ExceptionMessage> responseBody = new LinkedList<ExceptionMessage>();

        Throwable cause = ex;
        do {
            if (cause instanceof ConstraintViolationException) {
                parseConstraintViolation(responseBody, (ConstraintViolationException) cause);
                break;
            }
            cause = cause.getCause();
        } while (cause != null);
        if (responseBody.isEmpty()) {
            ExceptionMessage message = new ExceptionMessage(RestFriendlyException.UNKNOWN_ERROR);
            responseBody.add(message);
            logger.warn("{}", RestFriendlyException.UNKNOWN_ERROR);
        }
        return responseBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ExceptionMessage> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ExceptionMessage> responseBody = new LinkedList<ExceptionMessage>();
        parseConstraintViolation(responseBody, ex);
        return responseBody;
    }

    public void parseConstraintViolation(List<ExceptionMessage> responseBody, ConstraintViolationException cause) {
        for (ConstraintViolation eachViolation : cause.getConstraintViolations()) {
            responseBody.add(new ExceptionMessage(parseConstraintViolationMessage(eachViolation.getMessage()), eachViolation.getPropertyPath().toString()));
            logger.warn("{} : {}", eachViolation.getMessage(), eachViolation.getPropertyPath().toString());
        }
    }

    private String parseConstraintViolationMessage(String message) {
        return messageToCode.containsKey(message) ? messageToCode.get(message) : message;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ExceptionMessage> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ExceptionMessage message = new ExceptionMessage(RestFriendlyException.ATTACHMENT_EXCEED_MAX_SIZE, "file");
        return Collections.singletonList(message);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public List<ExceptionMessage> handleAccessDeniedException() {
        ExceptionMessage message = new ExceptionMessage("security.accessDenied");
        return Collections.singletonList(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ExceptionMessage> handleInvalidDataFormat(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            ExceptionMessage message = new ExceptionMessage(RestFriendlyException.INCORRECT_VALUE, cause.getValue().toString());
            return Collections.singletonList(message);
        }
        if (ex.getCause() instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException cause = (UnrecognizedPropertyException) ex.getCause();
            return Collections.singletonList(new ExceptionMessage(RestFriendlyException.UNRECOGNIZED_PROPERTY, cause.getPropertyName()));
        }
        if (ex.getCause() instanceof JsonMappingException) {
            JsonMappingException cause = (JsonMappingException) ex.getCause();
            if (!cause.getPath().isEmpty()) {
                return Collections.singletonList(new ExceptionMessage(RestFriendlyException.INCORRECT_VALUE, cause.getPath().get(0).getFieldName()));
            } else {
                return Collections.singletonList(new ExceptionMessage(RestFriendlyException.INCORRECT_VALUE, "type"));//Default behaviour handles inheritance mapping problems
            }
        }
        return Collections.singletonList(new ExceptionMessage(RestFriendlyException.REQUEST_NOT_PARSEABLE));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public List<ExceptionMessage> handleUnknownError(RuntimeException ex) {
        if ("Broken pipe".equalsIgnoreCase(getMostSpecificCause(ex))) {
            if (logger.isDebugEnabled()) logger.debug("{}:{}:Client has gone away", SecurityUtils.getCurrentTenantId(), SecurityUtils.getUsername(), ex);
        } else {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                Map<String, String[]> parameterMap = request.getParameterMap();
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    sb.append(entry.getKey()).append("=>").append(Arrays.deepToString(entry.getValue())).append("\n");
                }
                logger.error("Exception while processing request {} with method {} with parameters: {}", request.getRequestURI(), request.getMethod(), sb.toString());
            }
            String mess = String.valueOf(ex);
            if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) mess += ": " + ex.getStackTrace()[0].toString();
            String msg = String.format("An unknown error occurred for user %s of tenant %s. Details:%s ", SecurityUtils.getUsername(), SecurityUtils.getCurrentTenantId(), mess);
            logger.error(msg, ex);
            mailService.sendMail("vomel@idg.net.ua", "500!!!", msg, false);
            mailService.sendMail("valentin.vakatsiienko@idg.net.ua", "500!!!", msg, false);
            mailService.sendMail("xmityaz@idg.net.ua", "500!!!", msg, false);
        }
        return Collections.singletonList(new ExceptionMessage(RestFriendlyException.UNKNOWN_ERROR));
    }

    public String getMostSpecificCause(Throwable t) {
        Throwable rootCause = getRootCause(t);
        return rootCause != null ? rootCause.getMessage() : null;
    }

    public static Throwable getRootCause(Throwable t) {
        if (t == null) return null;
        if (t.getCause() == null) return t;
        return getRootCause(t.getCause());
    }

    @ExceptionHandler(WubookException.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    @ResponseBody
    public List<ExceptionMessage> handleWubookError(WubookException ex) {
        logger.error("Error communicating with WuBook", ex);
        return Collections.singletonList(new ExceptionMessage(RestFriendlyException.WUBOOK_ERROR, "(" + ex.getCode() + "): " + ex.getMessage()));
    }

    @ExceptionHandler(CannotAcquireLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public List<ExceptionMessage> handleCannotAcquireLock(CannotAcquireLockException ex) {
        return Collections.singletonList(new ExceptionMessage(RestFriendlyException.CONFLICT));
    }

    /*@ExceptionHandler(JpaSystemException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public List<ExceptionMessage> handleJpaSystemException(JpaSystemException ex) {
        Throwable cause = ex.getCause().getCause();
        if (cause instanceof PSQLException) {
            PSQLException exception = (PSQLException) cause;
        }
        return Collections.singletonList(new ExceptionMessage(RestFriendlyException.CONFLICT));
    }*/

}
