package com.idgindigo.pms.web.controller.pms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.restutils.exception.ImportException;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.web.controller.ResponseEntity;
import com.idgindigo.pms.web.controller.extranet.DocumentController;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 5/21/14 1:01 PM
 */
@Controller
@RequestMapping(ImportController.URL)
public class ImportController {
    public static final String URL = "import";
    @Inject
    private AdultRepository repository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity performImport(@RequestParam("file") MultipartFile file, @RequestParam("struct") String struct) throws IOException {
        for (Row row : getSheet(file)) {
            createAdult(new AdultParser(row, new ObjectMapper().<Map<String, Integer>>readValue(struct, new TypeReference<HashMap<String, Integer>>() {
            })));
        }
        return new ResponseEntity();
    }

    private Sheet getSheet(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String[] split = DocumentController.DOT.split(name);
        String extension = split[split.length - 1];

        InputStream stream = new ByteArrayInputStream(file.getBytes());

        try {
            switch (extension) {
                case "xls":
                    return new HSSFWorkbook(stream).getSheetAt(0);
                case "xlsx":
                    return new XSSFWorkbook(stream).getSheetAt(0);
                default:
                    throw new ImportException(ImportException.UNSUPPORTED_FILE_TYPE);
            }
        } catch (IOException e) {
            throw new ImportException(ImportException.FILE_CORRUPTED);
        }
    }

    private void createAdult(AdultParser parser) {
        Adult adult = new Adult();
        adult.setFirstName(parser.get("firstName"));
        adult.setLastName(parser.get("lastName"));
        adult.setPatronymic(parser.get("patronymic"));
        adult.setDob(parseDate(parser.get("dob")));
        adult.setIdentity(parser.get("identity"));
        adult.setAddress(parser.get("address"));
        adult.setPhone(parser.get("phone"));
        adult.setEmail(parser.get("email"));
        adult.setPassportNumber(parser.get("passportNumber"));
        adult.setPassportValidTill(parser.get("passportValidTill"));
        adult.setPassportIssued(parser.get("passportIssued"));
        adult.setCio(parser.get("cio"));
        repository.save(adult);
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, ChannelService.BOOKING_FORMATTER);
        } catch (Exception e) {
            throw new ImportException(ImportException.INVALID_DATE_FORMAT);
        }
    }

    @AllArgsConstructor
    private class AdultParser {
        private Row row;
        private Map<String, Integer> mapping;

        public String get(String field) {
            if (!mapping.containsKey(field) || mapping.get(field) == null) {
                return null;
            }
            return getCell(field).getStringCellValue();
        }

        private Cell getCell(String field) {
            Cell cell = row.getCell(getCol(field));
            if (cell == null) {
                throw new ImportException(ImportException.INVALID_MAPPING, field);
            }
            return cell;
        }

        private int getCol(String field) {
            return mapping.get(field) - 1;
        }
    }
}
