package hawk.repos;

import hawk.entity.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepo extends CrudRepository<Todo, Long> {
}
