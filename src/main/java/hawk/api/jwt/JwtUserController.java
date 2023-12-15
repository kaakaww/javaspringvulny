package hawk.api.jwt;

import hawk.entity.User;
import hawk.form.Search;
import hawk.service.UserSearchService;
import hawk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jwt/users")
public class JwtUserController {

    private final UserService userService;
    private final UserSearchService userSearchService;

    @Autowired
    public JwtUserController(UserService userService, UserSearchService userSearchService) {
        this.userService = userService;
        this.userSearchService = userSearchService;
    }

    @GetMapping("/search/")
    public ResponseEntity<List<User>> searchAll() {
        Search search = new Search("");
        return ResponseEntity.ok(this.userService.findUsersByName(""));
    }

    @GetMapping("/search/{text}")
    public ResponseEntity<List<User>> search(@PathVariable("text") String text) {
        Search search = new Search(text);
        return ResponseEntity.ok(this.userService.findUsersByName(search.getSearchText()));
    }

    @GetMapping("/search/bad/{name}")
    public ResponseEntity<List<User>> searchCrappy(@PathVariable("name") String text) {
        Search search = new Search(text);
        return ResponseEntity.ok(this.userSearchService.search(search));
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User returnUser = userService.addUser(user);
        return ResponseEntity.ok(returnUser);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Optional<User>> search(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.userSearchService.find(id));
    }
}
