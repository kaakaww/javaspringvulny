package hawk.service;

import hawk.entity.Item;
import hawk.form.Search;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    @Autowired
    EntityManager entityManager;

    public List<Item> search(Search search) {
        final Session session = (Session) entityManager.unwrap(Session.class);
        List items = session.doReturningWork(new ReturningWork<List<Item>>() {
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
        return items;
    }


}
