package hawk.repos;

import hawk.entity.Item;
import hawk.form.Search;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ItemRepo extends CrudRepository<Item, Long> {

    List<Item> findByNameOrDescription(String name, String description);

}


