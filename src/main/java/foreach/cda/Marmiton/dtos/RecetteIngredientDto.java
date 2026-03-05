package foreach.cda.Marmiton.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteIngredientDto {
    private Long ingredientId;
    private String ingredientLibelle;
    private String quantite;
}
