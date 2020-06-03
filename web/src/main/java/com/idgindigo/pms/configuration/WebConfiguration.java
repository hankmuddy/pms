package com.idgindigo.pms.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.idgindigo.pms.restutils.exception.handler.RestExceptionHandler;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateDeserializer;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateSerializer;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateTimeDeserializer;
import com.idgindigo.pms.restutils.serializer.CustomLocalDateTimeSerializer;
import com.idgindigo.pms.restutils.serializer.LocalDateKeyDeserializer;
import com.idgindigo.pms.restutils.view.JsonViewSupportFactoryBean;
import com.idgindigo.pms.restutils.view.ViewAwareJsonMessageConverter;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.web.controller.pms.ApiController;
import com.idgindigo.pms.web.controller.pms.BookingController;
import com.idgindigo.pms.web.controller.pms.MerchantController;
import com.idgindigo.pms.web.intercenptor.ApiInterceptor;
import com.idgindigo.pms.web.intercenptor.ExtranetInterceptor;
import com.idgindigo.pms.web.intercenptor.WubookPushInterceptor;
import com.idgindigo.pms.web.utils.converter.InstantToLocalDateTimeConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author vomel
 * @since 29.10.13 12:59
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)
@ComponentScan(basePackages = {
        "com.idgindigo.pms.web",
        "com.idgindigo.pms.web.websocket",
        "com.idgindigo.pms.web.utils",
        "com.idgindigo.pms.restutils.crud",
        "com.idgindigo.pms.service.filtering"
})
public class WebConfiguration extends WebMvcConfigurationSupport {

    public static final long MAX_UPLOAD_SIZE = 10000000L;
    public static final String REST_URL_PREFIX = "/rest/";
    public static final String APP_NAME = "home";
    public static final String APP_URL = "/" + APP_NAME;
    public static final String ADMIN_APP_NAME = "admin";
    public static final String ADMIN_APP_URL = "/" + ADMIN_APP_NAME;
    public static final String ADMIN_URL_PREFIX = ADMIN_APP_URL + "/";

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("index*").setViewName("redirect:" + APP_URL);
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/button").setViewName("button");
//        registry.addViewController("/merchantform").setViewName("merchant");
        registry.addViewController("/form").setViewName("form");
        registry.addViewController(APP_NAME).setViewName(APP_NAME);
        registry.addViewController(ADMIN_APP_NAME).setViewName(ADMIN_APP_NAME);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("client/**").addResourceLocations("/resources/client/").setCachePeriod(31556926);
        registry.addResourceHandler("themes/**").addResourceLocations("/resources/themes/").setCachePeriod(31556926);
        registry.addResourceHandler("styles/**").addResourceLocations("/resources/styles/").setCachePeriod(31556926);
        registry.addResourceHandler("pbb/**").addResourceLocations("/resources/pbb/").setCachePeriod(31556926);
        registry.addResourceHandler("confirmationletter/**").addResourceLocations("/resources/confirmationletter/").setCachePeriod(31556926);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Override
    public HandlerMapping resourceHandlerMapping() {
        AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) super.resourceHandlerMapping();
        handlerMapping.setOrder(-1);
        return handlerMapping;
    }

    @Override
    @Bean
    public HandlerMapping viewControllerHandlerMapping() {
        return super.viewControllerHandlerMapping();
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setExposeContextBeansAsAttributes(true);
        viewResolver.setExposedContextBeanNames(new String[]{"localeResolver"});
        return viewResolver;
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver getMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(MAX_UPLOAD_SIZE);
        return multipartResolver;
    }

    @Override
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return super.requestMappingHandlerAdapter();
    }

    @Override
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping mapping = super.requestMappingHandlerMapping();
        mapping.setInterceptors(new Object[]{
                getInterceptor(ApiController.URL + "/**", apiInterceptor(), ApiController.URL + "/labels"),
                getInterceptor(MerchantController.URL + "/**", apiInterceptor()),
                getInterceptor(BookingController.URL + "/**", wubookInterceptor(),
                        BookingController.URL + "/cc",
                        BookingController.URL + "/importBookingsForDates",
                        BookingController.URL + "/attachPushUrlsToLcodes",
                        BookingController.URL + "/importBookings"
                ),
                new MappedInterceptor(new String[]{"/**"}, oemiv()),
                new MappedInterceptor(new String[]{"/rest/**"}, extranet())
        });
        return mapping;
    }

    @Bean(autowire = Autowire.BY_NAME)
    public OpenEntityManagerInViewInterceptor oemiv() {
        return new OpenEntityManagerInViewInterceptor();
    }

    public MappedInterceptor getInterceptor(String url, HandlerInterceptor interceptor, String... ignoreUrls) {
        return new MappedInterceptor(new String[]{url}, ignoreUrls, interceptor);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        configurer.favorParameter(true);
        configurer.favorPathExtension(false);
        configurer.parameterName("mediaType");
        configurer.ignoreAcceptHeader(true);
        configurer.useJaf(false);
        configurer.mediaType("json", new MediaType("application", "json", Charset.forName("UTF-8")));
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jacksonConverter = new ViewAwareJsonMessageConverter();
        jacksonConverter.setObjectMapper(objectMapper());
        jacksonConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.valueOf("application/schema+json")
        ));
        converters.add(jacksonConverter);
    }

    @Bean
    public RestExceptionHandler exceptionHandler() {
        return new RestExceptionHandler();
    }


    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
        bean.setIndentOutput(true);
        bean.afterPropertiesSet();
        ObjectMapper objectMapper = bean.getObject();
        objectMapper
                .registerModule(new Hibernate4Module().enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING))
                .registerModule(new JodaModule()
                        .addSerializer(LocalDate.class, new CustomLocalDateSerializer())
                        .addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer())
                        .addDeserializer(LocalDate.class, new CustomLocalDateDeserializer())
                        .addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer())
                        .addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer()));
        return objectMapper;
    }

    @Bean
    public JsonViewSupportFactoryBean initJsonViewSupport() {
        return new JsonViewSupportFactoryBean();
    }

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        return new ResourceBundleMessageSource() {{
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasenames("classpath:messages");
            messageSource.setDefaultEncoding("UTF-8");
            messageSource.setCacheSeconds(0);
            setParentMessageSource(messageSource);
        }};
    }

    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    @Bean
    public ApiInterceptor apiInterceptor() {
        return new ApiInterceptor();
    }

    @Bean
    public WubookPushInterceptor wubookInterceptor() {
        return new WubookPushInterceptor();
    }

    @Bean
    public HandlerInterceptor extranet() {
        return new ExtranetInterceptor();
    }

    public static class CustomLocaleResolver implements LocaleResolver {

        private LocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String tag = SecurityUtils.isRecognizedUser()
                    ? SecurityUtils.getUserDetails().getAuthentication().getLanguage()//Will return null if users locale is not set
                    : null;
            if (StringUtils.isNotBlank(tag)) {
                return Locale.forLanguageTag(tag);
            } else {
                return acceptHeaderLocaleResolver.resolveLocale(request);
            }
        }

        @Override
        public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
            throw new UnsupportedOperationException("Locale can only be set as user profile parameter");
        }
    }

    @Override
    @Bean
    public FormattingConversionService mvcConversionService() {
        return super.mvcConversionService();
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.removeConvertible(String.class, LocalDateTime.class);
        registry.addConverter(new InstantToLocalDateTimeConverter());
    }
}