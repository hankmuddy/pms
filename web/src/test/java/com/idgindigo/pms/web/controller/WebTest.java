package com.idgindigo.pms.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idgindigo.pms.InitializingTest;
import com.idgindigo.pms.configuration.MailConfig;
import com.idgindigo.pms.configuration.MethodSecurityConfig;
import com.idgindigo.pms.configuration.SecurityConfiguration;
import com.idgindigo.pms.configuration.ServiceConfig;
import com.idgindigo.pms.configuration.TestLoginsJpaConfig;
import com.idgindigo.pms.configuration.TestWebConfig;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.domain.Identifiable;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.utils.UserProvider;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/14/13 4:11 PM
 */
@WebAppConfiguration
@ContextConfiguration(classes = {TestWebConfig.class, TestLoginsJpaConfig.class, SecurityConfiguration.class, ServiceConfig.class, MailConfig.class, MethodSecurityConfig.class})
@ComponentScan(basePackages = "com.idgindigo.pms.utils")
@ActiveProfiles("dev")
public abstract class WebTest<T extends BaseIdentifiable> extends InitializingTest {
    public static final ResultMatcher OK = status().isOk();
    public static final ResultMatcher CREATED = status().isCreated();
    public static final ResultMatcher FORBIDDEN = status().isForbidden();
    private static Logger logger = LoggerFactory.getLogger(WebTest.class);
    public static final String BAD_REQUEST_CODE_MESSAGE_PATH = "$..[0].code";
    public static final String BAD_REQUEST_SOURCE_MESSAGE_PATH = "$..[0].source";
    @Inject
    protected WebApplicationContext context;
    @Inject
    private HotelRepository hotelRepository;
    @Inject
    protected UserProvider userProvider;
    @Inject
    protected AuthenticationRepository authenticationRepository;
    protected MockMvc mvc;
    protected MockMvc securedMvc;
    protected MockHttpSession session;
    @Inject
    protected ObjectMapper objectMapper;
    @Resource
    protected FilterChainProxy springSecurityFilterChain;

    @BeforeClass
    protected void init() {
//        WubookPushInterceptor wubookPushInterceptor = new WubookPushInterceptor();
//        Deencapsulation.setField(wubookPushInterceptor, "hotelRepository", hotelRepository);

        OpenEntityManagerInViewFilter oemivFilter = new OpenEntityManagerInViewFilter();
        oemivFilter.setServletContext(context.getServletContext());

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(CharEncoding.UTF_8);
        filter.setForceEncoding(true);

        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .addFilter(wubookPushInterceptor)
                .addFilter(oemivFilter)
                .addFilter(filter)
                .build();

        securedMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .addFilter(oemivFilter)
                .addFilter(filter)
                .build();

        session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

    }

    protected MockHttpServletRequestBuilder preparePost(Object entity) throws Exception {
        return buildRequest(post(getUrl()), entity);
    }

    protected MockHttpServletRequestBuilder preparePost(Object entity, String path) throws Exception {
        return buildRequest(post(getUrl() + path), entity);
    }

    protected MockHttpServletRequestBuilder preparePost(Object entity, String path, boolean absolute) throws Exception {
        if (absolute) {
            return buildRequest(post(path), entity);
        } else {
            return preparePost(entity, path);
        }
    }

    protected MockHttpServletRequestBuilder preparePost(String path) throws JsonProcessingException {
        return buildRequest(post(getUrl() + path));
    }

    protected MockHttpServletRequestBuilder preparePut(Identifiable entity) throws Exception {
        return buildRequest(put(getUrl() + entity.getId()), entity);
    }

    protected MockHttpServletRequestBuilder preparePut(String path) throws JsonProcessingException {
        return buildRequest(put(getUrl() + path));
    }

    protected MockHttpServletRequestBuilder preparePut(Object entity, String path) throws Exception {
        return buildRequest(put(getUrl() + path), entity);
    }

    protected <T extends BaseEntity> List<T> getEntityList(Class<T> clazz) throws Exception {
        return convertResponseWithObjectList(objectMapper,
                mvc.perform(prepareGet()).andExpect(status().isOk()).andReturn().getResponse(),
                clazz);
    }

    protected <T extends BaseIdentifiable> List<T> getEntityList(String url, Class<T> clazz) throws Exception {
        return convertResponseWithObjectList(objectMapper,
                mvc.perform(buildRequest(get(getUrl() + url)).param(BaseCrudController.LIMIT, "10")).andExpect(status().isOk()).andReturn().getResponse(),
                clazz);
    }

    protected T getEntityFromResponse(MockHttpServletResponse response, BaseRepository<T> repository) throws IOException {
        long id = ((Number) ((Map) objectMapper.readValue(response.getContentAsByteArray(), Map.class).get("content")).get("id")).longValue();
        return repository.findOne(id);
    }

    protected abstract String getUrl();

    protected MockHttpServletRequestBuilder prepareDelete(long id) {
        return buildRequest(delete(getUrl() + id));
    }

    protected MockHttpServletRequestBuilder prepareGet() {
        return prepareGet("");
    }

    protected MockHttpServletRequestBuilder prepareGet(String url) {
        return buildRequest(get(getUrl() + url)).param(BaseCrudController.LIMIT, "0");
    }

    protected MockHttpServletRequestBuilder prepareGet(long id) {
        return prepareGet(String.valueOf(id));
    }

