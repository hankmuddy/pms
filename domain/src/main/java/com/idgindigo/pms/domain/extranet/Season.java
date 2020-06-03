package com.idgindigo.pms.domain.extranet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.joda.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author vomel
 * @since 01.11.13 12:16
 */
@Entity
@Getter
@Setter
@Check(constraints = "start <= until")
public class Season extends ApprovableEntity {
    public static final String SEASON = "season";
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate until;
    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    @JsonView(ListView.class)
//    @JsonIgnore
//    @Getter(onMethod = @_({@JsonIgnore}))
//    @Setter(onMethod = @_({@JsonProperty}))
    private Plan plan;

    @OneToMany(mappedBy = SEASON)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonIgnore}))
    @Setter(onMethod = @_({@JsonProperty}))
    private List<Living> livings;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .appendSuper(super.toString())
                .append("start", start)
                .append("until", until)
                .toString();
    }
}