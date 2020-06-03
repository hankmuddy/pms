package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.extranet.service.FixedService;
import com.idgindigo.pms.price.PriceResolver;
import com.idgindigo.pms.price.SimpleServicePriceResolver;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 12:52 PM
 */
@Entity
//@JsonTypeName("SIMPLE_SERVICE")
@Getter
@Setter
public class SimpleService extends FixedService {
    public static final String SIMPLE_SERVICE = "simpleService";

    @NotNull
    private String measure;

    @Override
    public String getType() {
        return SIMPLE_SERVICE;
    }

    @Override
    public Class<? extends PriceResolver> getPriceResolver() {
        return SimpleServicePriceResolver.class;
    }
}