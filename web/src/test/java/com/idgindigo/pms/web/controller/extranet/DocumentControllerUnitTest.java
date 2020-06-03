package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import org.springframework.mock.web.MockMultipartFile;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.Document.DocType;
import static com.idgindigo.pms.utils.EntityProvider.randomString;

/**
 * @author vomel
 * @since 17.03.14 14:41
 */
public class DocumentControllerUnitTest {

    @Test(dataProvider = "data")
    public void testGetExtension(String filename, DocType docType, String expected, String exceptionMessage) {
        try {
            String extension = DocumentController.getExtension(new MockMultipartFile(randomString(), filename, "ctype:" + randomString(), "U2".getBytes()), docType);
            if (exceptionMessage != null) {
                Assert.fail("should have failed with: " + exceptionMessage);
            }
            Assert.assertEquals(extension, expected);
        } catch (RestFriendlyException e) {
            Assert.assertEquals(e.getMessage(), exceptionMessage);
        }
    }

    @DataProvider(name = "data")
    public Object[][] getData() {
        List<Object[]> result = new ArrayList<>(8);
        result.add(new Object[]{null, DocType.PHOTO, null, "document.attachmentWrongType"});
        result.add(new Object[]{"", DocType.PHOTO, null, "document.attachmentWrongType"});
        result.add(new Object[]{"application/octet-stream", DocType.PHOTO, null, "document.attachmentWrongType"});
        result.add(new Object[]{"aaa.jpg", DocType.PHOTO, ".jpg", null});
        result.add(new Object[]{"aaa.gif", DocType.PHOTO, ".gif", null});
        result.add(new Object[]{"aaa.png", DocType.PHOTO, ".png", null});
        result.add(new Object[]{"aaa.tiff", DocType.PHOTO, null, "document.attachmentWrongType"});
        result.add(new Object[]{"aaa.jpg", DocType.LOGO, ".jpg", null});
        result.add(new Object[]{"aaa.gif", DocType.LOGO, ".gif", null});
        result.add(new Object[]{"aaa.png", DocType.LOGO, ".png", null});
        result.add(new Object[]{"aaa.tiff", DocType.LOGO, null, "document.attachmentWrongType"});
        result.add(new Object[]{"aaa.jpg", DocType.SCAN, ".jpg", null});
        result.add(new Object[]{"aaa.pdf", DocType.SCAN, ".pdf", null});
        result.add(new Object[]{"aaa.png", DocType.SCAN, null, "document.attachmentWrongType"});
        result.add(new Object[]{"aaa.gif", DocType.SCAN, null, "document.attachmentWrongType"});
        result.add(new Object[]{"aaa.tiff", DocType.SCAN, null, "document.attachmentWrongType"});
        return result.toArray(new Object[result.size()][]);
    }
}
