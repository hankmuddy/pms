package com.idgindigo.pms.service.admin;

import com.idgindigo.pms.domain.Role;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.logins.repository.ManagerRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.web.controller.admin.HotelUserDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author valentyn_vakatsiienko
 * @since 12/20/13 5:30 PM
 */
@Service
public class HotelService {
    private static final int TENANT_LENGTH = 6;
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    @Inject
    private HotelRepository hotelRepository;
    @Inject
    private EntityManagerFactory entityManagerFactory;
    @Inject
    private ManagerRepository managerRepository;

    public Hotel save(Hotel hotel) {
        injectManagement(hotel);
        validate(hotel);
        if (hotel.getManager() != null && hotel.getSupervisor() == null) {
            hotel.setSupervisor(hotel.getManager().getSupervisor());
        }
        do {
            hotel.setTenantId(RandomStringUtils.randomNumeric(TENANT_LENGTH).toLowerCase());
        } while (hotelRepository.findByTenantId(hotel.getTenantId()) != null);
        return hotelRepository.saveAndFlush(hotel);
    }

    private void injectManagement(Hotel hotel) {
        Authentication authentication = SecurityUtils.getUserDetails().getAuthentication();
        switch (authentication.getUserType()) {
            case Manager.MANAGER:
                Manager manager = managerRepository.findOne(authentication.getId());
                hotel.setManager(manager);
                hotel.setSupervisor(manager.getSupervisor());
                break;
            case ManagerSupervisor.MANAGER_SUPERVISOR:
                ManagerSupervisor supervisor = (ManagerSupervisor) authentication;
                hotel.setSupervisor(supervisor);
                break;
        }
    }

    private void validate(Hotel hotel) {
        Manager manager = hotel.getManager();
        if (manager != null) {
            manager = managerRepository.findOne(manager.getId());
        }
        ManagerSupervisor supervisor = hotel.getSupervisor();
//        if (supervisor != null) {
//            supervisor = supervisorRepository.findOne(supervisor.getId());
//        }

        if (manager != null && supervisor != null) {
            if (!manager.getSupervisor().equals(supervisor)) {
                //TODO fail
            }
        }
    }

    public void initHotel(HotelUserDto hotelAdminData, Iterable<Role> roles) {
        HotelUser authentication = hotelAdminData.getAuthentication();
        Hotel hotel = authentication.getHotel();
        if (SecurityUtils.isRecognizedUser()) {
            SecurityUtils.getUserDetails().getAuthentication().setHotel(hotel);
        } else {
            SecurityUtils.authenticateNonTenantUser(authentication);
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        User hotelAdmin = hotelAdminData.getUser();

        Role adminRole = new Role();
        adminRole.setName("role.admin");

        adminRole.getPermissions().addAll(SecurityUtils.NON_ADMIN_AUTHORITIES);
        entityManager.persist(adminRole);

        hotelAdmin.setRole(adminRole);
        entityManager.persist(hotelAdmin);

        if (roles != null) {
            for (Role role : roles) {
                entityManager.persist(role);
            }
        }

        entityManager.getTransaction().commit();
    }

    @Scheduled(cron = "0 0 10 * * ?", zone = "UTC")//Every day at midnight
    public void manageBlocking() {
        for (Hotel hotel : hotelRepository.findBlockableByDate(LocalDate.now(DateTimeZone.UTC))) {
            if (hotel.getPaidUntil().plusDays(1).equals(LocalDate.now(DateTimeZone.forID(hotel.getInfo().getTimeZone())))) {
                logger.info("Blocking hotel {}...", hotel);
                hotelRepository.setBlocked(hotel.getId(), true);
                logger.info("Hotel {} is blocked successfully", hotel);
            }
        }
    }
}
