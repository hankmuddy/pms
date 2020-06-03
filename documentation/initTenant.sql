--liquibase formatted sql

--changeset 2013-09-04_15:41
CREATE OR REPLACE FUNCTION public.init_tenant(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('DROP SCHEMA IF EXISTS %I CASCADE;', $1);
  EXECUTE FORMAT('CREATE SCHEMA %I AUTHORIZATION postgres;', $1);
  EXECUTE FORMAT('SET search_path TO %I;', $1);
  CREATE TABLE accommodation
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    approved    BOOL,
    name        VARCHAR(255),
    shortname   VARCHAR(255),
    createdby   BIGINT,
    updatedby   BIGINT
  );
  CREATE TABLE application_user
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    address     VARCHAR(255),
    email       VARCHAR(255),
    firstname   VARCHAR(255),
    lastname    VARCHAR(255),
    patronymic  VARCHAR(255),
    phone       VARCHAR(255),
    position    VARCHAR(255),
    username    VARCHAR(255),
    createdby   BIGINT,
    updatedby   BIGINT,
    role        BIGINT
  );
  CREATE TABLE bankdetails
  (
    id              BIGINT PRIMARY KEY NOT NULL,
    active          BOOL               NOT NULL,
    createddate     TIMESTAMP,
    updateddate     TIMESTAMP,
    account         VARCHAR(255),
    additional      VARCHAR(1000),
    bankaddress     VARCHAR(1000),
    bankname        VARCHAR(255),
    blocked         BOOL               NOT NULL,
    cardnumber      BIGINT,
    corraccount     VARCHAR(255),
    corrbankaddress VARCHAR(1000),
    corrbankname    VARCHAR(255),
    corrswift       VARCHAR(255),
    iban VARCHAR(255),
    defaultdetails  BOOL,
    description     VARCHAR(255),
    edrpou          BIGINT,
    holder          VARCHAR(255),
    mfo             BIGINT,
    name            VARCHAR(255),
    paymenttype     INT,
    swift           VARCHAR(255),
    createdby       BIGINT,
    updatedby       BIGINT
  );
  CREATE TABLE baseroom
  (
    type            VARCHAR(31)        NOT NULL,
    id              BIGINT PRIMARY KEY NOT NULL,
    active          BOOL               NOT NULL,
    createddate     TIMESTAMP,
    updateddate     TIMESTAMP,
    approved        BOOL,
    additional      INT,
    adults          INT,
    children        INT,
    defaultbaseroom BOOL,
    defaultprice    BIGINT,
    name            VARCHAR(255),
    rid             BIGINT,
    shortname       VARCHAR(4)         NOT NULL,
    adultbedprice   BIGINT,
    area            INT,
    childbedprice   BIGINT,
    description     VARCHAR(1000),
    otarooms        INT,
    smoking         VARCHAR(255),
    createdby       BIGINT,
    updatedby       BIGINT,
    details_id      BIGINT,
    roomtype_id     BIGINT
  );
  CREATE TABLE baseroomuse
  (
    type             VARCHAR(31)        NOT NULL,
    id               BIGINT PRIMARY KEY NOT NULL,
    active           BOOL               NOT NULL,
    createddate      TIMESTAMP,
    updateddate      TIMESTAMP,
    enddate          DATE,
    startdate        DATE,
    acode            VARCHAR(255),
    checkintime      TIMESTAMP,
    checkouttime     TIMESTAMP,
    comment          VARCHAR,
    description      VARCHAR,
    rcode            VARCHAR(255),
    source           VARCHAR(255),
    status           VARCHAR(255),
    total            BIGINT,
    totalpaid        BIGINT,
    createdby        BIGINT,
    updatedby        BIGINT,
    room_id          BIGINT,
    baseroom_id      BIGINT,
    customergroup_id BIGINT,
    movedfrom_id     BIGINT,
    plan_id          BIGINT
  );
  CREATE TABLE baseroomvalue
  (
    id                BIGINT PRIMARY KEY NOT NULL,
    active            BOOL               NOT NULL,
    createddate       TIMESTAMP,
    updateddate       TIMESTAMP,
    closed            INT,
    closedtodeparture BOOL,
    maxstay           INT,
    minstay           INT,
    minstayarrival    INT,
    otaallowed        BOOL,
    date              DATE,
    createdby         BIGINT,
    updatedby         BIGINT,
    room_id           BIGINT
  );
  CREATE TABLE baseserviceuse
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    approved    BOOL,
    date        DATE,
    description VARCHAR(255),
    quantity    INT,
    rawtotal    BIGINT             NOT NULL,
    total       BIGINT             NOT NULL,
    createdby   BIGINT,
    updatedby   BIGINT,
    bill_id     BIGINT             NOT NULL,
    refund_id   BIGINT,
    roomuse_id  BIGINT             NOT NULL,
    service_id  BIGINT
  );
  CREATE TABLE bill
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    approved    BOOL,
    description VARCHAR(255),
    discount    BIGINT             NOT NULL,
    rawtotal    BIGINT             NOT NULL,
    total       BIGINT             NOT NULL,
    totalpaid   BIGINT             NOT NULL,
    createdby   BIGINT,
    updatedby   BIGINT,
    group_id    BIGINT,
    roomuse_id  BIGINT
  );
  CREATE TABLE company
  (
    id                  BIGINT PRIMARY KEY NOT NULL,
    active              BOOL               NOT NULL,
    createddate         TIMESTAMP,
    updateddate         TIMESTAMP,
    accountnumber       VARCHAR(255),
    bankmfo             VARCHAR(255),
    bankname            VARCHAR(255),
    city                VARCHAR(255),
    country             VARCHAR(255),
    discount            INT                NOT NULL,
    email               VARCHAR(255),
    inn                 BIGINT,
    legaladdress        VARCHAR(255),
    listmembership      INT,
    membershipreason    VARCHAR(255),
    name                VARCHAR(255),
    ownershiptype       VARCHAR(255),
    phone               VARCHAR(255),
    postaddress         VARCHAR(255),
    registrationaddress VARCHAR(255),
    createdby           BIGINT,
    updatedby           BIGINT
  );
  CREATE TABLE companycontact
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    description VARCHAR(255),
    email       VARCHAR(255),
    name        VARCHAR(255),
    phone       VARCHAR(255),
    post        VARCHAR(255),
    createdby   BIGINT,
    updatedby   BIGINT,
    company_id  BIGINT
  );
  CREATE TABLE companydocuments
  (
    company_id BIGINT NOT NULL,
    document   VARCHAR(255)
  );
  CREATE TABLE customergroup
  (
    id              BIGINT PRIMARY KEY NOT NULL,
    active          BOOL               NOT NULL,
    createddate     TIMESTAMP,
    updateddate     TIMESTAMP,
    approved        BOOL,
    closed          BOOL               NOT NULL,
    discount        INT,
    includecustomer BOOL,
    pov             VARCHAR(255)       NOT NULL,
    total           BIGINT,
    totalpaid       BIGINT,
    createdby       BIGINT,
    updatedby       BIGINT,
    company_id      BIGINT,
    customer_id     BIGINT
  );
  CREATE TABLE document
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    accesskey   VARCHAR(255),
    contenttype VARCHAR(255),
    filecontent OID,
    filename    VARCHAR(255),
    type        VARCHAR(255)       NOT NULL,
    createdby   BIGINT,
    updatedby   BIGINT
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
  CREATE TABLE groupmember
  (
    id               BIGINT PRIMARY KEY NOT NULL,
    active           BOOL               NOT NULL,
    createddate      TIMESTAMP,
    updateddate      TIMESTAMP,
    createdby        BIGINT,
    updatedby        BIGINT,
    customergroup_id BIGINT             NOT NULL,
    person_id        BIGINT
  );
  CREATE TABLE groupmembertoroomuse
  (
    id             BIGINT PRIMARY KEY NOT NULL,
    active         BOOL               NOT NULL,
    createddate    TIMESTAMP,
    updateddate    TIMESTAMP,
    createdby      BIGINT,
    updatedby      BIGINT,
    groupmember_id BIGINT,
    roomuse_id     BIGINT
  );
  CREATE TABLE hibernate_sequences
  (
    sequence_name          VARCHAR(255),
    sequence_next_hi_value INT
  );
  CREATE TABLE living
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    deprecated  BOOL               NOT NULL,
    system      BOOL,
    createdby   BIGINT,
    updatedby   BIGINT,
    fri         BIGINT,
    mon         BIGINT,
    sat         BIGINT,
    sun         BIGINT,
    thu         BIGINT,
    tue         BIGINT,
    wed         BIGINT,
    plan_id     BIGINT,
    room_id     BIGINT,
    season_id   BIGINT
  );
  CREATE TABLE livinguse
  (
    active       BOOL               NOT NULL,
    livingamount BIGINT             NOT NULL,
    tourismtax   REAL               NOT NULL,
    id           BIGINT PRIMARY KEY NOT NULL
  );
  CREATE TABLE payment
  (
    id             BIGINT PRIMARY KEY NOT NULL,
    active         BOOL               NOT NULL,
    createddate    TIMESTAMP,
    updateddate    TIMESTAMP,
    approved       BOOL,
    date           TIMESTAMP,
    description    VARCHAR(255),
    total          BIGINT,
    createdby      BIGINT,
    updatedby      BIGINT,
    bankdetails_id BIGINT             NOT NULL,
    bill_id        BIGINT
  );
  CREATE TABLE periodroomtypeinfo
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    datestart   DATE,
    livingprice BIGINT             NOT NULL,
    createdby   BIGINT,
    updatedby   BIGINT,
    roomtype_id BIGINT
  );
  CREATE TABLE person
  (
    type              VARCHAR(31)        NOT NULL,
    id                BIGINT PRIMARY KEY NOT NULL,
    active            BOOL               NOT NULL,
    createddate       TIMESTAMP,
    updateddate       TIMESTAMP,
    dob               DATE,
    firstname         VARCHAR(255),
    identity          VARCHAR(255),
    lastname          VARCHAR(255),
    patronymic        VARCHAR(255),
    address           VARCHAR(255),
    carbrand          VARCHAR(255),
    carnumber         VARCHAR(255),
    cio               VARCHAR(255),
    city              VARCHAR(255),
    country           VARCHAR(255),
    discount          INT,
    email             VARCHAR(255),
    entrynumber       VARCHAR(255),
    entrytype         VARCHAR(255),
    entryvalidfrom    DATE,
    entryvalidtill    DATE,
    language          VARCHAR(255),
    listmembership    INT,
    membershipreason  VARCHAR(255),
    passportissued    VARCHAR(255),
    passportnumber    VARCHAR(255),
    passportvalidtill VARCHAR(255),
    phone             VARCHAR(255),
    visatype          VARCHAR(255),
    createdby         BIGINT,
    updatedby         BIGINT,
    postindex         INT
  );
  CREATE TABLE plan
  (
    type        VARCHAR(31)        NOT NULL,
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    approved    BOOL,
    name        VARCHAR(255),
    pid         BIGINT,
    board       VARCHAR(255),
    defaultplan BOOL,
    value       BIGINT,
    variation   INT,
    createdby   BIGINT,
    updatedby   BIGINT,
    plan_id     BIGINT
  );
  CREATE TABLE planrestriction
  (
    type              VARCHAR(31)        NOT NULL,
    id                BIGINT PRIMARY KEY NOT NULL,
    active            BOOL               NOT NULL,
    createddate       TIMESTAMP,
    updateddate       TIMESTAMP,
    name              VARCHAR(255),
    pid               BIGINT,
    closed            INT,
    closedtodeparture BOOL,
    maxstay           INT,
    minstay           INT,
    minstayarrival    INT,
    createdby         BIGINT,
    updatedby         BIGINT
  );
  CREATE TABLE refund
  (
    id             BIGINT PRIMARY KEY NOT NULL,
    active         BOOL               NOT NULL,
    createddate    TIMESTAMP,
    updateddate    TIMESTAMP,
    total          BIGINT             NOT NULL,
    createdby      BIGINT,
    updatedby      BIGINT,
    bankdetails_id BIGINT,
    group_id       BIGINT,
    roomuse_id     BIGINT
  );
  CREATE TABLE role
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    name        VARCHAR(255),
    createdby   BIGINT,
    updatedby   BIGINT
  );
  CREATE TABLE rolepermission
  (
    role_id    BIGINT NOT NULL,
    permission VARCHAR(255)
  );
  CREATE TABLE room
  (
    id               BIGINT PRIMARY KEY NOT NULL,
    active           BOOL               NOT NULL,
    createddate      TIMESTAMP,
    updateddate      TIMESTAMP,
    approved         BOOL,
    floor            INT,
    number           VARCHAR(255),
    position         INT,
    createdby        BIGINT,
    updatedby        BIGINT,
    accommodation_id BIGINT             NOT NULL,
    roomtype_id      BIGINT             NOT NULL
  );
  CREATE TABLE roomtypedetails
  (
    id             BIGINT PRIMARY KEY NOT NULL,
    active         BOOL               NOT NULL,
    createddate    TIMESTAMP,
    updateddate    TIMESTAMP,
    bunk           INT                NOT NULL,
    futon          INT                NOT NULL,
    largedouble    INT                NOT NULL,
    single         INT                NOT NULL,
    sofabed        INT                NOT NULL,
    standarddouble INT                NOT NULL,
    xlargedouble   INT                NOT NULL,
    createdby      BIGINT,
    updatedby      BIGINT
  );
  CREATE TABLE roomtypetofacility
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    facility_id BIGINT             NOT NULL,
    roomtype_id BIGINT             NOT NULL
  );
  CREATE TABLE roomtypetophoto
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    document_id BIGINT             NOT NULL,
    roomtype_id BIGINT             NOT NULL
  );
  CREATE TABLE roomtypevalue
  (
    id             BIGINT PRIMARY KEY NOT NULL,
    active         BOOL               NOT NULL,
    createddate    TIMESTAMP,
    updateddate    TIMESTAMP,
    adultbedprice  BIGINT,
    childbedprice  BIGINT,
    date           DATE,
    roomsavailable INT,
    createdby      BIGINT,
    updatedby      BIGINT,
    roomtype_id    BIGINT
  );
  CREATE TABLE season
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    approved    BOOL,
    start       DATE,
    until       DATE,
    createdby   BIGINT,
    updatedby   BIGINT,
    plan_id     BIGINT
  );
  CREATE TABLE simpleservice
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    deprecated  BOOL               NOT NULL,
    system      BOOL,
    createdby   BIGINT,
    updatedby   BIGINT,
    price       BIGINT,
    title       VARCHAR(255),
    measure     VARCHAR(255)
  );
  CREATE TABLE simpleserviceuse
  (
    active BOOL               NOT NULL,
    id     BIGINT PRIMARY KEY NOT NULL
  );
  ALTER TABLE accommodation ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE accommodation ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE application_user ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE application_user ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE application_user ADD FOREIGN KEY (role) REFERENCES role (id);
  CREATE UNIQUE INDEX uk_6c0v0rco93sykgyetukfmkkod ON application_user (username);
  ALTER TABLE bankdetails ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE bankdetails ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE baseroom ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE baseroom ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE baseroom ADD FOREIGN KEY (roomtype_id) REFERENCES baseroom (id);
  ALTER TABLE baseroom ADD FOREIGN KEY (details_id) REFERENCES roomtypedetails (id);
  CREATE UNIQUE INDEX uk_9lhbgsyahf6mrvl78cwq6pj2f ON baseroom (rid);
  CREATE UNIQUE INDEX uk_a7c3d1e6fve19cxhh46qlfo03 ON baseroom (shortname);
  ALTER TABLE baseroomuse ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE baseroomuse ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE baseroomuse ADD FOREIGN KEY (baseroom_id) REFERENCES baseroom (id);
  ALTER TABLE baseroomuse ADD FOREIGN KEY (movedfrom_id) REFERENCES baseroomuse (id);
  ALTER TABLE baseroomuse ADD FOREIGN KEY (customergroup_id) REFERENCES customergroup (id);
  ALTER TABLE baseroomuse ADD FOREIGN KEY (plan_id) REFERENCES plan (id);
  ALTER TABLE baseroomuse ADD FOREIGN KEY (room_id) REFERENCES room (id);
  ALTER TABLE baseroomvalue ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE baseroomvalue ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE baseroomvalue ADD FOREIGN KEY (room_id) REFERENCES baseroom (id);
  CREATE UNIQUE INDEX uk_a44cx0vpvn4p031792eq1r8dh ON baseroomvalue (room_id, date);
  ALTER TABLE baseserviceuse ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE baseserviceuse ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE baseserviceuse ADD FOREIGN KEY (roomuse_id) REFERENCES baseroomuse (id);
  ALTER TABLE baseserviceuse ADD FOREIGN KEY (bill_id) REFERENCES bill (id);
  ALTER TABLE baseserviceuse ADD FOREIGN KEY (refund_id) REFERENCES refund (id);
  ALTER TABLE bill ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE bill ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE bill ADD FOREIGN KEY (roomuse_id) REFERENCES baseroomuse (id);
  ALTER TABLE bill ADD FOREIGN KEY (group_id) REFERENCES customergroup (id);
  ALTER TABLE company ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE company ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE companycontact ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE companycontact ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE companycontact ADD FOREIGN KEY (company_id) REFERENCES company (id);
  ALTER TABLE companydocuments ADD FOREIGN KEY (company_id) REFERENCES company (id);
  ALTER TABLE customergroup ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE customergroup ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE customergroup ADD FOREIGN KEY (company_id) REFERENCES company (id);
  ALTER TABLE customergroup ADD FOREIGN KEY (customer_id) REFERENCES person (id);
  ALTER TABLE document ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE document ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  CREATE UNIQUE INDEX uk_9j6w8l49bm36424t3kuw89qj3 ON document (accesskey);
  CREATE UNIQUE INDEX uk_b4cnlopmbs70qjswc9umy6bkj ON facility (name);
  ALTER TABLE groupmember ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE groupmember ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE groupmember ADD FOREIGN KEY (customergroup_id) REFERENCES customergroup (id);
  ALTER TABLE groupmember ADD FOREIGN KEY (person_id) REFERENCES person (id);
  ALTER TABLE groupmembertoroomuse ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE groupmembertoroomuse ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE groupmembertoroomuse ADD FOREIGN KEY (roomuse_id) REFERENCES baseroomuse (id);
  ALTER TABLE groupmembertoroomuse ADD FOREIGN KEY (groupmember_id) REFERENCES groupmember (id);
  ALTER TABLE living ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE living ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE living ADD FOREIGN KEY (room_id) REFERENCES baseroom (id);
  ALTER TABLE living ADD FOREIGN KEY (plan_id) REFERENCES plan (id);
  ALTER TABLE living ADD FOREIGN KEY (season_id) REFERENCES season (id);
  CREATE UNIQUE INDEX uk_aymg0wjflwdoct8hsq8gst7yw ON living (room_id, season_id);
  CREATE UNIQUE INDEX uk_eiife4f0ddxvbo2crxqwmi7xa ON living (room_id, plan_id);
  ALTER TABLE livinguse ADD FOREIGN KEY (id) REFERENCES baseserviceuse (id);
  ALTER TABLE payment ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE payment ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE payment ADD FOREIGN KEY (bankdetails_id) REFERENCES bankdetails (id);
  ALTER TABLE payment ADD FOREIGN KEY (bill_id) REFERENCES bill (id);
  ALTER TABLE periodroomtypeinfo ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE periodroomtypeinfo ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE periodroomtypeinfo ADD FOREIGN KEY (roomtype_id) REFERENCES baseroom (id);
  CREATE UNIQUE INDEX uk_of6gc6tr4le0jxnfta3w5o66e ON periodroomtypeinfo (datestart, roomtype_id);
  ALTER TABLE person ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE person ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  CREATE UNIQUE INDEX uk_585qcyc8qh7bg1fwgm1pj4fus ON person (email);
  ALTER TABLE plan ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE plan ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE plan ADD FOREIGN KEY (plan_id) REFERENCES plan (id);
  CREATE UNIQUE INDEX uk_rj3sbkfpr08prx7c5h7fm0ir8 ON plan (name);
  ALTER TABLE planrestriction ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE planrestriction ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE refund ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE refund ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE refund ADD FOREIGN KEY (bankdetails_id) REFERENCES bankdetails (id);
  ALTER TABLE refund ADD FOREIGN KEY (roomuse_id) REFERENCES baseroomuse (id);
  ALTER TABLE refund ADD FOREIGN KEY (group_id) REFERENCES customergroup (id);
  ALTER TABLE role ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE role ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  CREATE UNIQUE INDEX uk_8sewwnpamngi6b1dwaa88askk ON role (name);
  ALTER TABLE rolepermission ADD FOREIGN KEY (role_id) REFERENCES role (id);
  ALTER TABLE room ADD FOREIGN KEY (accommodation_id) REFERENCES accommodation (id);
  ALTER TABLE room ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE room ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE room ADD FOREIGN KEY (roomtype_id) REFERENCES baseroom (id);
  CREATE UNIQUE INDEX uk_2by5on3vbjry82h110fhbaujg ON room (number, floor, accommodation_id);
  ALTER TABLE roomtypedetails ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE roomtypedetails ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE roomtypetofacility ADD FOREIGN KEY (roomtype_id) REFERENCES baseroom (id);
  ALTER TABLE roomtypetofacility ADD FOREIGN KEY (facility_id) REFERENCES facility (id);
  CREATE UNIQUE INDEX uk_540pkxce796ehye0l0nrbksyy ON roomtypetofacility (roomtype_id, facility_id);
  ALTER TABLE roomtypetophoto ADD FOREIGN KEY (roomtype_id) REFERENCES baseroom (id);
  ALTER TABLE roomtypetophoto ADD FOREIGN KEY (document_id) REFERENCES document (id);
  CREATE UNIQUE INDEX uk_16xxx10nbvq6tcimaw5iyg4td ON roomtypetophoto (roomtype_id, document_id);
  ALTER TABLE roomtypevalue ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE roomtypevalue ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE roomtypevalue ADD FOREIGN KEY (roomtype_id) REFERENCES baseroom (id);
  CREATE UNIQUE INDEX uk_e8aird05qeyvxmfh3ijws13bs ON roomtypevalue (roomtype_id, date);
  ALTER TABLE season ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE season ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  ALTER TABLE season ADD FOREIGN KEY (plan_id) REFERENCES plan (id);
  ALTER TABLE simpleservice ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE simpleservice ADD FOREIGN KEY (createdby) REFERENCES application_user (id);
  CREATE UNIQUE INDEX uk_5bbcr7ah0l4jjyfbl0sosslkm ON simpleservice (title);
  ALTER TABLE simpleserviceuse ADD FOREIGN KEY (id) REFERENCES baseserviceuse (id);


