package hawk.api.jwt;

import hawk.entity.User;
import hawk.form.Search;
import hawk.service.UserSearchService;
import hawk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity searchAll() {
        Search search = new Search("");
        return ResponseEntity.ok(this.userService.findUsersByName(""));
    }

    @GetMapping("/search/{text}")
    public ResponseEntity search(@PathVariable("text") String text) {
        Search search = new Search(text);
        return ResponseEntity.ok(this.userService.findUsersByName(search.getSearchText()));
    }

    @GetMapping("/search/bad/{text}")
    public ResponseEntity searchCrappy(@PathVariable("text") String text) {
        Search search = new Search(text);
        return ResponseEntity.ok(this.userSearchService.search(search));
    }
}
