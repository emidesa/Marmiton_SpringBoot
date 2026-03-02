package foreach.cda.Marmiton.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteIngredientId implements Serializable {

    @Column(name = "recette_id")
    private Long recetteId;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecetteIngredientId that = (RecetteIngredientId) o;
        return Objects.equals(recetteId, that.recetteId) &&
               Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recetteId, ingredientId);
    }
}