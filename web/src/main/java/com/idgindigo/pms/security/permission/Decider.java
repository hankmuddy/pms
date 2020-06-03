package com.idgindigo.pms.security.permission;

/**
 * @author valentyn_vakatsiienko
 * @since 4/8/14 12:17 PM
 */
public interface Decider {
    public static final Decider FALSE = new Decider() {
        @Override
        public boolean decide(Object target) {
            return false;
        }
    };
    public static final Decider TRUE = new Decider() {
        @Override
        public boolean decide(Object target) {
            return true;
        }
    };

    boolean decide(Object target);
}
