package foreach.cda.Marmiton.DTO.Response;

import foreach.cda.Marmiton.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String mail;
    private String nom;
    private String prenom;
    private Role role;
    private String telephone;

}