    public static <T> T convertResponseWithSingleObject(ObjectMapper mapper, String response, Class<T> clazz) throws IOException {
        JSONObject responseEntity = mapper.readValue(response, JSONObject.class);
        @SuppressWarnings("unchecked")
        String content = JSONObject.toJSONString((Map<String, ?>) responseEntity.get("content"));
        return mapper.readValue(content, clazz);
    }

    public static <K, V> Map<K, V> convertResponseWithObjectMap(ObjectMapper mapper, MockHttpServletResponse response, Class<K> classK, Class<V> classV) throws IOException {
        JSONObject responseEntity;
        try {
            responseEntity = mapper.readValue(response.getContentAsString(), JSONObject.class);
        } catch (JsonMappingException e) {
            logger.error(response.getStatus() + " => Could not parse response: " + response.getContentAsString(), e);
            return Collections.emptyMap();
        }
        Map<K, V> result = new HashMap<>();
        Map<String, V> content = (Map<String, V>) responseEntity.get("content");
        for (Map.Entry<String, V> entry : content.entrySet()) {
            result.put(mapper.readValue(entry.getKey(), classK), entry.getValue());
        }
        return result;
    }

    public static <T> List<T> convertResponseWithObjectList(ObjectMapper mapper, MockHttpServletResponse response, Class<T> clazz) throws IOException {
        JSONObject responseEntity;
        try {
            responseEntity = mapper.readValue(response.getContentAsString(), JSONObject.class);
        } catch (JsonMappingException e) {
            logger.error(response.getStatus() + " => Could not parse response: " + response.getContentAsString(), e);
            return Collections.emptyList();
        }
        List<?> content = (List) responseEntity.get("content");
        if (!content.isEmpty() && !(content.get(0) instanceof Map)) {
            return parseFromList(content);
        }
        return parseFromMap(mapper, clazz, (Iterable<Map<String, ?>>) content);
    }

    private static <T> List<T> parseFromList(Object content) {
        List<T> result = new ArrayList<>();
        Iterable<T> collection = (Collection) content;
        for (T entity : collection) {
            result.add(entity);
        }
        return result;
    }

    private static <T> List<T> parseFromMap(ObjectMapper mapper, Class<T> clazz, Iterable<Map<String, ?>> content) throws IOException {
        List<T> result = new ArrayList<>();
        for (Map<String, ?> entity : content) {
            result.add(mapper.readValue(JSONObject.toJSONString(entity), clazz));
        }
        return result;
    }

    public static <T> int getEntityCount(MockMvc mvc, String url, ObjectMapper mapper, Class<T> domainClass) throws Exception {
        return convertResponseWithObjectList(mapper,
                mvc.perform(get(url).param(BaseCrudController.LIMIT, "0")).andReturn().getResponse(),
                domainClass)
                .size();
    }

    protected void loginAs(User user) {
        Authentication authentication = authenticationRepository.findByUsername(user.getUsername()).get(0);
        assertNotNull(authentication, "There`s no authentication for user " + user + " in the logins database, cant authenticate.");
        SecurityUtils.authenticateUser(authentication, user);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
    }

    protected void loginAs(Authentication user) {
        SecurityUtils.authenticateNonTenantUser(user);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
    }

    @AfterMethod
    protected final void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    protected MockHttpServletRequestBuilder buildRequest(MockHttpServletRequestBuilder builder, Object entity) throws Exception {
        return builder.contentType(MediaType.APPLICATION_JSON)
                .content(serialize(entity))
                .session(session);
    }

    private MockHttpServletRequestBuilder buildRequest(MockHttpServletRequestBuilder builder) {
        return builder.session(session);
    }

    protected void testForbidden(MockHttpServletRequestBuilder builder, User user) throws Exception {
        if (user != null) {
            loginAs(user);
        }
        securedMvc.perform(builder)
                .andExpect(status().isForbidden());
    }

    protected void testNotFound(MockHttpServletRequestBuilder builder) throws Exception {
        mvc.perform(builder)
                .andExpect(status().isNotFound());
    }

    protected void testMethodNotAllowed(MockHttpServletRequestBuilder builder) throws Exception {
        mvc.perform(builder)
                .andExpect(status().isMethodNotAllowed());
    }

    protected void testBadRequest(MockHttpServletRequestBuilder builder, String message) throws Exception {
        mvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(BAD_REQUEST_CODE_MESSAGE_PATH, equalTo(message)));
    }

    protected void testForbidden(MockHttpServletRequestBuilder builder) throws Exception {
        testForbidden(builder, null);
    }

    protected void testAuthorized(MockHttpServletRequestBuilder builder) throws Exception {
        securedMvc.perform(builder)
                .andExpect(status().isOk());
    }

    protected void testOk(MockHttpServletRequestBuilder builder, User user) throws Exception {
        if (user != null) {
            loginAs(user);
        }
        mvc.perform(builder)
                .andExpect(status().isOk());
    }

    protected void testOk(MockHttpServletRequestBuilder builder) throws Exception {
        testOk(builder, null);
    }

    protected void testCreated(MockHttpServletRequestBuilder builder, User user) throws Exception {
        if (user != null) {
            loginAs(user);
        }
        mvc.perform(builder)
                .andExpect(status().isCreated());
    }

    protected void testCreated(MockHttpServletRequestBuilder builder) throws Exception {
        testCreated(builder, null);
    }

    protected String serialize(Object entity) throws Exception {
        ObjectMapper mapper = new ObjectMapper().setSerializerFactory(objectMapper.getSerializerFactory())
                .disable(MapperFeature.USE_ANNOTATIONS);
        return mapper.writeValueAsString(entity);
    }
}
