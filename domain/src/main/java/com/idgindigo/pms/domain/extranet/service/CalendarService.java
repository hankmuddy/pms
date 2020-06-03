package com.idgindigo.pms.domain.extranet.service;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 11:03 AM
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class CalendarService extends Service {
    public static final String CALENDAR_SERVICE = "calendarService";

//    public abstract <T extends Rate> T toRate();
}
