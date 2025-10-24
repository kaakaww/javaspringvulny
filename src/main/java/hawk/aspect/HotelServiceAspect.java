package hawk.aspect;

import hawk.context.TenantContext;
import hawk.hotel.service.HotelService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HotelServiceAspect {
    private static final Logger log = LoggerFactory.getLogger(HotelServiceAspect.class);

    @Before("execution(* hawk.hotel.service.HotelService.*(..)) && target(hotelService)")
    public void enableTenantFilter(JoinPoint pjp, HotelService hotelService) throws Throwable {
        String methodName = pjp.getSignature().getName();
        String tenantId = TenantContext.getCurrentTenant();

        // Skip the deliberately vulnerable methods
        if (methodName.contains("Unfiltered")) {
            log.info("HotelServiceAspect: Skipping filter for vulnerable method: {}", methodName);
            return;
        }

        log.info("HotelServiceAspect: Enabling tenant filter for method: {} with tenantId: {}", methodName, tenantId);

        if (tenantId != null && hotelService.entityManager != null) {
            org.hibernate.Filter filter = hotelService.entityManager.unwrap(Session.class).enableFilter("tenantFilter");
            filter.setParameter("tenantId", tenantId);
            filter.validate();
            log.info("HotelServiceAspect: Tenant filter enabled successfully for tenantId: {}", tenantId);
        } else {
            log.warn("HotelServiceAspect: Could not enable filter - tenantId: {}, entityManager present: {}", tenantId, hotelService.entityManager != null);
        }
    }
}
