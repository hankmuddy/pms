package com.idgindigo.pms.restutils.view;

import com.idgindigo.pms.domain.BaseEntity;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author valentyn_vakatsiienko
 * @since 11/4/13 4:16 PM
 */
public class ViewInjectingReturnValueHandler implements
        HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ViewInjectingReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {

        Class<? extends BaseEntity.BaseView> viewClass = getDeclaredViewClass(returnType);
        if (viewClass != null) {
            returnValue = wrapResult(returnValue, viewClass);
        }

        delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    /**
     * Returns the view class declared on the method, if it exists.
     * Otherwise, returns null.
     *
     * @param returnType
     * @return
     */
    private Class<? extends BaseEntity.BaseView> getDeclaredViewClass(MethodParameter returnType) {
        ResponseView annotation = returnType.getMethodAnnotation(ResponseView.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            return null;
        }
    }

    private Object wrapResult(Object result, Class<? extends BaseEntity.BaseView> viewClass) {
        PojoView response = new PojoView(result, viewClass);
        return response;
    }
}
