package hawk.controller;

import hawk.entity.User;
import hawk.form.Search;
import hawk.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    UserSearchService userSearchService;

    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("title", "Admin");
        return "admin";
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("title", "Users");
        return "users";
    }

    @GetMapping("/admin/companies")
    public String companies(Model model) {
        model.addAttribute("title", "Companies");
        return "companies";
    }

    @GetMapping( "/admin/search")
    public String searchForm(Model model) {
        model.addAttribute("search", new Search());
        model.addAttribute("title", "User Search");
        return "user-search";
    }

    @PostMapping( "/admin/search")
    public String searchSubmit(@ModelAttribute Search search, Model model) {
        List<User> users = userSearchService.search(search);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        model.addAttribute("title", "User Search");
        return "user-search";
    }
}
