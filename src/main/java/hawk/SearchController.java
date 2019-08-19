package hawk;

import hawk.form.Search;
import hawk.repos.ItemsRepo;
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
        return "search";
    }

    @PostMapping("/search")
    public String searchSubmit(@ModelAttribute Search search, Model model) {
        List itemsx = searchService.search(search);
        //List items = repo.findByNameContainingOrDescriptionContaining(search.getSearchText(), search.getSearchText());
        model.addAttribute("items", itemsx);
        model.addAttribute("search", search);
        return "searchResult";
    }

}
