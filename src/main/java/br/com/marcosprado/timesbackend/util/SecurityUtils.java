package br.com.marcosprado.timesbackend.util;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static ClientAggregate getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof ClientAggregate) {
            return (ClientAggregate) authentication.getPrincipal();
        }
        throw ResourceNotFoundException.userNotFound();
    }

    public static Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static boolean isAdmin() {
        return getCurrentUser().getRole().name().equals("ADMIN");
    }
}
