DROP SCHEMA IF EXISTS logins CASCADE;
CREATE SCHEMA logins
  AUTHORIZATION postgres;
SET search_path TO logins;
CREATE TABLE activation
(
  id                BIGINT PRIMARY KEY NOT NULL,
  active            BOOL               NOT NULL,
  createddate       TIMESTAMP,
  updateddate       TIMESTAMP,
  email             VARCHAR(255),
  firstname         VARCHAR(255),
  activationkey     VARCHAR(255),
  lastname          VARCHAR(255),
  phone             VARCHAR(255),
  position          VARCHAR(255),
  authentication_id BIGINT
);
CREATE TABLE authentication
(
  usertype      VARCHAR(31)        NOT NULL,
  id            BIGINT PRIMARY KEY NOT NULL,
  active        BOOL               NOT NULL,
  lastloggedin  TIMESTAMP,
  createddate   TIMESTAMP,
  updateddate   TIMESTAMP,
  language      VARCHAR(255),
  password      VARCHAR(255),
  username      VARCHAR(255),
  status        VARCHAR(255),
  hotel_id      BIGINT,
  supervisor_id BIGINT
);
CREATE TABLE bookingbuttonsettings
(
  id              BIGINT PRIMARY KEY NOT NULL,
  active          BOOL               NOT NULL,
  createddate     TIMESTAMP,
  updateddate     TIMESTAMP,
  backgroundcolor VARCHAR(255),
  textcolor       VARCHAR(255),
  currency        VARCHAR(255),
  language        VARCHAR(255),
  name            VARCHAR(255),
  hotel_id        BIGINT             NOT NULL,
  cancel          BOOL DEFAULT TRUE  NOT NULL,
  width           INT,
  height          INT
);
CREATE TABLE bookingbuttonsettingsvalues
(
  id          BIGINT PRIMARY KEY NOT NULL,
  active      BOOL               NOT NULL,
  createddate TIMESTAMP,
  updateddate TIMESTAMP,
  key         VARCHAR(255),
  value       VARCHAR(255),
  bbs_id      BIGINT             NOT NULL
);
CREATE TABLE facility
(
  type        VARCHAR(31)        NOT NULL,
  id          BIGINT PRIMARY KEY NOT NULL,
  active      BOOL               NOT NULL,
  createddate TIMESTAMP,
  updateddate TIMESTAMP,
  name        VARCHAR(255)       NOT NULL
);
CREATE TABLE hibernate_sequences
(
  sequence_name          VARCHAR(255),
  sequence_next_hi_value INT
);
CREATE TABLE hotel
(
  id            BIGINT PRIMARY KEY         NOT NULL,
  active        BOOL                       NOT NULL,
  createddate   TIMESTAMP,
  updateddate   TIMESTAMP,
  blocked       BOOL,
  importstatus  VARCHAR(32) DEFAULT 'INIT' NOT NULL,
  lcode         VARCHAR(255),
  maxrooms      INT,
  paiduntil     DATE,
  tenantid      VARCHAR(255),
  wuname        VARCHAR(255),
  wupass        VARCHAR(255),
  info_id       BIGINT,
  manager_id    BIGINT,
  supervisor_id BIGINT,
  extranet      BOOL                       NOT NULL DEFAULT FALSE
);
CREATE TABLE hotelinfo
(
  id                      BIGINT PRIMARY KEY NOT NULL,
  active                  BOOL               NOT NULL,
  createddate             TIMESTAMP,
  updateddate             TIMESTAMP,
  accountphone            VARCHAR(255),
  bookphone               VARCHAR(255),
  checkin                 INT,
  checkout                INT,
  city                    VARCHAR(255),
  country                 VARCHAR(255),
  currency                VARCHAR(255),
  description             VARCHAR(2000),
  earlycheckinend         INT,
  earlycheckinnobreakfast BOOL               NOT NULL,
  earlycheckinstart       INT,
  email                   VARCHAR(255),
  importantinfo           TEXT,
  index                   VARCHAR(255),
  infophone               VARCHAR(255),
  latecheckoutend         INT,
  latecheckoutstart       INT,
  latitude                DOUBLE PRECISION,
  logo                    VARCHAR(255),
  longitude               DOUBLE PRECISION,
  mainphoto               TEXT,
  multiplier              INT,
  name                    VARCHAR(255),
  officialaddress         VARCHAR(255),
  paymentinfo             TEXT,
  postaddress             VARCHAR(255),
  timezone                VARCHAR(255),
  tourismtax              REAL               NOT NULL,
  tourismtaxfromfullprice BOOL               NOT NULL,
  vat                     REAL               NOT NULL,
  website                 VARCHAR(255),
  color                   VARCHAR(255)
);
CREATE TABLE hoteltofacility
(
  id          BIGINT PRIMARY KEY NOT NULL,
  active      BOOL               NOT NULL,
  createddate TIMESTAMP,
  updateddate TIMESTAMP,
  chargefree  VARCHAR(255)       NOT NULL,
  facility_id BIGINT             NOT NULL,
  hotel_id    BIGINT             NOT NULL
);
ALTER TABLE activation ADD FOREIGN KEY (authentication_id) REFERENCES authentication (id);
CREATE UNIQUE INDEX uk_dxnwnxap40r8vrnb8lsa2cfki ON activation (activationkey);
ALTER TABLE authentication ADD FOREIGN KEY (supervisor_id) REFERENCES authentication (id);
ALTER TABLE authentication ADD FOREIGN KEY (hotel_id) REFERENCES hotel (id);
CREATE UNIQUE INDEX uk_ab33jgv4acxpjehvyugm4y3da ON authentication (username, hotel_id);
ALTER TABLE bookingbuttonsettings ADD FOREIGN KEY (hotel_id) REFERENCES hotel (id);
ALTER TABLE bookingbuttonsettingsvalues ADD FOREIGN KEY (bbs_id) REFERENCES bookingbuttonsettings (id);
CREATE UNIQUE INDEX uk_b4cnlopmbs70qjswc9umy6bkj ON facility (name);
ALTER TABLE hotel ADD FOREIGN KEY (manager_id) REFERENCES authentication (id);
ALTER TABLE hotel ADD FOREIGN KEY (supervisor_id) REFERENCES authentication (id);
ALTER TABLE hotel ADD FOREIGN KEY (info_id) REFERENCES hotelinfo (id);
ALTER TABLE hotel ADD CONSTRAINT uk_cd3yn9ustwkyg5vsjers3e0ne UNIQUE (lcode);
ALTER TABLE hoteltofacility ADD FOREIGN KEY (facility_id) REFERENCES facility (id);
ALTER TABLE hoteltofacility ADD FOREIGN KEY (hotel_id) REFERENCES hotel (id);
CREATE UNIQUE INDEX uk_t5ug9w8nw63pgabssr1s7uy1k ON hoteltofacility (hotel_id, facility_id);

