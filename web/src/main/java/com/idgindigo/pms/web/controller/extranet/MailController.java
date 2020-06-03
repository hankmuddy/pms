package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.broadcast.BroadcastService;
import com.idgindigo.pms.service.extranet.MailService;
import com.idgindigo.pms.web.utils.MailHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 3/20/14 1:41 PM
 */
@Controller
@RequestMapping(MailController.URL)
public class MailController {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + "mail";
    @Inject
    private MailService mailService;
    @Inject
    private BroadcastService broadcastService;
    @Inject
    private MailHelper helper;

    /*@RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public void sendTest() {
        RoomUse roomUse = new RoomUse();
        roomUse.setCreatedDate(LocalDateTime.now());

        CustomerGroup group = new CustomerGroup();
        Adult customer = new Adult();
        group.setCustomer(customer);
        customer.setEmail("atata");
        customer.setFirstName("Jack");
        customer.setLastName("Daniels");


        List<GroupMember> members = group.getGroupMembers();
        members.add(new GroupMember(new Adult(), group));
        members.add(new GroupMember(new Child(), group));
        roomUse.setCustomerGroup(group);

        roomUse.setStartDate(LocalDate.now());
        roomUse.setEndDate(roomUse.getStartDate().plusDays(1));

        Room room = new Room();
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setName("Room Type Name");
        room.setRoomType(roomType);
        roomUse.setRoom(room);
        roomUse.setAcode("ACODE");

        Plan plan = new Plan();
        plan.setBoard(Plan.Board.AI);
        roomUse.setPlan(plan);

        mailService.sendMail("valentin.vakatsiienko@idg.net.ua", "test", helper.getConfirmationMailBody(roomUse, Locale.forLanguageTag("ru")), true);
    }*/

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void send(@RequestBody @Valid final MailDto dto) {
        final String username = SecurityUtils.getUsername();
        final String tenantId = SecurityUtils.getCurrentTenantId();
        mailService.sendMail(dto.getTo(), dto.getSubject(), dto.getContent(), dto.isHtml(), new MailService.EmailCallback() {
            @Override
            public void onSuccess() {
                broadcastService.pushMailSent(tenantId, username, dto.getTo());
            }

            @Override
            public void onFailure(Exception e) {
                broadcastService.pushMailFailed(tenantId, username, dto.getTo());
            }
        });
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MailDto {
        @NotNull
        private String to;
        @NotNull
        private String subject;
        @NotNull
        private String content;
        private boolean html = true;
    }
}
