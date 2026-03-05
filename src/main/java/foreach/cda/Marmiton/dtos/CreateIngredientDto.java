package foreach.cda.Marmiton.dtos;

import foreach.cda.Marmiton.entity.Ingredient;
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
public class CreateIngredientDto {
    
    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 100)
    private String libelle;
    
    @NotNull(message = "Le type est obligatoire")
    private Ingredient.Type type;
    
    @Min(value = 0, message = "Le nombre de calories doit être positif")
    private Integer nombreCalories;
}
