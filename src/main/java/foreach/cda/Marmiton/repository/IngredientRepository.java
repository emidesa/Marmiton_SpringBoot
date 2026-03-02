package foreach.cda.Marmiton.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import foreach.cda.Marmiton.entity.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByType(Ingredient.Type type);
    List<Ingredient> findByLibelleContainingIgnoreCase(String libelle);
}
