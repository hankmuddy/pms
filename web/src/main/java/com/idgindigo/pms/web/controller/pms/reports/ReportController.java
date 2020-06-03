package com.idgindigo.pms.web.controller.pms.reports;

import com.idgindigo.pms.logins.repository.HotelInfoRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.GroupMemberToRoomUseRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.repository.pms.SimpleServiceUseRepository;
import com.idgindigo.pms.repository.pms.PaymentRepository;
import com.idgindigo.pms.repository.pms.RefundRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.service.admin.SettingsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author vomel
 * @since 24.03.14 14:26
 */
@Controller
@RequestMapping(ReportController.URL)
public class ReportController {
    public static final String URL = "/report";
    public static final String PDF = "application/pdf";
    public static final String INLINE = "inline; filename*=UTF-8''%s";
    public static final String CONTENT_DISP = "Content-disposition";

    @Inject
    protected HotelInfoRepository hotelInfoRepository;
    @Inject
    protected AdultRepository adultRepository;
    @Inject
    protected RoomTypeRepository roomTypeRepository;
    @Inject
    protected BankDetailsRepository bankDetailsRepository;
    @Inject
    protected BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    protected LivingUseRepository livingUseRepository;
    @Inject
    protected SimpleServiceUseRepository simpleServiceUseRepository;
    @Inject
    protected RoomUseRepository roomUseRepository;
    @Inject
    protected RoomRepository roomRepository;
    @Inject
    protected RefundRepository refundRepository;
    @Inject
    protected GroupMemberToRoomUseRepository groupMemberToRoomUseRepository;
    @Inject
    protected BillRepository billRepository;
    @Inject
    protected PaymentRepository paymentRepository;
    @Inject
    protected SettingsService settingsService;

    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    protected LocalDate TodayDate = LocalDate.now(DateTimeZone.UTC);

    protected Locale UserLocale = LocaleContextHolder.getLocale();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class Files {
        private String jasper;
        private String pdf;
    }

    public Map<String, Map<String, Files>> ReportMap = new HashMap<String, Map<String,Files>>() {{
        put("DEF", new HashMap<String, Files>() {{
            put("breakfast",             new Files("Breakfast.jasper",              "Справка по питанию.pdf"));
            put("creditAccountOrder",    new Files("CreditAccountOrderRu.jasper",   "Приходный кассовый ордер КО-1 (РФ).pdf"));
            put("dateStat",              new Files("DateStat.jasper",               "Статистика по датам.pdf"));
            put("folio",                 new Files("folioRu.jasper",                "Деталировка услуг по заявке.pdf"));
            put("foreignGuestBook",      new Files("ForeignGuestBookRu.jasper",     "Журнал регистрации иностранных граждан (РФ).pdf"));
            put("guestBook",             new Files("GuestBookRu.jasper",            "Журнал учета граждан.pdf"));
            put("guestCard",             new Files("GuestCardRu.jasper",            "Карта гостя (РФ).pdf"));
            put("guestForm",             new Files("GuestFormRu.jasper",            "Анкета (РФ).pdf"));
            put("freeRooms",             new Files("FreeRooms.jasper",              "Свободные номера за период.pdf"));
            put("hotelBill",             new Files("HotelBillRu.jasper",            "Счет гостиницы (РФ).pdf"));
            put("hotelLoad",             new Files("HotelLoad.jasper",              "Справка по загрузке.pdf"));
            put("income",                new Files("Income.jasper",                 "Справка по заездам.pdf"));
            put("invoice",               new Files("InvoiceRu.jasper",              "Счет-фактура (РФ).pdf"));
            put("receptionCash",         new Files("ReceptionCash.jasper",          "Кассовый отчет рецепции.pdf"));
            put("roomLoadMap",           new Files("RoomLoadMap.jasper",            "Карта занятости номерного фонда.pdf"));
        }});
        put("UA", new HashMap<String, Files>() {{
            put("bookingRegBook",        new Files("BookingRegBook.jasper",         "Журнал регистрации заявок.pdf"));
            put("hotelBill",             new Files("HotelBill.jasper",              "Счет гостиницы.pdf"));
            put("creditAccountOrder",    new Files("CreditAccountOrderUa.jasper",   "Прибутковий касовый ордер.pdf"));
            put("debitAccountOrder",     new Files("DebitAccountOrderUa.jasper",    "Видатковий касовый ордер.pdf"));
            put("folio",                 new Files("folioUa.jasper",                "Деталювання послуг за заявкою.pdf"));
            put("foreignGuestBook",      new Files("ForeignGuestBookUa.jasper",     "Журнал обліку іноземних громадян.pdf"));
            put("guestBook",             new Files("GuestBookUa.jasper",            "Журнал обліку громадян.pdf"));
            put("guestCard",             new Files("GuestCardUa.jasper",            "Реєстраційна карта гостя.pdf"));
            put("guestForm",             new Files("GuestFormUa.jasper",            "Анкета проживаючого.pdf"));
            put("hotelCard",             new Files("HotelCardUa.jasper",            "Візитна карта - Hotel Card.pdf"));
            put("roomTypeSales",         new Files("RoomTypeSalesUa.jasper",        "Відомість реализації за категоріями номерів.pdf"));
        }});
        put("EN", new HashMap<String, Files>() {{
            put("folio",                 new Files("folioEn.jasper",                "Detailed services.pdf"));
        }});
    }};

