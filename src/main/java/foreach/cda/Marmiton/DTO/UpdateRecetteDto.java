package foreach.cda.Marmiton.DTO;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecetteDto {
    
    @Size(max = 150)
    private String nomPlat;
    
    @Min(value = 0, message = "La durée de préparation doit être positive")
    private Integer dureePreparation;
    
    @Min(value = 0, message = "La durée de cuisson doit être positive")
    private Integer dureeCuisson;
    
    @Min(value = 0, message = "Le nombre de calories doit être positif")
    private Integer nombreCalories;
    
    private Boolean partage;
    
    private List<IngredientQuantiteDto> ingredients;
}
