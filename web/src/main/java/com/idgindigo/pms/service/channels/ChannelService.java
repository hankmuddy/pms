package com.idgindigo.pms.service.channels;

import com.idgindigo.pms.channel.wubook.BookingStatus;
import com.idgindigo.pms.channel.wubook.Printer;
import com.idgindigo.pms.channel.wubook.ResponseCode;
import com.idgindigo.pms.channel.wubook.Wubook;
import com.idgindigo.pms.channel.wubook.WubookAccount;
import com.idgindigo.pms.channel.wubook.WubookCaller;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.channel.wubook.WubookException;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.RoomClosed;
import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.WubookRoom;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.extranet.plan.BasePlan;
import com.idgindigo.pms.domain.extranet.plan.CompactRestriction;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.plan.VirtualPlan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.repository.extranet.VirtualRoomRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.repository.extranet.plan.BasePlanRepository;
import com.idgindigo.pms.repository.extranet.plan.CompactRestrictionRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.repository.extranet.plan.VirtualPlanRepository;
import com.idgindigo.pms.repository.extranet.roomuse.BaseRoomUseRepository;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.broadcast.BroadcastService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ACCOMODATIONZ_COM;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.AGODA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.AIRBNB;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.AMSTERDAM_BED_AND_BREAKFASTS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.APARTMENTS_UNLIMITED;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ATRAPALO;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BB_PLANET;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BEDANDBREAKFAST_EU;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BED_AND_BREAKFAST_IT;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BESTDAY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BE_MY_GUEST;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BOOKING;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BOOKING_PIEMONTE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BRONEVIK;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BUDGETPLACES;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.CAPRI_IT_CAPRIONLINE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.CAPRI_NET_CAPRIONLINE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.CAPRI_ONLINE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.CLEARINGSTATION;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.CLEARTRIP;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.EASYTOBOOK;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ELONG_EXPEDIA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ERFGOED;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ESCAPIO;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.EXPEDIA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.FEDERALBERGHI;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.FENILOT;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.FERATEL;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.FLAT_CLUB;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.FLIPKEY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.GARDAPASS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.GAY_VILLE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOLIDAY_VELVET;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOMEAWAY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOSTELBOOKERS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOSTELS_CLUB;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOSTEL_WORLD;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOTELINN;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOTELITA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOTELS_COM;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOTEL_BEDS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOTEL_DE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOTUSA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HOUSE_TRIP;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.HRS_COM;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.INNTOPIA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.IN_ITALIA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.IN_TOSCANA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ITALYTRAVELLER_CAPRIONLINE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ITWG;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.I_CASTELLI;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.JACTRAVEL;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.LASTMINUTE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.LATE_ROOMS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.LOVING_APARTMENTS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.MBE_TRAVEL;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.MOENA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.MR_AND_MRS_SMITH;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.OKTOGO;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ONLY_APARTMENTS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ORBITZ;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.OSTROVOK;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.POSITANO_COM_CAPRIONLINE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.RESERVER_IT;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.SABRE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.SLEEPINGIN;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.SPLENDIA;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.SYNXIS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.TABLETHOTELS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.THE9FLATS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.THEBESTSPAHOTELS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.THE_101_HOTELS;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.TRAVELOCITY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.TRAVELREPUBLIC;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.TRAVEL_EUROPE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.TRAVEL_GURU;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.VASHOTEL;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.VENERE_COM;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.VIVAFIRENZE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.WIMDU;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.WUBOOK_BUTTON;
import static com.idgindigo.pms.domain.extranet.CustomerGroup.PurposeOfVisit.TOURISM;
import static com.idgindigo.pms.domain.extranet.plan.VirtualPlan.Variation;
import static com.idgindigo.pms.service.pms.RoomUseService.GroupRoomUseDto;
import static com.idgindigo.pms.service.pms.RoomUseService.RoomUseWithOverrides;

/**
 * @author vomel
 * @since 17.12.13 13:54
 */
