package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * @author vomel
 * @since 29.10.13 16:14
 */
public abstract class BaseWebCrudTest<T extends BaseIdentifiable> extends InMemoryDbWebTest<T> {

    @SuppressWarnings("unchecked cast")
    @Test
    public void testWebCrud() throws Exception {
        loginBeforePost();

        T entity = createEntity();

        //C
        MockHttpServletResponse response = performPost(entity);

        entity = convertResponseWithSingleObject(objectMapper, response.getContentAsString(), (Class<T>) entity.getClass());
        String entityUrl = getUrl() + entity.getId();

        //R
        if (doRunGetStep()) {
            reLoginBeforeGet(entity);
            response = mvc.perform(get(entityUrl))
                    .andReturn().getResponse();
            T receivedEntity = convertResponseWithSingleObject(objectMapper, response.getContentAsString(), (Class<T>) entity.getClass());
            assertEquals(receivedEntity, entity, "Entity obtained with get request doesn`t match saved one");
        }

        //U
        if (doRunUpdateStep()) {
            reLoginBeforePut(entity);
            updateEntity(entity);
            response = mvc.perform(put(getUrl() + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(serialize(entity)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            entity = convertResponseWithSingleObject(objectMapper, response.getContentAsString(), (Class<T>) entity.getClass());
            assertEquals(entity, entity, "Entity returned by update request doesn`t match saved one");
        }

        reLoginBeforeDelete(entity);

        //D
        if (doRunDeleteStep()) {
            mvc.perform(delete(getUrl() + entity.getId()))
                    .andExpect(status().isOk());
            if (doRunGetStep()) {
                if (isSoftDelete()) {
                    response = mvc.perform(get(getUrl() + entity.getId()))
                            .andReturn().getResponse();
                    T receivedEntity = convertResponseWithSingleObject(objectMapper, response.getContentAsString(), (Class<T>) entity.getClass());
                    assertFalse(receivedEntity.getActive());
                } else {
                    mvc.perform(get(getUrl() + entity.getId())).andExpect(status().isNotFound());
                }
            }
        }

//        if (doRunListStep()) {
//            reLoginBeforeList();
//
//            //List
//            int sizeBefore = getEntityCount(mvc, getUrl(), mapper, entity.getClass());
//            for (int i = 0; i < ENTITY_LIST_SIZE; i++) {
//                performPost(createEntity(), mapper);
//            }
//            int sizeAfter = getEntityCount(mvc, getUrl(), mapper, entity.getClass());
//            assertTrue(sizeAfter == sizeBefore + ENTITY_LIST_SIZE);
//        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private MockHttpServletResponse performPost(T entity) throws Exception {
        String content = serialize(entity);
        MockHttpServletResponse response = mvc.perform(addPostParameters(post(getUrl())).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        return response;
    }

    protected MockHttpServletRequestBuilder addPostParameters(MockHttpServletRequestBuilder builder) {
        return builder;
    }

    protected void loginBeforePost() {
        loginAs(userProvider.getPersistentEntity());
    }

    protected boolean doRunGetStep() {
        return true;
    }

    protected void reLoginBeforeGet(T entity) {
        reLoginBeforeGet();
    }

    protected void reLoginBeforeGet() {
    }

    protected void reLoginBeforePut(T entity) {
        reLoginBeforePut();
    }

    protected void reLoginBeforePut() {
    }

    protected void reLoginBeforeDelete(T entity) {
        reLoginBeforeDelete();
    }

    protected void reLoginBeforeDelete() {
    }

    protected boolean doRunListStep() {
        return true;
    }

    protected void reLoginBeforeList() {
    }

    protected boolean doRunUpdateStep() {
        return true;
    }

    protected boolean doRunDeleteStep() {
        return true;
    }

    protected T createEntity(Visitor<T>... visitor) {
        return getProvider().getTransientEntity(visitor);
    }

    protected void updateEntity(T entity) {
        getProvider().updateEntity(entity);
    }

    protected boolean isSoftDelete() {
        return false;
    }

    protected abstract EntityProvider<T> getProvider();

}
