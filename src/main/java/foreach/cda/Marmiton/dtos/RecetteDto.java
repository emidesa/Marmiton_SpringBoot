package foreach.cda.Marmiton.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteDto {
    private Long id;
    private String nomPlat;
    private Integer dureePreparation;
    private Integer dureeCuisson;
    private Integer nombreCalories;
    private Boolean partage;
    private Long userId;
    private String userNom;
    private String userPrenom;
    private List<RecetteIngredientDto> ingredients;
}
