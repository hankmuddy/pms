package com.idgindigo.pms.restutils.exception;

/**
 * @author vomel
 * @since 29.10.13 13:02
 */
public class RestFriendlyException extends RuntimeException {
    public static final String DUPLICATE_ENTRY = "constraint.duplicateEntry";
    public static final String WUBOOK_ERROR = "error.channels";
    public static final String ERROR_INVALID_FILTER_PARAMETERS = "filtering.invalidParameters";
    public static final String UNKNOWN_ERROR = "error.unknown";
    public static final String ATTACHMENT_MISSING = "document.attachmentMissing";
    public static final String ATTACHMENT_WRONG_TYPE = "document.attachmentWrongType";
    public static final String DOCUMENT_NOT_FOUND = "document.notFound";
    public static final String DOCUMENT_NOT_ALLOWED = "document.notAllowed";
    public static final String ATTACHMENT_EXCEED_MAX_SIZE = "document.maxUploadSizeExceed";
    public static final String ATTACHMENT_ALREADY_EXISTS = "constraint.document.attachmentAlreadyExists";
    public static final String CHANGE_PASSWORD_INVALID_PASSWORD = "changePassword.invalidPassword";
    public static final String CHANGE_PASSWORD_ACCESS_DENIED = "changePassword.accessDenied";
    public static final String CHANGE_PASSWORD_WRONG_OLD_PASSWORD = "changePassword.wrongOldPassword";
    public static final String INCORRECT_VALUE = "requestParsing.incorrectValue";
    public static final String UNRECOGNIZED_PROPERTY = "requestParsing.unrecognizedProperty";
    public static final String REQUEST_NOT_PARSEABLE = "requestParsing.requestCouldNotBeParsed";
    public static final String APPROVED_ENTITY_MODIFICATION = "modificationOfApprovedEntityForbidden";
    public static final String MAY_NOT_BE_NULL = "constraint.mayNotBeNull";
    public static final String ENTITY_IS_USED = "constraint.entityIsUsed";
    public static final String INTEGRITY_VIOLATION = "constraint.dataViolatesIntegrity";
    public static final String CONFLICT = "concurrentModification.conflict";
    public static final String MAX_ROOMS_EXCEEDED = "room.approve.maxRoomsExceeded";
    public static final String NON_APPROVED_DEPENDENCY = "nonApprovedDependency";
    public static final String WUBOOK_ALREADY_CONFIGURED = "wubookAlreadyConfigured";

    private String source;

    public RestFriendlyException(String code) {
        this(code, null);
    }

    public RestFriendlyException(String code, String source) {
        super(code);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
