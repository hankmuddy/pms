package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.CodeDto;
import com.idgindigo.pms.domain.extranet.Document;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToPhoto;
import com.idgindigo.pms.repository.extranet.DocumentRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToPhotoRepository;
import com.idgindigo.pms.restutils.PageWithTotalCount;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.idgindigo.pms.domain.extranet.Document.DocType;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 6:07 PM
 */
@Controller
@RequestMapping(DocumentController.URL)
public class DocumentController {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + "document";
    public static final String NO_ATTACHMENT_MESSAGE = "error.fileUpload.attachmentMissing";
    public static final String DOCUMENT_NOT_FOUND_MESSAGE = "error.document.notFound";
    public static final String UPLOAD_URL = "upload";
    public static final Pattern DOT = Pattern.compile("\\.");
    public static final String SUCCESS_TRUE = "{\"success\":true}";
    @Inject
    private DocumentRepository documentRepository;
    @Inject
    private RoomTypeToPhotoRepository rttpRepository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private SecureRandom secureRandom;
    private static String imagesFilePath;
    private static String imagesUrlPrefix;
    private static final Map<DocType, List<String>> ALLOWED_TYPES = new HashMap<DocType, List<String>>() {{
        put(DocType.PHOTO, Arrays.asList("jpg", "jpeg", "png", "gif"));
        put(DocType.LOGO, Arrays.asList("jpg", "jpeg", "png", "gif"));
        put(DocType.SCAN, Arrays.asList("jpg", "pdf"));
    }};

    @Value("${imagesFilePath}")
    public void setImagesFilePath(String ifp) {
        imagesFilePath = ifp;//NOSONAR
    }

    @Value("${imagesUrlPrefix}")
    public void setImagesUrlPrefix(String iup) {
        imagesUrlPrefix = iup;//NOSONAR
    }

    @RequestMapping(value = UPLOAD_URL, method = RequestMethod.POST, params = "type!=DOC")
    @ResponseBody
    public ResponseEntity<String> handlePhotoUpload(@RequestParam("file") MultipartFile file, @RequestParam("type") DocType type) throws IOException {
        Document photo = storePhoto(file, type);
        return new ResponseEntity<>(photo.getAccessKey());
    }

    private Document storePhoto(MultipartFile file, DocType type) throws IOException {
        if (file == null) {
            throw new RestFriendlyException(NO_ATTACHMENT_MESSAGE, "file");
        }
        Document photo = new Document(type);
        photo.setContentType(file.getContentType());
        String ext = getExtension(file, type);
        String tenantId = SecurityUtils.getHotel().getTenantId();
        do {
            photo.setAccessKey(tenantId + new BigInteger(130, secureRandom).toString(32) + ext);
        } while (documentRepository.existsByKey(photo.getAccessKey()));

        String currentTenantId = SecurityUtils.getCurrentTenantId();
        if (currentTenantId == null) {
            throw new RestFriendlyException(RestFriendlyException.DOCUMENT_NOT_ALLOWED, "currentUser");
        }
        File dest = new File(getHotelImagesDirectory(currentTenantId), photo.getAccessKey());
        FileCopyUtils.copy(file.getBytes(), dest);

        return documentRepository.save(photo);
    }

    public static File getHotelImagesDirectory(String currentTenantId) {
        File extDir = new File(imagesFilePath);
        Assert.isTrue(extDir.exists(), "images path does not exist at: " + imagesFilePath);
        Assert.isTrue(extDir.isDirectory(), "images path is not directory at: " + imagesFilePath);
        File hotelImagesDir = new File(extDir, currentTenantId);
        if (!hotelImagesDir.exists()) hotelImagesDir.mkdir();
        return hotelImagesDir;
    }

    static String getExtension(MultipartFile attachment, DocType docType) {
        if (StringUtils.isBlank(attachment.getOriginalFilename())) {
            throw new RestFriendlyException(RestFriendlyException.ATTACHMENT_WRONG_TYPE);
        }
        String[] type = DOT.split(attachment.getOriginalFilename());
        String extension = type[type.length - 1];
        List<String> types = ALLOWED_TYPES.get(docType);
        if (types == null || !types.contains(extension.toLowerCase())) throw new RestFriendlyException(RestFriendlyException.ATTACHMENT_WRONG_TYPE);
        return "." + extension;
    }

    @RequestMapping(value = "photo/{key}.{type}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deletePhoto(@PathVariable("key") String key, @PathVariable("type") String type, ServletResponse response) throws IOException {
        doDeletePhoto(key + "." + type);
        emptyResponse(response);
    }

    public static void emptyResponse(ServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        os.print(SUCCESS_TRUE);
        os.close();
    }

    private void doDeletePhoto(String key) {
        Document photo = validateByKey(key, documentRepository);
        rttpRepository.deleteByKey(key);
        documentRepository.delete(photo);
        File dest = new File(getHotelImagesDirectory(SecurityUtils.getCurrentTenantId()), photo.getAccessKey());
        dest.delete();
    }

    private static Document validateByKey(String key, DocumentRepository repository) {
        Document document = repository.findByAccessKey(key);
        if (document == null) {
            throw new RestFriendlyException(DOCUMENT_NOT_FOUND_MESSAGE, "key");
        }
        return document;
    }

    @RequestMapping("photos")
    @ResponseBody
    public ResponseEntity<List<CodeDto>> getAllPhotoCodes() {
        List<CodeDto> list = documentRepository.findAllByType(DocType.PHOTO);
        return new ResponseEntity<>(list, new PageWithTotalCount(0, list.isEmpty() ? 1 : list.size(), 0));
    }

    @RequestMapping("byRoomType/{id}")
    @ResponseBody
    public ResponseEntity<List<CodeDto>> getPhotoCodesByRoomType(@PathVariable("id") Long id) {
        List<CodeDto> list = rttpRepository.findDtosByRoomType(id);
        return new ResponseEntity<>(list, new PageWithTotalCount(0, list.isEmpty() ? 1 : list.size(), 0));
    }

    @RequestMapping(value = "bind", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Transactional
    public ResponseEntity<?> bindPhotoCodesToRoomType(@RequestBody RoomTypeToDocumentDto entity) {
        if (entity.getRoomType().getId() == null || !roomTypeRepository.exists(entity.getRoomType().getId())) throw new EntityNotFoundException();
        List<String> existing = rttpRepository.findByRoomType(entity.getRoomType().getId());
        Collection<RoomTypeToPhoto> added = new ArrayList<>();
        for (String code : entity.getCodes()) {
            if (existing.contains(code)) {
                existing.remove(code);
            } else {
                added.add(new RoomTypeToPhoto(entity.getRoomType(), documentRepository.findByAccessKey(code)));
            }
        }
        rttpRepository.save(added);
        if (!existing.isEmpty()) {
            rttpRepository.removeSelected(entity.getRoomType(), existing);
        }
        return new ResponseEntity<>();
    }

    public static String getImagesUrlPrefix() {
        return imagesUrlPrefix + "/" + SecurityUtils.getCurrentTenantId() + "/";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomTypeToDocumentDto {
        private RoomType roomType;
        private List<String> codes;
    }

}