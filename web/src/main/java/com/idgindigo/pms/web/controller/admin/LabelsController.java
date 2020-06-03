package com.idgindigo.pms.web.controller.admin;

import com.idgindigo.pms.web.utils.LabelsResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author vomel
 * @since 23.01.14 16:22
 */
@Controller
public class LabelsController {

    @Inject
    private LabelsResolver labelsResolver;

    @RequestMapping("labels")
    @ResponseBody
    public Map<String, String> getLabels(HttpServletRequest request) {
        return labelsResolver.getLabels(request);
    }
}
