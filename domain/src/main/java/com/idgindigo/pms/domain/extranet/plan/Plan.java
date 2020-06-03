package com.idgindigo.pms.domain.extranet.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 11:43 AM
 */
@Entity
@DiscriminatorValue(Plan.PLAN)
@Getter
@Setter
public class Plan extends BasePlan {
    public static final String PLAN = "plan";

    @NotNull
    @Enumerated(EnumType.STRING)//Missing on WuBook
    private Board board;

    @OneToMany(mappedBy = PLAN)
    @JsonView(SoloView.class)
    private List<Season> seasons;

    @OneToMany(mappedBy = PLAN)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonIgnore}))
    @Setter(onMethod = @_({@JsonProperty}))
    private List<Living> livings;

    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private Boolean defaultPlan = Boolean.FALSE;

    public enum Board {
        NB("Standard Rate"), BB("Standard Rate Breakfast Included"), HB("Half-Board Rate"), FB("Full-Board Rate"), AI("All-Inclusive Rate");
        private final String name;

        Board(String name) {
            this.name = name;
        }

        public static Board forName(String name) {
            for (Board board : values()) {
                if (board.name.equalsIgnoreCase(name)) return board;
            }
            return null;
        }

    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append("name", getName())
                .toString();
    }
}
