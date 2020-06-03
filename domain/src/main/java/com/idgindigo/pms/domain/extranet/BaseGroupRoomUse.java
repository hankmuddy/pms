package com.idgindigo.pms.domain.extranet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.HashSet;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.FRONT_DESK;

/**
 * @author valentyn_vakatsiienko
 * @since 5/23/14 11:02 AM
 */
@Entity
@Getter
@Setter
public class BaseGroupRoomUse extends BaseRoomUse {
    public static final Iterable<BaseGroupRoomUse.Status> NOT_REFUSED_STATUSES = new HashSet<Status>() {{
        add(BaseGroupRoomUse.Status.BOOKING_FREE);
        add(BaseGroupRoomUse.Status.BOOKING_WARRANTY);
        add(BaseGroupRoomUse.Status.LIVING);
        add(BaseGroupRoomUse.Status.OUTGO);
    }};

    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    protected BaseRoom baseRoom;
    @JsonIgnore
    @Column(updatable = false)
    protected String rcode;//wubook reservation_code for booking
    @Column(updatable = false)
    @NotNull
    @Enumerated(EnumType.STRING) protected Status status;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String comment;
    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    protected Plan plan;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    protected String description;
    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    protected CustomerGroup customerGroup;
    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    protected Source source = FRONT_DESK;

    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    protected long total;
    @JsonIgnore
    @Column(updatable = false)
    private String acode;//api reservation code for booking

    public boolean isFromChannel() {
        return source != FRONT_DESK;
    }

    public enum Source {
        FRONT_DESK, BOOKING_BUTTON, WUBOOK_BUTTON, EXPEDIA, BOOKING, HOTEL_DE, ITWG, IN_ITALIA, HOTELS_COM, RESERVER_IT, ACCOMODATIONZ_COM,
        HOTEL_BEDS, VENERE_COM, HRS_COM, TRAVEL_EUROPE, ATRAPALO, MBE_TRAVEL, ESCAPIO, BB_PLANET, SPLENDIA, AGODA,
        HOSTELS_CLUB, LASTMINUTE, TRAVELOCITY, SABRE, BUDGETPLACES, ORBITZ, LATE_ROOMS, IN_TOSCANA, HOSTEL_WORLD,
        HOSTELBOOKERS, HOTELITA, I_CASTELLI, VASHOTEL, SLEEPINGIN, OKTOGO, FERATEL, FENILOT, SYNXIS, TABLETHOTELS, EASYTOBOOK,
        VIVAFIRENZE, OSTROVOK, HOTUSA, AIRBNB, CLEARINGSTATION, WIMDU, HOUSE_TRIP, HOMEAWAY, HOLIDAY_VELVET, FLIPKEY, THE9FLATS,
        GAY_VILLE, AMSTERDAM_BED_AND_BREAKFASTS, APARTMENTS_UNLIMITED, BE_MY_GUEST, ERFGOED, LOVING_APARTMENTS,
        ONLY_APARTMENTS, ELONG_EXPEDIA, FLAT_CLUB, TRAVELREPUBLIC, THE_101_HOTELS, BEDANDBREAKFAST_EU, FEDERALBERGHI, CAPRI_ONLINE,
        BED_AND_BREAKFAST_IT, BRONEVIK, GARDAPASS, POSITANO_COM_CAPRIONLINE, ITALYTRAVELLER_CAPRIONLINE, CAPRI_IT_CAPRIONLINE, CAPRI_NET_CAPRIONLINE,
        INNTOPIA, MR_AND_MRS_SMITH, HOTELINN, THEBESTSPAHOTELS, CLEARTRIP, TRAVEL_GURU, BOOKING_PIEMONTE, BESTDAY, JACTRAVEL, MOENA
    }

    public enum Status {
        BOOKING_FREE, BOOKING_WARRANTY, LIVING, OUTGO, REFUSE, NOT_ARRIVED
    }
}