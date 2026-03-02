package foreach.cda.Marmiton.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recette_ingredient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteIngredient {

    @EmbeddedId
    private RecetteIngredientId id = new RecetteIngredientId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recetteId")
    @JoinColumn(name = "recette_id")
    private Recette recette;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(nullable = false, length = 50)
    private String quantite;
}
