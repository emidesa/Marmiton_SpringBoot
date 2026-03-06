package foreach.cda.Marmiton.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import foreach.cda.Marmiton.entity.User;
import foreach.cda.Marmiton.exceptions.ForbiddenException;
import foreach.cda.Marmiton.exceptions.NotFoundException;
import foreach.cda.Marmiton.repository.UserRepository;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Authentication requireAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new ForbiddenException("Authentification requise");
        }
        return auth;
    }

    public boolean isAdmin() {
        Authentication auth = requireAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    public User requireCurrentUser() {
        Authentication auth = requireAuthentication();
        String email = auth.getName();
        return userRepository.findByMail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé pour l'email: " + email));
    }
}