    public String getReport(String report, String ext) {
        String reportFile = "";
        if ("jasper".equals(ext)) {
            reportFile = ReportMap.get(getHotelCountry()) != null && ReportMap.get(getHotelCountry()).get(report) != null ? ReportMap.get(getHotelCountry()).get(report).getJasper() : ReportMap.get("DEF").get(report).getJasper();
        }
        if ("pdf".equals(ext)) {
            reportFile = ReportMap.get(getHotelCountry()) != null && ReportMap.get(getHotelCountry()).get(report) != null ? ReportMap.get(getHotelCountry()).get(report).getPdf() : ReportMap.get("DEF").get(report).getPdf();
        }
        return reportFile;
    }

    protected static void putOrEmpty(Map<String, Object> parameters, String key, Object value) {
        value = value != null && !"".equals(value) ? String.valueOf(value) : "";
        parameters.put(key, value);
    }

    public String lang(String key) {
        return messageSource.getMessage(key, new Object[0], key, UserLocale);
    }

    protected String getHotelCountry() {
        return settingsService.getHotelInfo() == null || settingsService.getHotelInfo().getCountry() == null ? "" : settingsService.getHotelInfo().getCountry();
    }

    protected String getHotelLogo() {
        return settingsService.getHotelInfo() == null || settingsService.getHotelInfo().getLogo() == null ? "reportPmsLogo.png" : settingsService.getHotelInfo().getLogo();
    }

    private static String imgPath;
    @Value("${imagesFilePath}")
    public void setPath (String path) { imgPath = path + "/" + getHotelLogo(); }

    protected String toDouble(Float sum) {
        return String.format("%.02f", sum);
    }

    protected String toMoney(Float sum) {
        return String.format("%.02f", sum / 100);
    }

    @AllArgsConstructor
    abstract static class ReportCreator {
        private HttpServletResponse response;
        private String jasperReportFile;
        private String pdfFileName;

        void createReport() throws JRException, IOException {
            //Fill *.jasper -> *.jrprint
            InputStream is = getClass().getClassLoader().getResourceAsStream("jaspercompiled/" + jasperReportFile);
            Assert.notNull(is, String.format("%s not found in classpath", jasperReportFile));

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);

            Map<String, Object> parameters = new HashMap<>();
            collectParameters(parameters);
            parameters.put("imgPath", imgPath);
            JasperPrint printedReport = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setContentType(PDF);
            // For the Save As dialogue replace 'inline' with 'attachment'. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.
            response.setHeader(CONTENT_DISP, String.format(INLINE, URLEncoder.encode(pdfFileName, "UTF-8")));
            OutputStream output = response.getOutputStream();

            //Export *.jrprint -> *.pdf
            JasperExportManager.exportReportToPdfStream(printedReport, output);
            output.close();
        }

        abstract void collectParameters(Map<String, Object> parameters);
    }
}
