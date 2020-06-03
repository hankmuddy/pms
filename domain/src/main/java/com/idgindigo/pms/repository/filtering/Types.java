package com.idgindigo.pms.repository.filtering;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.pms.BankDetails;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * @author valentyn_vakatsiienko
 * @since 10/23/13 6:48 PM
 */
public enum Types implements Type {
    NUMERIC {
        @Override
        public Object parseValue(String value) {
            return Long.valueOf(value);
        }
    },
    DATE {
        @Override
        public Object parseValue(String value) {
            return new LocalDate(Long.valueOf(value) * 1000, DateTimeZone.UTC);
        }
    },
    DATETIME {
        @Override
        public Object parseValue(String value) {
            return new LocalDateTime(Long.valueOf(value) * 1000, DateTimeZone.UTC);
        }
    },
    BOOLEAN {
        @Override
        public Object parseValue(String value) {
            return Boolean.valueOf(value);
        }
    },
    STRING {
        @Override
        public Object parseValue(String value) {
            return value;
        }
    },
    FIELD {
        @Override
        public Object parseValue(String value) {
            return value;
        }
    },

    //Room use
    ROOM_USE_STATUS {
        @Override
        public Object parseValue(String value) {
            return BaseGroupRoomUse.Status.valueOf(value.toUpperCase());
        }
    },
    ROOM_USE_SOURCE {
        @Override
        public Object parseValue(String value) {
            return BaseGroupRoomUse.Source.valueOf(value.toUpperCase());
        }
    },

    //Customer group
    POV {
        @Override
        public Object parseValue(String value) {
            return CustomerGroup.PurposeOfVisit.valueOf(value.toUpperCase());
        }
    },

    //Plan
    PLAN_BOARD {
        @Override
        public Object parseValue(String value) {
            return Plan.Board.valueOf(value);
        }
    },

    //BankDetails
    PAYMENT_METHOD {
        @Override
        public Object parseValue(String value) {
            return BankDetails.PaymentType.valueOf(value.toUpperCase());
        }
    }
}
