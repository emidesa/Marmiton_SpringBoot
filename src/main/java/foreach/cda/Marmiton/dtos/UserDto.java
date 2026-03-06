package foreach.cda.Marmiton.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String mail;
}
