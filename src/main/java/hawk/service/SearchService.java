package hawk.service;

import hawk.entity.Item;
import hawk.form.Search;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SearchService {

    private static final Logger LOGGER = Logger.getLogger(SearchService.class.getName());

    @Autowired
    EntityManager entityManager;

    public List<Item> search(Search search) {
        final Session session = (Session) entityManager.unwrap(Session.class);
        return session.doReturningWork(new ReturningWork<List<Item>>() {
            @Override
            public List<Item> execute(Connection connection) throws SQLException {
                List<Item> items = new ArrayList<>();
                // The wrong way
                String query = "select id, name, description from ITEM where description like '%" +
                        search.getSearchText() + "%'";

                LOGGER.log(Level.INFO, "SQL Query: {0}",  query);;
                ResultSet rs = connection
                        .createStatement()
                        .executeQuery(query);

                /* The righter way, should probably use built in Data Model for this, but this is safe
                String query = "select id, name, description from ITEM where description like ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, "%" + search.getSearchText() + "%");
                LOGGER.log(Level.INFO, "SQL Query {0}",  statement);
                ResultSet rs = statement.executeQuery();
                */

                while (rs.next()) {
                    items.add(new Item(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
                }
                rs.close();
                return items;
            }
        });

    }


}
