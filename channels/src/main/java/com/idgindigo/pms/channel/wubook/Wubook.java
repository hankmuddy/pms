package com.idgindigo.pms.channel.wubook;


import java.util.List;
import java.util.Map;

/**
 * @author vomel
 * @since 16.12.13 16:31
 */
public interface Wubook {
//new room(token, lcode, woodoo, name, beds, defprice, avail, shortname, defboard [, names, descriptions, boards, rtype])

    /**
     * Use this API to add a new room.
     * Almost all parameters are probably trivial enough and they don't require too many explanation. The parameters names,
     * descriptions and boards are complex XMLRPC types. Names are a simple struct: {'it': 'Italian name', 'en': 'English Name'}. The same applies for the descriptions parameter: {'it': 'Italian description', 'en': 'English Description'}. The boards parameter is more complex. For each additional board, you must specify a price variation (which is a variation type and a variation value). The boards parameter is a nested struct. We provide 4 types of price variation: Static increase (dtype= 2), Static decrease (dtype= -2), Percentage increase (dtype= 1), Percentage decrease (dtype= -1).
     *
     * @param woodoo    WooDoo Ony Flag. If true (1), this room will not be sold on the WuBook Online Reception (booking engine)
     * @param name      the name of the room
     * @param beds      the default occupancy of the room (number of adults)
     * @param defprice  the default price of the room. This is the price used when no specific informations have been saved for a day, >= 6
     * @param avail     the default availability of the room. This is the availability used when no specific informations have been saved for a day
     * @param shortname the room shortname: unique, max 4 chars
     * @param defboard  default board for the room. It can be nb (no board, room only), bb (breakfast), hb (half board), fb (full board)
     * @return WuBook will return its ID once inserted.
     */
    long newRoom(String token, String name, int beds, double defprice, int avail, String shortname, String defboard /*[, names, descriptions, boards, rtype]*/);

//new_virtual_room(token, lcode, rid, woodoo, name, beds, children, defprice, shortname, defboard[, names, descriptions, boards])

    /**
     * @param token     token
     * @param rid       wubook room ID
     * @param name      the name of the room
     * @param beds      the default occupancy of the room (number of adults)
     * @param children  the number of children
     * @param defprice  the default price of the room. This is the price used when no specific informations have been saved for a day, >= 6
     * @param shortname the room shortname: unique, max 4 chars
     * @param defboard  default board for the room. It can be nb (no board, room only), bb (breakfast), hb (half board), fb (full board)
     * @return WuBook will return its ID once inserted
     */
    long newVirtualRoom(String token, long rid, String name, int beds, int children, double defprice, String shortname, String defboard/*[, names, descriptions, boards]*/);

//fetch rooms(token, lcode)

    /**
     * Returns the rooms for the property identified by lcode. For each room, Wired! returns the following parameters:
     * id: the room id, used to identify the room
     * name: the room name
     * shortname: the room shortname: unique
     * occupancy: the room occupancy
     * men: adults (note: adults + children = occupancy)
     * children: children (note: adults + children = occupancy)
     * subroom: mother room
     * When subroom is 0 (zero), the returned room is a true and independent room.
     * When subroom is not 0, this value is a room id. It means that this room is sharing the availability with the room identified by the subroom value. Only availability is shared. You can update the other values (price, restrictions and so on). If you try to update a subroom availability, nothing happens.
     *
     * @return rooms for the property identified by lcode
     */
    List<Map<String, Object>> fetchRooms(String token);

//mod room(token, lcode, rid, name, beds, defprice, avail, shortname, defboard[, names, descriptions, boards])

    /**
     * @param token     token
     * @param rid       wubook room ID
     * @param name      the name of the room
     * @param beds      the default occupancy of the room (number of adults)
     * @param defprice  the default price of the room. This is the price used when no specific informations have been saved for a day, >= 6
     * @param avail     the default availability of the room. This is the availability used when no specific informations have been saved for a day
     * @param shortname the room shortname: unique, max 4 chars
     * @param defboard  default board for the room. It can be nb (no board, room only), bb (breakfast), hb (half board), fb (full board)
     * @return WuBook will return its ID once modified.
     */
    long modRoom(String token, long rid, String name, int beds, double defprice, int avail, String shortname, String defboard/*[, names, descriptions, boards]*/);

//mod_virtual_room(token, lcode, rid,  name, beds, children, defprice, short, defboard[, names, descriptions, boards])

    /**
     * @param token     token
     * @param rid       wubook room ID
     * @param name      the name of the room
     * @param beds      the default occupancy of the room (number of adults)
     * @param children  the number of children
     * @param defprice  the default price of the room. This is the price used when no specific informations have been saved for a day, >= 6
     * @param shortname the room shortname: unique, max 4 chars
     * @param defboard  default board for the room. It can be nb (no board, room only), bb (breakfast), hb (half board), fb (full board)
     * @return WuBook will return its ID once modified.
     */
    long modVirtualRoom(String token, long rid, String name, int beds, int children, double defprice, String shortname, String defboard/*[, names, descriptions, boards]*/);

//del room(token, lcode, rid)

    /**
     * Delete the Room identified by rid.
     *
     * @param rid Room Identifier. Virtual room is associated to this room
     */
    void delRoom(String token, long rid);

//fetch_rooms_values(token, lcode, dfrom, dto[, rooms])

