package com.idgindigo.pms.web.controller.pms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idgindigo.pms.restutils.exception.ImportException;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 5/21/14 1:25 PM
 */
public class ImportControllerTest extends InMemoryDbWebTest {
    @Inject
    private ImportController controller;

    @Test(dataProvider = "testImport")
    public void testImport(String fileName) throws IOException {
        MockMultipartFile file = getFile(fileName);
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 1);
        mapping.put("lastName", 2);

        controller.performImport(file, new ObjectMapper().writeValueAsString(mapping));
    }

    @DataProvider(name = "testImport")
    public Object[][] getData_testImport() {
        List<Object[]> result = new ArrayList<>(2);
        result.add(new Object[]{"xls.xls"});
        result.add(new Object[]{"xlsx.xlsx"});
        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testInvalidFile() throws IOException {
        MockMultipartFile file = getFile("invalid.xls");
        try {
            controller.performImport(file, new ObjectMapper().writeValueAsString(new HashMap<String, Integer>()));
            Assert.fail();
        } catch (ImportException e) {
            assertEquals(e.getMessage(), ImportException.FILE_CORRUPTED);
        }
    }

    public MockMultipartFile getFile(String name) throws IOException {
        byte[] bytes = IOUtils.toByteArray(new ClassPathResource(name).getInputStream());
        return new MockMultipartFile(name, name, "Excel", bytes);
    }

    @Override
    protected String getUrl() {
        return ImportController.URL;
    }
}
