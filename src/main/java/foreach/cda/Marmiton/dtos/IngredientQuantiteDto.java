package foreach.cda.Marmiton.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientQuantiteDto {
    
    @NotNull(message = "L'ID de l'ingrédient est obligatoire")
    private Long ingredientId;
    
    @NotBlank(message = "La quantité est obligatoire")
    private String quantite;
}
