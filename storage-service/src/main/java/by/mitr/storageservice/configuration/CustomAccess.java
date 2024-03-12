package by.mitr.storageservice.configuration;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;


public class CustomAccess implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        var principal = authentication.get().getPrincipal();
        var name = ((Jwt) principal).getSubject();

        return new AuthorizationDecision(name.equals("admin"));
    }
}