@Service//Board,Prices?,CustomerGroup?
public class ChannelService {
    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);
    private static final String WUBOOK_DATE_PATTERN = "dd/MM/yyyy";
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(WUBOOK_DATE_PATTERN);
    public static final DateTimeFormatter BOOKING_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final Pattern COMMA = Pattern.compile(",");
    private static final Pattern SPACE = Pattern.compile(" ");
    public static final Pattern BOARD = Pattern.compile(".*\\((.*)\\)");
    public static final int WUBOOK_MAX_OUTPUT = 120;
    public static final int WUBOOK_REQUESTS_LIMIT = 24;
    public static final String EMAIL_REGEX = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private VirtualRoomRepository virtualRoomRepository;
    @Inject
    private LivingRepository livingRepository;
    @Inject
    private BaseRoomUseRepository baseRoomUseRepository;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private BasePlanRepository basePlanRepository;
    @Inject
    private SeasonRepository seasonRepository;
    @Inject
    private AdultRepository adultRepository;
    @Inject
    private VirtualPlanRepository virtualPlanRepository;
    @Inject
    private CompactRestrictionRepository compactRestrictionRepository;

    @Value("${applicationUrl}")
    private String applicationUrl;
    @Inject
    private BroadcastService broadcastService;

    public String getApplicationUrl() {
        return applicationUrl;
    }

    private static final String DEFAULT_BOARD = "NB";

    public void exportToWubook(Hotel hotel) {
        logger.info("Exporting roomTypes...");
        List<RoomType> roomTypes = roomTypeRepository.findByRidNullAndApprovedTrue();
        exportRoomTypes(roomTypes, SecurityUtils.getWubookAccount(hotel));
        roomTypeRepository.save(roomTypes);
        logger.info("Exported roomTypes successfully: {}", roomTypes);

        logger.info("Exporting virtual rooms...");
        List<VirtualRoom> virtualRooms = virtualRoomRepository.findByRidNullAndApprovedTrue();
        exportVirtualRooms(virtualRooms, SecurityUtils.getWubookAccount(hotel));
        virtualRoomRepository.save(virtualRooms);
        logger.info("Exported virtual rooms successfully: {}", virtualRooms);

        logger.info("Exporting plans ...");
        List<Plan> plans = planRepository.findByPidNullAndApprovedTrue();
        exportPlans(plans, SecurityUtils.getWubookAccount(hotel));
        planRepository.save(plans);
        logger.info("Exported plans successfully: {}", plans);

        logger.info("Exporting virtual plans ...");
        List<VirtualPlan> virtualPlans = virtualPlanRepository.findByPidNullAndApprovedTrue();
        exportVirtualPlans(virtualPlans, SecurityUtils.getWubookAccount(hotel));
        virtualPlanRepository.save(virtualPlans);
        logger.info("Exported virtual plans successfully: {}", virtualPlans);

        logger.info("Exporting restriction plans ...");
        List<CompactRestriction> restrictionPlans = compactRestrictionRepository.findByPidNull();
        exportRestrictionPlans(restrictionPlans, SecurityUtils.getWubookAccount(hotel));
        compactRestrictionRepository.save(restrictionPlans);
        logger.info("Exported restriction plans successfully: {}", restrictionPlans);
    }

    public long newRoom(final RoomType roomType, WubookAccount account) {
        return new WubookCaller<Long>(account) {
            @Override
            protected Long run(Wubook wubook, String token) {
                return wubook.newRoom(token, roomType.getName(), roomType.getAdults(), convertPrice(roomType.getDefaultPrice()), roomType.getOtaRooms(), roomType.getShortname(), DEFAULT_BOARD);
            }
        }.call();
    }

    public void exportRoomTypes(final Iterable<RoomType> roomTypes, WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                for (RoomType roomType : roomTypes) {
                    roomType.setRid(wubook.newRoom(token, roomType.getName(), roomType.getAdults(), convertPrice(roomType.getDefaultPrice()), roomType.getOtaRooms(), roomType.getShortname(), DEFAULT_BOARD));
                }
                return null;
            }
        }.call();
    }

    public long newVirtualRoom(final VirtualRoom virtualRoom, WubookAccount account) {
        return new WubookCaller<Long>(account) {
            @Override
            protected Long run(Wubook wubook, String token) {
                return wubook.newVirtualRoom(token, virtualRoom.roomType().getRid(), virtualRoom.getName(), virtualRoom.getAdults() + virtualRoom.getChildren(), virtualRoom.getChildren(), convertPrice(virtualRoom.getDefaultPrice()), virtualRoom.getShortname(), DEFAULT_BOARD);
            }
        }.call();
    }

    public void exportVirtualRooms(final Iterable<VirtualRoom> virtualRooms, WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                for (VirtualRoom virtualRoom : virtualRooms) {
                    virtualRoom.setRid(wubook.newVirtualRoom(token, virtualRoom.roomType().getRid(), virtualRoom.getName(), virtualRoom.getAdults() + virtualRoom.getChildren(), virtualRoom.getChildren(), convertPrice(virtualRoom.getDefaultPrice()), virtualRoom.getShortname(), DEFAULT_BOARD));
                }
                return null;
            }
        }.call();
    }

    public long modRoom(final RoomType roomType, WubookAccount account) {
        return new WubookCaller<Long>(account) {
            @Override
            protected Long run(Wubook wubook, String token) {
                return wubook.modRoom(token, roomType.getRid(), roomType.getName(), roomType.getAdults() + roomType.getChildren(), convertPrice(roomType.getDefaultPrice()), roomType.getOtaRooms(), roomType.getShortname(), DEFAULT_BOARD);
            }
        }.call();
    }

    public void delRoom(final long rid, WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                wubook.delRoom(token, rid);
                return null;
            }
        }.call();
    }

    public void exportPlans(final Iterable<Plan> plans, WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                for (Plan plan : plans) {
                    plan.setPid(wubook.addPlan(token, plan.getName()));
                }
                return null;
            }
        }.call();
    }

    public Collection<BasePlan> fetchPlans(WubookAccount account) {
        return new WubookCaller<List<BasePlan>>(account) {
            @Override
            protected List<BasePlan> run(Wubook wubook, String token) {
                List<Map<String, Object>> wubookPlans = wubook.getPlans(token);
                if (logger.isTraceEnabled()) {
                    new Printer().print(wubookPlans);
                }
                processPlans(wubookPlans);
                return basePlanRepository.findAll();
            }
        }.call();
    }

