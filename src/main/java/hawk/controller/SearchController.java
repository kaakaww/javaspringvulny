package hawk.controller;

import hawk.entity.Item;
import hawk.form.Search;
import hawk.repos.ItemsRepo;
import hawk.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    ItemsRepo repo;

    @Autowired
    EntityManager entityManager;

    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("search", new Search());
        model.addAttribute("title", "Search");
        return "search";
    }

    @PostMapping("/search")
    public String searchSubmit(@ModelAttribute Search search, Model model) {
        List<Item> items = searchService.search(search);
        model.addAttribute("items", items);
        model.addAttribute("search", search);
        model.addAttribute("title", "Search");
        return "search";
    }

}
