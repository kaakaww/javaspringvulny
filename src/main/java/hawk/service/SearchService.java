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
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.*;

public class SearchService {
    private static final Logger LOGGER = Logger.getLogger( SearchService.class.getName() );
    @Autowired
    EntityManager entityManager;

    public List<Item> search(Search search) {
        LOGGER.setLevel(Level.FINE);

        final Session session = (Session) entityManager.unwrap(Session.class);
        return session.doReturningWork(new ReturningWork<List<Item>>() {
            @Override
            public List<Item> execute(Connection connection) throws SQLException {
                List<Item> items = new ArrayList<>();
                String query = "select id, name, description from ITEM where description like '%" +
                        search.getSearchText() + "%'";

                LOGGER.log(Level.INFO, "SQL Query " + query);
                ResultSet rs = connection
                        .createStatement()
                        .executeQuery(query);

                while (rs.next()) {
                    items.add(new Item(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
                }
                return items;
            }
        });
    }


}
