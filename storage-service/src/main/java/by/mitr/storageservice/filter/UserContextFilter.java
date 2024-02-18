package by.mitr.storageservice.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
//@Slf4j
//public class UserContextFilter {
//}

//public class UserContextFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest =
//                (HttpServletRequest) servletRequest;
//        UserContextHolder.getContext()
//                .setCorrelationId(
//                        httpServletRequest.getHeader(UserContext.CORRELATION_ID));
//        UserContextHolder.getContext().setUserId(
//                httpServletRequest.getHeader(UserContext.USER_ID));
//        UserContextHolder.getContext().setAuthToken(
//                httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
//        UserContextHolder.getContext().setOrganizationId(
//                httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));
//        log.info("UserContextFilter Correlation id: {}",
//                UserContextHolder.getContext().getCorrelationId());
//
//        filterChain.doFilter(httpServletRequest, servletResponse);
//    }
//}


@Component
@Slf4j
public class UserContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}