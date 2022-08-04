package hawk.aspect;

import hawk.context.TenantContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import hawk.service.UserService;

@Aspect
@Component
public class UserServiceAspect {
    @Before("execution(* hawk.service.UserService.*(..))&& target(userService) ")
    public void aroundExecution(JoinPoint pjp, UserService userService) throws Throwable {
        org.hibernate.Filter filter = userService.entityManager.unwrap(Session.class).enableFilter("tenantFilter");
        filter.setParameter("tenantId", TenantContext.getCurrentTenant());
        filter.validate();
    }
}
