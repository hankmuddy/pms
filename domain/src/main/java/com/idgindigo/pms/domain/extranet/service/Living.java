package com.idgindigo.pms.domain.extranet.service;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.price.LivingPriceResolver;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 3:47 PM
 * <p/>
 * This entity corresponds to Catalog table in rate_calendar.jpg
 */
@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"room_id", "plan_id"}),
        @UniqueConstraint(columnNames = {"room_id", "season_id"})})
@Check(constraints = "(plan_id IS NULL OR season_id IS NULL) AND (plan_id IS NOT NULL OR season_id IS NOT NULL) AND room_id IS NOT NULL")
public class Living extends CalendarService {
    public static final String LIVING = "living";

    @ManyToOne
    @NotNull
    @JoinColumn(updatable = false)
    private BaseRoom room;
    @ManyToOne
    @JoinColumn(updatable = false)
    private Plan plan;
    @ManyToOne
    @JoinColumn(updatable = false)
    private Season season;
    @NotNull
    private Long mon;
    @NotNull
    private Long tue;
    @NotNull
    private Long wed;
    @NotNull
    private Long thu;
    @NotNull
    private Long fri;
    @NotNull
    private Long sat;
    @NotNull
    private Long sun;

    @Override
    public String getType() {
        return LIVING;
    }

    @Override
    public Class<LivingPriceResolver> getPriceResolver() {
        return LivingPriceResolver.class;
    }

    public void setPrices(Long price) {
        mon = price;
        tue = price;
        wed = price;
        thu = price;
        fri = price;
        sat = price;
        sun = price;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.SIMPLE_STYLE)
                .append("room", room)
                .append("plan", plan)
                .append("season", season)
                .append("prices", mon + "," + tue + "," + wed + "," + thu + "," + fri + "," + sat + "," + sun)
                .toString();
    }
}
