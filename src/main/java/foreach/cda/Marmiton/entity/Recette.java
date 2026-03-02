package foreach.cda.Marmiton.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recette")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_plat", nullable = false, length = 150)
    private String nomPlat;

    @Column(name = "duree_preparation", nullable = false)
    private Integer dureePreparation;

    @Column(name = "duree_cuisson", nullable = false)
    private Integer dureeCuisson;

    @Column(name = "nombre_calories")
    private Integer nombreCalories;

    @Column(nullable = false)
    private Boolean partage = false;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recette", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecetteIngredient> recetteIngredients = new HashSet<>();

    @ManyToMany(mappedBy = "recettesFavorites")
    private Set<User> usersFavoris = new HashSet<>();

    // Méthodes utilitaires pour gérer les ingrédients
    public void addIngredient(Ingredient ingredient, String quantite) {
        RecetteIngredient recetteIngredient = new RecetteIngredient();
        recetteIngredient.setRecette(this);
        recetteIngredient.setIngredient(ingredient);
        recetteIngredient.setQuantite(quantite);
        recetteIngredients.add(recetteIngredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        recetteIngredients.removeIf(ri -> ri.getIngredient().equals(ingredient));
    }
}
