package hawk;

import hawk.entity.Item;
import hawk.form.Search;
import hawk.repos.ItemRepo;
import hawk.repos.ItemsRepo;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    ItemsRepo repo;

    @Autowired
    EntityManager entityManager;

    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("search", new Search());
        return "search";
    }

    @PostMapping("/search")
    public String searchSubmit(@ModelAttribute Search search, Model model) {
        final Session session = (Session) entityManager.unwrap(Session.class);
        List itemsx = session.doReturningWork(new ReturningWork<List<Item>>() {
            @Override
            public List<Item> execute(Connection connection) throws SQLException {
                List<Item> items = new ArrayList<>();
                ResultSet rs = connection
                        .createStatement()
                        .executeQuery(
                                "select * from ITEM where name like '%" + search.getSearchText() + "%'"
                        );
                //or description like '%" + search.getSearchText() + "%'
                while (rs.next()) {
                    items.add(new Item(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
                }
                return items;
            }
        });

        //List items = repo.findByNameContainingOrDescriptionContaining(search.getSearchText(), search.getSearchText());
        model.addAttribute("items", itemsx);
        model.addAttribute("search", search);
        return "searchResult";
    }

}
