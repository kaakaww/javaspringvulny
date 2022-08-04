package hawk.service;

import hawk.entity.User;
import hawk.form.Search;
import hawk.repos.UserRepo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserSearchService.class.getName());

    @Autowired
    public
    EntityManager entityManager;

    @Autowired
    private UserRepo userRepo;

    public User findUser(String name) {
        return this.userRepo.findByName(name);
    }

    public List<User> findUsersByName(String  name) { return this.userRepo.findAllByNameIsLike('%' + name + '%'); }
}
