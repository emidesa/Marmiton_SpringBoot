package foreach.cda.Marmiton.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import foreach.cda.Marmiton.DTO.Request.UserRequest;
import foreach.cda.Marmiton.DTO.Response.UserResponse;
import foreach.cda.Marmiton.entity.User;


@Component
public class UserMapper {

    private final ModelMapper modelMapper;
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public User toEntity(UserRequest request) {
        return modelMapper.map(request, User.class);
    }

    public UserResponse toResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
