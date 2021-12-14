package hawk.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

@Controller
public class Log4jController {

    private static final Logger logger = LogManager.getLogger();

    @PostMapping("/log4j")
    public ResponseEntity searchForm(HttpServletRequest request,
                                     @RequestBody Map<String, String> body) {
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
