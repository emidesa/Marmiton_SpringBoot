package foreach.cda.Marmiton.dtos;

import foreach.cda.Marmiton.entity.Ingredient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIngredientDto {
    
    @Size(max = 100)
    private String libelle;
    
    private Ingredient.Type type;
    
    @Min(value = 0, message = "Le nombre de calories doit être positif")
    private Integer nombreCalories;
}