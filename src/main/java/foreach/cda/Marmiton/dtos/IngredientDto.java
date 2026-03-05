package foreach.cda.Marmiton.dtos;

import foreach.cda.Marmiton.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private Long id;
    private String libelle;
    private Ingredient.Type type;
    private Integer nombreCalories;
}