//    public Collection<Plan> fetchRPlans(WubookAccount account) {
//        return new WubookCaller<Collection<Plan>>(account) {
//            @Override
//            protected Collection<Plan> run(Wubook wubook, String token) {
//                List<Map<String, Object>> wubookPlans = wubook.getRPlans(token);
//                if (logger.isTraceEnabled()) {
//                    new Printer().print(wubookPlans);
//                }
//                processRPlans(wubookPlans);
//                return planRepository.findAll();
//            }
//        }.call();
//    }

    private void processPlans(Iterable<Map<String, Object>> wubookPlans) {
//vpid=parent_pid, variation_type=1, variation=30.0, name, id
        Collection<Map<String, Object>> vPlans = new ArrayList<>();
        for (Map<String, Object> wubookPlan : wubookPlans) {
            if (wubookPlan.containsKey("vpid")) { //vpid may be null if the virtual plan refers to the WuBook Parity
                vPlans.add(wubookPlan);
            } else {
                handlePlan(wubookPlan);
            }
        }
        handleVPlans(vPlans);
    }

    private void handleVPlans(Collection<Map<String, Object>> vPlans) {
        Collection<VirtualPlan> toSave = new ArrayList<>(vPlans.size());
        for (Map<String, Object> vPlan : vPlans) {
            long id = number(vPlan, "id").longValue();
            long vpid = number(vPlan, "vpid").longValue();

            if (virtualPlanRepository.findByPid(id) != null || vpid <= 0) {
                continue;
            }

            VirtualPlan parsed = new VirtualPlan();
            parsed.setName(string(vPlan, "name"));
            parsed.setPid(id);
            parsed.setVariation(Variation.forValue(number(vPlan, "variation_type").intValue()));
            parsed.setValue(deConvertPrice(number(vPlan, "variation").doubleValue()));
            parsed.setPlan(planRepository.findByPid(vpid));
            parsed.setApproved(true);
            toSave.add(parsed);
        }
        virtualPlanRepository.save(toSave);
    }

    private void handlePlan(Map<String, Object> wubookPlan) {
        Plan plan = processPlan(wubookPlan);
        Object[] seasons = (Object[]) wubookPlan.get("periods");
        processSeasons(plan, seasons);
        Map<String, Object> rack = (Map<String, Object>) wubookPlan.get("rack");
        if (rack != null) {
            for (Map.Entry<String, Object> rackEntry : rack.entrySet()) {
                processLiving(rackEntry, null, plan);
            }
        }
    }

    private Plan processPlan(Map<String, Object> wubookPlan) {
        long pid = number(wubookPlan, "id").longValue();
        Plan plan = planRepository.findByPid(pid);
        if (plan != null) return plan;
        plan = new Plan();
        plan.setPid(pid);
        plan.setName(string(wubookPlan, "name"));
        plan.setBoard(Plan.Board.NB);
        plan.setApproved(Boolean.TRUE);
        planRepository.save(plan);
        return plan;
    }

    private void processSeasons(Plan plan, Object[] wubookSeasons) {
        if (wubookSeasons == null) return;
        for (Object wubookSeasonObj : wubookSeasons) {
            Map<String, Object> wubookSeason = (Map<String, Object>) wubookSeasonObj;
            Season season = processSeason(plan, wubookSeason);
            Map<String, Object> values = (Map<String, Object>) wubookSeason.get("values");
            processSeasonValues(season, values);
        }
    }

    private Season processSeason(Plan plan, Map<String, Object> wubookSeason) {
        LocalDate start = LocalDate.parse(string(wubookSeason, "dfrom"), FORMATTER);
        LocalDate until = LocalDate.parse(string(wubookSeason, "dto"), FORMATTER);
        Season season = seasonRepository.findByStartAndEndAndPlanId(start, until, plan.getId());
        if (season != null) {
            return season;
        }
        season = new Season();
        season.setStart(start);
        season.setUntil(until);
        season.setApproved(Boolean.TRUE);
        season.setPlan(plan);
        seasonRepository.save(season);
        return season;
    }

    private void processSeasonValues(Season season, Map<String, Object> values) {
        for (Map.Entry<String, Object> value : values.entrySet()) {
            processLiving(value, season, null);
        }
    }

    private void processLiving(Map.Entry<String, Object> value, Season season, Plan plan) {
        BaseRoom baseRoom = baseRoomRepository.findByRid(Long.valueOf(value.getKey()));
        if (livingRepository.findByRoomAndPlanAndSeason(baseRoom, plan, season) != null) {
            return;
        }
        Living living = new Living();
        living.setSeason(season);
        living.setPlan(plan);
        living.setRoom(baseRoom);
        Object[] wubookPrices = (Object[]) value.getValue();
        living.setMon(deConvertPrice((Double) wubookPrices[0]));
        living.setTue(deConvertPrice((Double) wubookPrices[1]));
        living.setWed(deConvertPrice((Double) wubookPrices[2]));
        living.setThu(deConvertPrice((Double) wubookPrices[3]));
        living.setFri(deConvertPrice((Double) wubookPrices[4]));
        living.setSat(deConvertPrice((Double) wubookPrices[5]));
        living.setSun(deConvertPrice((Double) wubookPrices[6]));
        if (logger.isTraceEnabled()) {
            logger.trace("Saving wubookPrices: '{}' to living: {}", Arrays.toString(wubookPrices), living);
        }
        livingRepository.save(living);
    }

    private static String string(Map<String, Object> wubookMap, String key) {
        return String.valueOf(wubookMap.get(key));
    }

    public Collection<RoomType> fetchRooms(WubookAccount account) {
        return new WubookCaller<Collection<RoomType>>(account) {
            @Override
            protected Collection<RoomType> run(Wubook wubook, String token) {
                List<Map<String, Object>> wubookRooms = wubook.fetchRooms(token);
                new Printer().print(wubookRooms);
                Collection<RoomType> roomTypes = new ArrayList<>(wubookRooms.size());
                Map<Long, RoomType> ridToRoomType = new HashMap<>(wubookRooms.size());
                for (Iterator<Map<String, Object>> iterator = wubookRooms.iterator(); iterator.hasNext(); ) {
                    Map<String, Object> wubookRoom = iterator.next();
                    if (isRoomType(wubookRoom)) {
                        RoomType roomType = roomTypeRepository.findByRid(number(wubookRoom, "id").longValue());
                        if (roomType == null) {
                            roomType = roomTypeRepository.save(parseWubookRoom(wubookRoom, RoomType.class));
                        }
                        iterator.remove();
                        ridToRoomType.put(roomType.getRid(), roomType);
                        roomTypes.add(roomType);
                    }
                }
                Collection<BaseRoom> baseRooms = new ArrayList<>(wubookRooms.size());
                for (Map<String, Object> wubookRoom : wubookRooms) {
                    Assert.isTrue(!isRoomType(wubookRoom));
                    VirtualRoom virtualRoom = virtualRoomRepository.findByRid(number(wubookRoom, "id").longValue());
                    if (virtualRoom == null) {
                        virtualRoom = parseWubookRoom(wubookRoom, VirtualRoom.class);
                        RoomType parent = ridToRoomType.get(number(wubookRoom, "subroom").longValue());
                        virtualRoom.setRoomType(parent);
                        virtualRoom = baseRoomRepository.save(virtualRoom);

                        if (parent.getVirtualRooms() == null) parent.setVirtualRooms(new ArrayList<VirtualRoom>());
                        parent.getVirtualRooms().add(virtualRoom);
                    }
                    baseRooms.add(virtualRoom);
                }
                return roomTypes;
            }
        }.call();
    }

    private static <T extends WubookRoom> T parseWubookRoom(Map<String, Object> wubookRoom, Class<T> clazz) {
        T result;
        try {
            result = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("could not instantiate WubookRoom", e);
        }
        //ignoring board,boards,occupancy
        result.setRid(number(wubookRoom, "id").longValue());
        result.setName(((String) wubookRoom.get("name")).trim());
        result.setShortname(((String) wubookRoom.get("shortname")).trim());
        result.setAdults(number(wubookRoom, "men").intValue());//occupancy is the same as men
        result.setChildren(number(wubookRoom, "children").intValue());
        result.setDefaultPrice(10L);//wubook does not provide us with default price for
        result.setApproved(Boolean.TRUE);
        return result;
    }

    private static boolean isRoomType(Map<String, Object> wubookRoom) {
        return number(wubookRoom, "subroom").intValue() == 0;
    }

    private static Number number(Map<String, Object> wubookRoom, String key) {
        return (Number) wubookRoom.get(key);
    }

    public String updateRoomValues(@NotEmpty final List<RoomTypeValue> roomTypeValues, @NotNull final LocalDate dfrom, @NotNull WubookAccount account) {
        Collections.sort(roomTypeValues, RoomTypeValue.DATE_COMPARATOR);
        LocalDate nextDate = roomTypeValues.get(0).getDate();
        for (RoomTypeValue value : roomTypeValues) {
            if (!value.getDate().equals(nextDate)) {
                throw new IllegalArgumentException("roomTypeValues may not contain blanks");
            }
            nextDate = nextDate.plusDays(1);
        }
        return new WubookCaller<String>(account) {
            @Override
            protected String run(Wubook wubook, String token) {
                return wubook.updateRoomsValues(token, dfrom.toString(WUBOOK_DATE_PATTERN), parseRoomTypeValues(roomTypeValues, false));
            }
        }.call();
    }

    public String updateSparseRoomValues(final List<RoomTypeValue> values, WubookAccount account) {
        return new WubookCaller<String>(account) {
            @Override
            protected String run(Wubook wubook, String token) {
                return wubook.updateSparseRoomValues(token, parseRoomTypeValues(values, true));
            }
        }.call();
    }

    private Object[] parseRoomTypeValues(List<RoomTypeValue> values, boolean includeDates) {
        Map<Long, List<RoomTypeValue>> valuesByRoomType = new HashMap<>();
        for (RoomTypeValue value : values) {
            Long rid = value.getRoomType().getRid();
            if (!valuesByRoomType.containsKey(rid)) {
                valuesByRoomType.put(rid, new ArrayList<RoomTypeValue>());
            }
            valuesByRoomType.get(rid).add(value);
        }
        Collection<Object> updated = new ArrayList<>(valuesByRoomType.size());
        for (Map.Entry<Long, List<RoomTypeValue>> longListEntry : valuesByRoomType.entrySet()) {
            updated.add(parseRoomType(longListEntry.getKey(), longListEntry.getValue(), includeDates));
        }
        return updated.toArray();
    }

    public String updateVirtualRoomValues(final List<BaseRoomValue> values, WubookAccount account) {
        return new WubookCaller<String>(account) {
            @Override
            protected String run(Wubook wubook, String token) {
                return wubook.updateSparseRoomValues(token, parseVirtualRoomValues(values));
            }
        }.call();
    }

    private Object[] parseVirtualRoomValues(List<BaseRoomValue> values) {
        Map<Long, List<BaseRoomValue>> valuesByVirtualRoom = new HashMap<>();
        for (BaseRoomValue value : values) {
            Long rid = baseRoomRepository.findOne(value.getRoom().getId()).getRid();
            if (!valuesByVirtualRoom.containsKey(rid)) {
                valuesByVirtualRoom.put(rid, new ArrayList<BaseRoomValue>());
            }
            valuesByVirtualRoom.get(rid).add(value);
        }
        Collection<Object> updated = new ArrayList<>(valuesByVirtualRoom.size());
        for (Map.Entry<Long, List<BaseRoomValue>> longListEntry : valuesByVirtualRoom.entrySet()) {
            updated.add(parseRoom(longListEntry.getKey(), longListEntry.getValue()));
        }
        return updated.toArray();
    }

    public WubookRoomUses fetchBookings(final LocalDate dfrom, final LocalDate dto, final Boolean oncreated, final WubookAccount account) {
        return new WubookCaller<WubookRoomUses>(account) {
            @Override
            protected WubookRoomUses run(Wubook wubook, String token) {
                String tenantId = SecurityUtils.getCurrentTenantId();
                String username = SecurityUtils.getUsername();
                Object[] bookings = fetchBookings(wubook, token, account, dfrom, dto, oncreated, tenantId, username);
                List<Map<String, Object>> refused = new ArrayList<>();
                List<Map<String, Object>> confirmed = new ArrayList<>();
                for (Object bookingObject : bookings) {
                    Map<String, Object> booking = (Map<String, Object>) bookingObject;
                    Integer status = (Integer) booking.get("status");
                    switch (status) {
                        case BookingStatus.DELETED:
                        case BookingStatus.REFUSED:
                            refused.add(booking);
                            break;
                        case BookingStatus.CONFIRMED:
                        case BookingStatus.WAITING:
                        case BookingStatus.ACCEPTED:
                            confirmed.add(booking);
                            break;
                        default:
                            logger.error("{}:{}:Unsupported BookingStatus detected: {}", tenantId, username, status);
                    }
                }
                return new WubookRoomUses(confirmed, refused);
            }
        }.call();
    }

    static Object[] fetchBookings(Wubook wubook, String token, WubookAccount account, LocalDate dfrom, LocalDate dto, Boolean oncreated, String tenantId, String username) {
        Object[] bookings = wubook.fetchNewBookings(token);
        if (bookings.length == 0) {
            logger.warn("Could not find 'unfetched' bookings, trying all bookings for today for account {}", account);
            //TODO maintain situation when day changes while this call is made
            bookings = wubook.fetchBookings(token, dfrom.toString(WUBOOK_DATE_PATTERN), dto.toString(WUBOOK_DATE_PATTERN), oncreated);
        }
        if (bookings.length == WUBOOK_MAX_OUTPUT) {
            Days period = Days.daysBetween(dfrom, dto);//731
            int days = period.getDays();//731
            int chunk = days / (WUBOOK_REQUESTS_LIMIT - 3);//720/21=34
            Collection<Object> temp = new ArrayList<>();
            for (int i = 0; i <= days; i += chunk) {//i:0,34,68,102... ~ 22 times
                String start = dfrom.plusDays(i).toString(WUBOOK_DATE_PATTERN);
                String end = dfrom.plusDays(i + chunk).toString(WUBOOK_DATE_PATTERN);
                logger.debug("{}:{}:Fetching bookings from {} till {}", tenantId, username, start, end);
                Object[] found = wubook.fetchBookings(token, start, end, oncreated);
                logger.debug("{}:{}:Found totally {} bookings for this period", tenantId, username, found.length);
                temp.addAll(Arrays.asList(found));
            }
            bookings = temp.toArray();
        }
        return bookings;
    }

    public String attachPushUrlToLcode(final WubookAccount account) {
        return new WubookCaller<String>(account) {
            @Override
            protected String run(Wubook wubook, String token) {
                return wubook.pushActivation(token, applicationUrl);
            }
        }.call();
    }

    public Map<String, Object> fetchCc(final String rcode, final String pwd, @NotNull WubookAccount account) {
        return new WubookCaller<Map<String, Object>>(account) {
            @Override
            protected Map<String, Object> run(Wubook wubook, String token) {
                return wubook.fetchCc(token, rcode, pwd);
            }

            @Override
            protected void onFailure(WubookException e) {
                if (e.getCode() == ResponseCode.INVALID_INPUT)
                    throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "pwd");
                super.onFailure(e);
            }
        }.call();
    }

    // [{'dfrom': '21/12/2013', 'dto': '23/12/2013', 'values': {'1234': [1,2,3,4,5,6,7]}}]
    public void updatePlanPeriods(@NotNull final Plan plan, @NotNull Collection<Season> seasons, @NotNull WubookAccount account) {
        final List<Map<String, Object>> periods = new ArrayList<>(seasons.size());
        for (final Season season : seasons) {
            if (!season.getPlan().equals(plan)) {
                continue;
            }
            periods.add(new HashMap<String, Object>() {
                {
                    put("dfrom", season.getStart().toString(FORMATTER));
                    put("dto", season.getUntil().toString(FORMATTER));
                    put("values", new HashMap<String, Double[]>() {
                        {
                            for (Living living : season.getLivings()) {
                                put(
                                        String.valueOf(living.getRoom().getRid()),
                                        new Double[]{
                                                convertPrice(living.getMon()),
                                                convertPrice(living.getTue()),
                                                convertPrice(living.getWed()),
                                                convertPrice(living.getThu()),
                                                convertPrice(living.getFri()),
                                                convertPrice(living.getSat()),
                                                convertPrice(living.getSun())
                                        }
                                );
                            }
                        }
                    });
                }
            });
        }

        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                Map<String, Object>[] args = new HashMap[periods.size()];
                wubook.updatePlanPeriods(token, plan.getPid(), periods.toArray(args));
                return null;
            }
        }.call();

    }

    public GroupRoomUseDto parseRefuse(String rcode) {
        List<RoomUse> existing = roomUseRepository.findByRcode(String.valueOf(rcode));
        if (existing.isEmpty()) return null;
        List<RoomUseWithOverrides> roomUsesWithOverrides = new ArrayList<>();
        CustomerGroup group = existing.get(0).getCustomerGroup();
        for (RoomUse use : existing) {
            use.getCustomerGroup().setGroupMembers(null);
            roomUsesWithOverrides.add(new RoomUseWithOverrides(use));
        }
        return new GroupRoomUseDto(rcode, group, roomUsesWithOverrides);
    }

    public GroupRoomUseDto parseRoomUse(Map<String, Object> booking, Collection<Long> reservedRoomIds) {
        String[] roomIds = COMMA.split((String) booking.get("rooms"));
        if (roomIds.length == 0) return null;

        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setCustomer(parseCustomer(booking));
        customerGroup.setIncludeCustomer(true);//TODO ADD GROUP MEMBERS
        customerGroup.setPov(TOURISM);//TODO Do not forget to update on UI
        String rcode = string(booking, "reservation_code");
        List<RoomUseWithOverrides> roomUses = new ArrayList<>();
        for (int index = 0; index < roomIds.length; index++) {
            String roomId = roomIds[index];
            RoomUse roomUse = new RoomUse();
            LocalDate startDate = LocalDate.parse(string(booking, "date_arrival"), FORMATTER);
            LocalDate endDate = LocalDate.parse(string(booking, "date_departure"), FORMATTER);
            roomUse.setStartDate(startDate);
            roomUse.setEndDate(endDate);

            BaseRoom baseRoom = baseRoomRepository.findByRid(Long.valueOf(roomId));
            String message0 = String.format("Expected to find virtual room by RID:%s", roomId);
            if (baseRoom == null) {
                logger.error(message0);
                broadcastService.broadcastBookingFailed(Collections.<BaseGroupRoomUse>singletonList(roomUse));
                continue;
            }
            List<Room> freeRooms = roomUseRepository.getFreeRooms(baseRoom.roomType().getId(), startDate, endDate);

            for (Room freeRoom : freeRooms) {
                if (!reservedRoomIds.contains(freeRoom.getId())) {
                    logger.debug("Assigning room:{}/{} to rid:{}", freeRoom.getId(), freeRoom.getRoomType(), roomId);
                    roomUse.setRoom(freeRoom);
                    reservedRoomIds.add(freeRoom.getId());
                    break;
                }
            }
            /*
            if (roomUse.getRoom() == null) {
                String message = "Cannot book room with RID '%s' - not enough free rooms found for dates: %s - %s";
                String msg = String.format(message, roomId, startDate, endDate);
                logger.error(msg);//TODO maintain this somehow, consider informing administrators
                WebSocketEndpoint.broadcast(msg, WebSocketMessage.NOT_ENOUGH_ROOMS_SINGLE, SecurityUtils.getCurrentTenantId());
                continue;
            }
            */

            roomUse.setBaseRoom(baseRoom);
            if (booking.containsKey("customer_notes")) {
                roomUse.setComment(string(booking, "customer_notes"));
            }
            roomUse.setCustomerGroup(customerGroup);
            roomUse.setSource(parseSource((Integer) booking.get("id_channel")));

            Plan plan = selectPlan(index, roomUse.getBaseRoom().getId(), (Map<String, Object>) booking.get("ancillary"));

            if (plan == null) {
                List<Plan> plans = planRepository.findAll();
                Assert.isTrue(!plans.isEmpty(), "Expected to find at least one plan");
                Assert.isTrue(plans.get(0).getApproved(), "Expected for plan '" + plan + "' to be approved");
                plan = plans.get(0);
            }

            roomUse.setPlan(plan);

            roomUse.setRcode(rcode);
            roomUse.setStatus(BaseGroupRoomUse.Status.BOOKING_FREE);

            //Parse prices
            RoomUseWithOverrides result = new RoomUseWithOverrides(roomUse);
            Object[] prices = (Object[]) ((Map) booking.get("dayprices")).get(roomId);
            for (int i = 0; i < prices.length; i++) {
                Double price = (Double) prices[i];
                LocalDate date = startDate.plusDays(i);
                result.getPrices().put(date, deConvertPrice(price));
            }
            roomUses.add(result);
        }

        return new GroupRoomUseDto(rcode, customerGroup, roomUses);//TODO PROPERLY HANDLE GROUP BOOKING OF DIFFERENT RCODES
    }

    public Plan selectPlan(int index, Long baseRoomId, Map<String, Object> ancillary) {//TODO parse something useful
        if (ancillary == null) return null;
        for (Map.Entry<String, Object> ancilla : ancillary.entrySet()) {
            if (ancilla.getKey().startsWith("Room (" + index + ")")) {
                Map<String, Object> value = (Map<String, Object>) ancilla.getValue();
                Object[] rates = (Object[]) value.get("Daily rates");
                Plan planCandidate = null;
                for (Object rate : rates) {
                    //i.e. 2014-03-30: 155.00 (Half-Board Rate)
                    String rateString = (String) rate;
                    String[] split = SPACE.split(rateString);
                    LocalDate date = LocalDate.parse(StringUtils.chop(split[0]), BOOKING_FORMATTER);
                    long rateValue = deConvertPrice(Double.parseDouble(split[1]));
                    //TODO Improve Living Spec
                    List<Living> livings = livingRepository.findAll(new LivingSpecification(baseRoomId, date, rateValue));
                    for (Living living : livings) {
                        Season season = living.getSeason();
                        if (season != null && season.getStart().compareTo(date) <= 0 && season.getUntil().compareTo(date) > 0) {
                            return season.getPlan();//Return perfect match
                        }
                    }
                    Matcher matcher = BOARD.matcher(rateString);
                    String boardName = matcher.find() ? matcher.group(1) : null;
                    for (Living living : livings) {
                        Plan plan = living.getPlan();
                        if (plan != null && plan.getApproved()) {
                            planCandidate = plan;
                            if (plan.getBoard() == Plan.Board.forName(boardName)) {
                                return plan;//Return wild guess between equally suitable plans
                            }
                        }
                    }
                }
                //Did not find full season or plan match in correspondent ancilla
                return planCandidate;
            }
        }
        return null;
    }

    private RoomUse.Source parseSource(int id_channel) {
        switch (id_channel) {
            case 0:
                return WUBOOK_BUTTON;
            case 1:
                return EXPEDIA;
            case 2:
                return BOOKING;
            case 3:
                return HOTEL_DE;
            case 4:
                return ITWG;
            case 5:
                return IN_ITALIA;
            case 6:
                return HOTELS_COM;
            case 7:
                return RESERVER_IT;
            case 8:
                return ACCOMODATIONZ_COM;
            case 9:
                return HOTEL_BEDS;
            case 10:
                return VENERE_COM;
            case 11:
                return HRS_COM;
            case 13:
                return TRAVEL_EUROPE;
            case 14:
                return ATRAPALO;
            case 15:
                return MBE_TRAVEL;
            case 16:
                return ESCAPIO;
            case 17:
                return BB_PLANET;
            case 18:
                return SPLENDIA;
            case 19:
                return AGODA;
            case 20:
                return HOSTELS_CLUB;
            case 21:
                return LASTMINUTE;
            case 22:
                return TRAVELOCITY;
            case 23:
                return SABRE;
            case 24:
                return BUDGETPLACES;
            case 25:
                return ORBITZ;
            case 26:
                return LATE_ROOMS;
            case 27:
                return IN_TOSCANA;
            case 28:
                return HOSTEL_WORLD;
            case 29:
                return HOSTELBOOKERS;//HostelBookers
            case 30:
                return HOTELITA;//Hotelita
            case 31:
                return I_CASTELLI;//I Castelli
            case 32:
                return VASHOTEL;//Vashotel
            case 33:
                return SLEEPINGIN;//SleepingIn
            case 34:
                return OKTOGO;//OkToGo
            case 35:
                return FERATEL;//Feratel
            case 36:
                return FENILOT;//Fenilot
            case 37:
                return SYNXIS;//Synxis
            case 38:
                return TABLETHOTELS;//Tablethotels
            case 39:
                return EASYTOBOOK;//Easytobook
            case 40:
                return VIVAFIRENZE;//VivaFirenze
            case 41:
                return OSTROVOK;//Ostrovok
            case 42:
                return HOTUSA;//Hotusa
            case 43:
                return AIRBNB;//AirBnB
            case 44:
                return CLEARINGSTATION;//ClearingStation
            case 45:
                return WIMDU;//Wimdu
            case 46:
                return HOUSE_TRIP;//House Trip
            case 47:
                return HOMEAWAY;//HomeAway
            case 48:
                return HOLIDAY_VELVET;//Holiday Velvet
            case 49:
                return FLIPKEY;//FlipKey
            case 50:
                return THE9FLATS;//9Flats
            case 51:
                return GAY_VILLE;//Gay-Ville
            case 52:
                return AMSTERDAM_BED_AND_BREAKFASTS;//Amsterdam bed and breakfasts
            case 53:
                return APARTMENTS_UNLIMITED;//Apartments Unlimited
            case 54:
                return BE_MY_GUEST;//Be My Guest
            case 55:
                return ERFGOED;//Erfgoed
            case 56:
                return LOVING_APARTMENTS;//Loving Apartments
            case 57:
                return ONLY_APARTMENTS;//Only Apartments
            case 58:
                return ELONG_EXPEDIA;//Elong (Expedia)
            case 59:
                return FLAT_CLUB;//Flat Club
            case 60:
                return TRAVELREPUBLIC;//TravelRepublic
            case 61:
                return THE_101_HOTELS;//101 hotels
            case 62:
                return BEDANDBREAKFAST_EU;//BedAndBreakfast.eu
            case 63:
                return FEDERALBERGHI;//Federalberghi
            case 64:
                return CAPRI_ONLINE;//Capri Online
            case 65:
                return BED_AND_BREAKFAST_IT;//Bed-And-Breakfast.it
            case 66:
                return BRONEVIK;//Bronevik
            case 67:
                return GARDAPASS;//Gardapass
            case 68:
                return POSITANO_COM_CAPRIONLINE;//Positano.com (Caprionline)
            case 69:
                return ITALYTRAVELLER_CAPRIONLINE;//ItalyTraveller (Caprionline)
            case 70:
                return CAPRI_IT_CAPRIONLINE;//Capri.it (Caprionline)
            case 71:
                return CAPRI_NET_CAPRIONLINE;//Capri.net (Caprionline)
            case 72:
                return INNTOPIA;//Inntopia
            case 73:
                return MR_AND_MRS_SMITH;//Mr and Mrs Smith
            case 74:
                return HOTELINN;//HotelInn
            case 75:
                return THEBESTSPAHOTELS;//TheBestSpaHotels
            case 76:
                return CLEARTRIP;//Cleartrip
            case 77:
                return TRAVEL_GURU;//Travel Guru
            case 78:
                return AIRBNB;//AirBnB
            case 79:
                return BOOKING_PIEMONTE;//Booking Piemonte
            case 80:
                return BESTDAY;//Bestday
            case 81:
                return JACTRAVEL;//JacTravel
            case 82:
                return MOENA;//Moena
            default:
                throw new IllegalArgumentException("Unrecognized channel id: " + id_channel);

        }
    }

    Adult parseCustomer(Map<String, Object> booking) {
        Adult customer;
        String email = string(booking, "customer_mail");
        if (isEmailValid(email)) {
            if ((customer = adultRepository.findByEmail(email)) != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Re-using existing customer by email: {}", customer);
                }
                return customer;
            }
        }
        customer = new Adult();
        if (isEmailValid(email)) {
            customer.setEmail(email);
        }
        customer.setFirstName(string(booking, "customer_name"));
        customer.setLastName(string(booking, "customer_surname"));
        customer.setPhone(string(booking, "customer_phone"));
        if (booking.containsKey("customer_address")) {
            customer.setAddress(string(booking, "customer_address"));
        }
        return adultRepository.saveAndFlush(customer);
    }

    private static boolean isEmailValid(String email) {
        return !"null".equals(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    public long addPlan(final String name, @NotNull WubookAccount account) {
        return new WubookCaller<Long>(account) {
            @Override
            protected Long run(Wubook wubook, String token) {
                return wubook.addPlan(token, name);
            }
        }.call();
    }

    public void updatePlanRack(final long pid, final List<Living> livings, @NotNull WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                Map<String, Object[]> result = new HashMap<>();
                for (Living living : livings) {
                    String rid = String.valueOf(living.getRoom().getRid());
                    Assert.isTrue(!result.containsKey(rid));
                    result.put(rid,
                            new Object[]{convertPrice(living.getMon()), convertPrice(living.getTue()), convertPrice(living.getWed()), convertPrice(living.getThu()), convertPrice(living.getFri()), convertPrice(living.getSat()), convertPrice(living.getSun())});
                }
                wubook.updatePlanRack(token, pid, result);
                return null;
            }
        }.call();
    }

    public void delPlan(final long pid, @NotNull WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                wubook.delPlan(token, pid);
                return null;
            }
        }.call();
    }

    public VirtualPlan createVirtualPlan(final VirtualPlan plan, @NotNull WubookAccount account) {
        Long id = new WubookCaller<Long>(account) {
            @Override
            protected Long run(Wubook wubook, String token) {
                return wubook.addVPlan(token, plan.getName(), plan.getPlan().getPid(), plan.getVariation().getValue(), convertPrice(plan.getValue()));
            }
        }.call();
        plan.setPid(id);
        return virtualPlanRepository.save(plan);
    }

    public void exportVirtualPlans(final Iterable<VirtualPlan> virtualPlans, WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                for (VirtualPlan virtualPlan : virtualPlans) {
                    virtualPlan.setPid(wubook.addVPlan(token, virtualPlan.getName(), virtualPlan.getPlan().getPid(), virtualPlan.getVariation().getValue(), convertPrice(virtualPlan.getValue())));
                }
                return null;
            }
        }.call();
    }

    public void exportRestrictionPlans(final Iterable<CompactRestriction> restrictionPlans, WubookAccount account) {
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                for (CompactRestriction restriction : restrictionPlans) {
                    restriction.setPid(wubook.addRPlan(token, restriction.getName(), true));
                }
                return null;
            }
        }.call();
    }

    public CompactRestriction createCompactRestriction(final CompactRestriction restriction, @NotNull WubookAccount account) {
        final Long id = new WubookCaller<Long>(account) {
            @Override
            protected Long run(Wubook wubook, String token) {
                return wubook.addRPlan(token, restriction.getName(), true);
            }
        }.call();
        restriction.setPid(id);
        compactRestrictionRepository.setPid(id, restriction);
        updateRestrictionRules(restriction, account);
        return restriction;
    }

    public void updateRestrictionRules(final CompactRestriction restriction, @NotNull WubookAccount account) {
        final Map<String, Object> rules = new HashMap<String, Object>() {
            {
                putIfNotNull("min_stay", restriction.getMinStay());
                putIfNotNull("min_stay_arrival", restriction.getMinStayArrival());
                putIfNotNull("max_stay", restriction.getMaxStay());
                putIfNotNull("closed", parseClosed(restriction.getClosed()));
                putIfNotNull("closed_departure", restriction.getClosedToDeparture());
            }

            private int parseClosed(RoomClosed closed) {
                switch (closed) {
                    case OPEN:
                        return 0;
                    case CLOSED_TO_CHECKIN:
                        return 1;
                    case CLOSED:
                        return 2;
                    default:
                        throw new IllegalArgumentException("unsupported RoomClosed value: " + closed);
                }
            }

            private void putIfNotNull(String key, Object val) {
                if (val != null) {
                    put(key, val);
                }
            }
        };
        new WubookCaller<Void>(account) {
            @Override
            protected Void run(Wubook wubook, String token) {
                wubook.updateRules(token, restriction.getPid(), rules);
                return null;
            }
        }.call();
    }

    private static Object parseRoom(final long rid, final Collection<BaseRoomValue> roomTypeValue) {
        return new HashMap<String, Object>() {{
            put("id", rid);
            List<Map<String, Object>> rates = new ArrayList<>(roomTypeValue.size());
            for (final BaseRoomValue value : roomTypeValue) {
                rates.add(new HashMap<String, Object>() {
                    {
//                    put("booked", ???);
//                        putIfNotNull("price", value.getAdultBedPrice());
                        putIfNotNull("no_ota", value.getOtaAllowed() ? 0 : 1);
                        putIfNotNull("min_stay", value.getMinStay());
                        putIfNotNull("min_stay_arrival", value.getMinStayArrival());
                        putIfNotNull("max_stay", value.getMaxStay());
                        putIfNotNull("closed", value.getClosed().getValue());
//                    putIfNotNull("closed_arrival", ???);
                        putIfNotNull("closed_departure", value.getClosedToDeparture());
                        putIfNotNull("date", value.getDate().toString(WUBOOK_DATE_PATTERN));
                    }

                    private void putIfNotNull(String key, Object val) {
                        if (val != null) {
                            put(key, val);
                        }
                    }
                });
            }
            put("days", rates.toArray());
        }};
    }

    private static Object parseRoomType(final long rid, final Collection<RoomTypeValue> roomTypeValue, final boolean includeDates) {
        return new HashMap<String, Object>() {{
            put("id", rid);
            List<Map<String, Object>> rates = new ArrayList<>(roomTypeValue.size());
            for (final RoomTypeValue value : roomTypeValue) {
                rates.add(new HashMap<String, Object>() {
                    {
                        putIfNotNull("avail", value.getRoomsAvailable());
                        if (includeDates) {
                            putIfNotNull("date", value.getDate().toString(WUBOOK_DATE_PATTERN));
                        }
                    }

                    private void putIfNotNull(String key, Object val) {
                        if (val != null) {
                            put(key, val);
                        }
                    }
                });
            }
            put("days", rates.toArray());
        }};
    }

    private static double convertPrice(long price) {
        return price / 100.0;
    }

    private static long deConvertPrice(Double price) {
        return (long) (price * 100L);
    }

    private static class LivingSpecification implements Specification<Living> {
        private final Long baseRoomId;
        private final LocalDate date;
        private final long rateValue;

        public LivingSpecification(Long baseRoomId, LocalDate date, long rateValue) {
            this.baseRoomId = baseRoomId;
            this.date = date;
            this.rateValue = rateValue;
        }

        @Override
        public Predicate toPredicate(Root<Living> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            String dayOfWeek = date.dayOfWeek().getAsText(Locale.ENGLISH).substring(0, 3).toLowerCase();
            Path<BaseRoom> room = root.<BaseRoom>get("room");
            Predicate ridPredicate = cb.equal(room.<Long>get("id"), baseRoomId);
            Predicate dowPredicate = cb.equal(root.get(dayOfWeek), rateValue);
            Predicate dowAndRoom = cb.and(ridPredicate, dowPredicate);

//            Path<Season> seasonJoin = root.<Season>get("season");
//            Predicate start = cb.lessThanOrEqualTo(seasonJoin.<LocalDate>get("start"), date);
//            Predicate until = cb.greaterThan(seasonJoin.<LocalDate>get("until"), date);
//            Predicate seasonDates = cb.and(start, until);
//            Predicate season = cb.or(seasonDates,cb.isNull(seasonJoin));
            return dowAndRoom;
        }
    }
}
