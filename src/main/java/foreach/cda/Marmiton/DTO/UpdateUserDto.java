package foreach.cda.Marmiton.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    
    @Size(max = 100)
    private String nom;
    
    @Size(max = 100)
    private String prenom;
        
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
    
    @Size(max = 20)
    private String telephone;
    
    @Email(message = "L'email doit être valide")
    @Size(max = 150)
    private String mail;
}
