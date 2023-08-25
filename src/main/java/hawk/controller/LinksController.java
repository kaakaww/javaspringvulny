package hawk.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LinksController {

    @GetMapping(value = {"/links/**"})
    public String getPayload(Model model, HttpServletRequest request) {

        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String subPath = StringUtils.removeStart(fullPath, "/links");

        List<String> links = new ArrayList<>();
        links.add("blah");
        model.addAttribute("links", links);

        return "links";
    }

}