--Check constraints
  ALTER TABLE baseroomuse ADD CHECK ((startDate < endDate) AND
                                     (type != 'ROOM_USE' OR source IN ('FRONT_DESK', 'BOOKING_BUTTON') OR rcode IS NOT NULL) AND
                                     (type != 'ROOM_USE' OR source NOT IN ('BOOKING_BUTTON') OR acode IS NOT NULL));
  ALTER TABLE bill ADD CHECK ((discount BETWEEN 0 AND rawTotal AND totalPaid <= total) AND (roomUse_id IS NULL OR group_id IS NULL) AND
                              (roomUse_id IS NOT NULL OR group_id IS NOT NULL));
  ALTER TABLE customergroup ADD CHECK (customer_id IS NOT NULL OR company_id IS NOT NULL);
  ALTER TABLE living ADD CHECK ((plan_id IS NULL OR season_id IS NULL) AND (plan_id IS NOT NULL OR season_id IS NOT NULL) AND room_id IS NOT NULL);
  ALTER TABLE refund ADD CHECK ((roomUse_id IS NULL OR group_id IS NULL) AND (roomUse_id IS NOT NULL OR group_id IS NOT NULL) AND
                                (total = 0 OR bankDetails_id IS NOT NULL));
  ALTER TABLE roomtypevalue ADD CHECK (roomsAvailable >= 0);
  ALTER TABLE season ADD CHECK (start <= until);

