package foreach.cda.Marmiton.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import foreach.cda.Marmiton.entity.Recette;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    List<Recette> findAll();

    @Override
    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    Optional<Recette> findById(Long id);

    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    List<Recette> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    List<Recette> findByPartageTrue();

    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    List<Recette> findByNomPlatContainingIgnoreCase(String nomPlat);

    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    @Query("SELECT DISTINCT r FROM Recette r WHERE r.nombreCalories <= :maxCalories")
    List<Recette> findByMaxCalories(@Param("maxCalories") Integer maxCalories);

    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    @Query("SELECT DISTINCT r FROM Recette r WHERE (r.partage = true OR r.user.id = :userId)")
    List<Recette> findVisibleToUser(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    @Query("""
            SELECT DISTINCT r
            FROM Recette r
            WHERE LOWER(r.nomPlat) LIKE LOWER(CONCAT('%', :nomPlat, '%'))
              AND (r.partage = true OR r.user.id = :userId)
            """)
    List<Recette> searchVisibleByName(@Param("userId") Long userId, @Param("nomPlat") String nomPlat);

    @EntityGraph(attributePaths = {"user", "recetteIngredients", "recetteIngredients.ingredient"})
    @Query("""
            SELECT DISTINCT r
            FROM Recette r
            WHERE r.nombreCalories <= :maxCalories
              AND (r.partage = true OR r.user.id = :userId)
            """)
    List<Recette> findVisibleByMaxCalories(@Param("userId") Long userId, @Param("maxCalories") Integer maxCalories);
}
