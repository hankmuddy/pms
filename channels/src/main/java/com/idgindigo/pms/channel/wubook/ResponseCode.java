package com.idgindigo.pms.channel.wubook;

/**
 * @author vomel
 * @since 17.12.13 15:37
 */
public interface ResponseCode {

    int OK = 0;//Ok
    int AUTH_FAILED = -1;//	Authentication Failed
    int INVALID_TOKEN = -2;//	Invalid Token
    int SERVER_BUSY = -3;//	Server is busy: releasing tokens is now blocked. Please, retry again later
    int TOO_HIGH_REQUESTS_FREQ = -4;//	Token Request: requesting frequence too high
    int TOKEN_EXPIRED = -5;//	Token Expired
    int INACTIVE_LODGING = -6;//	Lodging is not active
    int INTERNAL_ERROR = -7;//	Internal Error
    int TOKEN_OVERUSED = -8;//	Token used too many times: please, create a new token
    int INVALID_ROOMS = -9;//	Invalid Rooms for the selected facility
    int INVALID_LCODE = -10;//Invalid lcode
    int SHORTNAME_NOT_UNIQUE = -11;//	Shortname has to be unique. This shortname is already used
    int SPECIAL_OFFER_INVOLVED = -12;//	Room Not Deleted: Special Offer Involved
    int WRONG_ARGUMENTS = -13;//	Wrong call: pass the correct arguments, please
    int NOT_SAME_DAYS_NUMBER = -14;//	Please, pass the same number of days for each room
    int PLAN_ALREADY_USED = -15;//	This plan is actually in use
    int FLOODING_DETECTED_ROOM = -16; //Flooding detected: more than 1010 daily updates for the same room in 3 minutes
    int NO_CC = -17; //No CC for this reservation
    int FLOODING_DETECTED_REQUEST = -18; //Flooding detected: more than 16000 daily updates on the same request
    int INVALID_DATE = -19; //Invalid date: past date
    int DYNAMIC_ERROR = -20; //Dynamic human readable error
    int DYNAMIC_MESSAGE = -21; //Dynamic message
    int INVALID_INPUT = -100;//Invalid Input
    int CHECK_DATES_OR_RESTRICTIONS = -101;//Malformed dates or restrictions unrespected
    int MALFORMED_REQUEST = -102; //Malformed request, inexistent function or wrong function arguments
    int INVALID_CODE = -1000;//Invalid Lodging/Portal code
    int INVALID_DATES = -1001;//Invalid Dates
    int BOOKING_NOT_INITIALIZED = -1002;//Booking not Initialized: use facility_request()
    int OBJECTS_UNAVAILABLE = -1003;//Objects not Available
    int INVALID_CUSTOMER_DATA = -1004;//Invalid Customer Data
    int INVALID_CARD = -1005;//Invalid Credit Card Data or Credit Card Rejected
    int INVALID_IATA = -1006;//Invalid Iata
    int ROOM_NOT_REQUESTED = -1007;//No room was requested: use rooms_request()
    int INVALID_ALIEN_CODE = -1008; //Invalid alien code
    int AMOUNT_ARGUMENT = -1009; //Amount Argument
    int ORIGIN_FIELD_NOT_SET = -1010; //Origin Field Not Setted
    int INVALID_BOARD = -1011; //Invalid board
    int ACTION_FAILED = -2001; //Cannot perform action for the given lodging
    int INVALID_ACCOUNT_ID = -2002; //Invalid account id
    int LODGING_NOT_FOUND = -2003; //Cannot find lodging
    int CHANNEL_DISABLED = -2004; //Channel is not enabled

}