ALTER TABLE hotelinfo ADD CHECK (checkIn > earlyCheckInEnd AND earlyCheckInEnd > earlyCheckInStart AND checkOut < lateCheckOutStart AND
                                 lateCheckOutStart < lateCheckOutEnd);

INSERT INTO logins.facility (type, id, active, createddate, updateddate, name) VALUES
('HOTEL', 1, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.SkiToDoorAccess'),
('HOTEL', 2, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.billiards'),
('HOTEL', 3, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.bowling'),
('HOTEL', 4, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.canoeing'),
('HOTEL', 5, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.cycling'),
('HOTEL', 6, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.darts'),
('HOTEL', 7, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.diving'),
('HOTEL', 8, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.fishing'),
('HOTEL', 9, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.golfCourse'),
('HOTEL', 10, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.hiking'),
('HOTEL', 11, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.horseRiding'),
('HOTEL', 12, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.miniGolf'),
('HOTEL', 13, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiEquipmentHire'),
('HOTEL', 14, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiPassVendor'),
('HOTEL', 15, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiSchool'),
('HOTEL', 16, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiStorage'),
('HOTEL', 17, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiing'),
('HOTEL', 18, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.snorkelling'),
('HOTEL', 19, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.squash'),
('HOTEL', 20, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.tableTennis'),
('HOTEL', 21, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.tennisCourt'),
('HOTEL', 22, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.waterPark'),
('HOTEL', 23, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.waterSport'),
('HOTEL', 24, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.windsurfing'),
('HOTEL', 25, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Business.business'),
('HOTEL', 26, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Business.fax'),
('HOTEL', 27, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Business.meeting'),
('HOTEL', 28, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.dailyMaid'),
('HOTEL', 29, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.dryCleaning'),
('HOTEL', 30, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.ironing'),
('HOTEL', 31, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.laundry'),
('HOTEL', 32, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.shoeshine'),
('HOTEL', 33, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.trouserPress'),
('HOTEL', 34, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.chapel'),
('HOTEL', 35, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.gamesRoom'),
('HOTEL', 36, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.garden'),
('HOTEL', 37, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.library'),
('HOTEL', 38, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.sharedKitchen'),
('HOTEL', 39, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.sharedLounge'),
('HOTEL', 40, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.sunTerrace'),
('HOTEL', 41, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.terrace'),
('HOTEL', 42, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.babysitting'),
('HOTEL', 43, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.casino'),
('HOTEL', 44, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.childPlayground'),
('HOTEL', 45, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.entertainmentStaff'),
('HOTEL', 46, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.eveningEntertainment'),
('HOTEL', 47, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.karaoke'),
('HOTEL', 48, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.kidsClub'),
('HOTEL', 49, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.nightclub'),
('HOTEL', 50, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.GroceryDeliveries'),
('HOTEL', 51, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.bar'),
('HOTEL', 52, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.bbq'),
('HOTEL', 53, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.breakfastInTheRoom'),
('HOTEL', 54, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.buffet'),
('HOTEL', 55, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.carteRestaurant'),
('HOTEL', 56, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.dietMenu'),
('HOTEL', 57, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.packedLunches'),
('HOTEL', 58, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.restaurant'),
('HOTEL', 59, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.roomService'),
('HOTEL', 60, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.snackBar'),
('HOTEL', 61, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.vendingMachineDrinks'),
('HOTEL', 62, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.vendingMachineSnacks'),
('HOTEL', 63, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.AdultOnly'),
('HOTEL', 64, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.AirConditioning'),
('HOTEL', 65, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.AllergyFreeRoom'),
('HOTEL', 66, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.BridalSuite'),
('HOTEL', 67, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.DesignatedSmokingArea'),
('HOTEL', 68, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.FacilitiesForDisabledGuests'),
('HOTEL', 69, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.FamilyRooms'),
('HOTEL', 70, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.Lift'),
('HOTEL', 71, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.NonSmokingRooms'),
('HOTEL', 72, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.NonSmokingThroughout'),
('HOTEL', 73, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.SoundproofRooms'),
('HOTEL', 74, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.VipRoom'),
('HOTEL', 75, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.heating'),
('HOTEL', 76, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.Hammam'),
('HOTEL', 77, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.Message'),
('HOTEL', 78, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.Sauna'),
('HOTEL', 79, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.beachfront'),
('HOTEL', 80, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.fitness'),
('HOTEL', 81, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.hotTub'),
('HOTEL', 82, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.indorPool'),
('HOTEL', 83, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.outdoorPool'),
('HOTEL', 84, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.privateBeach'),
('HOTEL', 85, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.seasonalIndoorPool'),
('HOTEL', 86, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.seasonalOutdoorPool'),
('HOTEL', 87, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.solarium'),
('HOTEL', 88, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.spaAndWellness'),
('HOTEL', 89, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.springBath'),
('HOTEL', 90, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.yearIndoorPool'),
('HOTEL', 91, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.yearOutdoorPool'),
('HOTEL', 92, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.allDayFrontDesk'),
('HOTEL', 93, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.atmCashMachine'),
('HOTEL', 94, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.conciergeService'),
('HOTEL', 95, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.currencyExchange'),
('HOTEL', 96, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.depositBox'),
('HOTEL', 97, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.expressCheckInOut'),
('HOTEL', 98, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.lockers'),
('HOTEL', 99, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.luggageStorage'),
('HOTEL', 100, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.newspapers'),
('HOTEL', 101, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.privateCheckInOut'),
('HOTEL', 102, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.ticketService'),
('HOTEL', 103, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.tourDesk'),
('HOTEL', 104, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.valetParking'),
('HOTEL', 105, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.barberShop'),
('HOTEL', 106, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.giftShop'),
('HOTEL', 107, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.miniMarket'),
('HOTEL', 108, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.shops'),
('HOTEL', 109, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.airportShuttle'),
('HOTEL', 110, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.airportShuttleFree'),
('HOTEL', 111, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.airportShuttleSurcharge'),
('HOTEL', 112, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.bicycleRental'),
('HOTEL', 113, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.bikesFree'),
('HOTEL', 114, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.carHire'),
('HOTEL', 115, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.shuttleFree'),
('HOTEL', 116, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.shuttleSurcharge'),
('HOTEL', 117, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.freeHotelInternet'),
('HOTEL', 118, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.freeRoomInternet'),
('HOTEL', 119, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.freeParking'),
('HOTEL', 120, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.paidParking');

INSERT INTO hotelinfo (id, name, timezone, checkin, checkout, officialaddress, active, tourismtax, earlycheckinnobreakfast, vat, tourismtaxfromfullprice, currency)
  VALUES (1, 'Hawaii Prince Hotel Waikiki', 'UTC', 720, 540, '100 Holomoana St., Honolulu, O`ahu Island 96815', TRUE, 0, FALSE, 0, FALSE, 'RUR'),
  (2, 'Gili Lankanfushi Maldives', 'UTC', 720, 540, 'Lankanfushi Island, North Male Atoll, 08290', TRUE, 0, FALSE, 0, FALSE, 'UAH')/*,
  (3, 'Neptun', 'UTC', 720, 540, 'Neptun Address', TRUE, 0, FALSE, 0, FALSE)*/;

INSERT INTO hotel (id, createddate, updateddate, tenantid, lcode, wuname, wupass, active, info_id, blocked, extranet)
  VALUES
  (1, '2013-12-26 15:51:48', '2013-12-26 15:51:48', 'test1', '1387194522', 'OW001', 'f5rsfzlt', TRUE, 1, FALSE, FALSE);
INSERT INTO hotel (id, createddate, updateddate, tenantid, active, info_id, blocked, extranet)
  VALUES (2, '2013-12-26 15:53:59', '2013-12-26 15:53:59', 'test2', TRUE, 2, FALSE, FALSE);
/*INSERT INTO hotel (id, createddate, updateddate, tenantid, lcode, wuname, wupass, active, info_id)
VALUES
  (3, '2013-12-26 15:51:48', '2013-12-26 15:51:48',
   'neptun', '1380878354',
   'MS096', '8e3t87h9', TRUE, 3);*/

INSERT INTO authentication (usertype, id, createddate, updateddate, password, username, status, hotel_id, supervisor_id, active)
  VALUES ('user', 1, '2013-12-26 15:51:48', '2013-12-26 15:51:48',
          '$2a$10$5HNjJKyPgWyUocpbxPwO5upsAUZSzXv1zugivCD/wOSqrnDVbm4hi', 'test1', 'ACTIVE', 1, NULL, TRUE),
  ('user', 2, '2013-12-26 15:53:59', '2013-12-26 15:53:59',
   '$2a$10$eSmki8k1DhHBhiK72sRPSeChv20JUHjKa5oyZEOXh9pkgHSYiHrD2', 'test2', 'ACTIVE', 2, NULL, TRUE),
/*  ('USER', 3, '2013-12-26 15:53:59', '2013-12-26 15:53:59',
   '$2a$10$eSmki8k1DhHBhiK72sRPSeChv20JUHjKa5oyZEOXh9pkgHSYiHrD2', 'neptun', 'ACTIVE', 3, NULL, TRUE),*/
  ('admin', 4, '2013-12-26 15:54:10', '2013-12-26 15:54:10',
   '$2a$10$SAaAPI8aJFe0C.4ZV4cEK.tbQofDfgJjcFwEHD/rJJcnGMfeo55WS', 'admin', NULL, NULL, NULL, TRUE);

INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value)
  VALUES ('Hotel', 4), ('Authentication', 5), ('HotelInfo', 4);

SELECT
  public.init_tenant('test1');

INSERT INTO test1.role (id, createddate, updateddate, active, name, createdby, updatedby)
  VALUES (1, '2013-12-26 15:52:34', '2013-12-26 15:52:34', TRUE, 'role.admin', NULL, NULL);
INSERT INTO test1.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Role', 2);
INSERT INTO test1.application_user (id, createddate, updateddate, active, address, username, firstname, lastname, patronymic, phone, createdby, updatedby, role)
  VALUES (1, '2013-12-26 15:52:34', '2013-12-26 15:52:34', TRUE, NULL, 'test1', 'Danny', 'Crane', NULL, '555-55-55', NULL, NULL, 1);
INSERT INTO test1.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('User', 2);
INSERT INTO test1.accommodation (id, createddate, updateddate, active, approved, name, shortname, createdby, updatedby)
  VALUES (1, '2013-12-24 18:15:55', '2013-12-24 18:15:55', TRUE, TRUE, 'Хоромы', 'X', NULL, NULL);
INSERT INTO test1.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Accommodation', 2);
INSERT INTO test1.baseroom (id, active, createddate, updateddate, approved, additional, adultbedprice, adults, childbedprice, children, defaultprice, name, otarooms, rid, shortname, createdby, updatedby, type)
  VALUES
  (1, TRUE, '2014-01-22 13:23:07', '2014-01-22 13:23:07', TRUE, 12, NULL, 12, NULL, 12, 111, 'Подозрительный Тип', 1, 1234, '1Vcs', NULL, NULL, 'roomType');
INSERT INTO test1.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Service', 3);
INSERT INTO test1.room (id, active, createddate, updateddate, approved, floor, number, createdby, updatedby, accommodation_id, roomtype_id, position)
  VALUES (1, TRUE, '2014-01-22 13:23:36', '2014-01-22 13:23:36', TRUE, 14, 1408, NULL, NULL, 1, 1, 0);
INSERT INTO test1.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Room', 2);
INSERT INTO test1.baseroom (id, shortname, name, active, createddate, updateddate, approved, additional, adults, children, defaultprice, createdby, updatedby, roomtype_id, rid, type)
  VALUES (2, 'VR1', 'Подозрительный Тип', TRUE, '2014-01-22 13:56:13', '2014-01-22 13:56:13', TRUE, 6, 2, 0, 500, NULL, NULL, 1, 54882, 'virtualRoom');
INSERT INTO test1.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('BaseRoom', 2);
INSERT INTO test1.roomtypetofacility (id, active, createddate, updateddate, facility_id, roomtype_id)
  VALUES (1, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 1, 1);
INSERT INTO test1.roomtypetofacility (id, active, createddate, updateddate, facility_id, roomtype_id)
  VALUES (2, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 2, 1);
INSERT INTO test1.roomtypetofacility (id, active, createddate, updateddate, facility_id, roomtype_id)
  VALUES (3, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 3, 1);
INSERT INTO test1.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('RoomTypeToFacility', 2);
INSERT INTO test1.rolepermission (role_id, permission) VALUES
(1, 'SETTINGS_SAVE'),
(1, 'USER_CREATE'),
(1, 'USER_LIST'),
(1, 'USER_VIEW'),
(1, 'USER_UPDATE'),
(1, 'EXPORT_TO_WUBOOK'),
(1, 'USER_DELETE');
INSERT INTO test1.bankdetails (id, active, createddate, updateddate, account, additional, bankaddress, bankname, blocked, cardnumber, corraccount, corrbankaddress, corrbankname, corrswift, defaultdetails, description, edrpou, holder, mfo, name, paymenttype, swift, createdby, updatedby) VALUES
(1, TRUE, '2014-06-26 11:31:24', '2014-06-26 11:31:24', null, null, null, '', FALSE, null, null, null, null, null, TRUE, '', null, null, null, 'Cash', 1, null, null, null);
INSERT INTO test1.plan (type, id, active, createddate, updateddate, approved, name, pid, board, defaultplan, value, variation, createdby, updatedby, plan_id) VALUES
('plan', 1, TRUE, '2014-06-26 11:32:25', '2014-06-26 11:32:25', TRUE, 'Шара', null, 'NB', FALSE, null, null, null, null, null);
INSERT INTO test1.living (id, active, createddate, updateddate, deprecated, system, createdby, updatedby, fri, mon, sat, sun, thu, tue, wed, plan_id, room_id, season_id) VALUES
(65536, TRUE, '2014-06-26 11:42:15', '2014-06-26 11:42:15', FALSE, TRUE, null, null, 111, 111, 111, 111, 111, 111, 111, 1, 1, null);
INSERT INTO test1.living (id, active, createddate, updateddate, deprecated, system, createdby, updatedby, fri, mon, sat, sun, thu, tue, wed, plan_id, room_id, season_id) VALUES
(65537, TRUE, '2014-06-26 11:42:15', '2014-06-26 11:42:15', FALSE, TRUE, null, null, 500, 500, 500, 500, 500, 500, 500, 1, 2, null);

SELECT
  public.init_tenant('test2');

INSERT INTO test2.role (id, createddate, updateddate, active, name, createdby, updatedby)
  VALUES (1, '2013-12-26 15:52:34', '2013-12-26 15:52:34', TRUE, 'role.admin', NULL, NULL);
INSERT INTO test2.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Role', 2);
INSERT INTO test2.application_user (id, createddate, updateddate, active, address, username, firstname, lastname, patronymic, phone, createdby, updatedby, role)
  VALUES
  (1, '2013-12-26 15:52:34', '2013-12-26 15:52:34', TRUE, NULL, 'test2', 'Alan', 'Shore', NULL, '555-55-55', NULL, NULL, 1);
INSERT INTO test2.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('User', 2);
INSERT INTO test2.accommodation (id, createddate, updateddate, active, approved, name, shortname, createdby, updatedby)
  VALUES (1, '2013-12-24 18:15:55', '2013-12-24 18:15:55', TRUE, TRUE, 'Хоромы', 'X', NULL, NULL);
INSERT INTO test2.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Accommodation', 2);
INSERT INTO test2.baseroom (id, active, createddate, updateddate, approved, additional, adultbedprice, adults, childbedprice, children, defaultprice, name, otarooms, rid, shortname, createdby, updatedby, type)
  VALUES
  (1, TRUE, '2014-01-22 13:23:07', '2014-01-22 13:23:07', TRUE, 12, NULL, 12, NULL, 12, 111, 'Подозрительный Тип', 1, NULL, '1Vcs', NULL, NULL, 'roomType');
INSERT INTO test2.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Service', 3);
INSERT INTO test2.room (id, active, createddate, updateddate, approved, floor, number, createdby, updatedby, accommodation_id, roomtype_id, position)
  VALUES (1, TRUE, '2014-01-22 13:23:36', '2014-01-22 13:23:36', TRUE, 14, 1408, NULL, NULL, 1, 1, 0);
INSERT INTO test2.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Room', 2);
INSERT INTO test2.baseroom (id, shortname, name, active, createddate, updateddate, approved, additional, adults, children, defaultprice, createdby, updatedby, roomtype_id, type)
  VALUES (2, 'VR1', 'Подозрительный Тип', TRUE, '2014-01-22 13:56:13', '2014-01-22 13:56:13', TRUE, 6, 2, 0, 500, NULL, NULL, 1, 'virtualRoom');
INSERT INTO test2.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('BaseRoom', 2);
INSERT INTO test2.roomtypetofacility (id, active, createddate, updateddate, facility_id, roomtype_id)
  VALUES (1, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 1, 1);
INSERT INTO test2.roomtypetofacility (id, active, createddate, updateddate, facility_id, roomtype_id)
  VALUES (2, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 2, 1);
INSERT INTO test2.roomtypetofacility (id, active, createddate, updateddate, facility_id, roomtype_id)
  VALUES (3, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 3, 1);
INSERT INTO test2.hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('RoomTypeToFacility', 2);
INSERT INTO test2.rolepermission (role_id, permission) VALUES
(1, 'SETTINGS_SAVE'),
(1, 'USER_CREATE'),
(1, 'USER_LIST'),
(1, 'USER_VIEW'),
(1, 'USER_UPDATE'),
(1, 'USER_DELETE');
INSERT INTO test2.bankdetails (id, active, createddate, updateddate, account, additional, bankaddress, bankname, blocked, cardnumber, corraccount, corrbankaddress, corrbankname, corrswift, defaultdetails, description, edrpou, holder, mfo, name, paymenttype, swift, createdby, updatedby) VALUES (1, TRUE, '2014-06-26 11:31:24', '2014-06-26 11:31:24', null, null, null, '', FALSE, null, null, null, null, null, TRUE, '', null, null, null, 'Cash', 1, null, null, null);
INSERT INTO test2.plan (type, id, active, createddate, updateddate, approved, name, pid, board, defaultplan, value, variation, createdby, updatedby, plan_id) VALUES
('plan', 1, TRUE, '2014-06-26 11:32:25', '2014-06-26 11:32:25', TRUE, 'Шара', null, 'NB', FALSE, null, null, null, null, null);
INSERT INTO test2.living (id, active, createddate, updateddate, deprecated, system, createdby, updatedby, fri, mon, sat, sun, thu, tue, wed, plan_id, room_id, season_id) VALUES
(65536, TRUE, '2014-06-26 11:42:15', '2014-06-26 11:42:15', FALSE, TRUE, null, null, 111, 111, 111, 111, 111, 111, 111, 1, 1, null);
INSERT INTO test2.living (id, active, createddate, updateddate, deprecated, system, createdby, updatedby, fri, mon, sat, sun, thu, tue, wed, plan_id, room_id, season_id) VALUES
(65537, TRUE, '2014-06-26 11:42:15', '2014-06-26 11:42:15', FALSE, TRUE, null, null, 500, 500, 500, 500, 500, 500, 500, 1, 2, null);

DELETE FROM public.databasechangelog;

INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_23_16:49', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-23 16:52:11.29', 29, 'EXECUTED',
   '7:74afe63a3e6b86e919ef9f4e4b082c6f', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_25_17:53', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-25 17:57:28.745', 31, 'EXECUTED',
   '7:e6878d6ca1995fb2b697331444fa1c56', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_07_08_12:23', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-07-08 13:34:55.152', 33, 'EXECUTED',
   '7:3dddda4ea024616eac8de8f8294fe486', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2013-09-04_15:44', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-04-10 16:39:55.106', 2, 'EXECUTED',
   '7:d41d8cd98f00b204e9800998ecf8427e', 'Empty', 'it works!!!', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2013-09-04_15:13', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-04-10 16:39:55.164', 3, 'EXECUTED',
   '7:6607ac6f49b93f490d0cfe5892d9e423', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-04-11_10:57', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-04-11 21:29:21.127', 4, 'EXECUTED',
   '7:afc0bd08e10b952cc8d6f60a79b2f006', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-05-27_15:15', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-27 15:29:01.254', 14, 'EXECUTED',
   '7:306e03babbdee0a41060c32f99284cfc', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-06-05_12:19', 'etypalchuk', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-11 13:13:36.417', 18, 'EXECUTED',
   '7:55292e95ca7a24eb0ab112fabe21eca5', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-04-14_13:17', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-04-16 13:54:50.968', 5, 'EXECUTED',
   '7:79578111fce64b9514dff739885cbc19', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_05_16:17', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-11 13:13:36.446', 19, 'EXECUTED',
   '7:7935d3f968a8f6c5103c9a471dbdd2d1', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-04-18_11:17', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-04-18 17:40:05.795', 6, 'EXECUTED',
   '7:6308d7469c1c8e0a81c16ef82f5ea992', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-04-18_1_15:10', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-05 13:13:02.679', 7, 'EXECUTED',
   '7:f0d97859e541d2da304dae33691e1974', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-04-25_12:35', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-05 13:13:02.695', 8, 'EXECUTED',
   '7:7775d69d63db36227c489e7c303574f6', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_06_13:18', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-11 13:13:36.471', 20, 'EXECUTED',
   '7:fb82dd0c96016a84a6e148c9000aca3c', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-05-06_11:34', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-06 12:51:51.607', 9, 'EXECUTED',
   '7:9a653017cb51a53a2cb17e02f2122b4c', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_06_14:18', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-11 13:13:36.497', 21, 'EXECUTED',
   '7:d86f63895d757880f33a581716bf8ed7', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2013-06-04_16:54', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-04 17:00:23.475', 15, 'EXECUTED',
   '7:f5032ab5d66464c5d77ca62f90ca05ad', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-05-07_16:05', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-08 14:06:44.785', 10, 'EXECUTED',
   '7:5fea3651f44ece05a62b59168833547f', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-05-12_11:26', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-14 15:16:03.243', 11, 'EXECUTED',
   '7:330c648063204bc44ddc6546595f7336', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-05-16_13:38', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-16 17:08:33.678', 12, 'EXECUTED',
   '7:9236179b849cc9d476b04f010a2d7122', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-05-20_13:23', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-05-20 15:53:31.664', 13, 'EXECUTED',
   '7:19a5a71fcc22a4675274bb359f689508', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-06-04_15:30', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-05 22:07:16.100', 16, 'EXECUTED',
   '7:8607e4c4761b96012fd06ff6e04583cc', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_04_16:54', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-11 13:05:12.261', 17, 'EXECUTED',
   '7:5829f185aa20cd3cc6d9614582b9885e', 'sql (x2)', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_11_12:23', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-11 14:07:04.530', 22, 'EXECUTED',
   '7:45887b0bb87b4619d675d999692ccc52', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_18_11:06', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-20 14:52:02.635', 26, 'EXECUTED',
   '7:9276a79d8ccb48c0a0a23ecc271268ce', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_12_12:23', 'etypalchuk', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-12 15:01:46.757', 23, 'EXECUTED',
   '7:b6fe47ff96520d12b414f6ec967316c7', 'sql, sqlFile', '', NULL, '3.2.0');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_18_14:14', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-20 14:52:02.673', 27, 'EXECUTED',
   '7:272e86d7e992c3caf8ada14467ed3cd9', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_15_14:18', 'etypalchuk', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-16 15:56:58.130', 25, 'EXECUTED',
   '7:60f1925fe12bf25ef15c06be6edf1160', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_19_14:18', 'etypalchuk', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-20 15:31:48.197', 28, 'EXECUTED',
   '7:2c1cddc3ddda332b069e02d799a7ae7e', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2013-09-04_15:41', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-07-08 13:34:55.92', 1, 'RERAN',
   '7:bb4951d4d63bd0390ba4296a95a4b0fd', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014_06_23_15:03', 'vaka', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-25 16:18:45.439', 30, 'EXECUTED',
   '7:8aa8c9073bbf2503302308124e9bc6e6', 'sql', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-07-01_11:35', 'etypalchuk', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-07-01 17:52:21.162', 32, 'EXECUTED',
   '7:2a07ec72d21b2581829a939f1697788d', 'sqlFile', '', NULL, '3.1.1');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase) VALUES
  ('2014-06-13_11:35', 'vomel', 'src/main/resources/META-INF/liquibase/liquibase.changelog.xml', '2014-06-24 15:38:52.76', 24, 'RERAN',
   '7:3a066e2243e3e3a743c786811ba76406', 'sqlFile', '', NULL, '3.1.1');