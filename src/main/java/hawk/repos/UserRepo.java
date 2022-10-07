package hawk.repos;

import hawk.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    User findByName(String name);
    List<User> findAllByNameIsLike(String name);
}
