package foreach.cda.Marmiton.services;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import foreach.cda.Marmiton.DTO.CreateUserDto;
import foreach.cda.Marmiton.DTO.UpdateUserDto;
import foreach.cda.Marmiton.DTO.UserDto;
import foreach.cda.Marmiton.entity.User;
import foreach.cda.Marmiton.mappers.UserMapper;
import foreach.cda.Marmiton.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // CREATE
    public UserDto createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByMail(createUserDto.getMail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        
        User user = userMapper.toEntity(createUserDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    // READ ALL
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    // READ ONE
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        return userMapper.toDto(user);
    }

    // UPDATE
    public UserDto updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (updateUserDto.getMail() != null && 
            !user.getMail().equals(updateUserDto.getMail()) && 
            userRepository.existsByMail(updateUserDto.getMail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        
        userMapper.updateEntityFromDto(updateUserDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    // DELETE
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id: " + id);
        }
        userRepository.deleteById(id);
    }
}