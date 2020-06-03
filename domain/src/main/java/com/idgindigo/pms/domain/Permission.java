package com.idgindigo.pms.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author valentyn_vakatsiienko
 * @since 10/31/13 5:21 PM
 */
public enum Permission implements GrantedAuthority {
   /* //Accommodation
    ACCOMMODATION_CREATE,
    ACCOMMODATION_VIEW,
    ACCOMMODATION_LIST,
    ACCOMMODATION_UPDATE,
    ACCOMMODATION_APPROVE,
    ACCOMMODATION_DELETE,

    //Adult
    ADULT_CREATE,
    ADULT_VIEW,
    ADULT_LIST,
    ADULT_UPDATE,
    ADULT_DELETE,

    //BaseRoom
    BASE_ROOM_LIST,

    //BaseRoomUse
    BASE_ROOM_USE_LIST,

    //BaseRoomValue
    BASE_ROOM_VALUE_CREATE,
    BASE_ROOM_VALUE_VIEW,
    BASE_ROOM_VALUE_LIST,
    BASE_ROOM_VALUE_UPDATE,
    BASE_ROOM_VALUE_DELETE,

    //CustomerGroup
    CUSTOMER_GROUP_CREATE,
    CUSTOMER_GROUP_VIEW,
    CUSTOMER_GROUP_LIST,
    CUSTOMER_GROUP_UPDATE,
    CUSTOMER_GROUP_APPROVE,
    CUSTOMER_GROUP_DELETE,

    CUSTOMER_GROUP_SET_DISCOUNT,
    CUSTOMER_GROUP_CLOSE,

    //Document
    DOCUMENT_CREATE,
    DOCUMENT_VIEW,
    DOCUMENT_LIST,
    DOCUMENT_DELETE,

    DOCUMENT_BIND_TO_ROOM_TYPE,*/

    SETTINGS_SAVE,
    EXPORT_TO_WUBOOK,

    USER_CREATE,
    USER_VIEW,
    USER_LIST,
    USER_UPDATE,
    USER_DELETE,

    //Admin permissions

    //Authentication
    AUTHENTICATION_CHANGE_PASSWORD {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },
    AUTHENTICATION_CHANGE_HOTEL {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },
    AUTHENTICATION_CREATE {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },
    AUTHENTICATION_VIEW {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },
    AUTHENTICATION_LIST {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },
    HOTEL_CREATE {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },
    HOTEL_LIST {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },
    HOTEL_VIEW {
        @Override
        public boolean isAdministrative() {
            return true;
        }
    },;

    public boolean isAdministrative() {
        return false;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