    Map<String, Object[]> fetchRoomsValues(String token, String dfrom, String dto/*[, rooms]*/);

//update_rooms_values(token, lcode, dfrom, rooms)

    /**
     * <p/>
     * This call is really powerful and can be used to modify each room values, for each day.
     * <p/>
     * It modifies the rooms values for the facility identified by lcode
     * <p/>
     * Each day item can contain the following, optional values: price, avail, min_stay, min_stay_arrival, max_stay, closed, closed_departure, no_ota
     * <p/>
     * <p/>
     * closed can be 0, 1 or 2. If 0, room is open. If 1, room is closed. If 2, room is closed to check-in (close to arrival). no_ota can be 0 or 1. If 1, connected channels are closed.
     * <p/>
     * All values are optional: passing a void item, nothing is updated. Only specified values are updated. The possiility to send empty dates makes possible to use update_rooms_values() also for isolated days. However, you can also check update_sparse_rooms_values(): it allows to send single and isolated days.
     *
     * @param token token
     * @param dfrom is the starting date. Days will be passed as ordered lists.
     * @param rooms is a structure containing a list of rooms. Each room contains a list of days. When you send an update impacting multiple rooms, these lists must have the same length
     * @return "Ok"
     */
    String updateRoomsValues(String token, String dfrom, Object[] rooms);

//update_sparse_rooms_values(token, lcode,  rooms)

    /**
     * This call is the update_rooms_values()'s syster. Differently by update_rooms_values(), it allows to specify isolated days. The rooms parameter has the same structure of the rooms parameter used on update_rooms_values(), but each day must contain a dates
     *
     * @param token token
     * @param rooms parameter has the same structure of the rooms parameter used on update_rooms_values(), but each day must contain a date.
     * @return "Ok"
     */
    String updateSparseRoomValues(String token, Object[] rooms);

//add_plan(tok, lcode, name)

    long addPlan(String token, String name);


//get_plans(tok, lcode, pid)

    List<Map<String, Object>> getPlans(String token);

    Object[] getPlans(String token, long pid);

//del_plan(tok, lcode, pid)

    void delPlan(String token, long pid);

//update_plan_rack(tok, lcode, pid, rack

    void updatePlanRack(String token, long pid, Map<String, Object[]> rack);

//update_plan_periods

    void updatePlanPeriods(String token, long pid, Map<String, Object>[] periods);

    String getToken();

    void release(String token);

//fetch_bookings(token, lcode [, dfrom= None, dto= None, oncreated= True, ancillary= False])

    Object[] fetchNewBookings(String token);

    /**
     * Do you want to fetch reservations? Use this call. When you specify the params dfrom and dto, all reservations made between these dates are returned.
     * Nothing more happens. If oncreated is False, dates are applied against the arrival date.
     * When ancillary is True, an additional tag is returned: < ancillary >. You will find inside this tag a struct (key= value).
     * This tag contains channel information (for each OTA, we send different informations).
     * When you don't provide a dates filter (dfrom and dto), you're making a free call. In this case, only Unfetched bookings are retrieved.
     * Moreover, these bookings are marked as Fetched: next free calls will not retrieve them.
     * Note that, when a reservation is modified (cancelled, or amount change), the reservation is marked as Unfetched.
     * At the next free call, the reservation will be returned with the same id. It's your task to handle modified and deleted reservations.
     * <p/> See https://sites.google.com/site/wubookdocs/wired/wired-pms-xml#c5 for details
     *
     * @param token     token
     * @param dfrom     booking received date <strong>dd/MM/yyyy</strong>
     * @param dto       booking received date <strong>dd/MM/yyyy</strong>
     * @param oncreated when <strong>False</strong>, dates are applied against the arrival date
     * @param ancillary When <strong>True</strong>, an additional tag is returned: &lt;ancillary&gt;. You will find inside this tag a struct (key= value). This tag contains channel information (for each OTA, we send different informations).
     * @return bookings
     */
    Object[] fetchBookings(String token, String dfrom, String dto, Boolean oncreated);

    Map<String, Object> fetchCc(String token, String rcode, String pwd);

//push_activation(token, lcode, url)

    String pushActivation(String token, String url);

    String getPushUrl(String token);

//add_vplan(token, lcode, name, pid, dtype, value)

    long addVPlan(String token, String name, long pid, int dtype, double value);

    /**
     * rplan_add_rplan(token, lcode, name, compact)
     * <p/>
     * This function creates a new restriction plan. With compact (use 0 or 1) you choose if the rplan has to be compact or granular. This
     * function returns the ID of the new restriction plan.
     *
     * @param token
     * @param name
     * @param compact
     * @return
     */
    long addRPlan(String token, String name, boolean compact);

    /**
     * rplan_update_rplan_rules(token, lcode, rpid, rules)
     * <p/>
     * To update a compact restriction plans, specifying the values of its -static- restrictions. Possible and mandatory values contatined on rules (which is a simple struct)
     * are min_stay, min_stay_arrival, max_stay, closed, closed_departure.
     * <p/>
     * With "closed" == 1, you setup the closed_arrival restriction. If you want to close the room, use closed = "2".
     */
    void updateRules(String token, long rpid, Map<String, Object> rules);

    List<Map<String, Object>> getRPlans(String token);

    Map<String, Object[]> getChannelSymbols(String token);
}
