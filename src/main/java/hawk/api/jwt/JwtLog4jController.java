package hawk.api.jwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Controller
public class JwtLog4jController {

    private static final Logger logger = LogManager.getLogger();

    @PostMapping("/api/jwt/log4j")
    public ResponseEntity logRequest(HttpServletRequest request,
                                     @RequestBody String body) {
        Enumeration<String> headers = request.getHeaderNames();
        logger.info("We are in the log4j request");
        while (headers.hasMoreElements()) {
            String it = headers.nextElement();
            logger.info("{} = {}", it, request.getHeader(it));
        }
        logger.info("{}", body);
        return ResponseEntity.ok(body);
    }

}
