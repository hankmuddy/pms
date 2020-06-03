package com.idgindigo.pms.security.service;

import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.security.permission.Decider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 4/22/14 2:28 PM
 */
public abstract class ConcreteDeciderService {
    private final Map<String, Decider> userTypeToDecider = new HashMap<>();

    protected void put(String key, Decider value) {
        userTypeToDecider.put(key, value);
    }

    public Decider getDecider() {
        return userTypeToDecider.get(SecurityUtils.getUserDetails().getAuthentication().getUserType());
    }
}
