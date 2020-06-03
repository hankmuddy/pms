package com.idgindigo.pms.web.intercenptor;

import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.web.controller.admin.SettingsController;
import com.idgindigo.pms.web.controller.extranet.AdultController;
import com.idgindigo.pms.web.controller.extranet.BaseRoomController;
import com.idgindigo.pms.web.controller.extranet.BaseRoomValueController;
import com.idgindigo.pms.web.controller.extranet.CustomerGroupController;
import com.idgindigo.pms.web.controller.extranet.DocumentController;
import com.idgindigo.pms.web.controller.extranet.ExtranetRoomUseController;
import com.idgindigo.pms.web.controller.extranet.GroupMemberController;
import com.idgindigo.pms.web.controller.extranet.LivingController;
import com.idgindigo.pms.web.controller.extranet.MailController;
import com.idgindigo.pms.web.controller.extranet.PersonController;
import com.idgindigo.pms.web.controller.extranet.RoomTypeController;
import com.idgindigo.pms.web.controller.extranet.RoomTypeFacilityController;
import com.idgindigo.pms.web.controller.extranet.RoomTypeValueController;
import com.idgindigo.pms.web.controller.extranet.SeasonController;
import com.idgindigo.pms.web.controller.extranet.VirtualRoomController;
import com.idgindigo.pms.web.controller.extranet.plan.CompactRestrictionController;
import com.idgindigo.pms.web.controller.extranet.plan.PlanController;
import com.idgindigo.pms.web.controller.extranet.plan.VirtualPlanController;
import com.idgindigo.pms.web.controller.pms.ChildController;
import com.idgindigo.pms.web.controller.pms.CompanyController;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * @author valentyn_vakatsiienko
 * @since 6/18/14 11:31 AM
 */
public class ExtranetInterceptor extends HandlerInterceptorAdapter {
    private Iterable<String> uris = new ArrayList<String>() {{
        //RoomUse
        add(ExtranetRoomUseController.URL);
        add(CustomerGroupController.URL);
        add(GroupMemberController.URL);
        //BaseRoom
        add(RoomTypeController.URL);
        add(BaseRoomController.URL);
        add(RoomTypeFacilityController.URL);
        add(VirtualRoomController.URL);
        add(BaseRoomValueController.URL);
        add(RoomTypeValueController.URL);
        //Plan
        add(PlanController.URL);
        add(SeasonController.URL);
        add(CompactRestrictionController.URL);
        add(VirtualPlanController.URL);
        add(LivingController.URL);
        //Person
        add(AdultController.URL);
        add(PersonController.URL);
        add(ChildController.URL);
        //Company
        add(CompanyController.URL);
        //Misc
        add(SettingsController.URL);
        add(MailController.URL);
        add(DocumentController.URL);
    }};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityUtils.isExtranet() && !isExtranetController(request.getRequestURI())) {
            throw new AccessDeniedException("forbidden");
        }
        return true;
    }

    private boolean isExtranetController(String requestURI) {
        boolean found = false;
        for (String uri : uris) {
            if (requestURI.contains(uri)) {
                found = true;
            }
        }
        return found;
    }
}