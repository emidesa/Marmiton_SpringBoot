package foreach.cda.Marmiton.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import foreach.cda.Marmiton.DTO.Request.UserRequest;
import foreach.cda.Marmiton.DTO.Response.UserResponse;
import foreach.cda.Marmiton.entity.User;
import foreach.cda.Marmiton.mappers.UserMapper;
import foreach.cda.Marmiton.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByMail(request.getMail())) {
            throw new IllegalArgumentException ("Un utilisateur avec cet email existe déjà");
        }
        
        // Créer l'utilisateur
        User user = userMapper.toEntity(request);        
        User savedUser = userRepository.save(user);
        
        return userMapper.toResponse(savedUser);
    }

    
}