--System services
  INSERT INTO simpleservice (id, active, createddate, updateddate, deprecated, price, system, title, createdby, updatedby, measure)
  VALUES
    (1, TRUE, '2013-12-19 10:52:11', '2013-12-19 10:52:11', FALSE, 10000, TRUE, 'service.adultBed', NULL, NULL, 'pcs'),
    (2, TRUE, '2013-12-19 10:52:11', '2013-12-19 10:52:11', FALSE, 10000, TRUE, 'service.childBed', NULL, NULL, 'pcs'),
    (3, TRUE, '2013-12-19 10:52:11', '2013-12-19 10:52:11', FALSE, 0, TRUE, 'service.earlyCheckIn', NULL, NULL, 'pcs'),
    (4, TRUE, '2013-12-19 10:52:11', '2013-12-19 10:52:11', FALSE, 0, TRUE, 'service.lateCheckOut', NULL, NULL, 'pcs');
  INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('Service', 2);

  INSERT INTO role (id, active, name) VALUES
    (2, TRUE, 'role.booking_dept'),
    (3, TRUE, 'role.front_desk'),
    (4, TRUE, 'role.accountant');
  INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) VALUES ('role', 2);

  INSERT INTO facility (type, id, active, createddate, updateddate, name) VALUES
    ('ROOM_TYPE', 1, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.SkiToDoorAccess'),
    ('ROOM_TYPE', 2, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.billiards'),
    ('ROOM_TYPE', 3, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.bowling'),
    ('ROOM_TYPE', 4, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.canoeing'),
    ('ROOM_TYPE', 5, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.cycling'),
    ('ROOM_TYPE', 6, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.darts'),
    ('ROOM_TYPE', 7, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.diving'),
    ('ROOM_TYPE', 8, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.fishing'),
    ('ROOM_TYPE', 9, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.golfCourse'),
    ('ROOM_TYPE', 10, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.hiking'),
    ('ROOM_TYPE', 11, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.horseRiding'),
    ('ROOM_TYPE', 12, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.miniGolf'),
    ('ROOM_TYPE', 13, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiEquipmentHire'),
    ('ROOM_TYPE', 14, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiPassVendor'),
    ('ROOM_TYPE', 15, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiSchool'),
    ('ROOM_TYPE', 16, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiStorage'),
    ('ROOM_TYPE', 17, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.skiing'),
    ('ROOM_TYPE', 18, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.snorkelling'),
    ('ROOM_TYPE', 19, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.squash'),
    ('ROOM_TYPE', 20, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.tableTennis'),
    ('ROOM_TYPE', 21, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.tennisCourt'),
    ('ROOM_TYPE', 22, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.waterPark'),
    ('ROOM_TYPE', 23, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.waterSport'),
    ('ROOM_TYPE', 24, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Activities.windsurfing'),
    ('ROOM_TYPE', 25, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Business.business'),
    ('ROOM_TYPE', 26, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Business.fax'),
    ('ROOM_TYPE', 27, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Business.meeting'),
    ('ROOM_TYPE', 28, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.dailyMaid'),
    ('ROOM_TYPE', 29, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.dryCleaning'),
    ('ROOM_TYPE', 30, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.ironing'),
    ('ROOM_TYPE', 31, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.laundry'),
    ('ROOM_TYPE', 32, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.shoeshine'),
    ('ROOM_TYPE', 33, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CleaningServices.trouserPress'),
    ('ROOM_TYPE', 34, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.chapel'),
    ('ROOM_TYPE', 35, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.gamesRoom'),
    ('ROOM_TYPE', 36, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.garden'),
    ('ROOM_TYPE', 37, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.library'),
    ('ROOM_TYPE', 38, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.sharedKitchen'),
    ('ROOM_TYPE', 39, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.sharedLounge'),
    ('ROOM_TYPE', 40, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.sunTerrace'),
    ('ROOM_TYPE', 41, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.CommonAreas.terrace'),
    ('ROOM_TYPE', 42, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.babysitting'),
    ('ROOM_TYPE', 43, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.casino'),
    ('ROOM_TYPE', 44, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.childPlayground'),
    ('ROOM_TYPE', 45, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.entertainmentStaff'),
    ('ROOM_TYPE', 46, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.eveningEntertainment'),
    ('ROOM_TYPE', 47, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.karaoke'),
    ('ROOM_TYPE', 48, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.kidsClub'),
    ('ROOM_TYPE', 49, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.EntertainmentFamily.nightclub'),
    ('ROOM_TYPE', 50, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.GroceryDeliveries'),
    ('ROOM_TYPE', 51, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.bar'),
    ('ROOM_TYPE', 52, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.bbq'),
    ('ROOM_TYPE', 53, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.breakfastInTheRoom'),
    ('ROOM_TYPE', 54, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.buffet'),
    ('ROOM_TYPE', 55, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.carteRestaurant'),
    ('ROOM_TYPE', 56, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.dietMenu'),
    ('ROOM_TYPE', 57, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.packedLunches'),
    ('ROOM_TYPE', 58, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.restaurant'),
    ('ROOM_TYPE', 59, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.roomService'),
    ('ROOM_TYPE', 60, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.snackBar'),
    ('ROOM_TYPE', 61, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.vendingMachineDrinks'),
    ('ROOM_TYPE', 62, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.FoodDrink.vendingMachineSnacks'),
    ('ROOM_TYPE', 63, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.AdultOnly'),
    ('ROOM_TYPE', 64, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.AirConditioning'),
    ('ROOM_TYPE', 65, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.AllergyFreeRoom'),
    ('ROOM_TYPE', 66, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.BridalSuite'),
    ('ROOM_TYPE', 67, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.DesignatedSmokingArea'),
    ('ROOM_TYPE', 68, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.FacilitiesForDisabledGuests'),
    ('ROOM_TYPE', 69, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.FamilyRooms'),
    ('ROOM_TYPE', 70, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.Lift'),
    ('ROOM_TYPE', 71, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.NonSmokingRooms'),
    ('ROOM_TYPE', 72, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.NonSmokingThroughout'),
    ('ROOM_TYPE', 73, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.SoundproofRooms'),
    ('ROOM_TYPE', 74, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.VipRoom'),
    ('ROOM_TYPE', 75, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Miscellaneous.heating'),
    ('ROOM_TYPE', 76, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.Hammam'),
    ('ROOM_TYPE', 77, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.Message'),
    ('ROOM_TYPE', 78, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.Sauna'),
    ('ROOM_TYPE', 79, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.beachfront'),
    ('ROOM_TYPE', 80, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.fitness'),
    ('ROOM_TYPE', 81, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.hotTub'),
    ('ROOM_TYPE', 82, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.indorPool'),
    ('ROOM_TYPE', 83, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.outdoorPool'),
    ('ROOM_TYPE', 84, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.privateBeach'),
    ('ROOM_TYPE', 85, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.seasonalIndoorPool'),
    ('ROOM_TYPE', 86, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.seasonalOutdoorPool'),
    ('ROOM_TYPE', 87, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.solarium'),
    ('ROOM_TYPE', 88, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.spaAndWellness'),
    ('ROOM_TYPE', 89, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.springBath'),
    ('ROOM_TYPE', 90, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.yearIndoorPool'),
    ('ROOM_TYPE', 91, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.PoolAndWellness.yearOutdoorPool'),
    ('ROOM_TYPE', 92, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.allDayFrontDesk'),
    ('ROOM_TYPE', 93, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.atmCashMachine'),
    ('ROOM_TYPE', 94, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.conciergeService'),
    ('ROOM_TYPE', 95, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.currencyExchange'),
    ('ROOM_TYPE', 96, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.depositBox'),
    ('ROOM_TYPE', 97, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.expressCheckInOut'),
    ('ROOM_TYPE', 98, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.lockers'),
    ('ROOM_TYPE', 99, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.luggageStorage'),
    ('ROOM_TYPE', 100, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.newspapers'),
    ('ROOM_TYPE', 101, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.privateCheckInOut'),
    ('ROOM_TYPE', 102, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.ticketService'),
    ('ROOM_TYPE', 103, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.tourDesk'),
    ('ROOM_TYPE', 104, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.ReceptionServices.valetParking'),
    ('ROOM_TYPE', 105, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.barberShop'),
    ('ROOM_TYPE', 106, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.giftShop'),
    ('ROOM_TYPE', 107, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.miniMarket'),
    ('ROOM_TYPE', 108, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Shops.shops'),
    ('ROOM_TYPE', 109, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.airportShuttle'),
    ('ROOM_TYPE', 110, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.airportShuttleFree'),
    ('ROOM_TYPE', 111, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.airportShuttleSurcharge'),
    ('ROOM_TYPE', 112, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.bicycleRental'),
    ('ROOM_TYPE', 113, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.bikesFree'),
    ('ROOM_TYPE', 114, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.carHire'),
    ('ROOM_TYPE', 115, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.shuttleFree'),
    ('ROOM_TYPE', 116, TRUE, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', 'facility.Transport.shuttleSurcharge');
  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
--rollback