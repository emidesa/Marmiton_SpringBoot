package foreach.cda.Marmiton.mappers;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import foreach.cda.Marmiton.DTO.CreateUserDto;
import foreach.cda.Marmiton.DTO.UpdateUserDto;
import foreach.cda.Marmiton.DTO.UserDto;
import foreach.cda.Marmiton.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setTelephone(user.getTelephone());
        dto.setMail(user.getMail());
        
        return dto;
    }

    public List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public User toEntity(CreateUserDto dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setPassword(dto.getPassword());
        user.setTelephone(dto.getTelephone());
        user.setMail(dto.getMail());
        
        return user;
    }

    public void updateEntityFromDto(UpdateUserDto dto, User user) {
        if (dto == null || user == null) {
            return;
        }
        
        if (dto.getNom() != null) {
            user.setNom(dto.getNom());
        }
        if (dto.getPrenom() != null) {
            user.setPrenom(dto.getPrenom());
        }
        if (dto.getTelephone() != null) {
            user.setTelephone(dto.getTelephone());
        }
        if (dto.getMail() != null) {
            user.setMail(dto.getMail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
    }
}