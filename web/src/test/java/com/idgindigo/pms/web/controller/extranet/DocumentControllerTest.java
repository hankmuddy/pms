package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.CodeDto;
import com.idgindigo.pms.domain.extranet.Document;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToPhoto;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.repository.extranet.DocumentRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToPhotoRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.utils.AuthenticationProvider;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.PhotoProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import mockit.Deencapsulation;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.Document.DocType.SCAN;
import static com.idgindigo.pms.web.controller.extranet.DocumentController.RoomTypeToDocumentDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 6:21 PM
 */
public class DocumentControllerTest extends InMemoryDbWebTest {

    @Inject
    private DocumentRepository documentRepository;
    @Inject
    private RoomTypeToPhotoRepository rttpRepository;
    @Inject
    private RoomTypeProvider rtProvider;
    @Inject
    private PhotoProvider provider;
    @Inject
    private HotelProvider hotelProvider;
    @Inject
    private DocumentController documentController;
    @Inject
    private AuthenticationProvider authenticationProvider;

    @Test
    public void test() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "CV.jpg", "jpg", "byteSource".getBytes());
        SecurityUtils.authenticateUser(authenticationProvider.hotelUser.getPersistentEntity(new Visitor<HotelUser>() {
            @Override
            public void visit(HotelUser entity) {
                entity.setHotel(hotelProvider.getPersistentEntity(new Visitor<Hotel>() {
                    @Override
                    public void visit(Hotel entity) {
                        entity.setTenantId("wbk");
                    }
                }));
            }
        }), userProvider.getPersistentEntity());
        File dir = new File("dir");
        if (!dir.exists()) dir.mkdir();
        Deencapsulation.setField(documentController, "imagesFilePath", dir.getAbsolutePath());
        String key = upload(file);
        File wbkDir = DocumentController.getHotelImagesDirectory("wbk");
        new File(wbkDir, key).delete();
    }

    private String upload(MockMultipartFile file) throws Exception {
        int initialSize = documentRepository.findAll().size();
        String content = mvc.perform(fileUpload(getUrl() + DocumentController.UPLOAD_URL)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("type", String.valueOf(SCAN))
                .session(session))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject responseEntity = objectMapper.readValue(content, JSONObject.class);
        String key = (String) responseEntity.get("content");
        Assert.assertTrue(documentRepository.findAll().size() == initialSize + 1, "The file was not saved in the DB");
        return key;
    }

    @Test
    public void testFindDocuments() throws Exception {
        rttpRepository.deleteAll();
        RoomType rt1 = rtProvider.getFirst();
        RoomType rt2 = rtProvider.getByIndex(1);
        Document rtp1 = provider.getPersistentEntity();
        Document rtp2 = provider.getPersistentEntity();
        Document rtp3 = provider.getPersistentEntity();
        Document rtp4 = provider.getPersistentEntity();
        Document rtp5 = provider.getPersistentEntity();
        rttpRepository.saveAndFlush(new RoomTypeToPhoto(rt1, rtp1));
        rttpRepository.saveAndFlush(new RoomTypeToPhoto(rt1, rtp2));
        rttpRepository.saveAndFlush(new RoomTypeToPhoto(rt1, rtp3));

        assertFacilities(rt1.getId(), Arrays.asList(rtp1.getAccessKey(), rtp2.getAccessKey(), rtp3.getAccessKey()));
        assertFacilities(rt2.getId(), Collections.<String>emptyList());
    }

    @Test
    public void testSaveFacilities() throws Exception {
        rttpRepository.deleteAll();
        RoomType rt1 = rtProvider.getFirst();
        RoomType rt2 = rtProvider.getByIndex(1);
        Document rtp1 = provider.getFirst();
        Document rtp2 = provider.getPersistentEntity();
        Document rtp3 = provider.getPersistentEntity();
        Document rtp4 = provider.getPersistentEntity();
        Document rtp5 = provider.getPersistentEntity();
        doPut(rt1, rtp1.getAccessKey(), rtp2.getAccessKey(), rtp3.getAccessKey());
        doPut(rt2, rtp3.getAccessKey(), rtp4.getAccessKey(), rtp5.getAccessKey());
        assertPhotos(rttpRepository.findDtosByRoomType(rt1.getId()), rtp1.getAccessKey(), rtp2.getAccessKey(), rtp3.getAccessKey());
        assertPhotos(rttpRepository.findDtosByRoomType(rt2.getId()), rtp3.getAccessKey(), rtp4.getAccessKey(), rtp5.getAccessKey());
        doPut(rt1, rtp1.getAccessKey(), rtp3.getAccessKey(), rtp5.getAccessKey());
        doPut(rt2, rtp2.getAccessKey(), rtp4.getAccessKey());
        assertPhotos(rttpRepository.findDtosByRoomType(rt1.getId()), rtp1.getAccessKey(), rtp3.getAccessKey(), rtp5.getAccessKey());
        assertPhotos(rttpRepository.findDtosByRoomType(rt2.getId()), rtp2.getAccessKey(), rtp4.getAccessKey());
    }

    @Test
    public void testFindAllPhotos() throws Exception {
        rttpRepository.deleteAll();
        provider.getRepository().deleteAll();
        RoomType rt1 = rtProvider.getFirst();
        RoomType rt2 = rtProvider.getByIndex(1);
        Document rtp1 = provider.getFirst();
        Document rtp2 = provider.getPersistentEntity();
        Document rtp3 = provider.getPersistentEntity();
        Document rtp4 = provider.getPersistentEntity();
        Document rtp5 = provider.getPersistentEntity();
        doPut(rt1, rtp1.getAccessKey(), rtp2.getAccessKey(), rtp3.getAccessKey());
        doPut(rt2, rtp3.getAccessKey(), rtp4.getAccessKey(), rtp5.getAccessKey());
        assertPhotos(getEntityList("photos", CodeDto.class), rtp1.getAccessKey(), rtp2.getAccessKey(), rtp3.getAccessKey(), rtp4.getAccessKey(), rtp5.getAccessKey());
    }

    private void doPut(RoomType roomType, String... facilities) throws Exception {
        Assert.assertEquals(mvc.perform(
                buildRequest(put(getUrl() + "bind"), new RoomTypeToDocumentDto(new RoomType(roomType.getId()), Arrays.asList(facilities)))
        ).andReturn().getResponse().getStatus(), HttpStatus.CREATED.value());
    }

    private void assertFacilities(Long id, Collection<String> expected) throws Exception {
        List<CodeDto> result = getEntityList("byRoomType/" + id, CodeDto.class);
        assertPhotos(result, expected.toArray(new String[expected.size()]));
    }

    private static void assertPhotos(List<CodeDto> result, String... expectedCodes) {
        Assert.assertEquals(result.size(), expectedCodes.length);
        List<String> c = Arrays.asList(expectedCodes);
        for (CodeDto codeDto : result) {
            Assert.assertTrue(c.contains(codeDto.getCode()));
        }
    }

    @Override
    protected String getUrl() {
        return DocumentController.URL + "/";
    }
}
