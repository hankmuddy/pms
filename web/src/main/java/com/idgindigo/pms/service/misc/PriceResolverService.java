package com.idgindigo.pms.service.misc;

import com.idgindigo.pms.domain.extranet.service.Service;
import com.idgindigo.pms.price.PriceResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author valentyn_vakatsiienko
 * @since 12/4/13 7:01 PM
 */
public class PriceResolverService implements ApplicationContextAware {
    private ApplicationContext context;

    public PriceResolver getPriceResolver(Service service) {
        return context.getBean(service.getPriceResolver());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
