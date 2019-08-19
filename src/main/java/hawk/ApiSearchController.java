package hawk;

import hawk.api.SearchResult;
import hawk.entity.Item;
import hawk.form.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiSearchController {

    @Autowired
    SearchService searchService;

    @GetMapping("/api/search")
    public SearchResult searchApi(@RequestParam(value = "searchText", defaultValue = "") String searchText) {
        List<Item> items = searchService.search(new Search(searchText));
        return new SearchResult(searchText, items);
    }

}
