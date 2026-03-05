package foreach.cda.Marmiton.dtos;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecetteDto {
    
    @NotBlank(message = "Le nom du plat est obligatoire")
    @Size(max = 150)
    private String nomPlat;
    
    @NotNull(message = "La durée de préparation est obligatoire")
    @Min(value = 0, message = "La durée de préparation doit être positive")
    private Integer dureePreparation;
    
    @NotNull(message = "La durée de cuisson est obligatoire")
    @Min(value = 0, message = "La durée de cuisson doit être positive")
    private Integer dureeCuisson;
    
    @Min(value = 0, message = "Le nombre de calories doit être positif")
    private Integer nombreCalories;
    
    private Boolean partage = false;
    
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long userId;
    
    private List<IngredientQuantiteDto> ingredients;
}
