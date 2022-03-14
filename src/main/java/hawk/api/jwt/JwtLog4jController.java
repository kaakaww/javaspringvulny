package hawk.api.jwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Controller
public class JwtLog4jController {

    private static final Logger logger = LogManager.getLogger(JwtLog4jController.class);

    @GetMapping("/log4j")
    public ResponseEntity logRequest(@RequestParam String text, HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String it = headers.nextElement();
            logger.info("{} = {}", it, request.getHeader(it));
        }
        logger.info("Hitting Log4J route");
        logger.info(text);
        return ResponseEntity.ok().build();
    }

}
