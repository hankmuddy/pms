package com.idgindigo.pms.domain.extranet.plan;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 2:06 PM
 */
@Entity
@DiscriminatorValue(VirtualPlan.VIRTUAL_PLAN)
@Getter
@Setter
public class VirtualPlan extends BasePlan {
    public static final String VIRTUAL_PLAN = "virtualPlan";

    @NotNull
    @ManyToOne
    @JoinColumn(updatable = false)
    private Plan plan;
    @NotNull
    private Variation variation;
    @Min(0)
    private long value;

    public enum Variation {
        FIXED_DISCOUNT(-2), PERCENTAGE_DISCOUNT(-1), FIXED_INCREASE(2), PERCENTAGE_INCREASE(1);
        private final int value;

        Variation(int value) {
            this.value = value;
        }

        public static Variation forValue(int value) {
            for (Variation variation : Variation.values()) {
                if (variation.getValue() == value) {
                    return variation;
                }
            }
            throw new IllegalArgumentException("Unrecognized Variation value: " + value);
        }

        public int getValue() {
            return value;
        }
    }
}