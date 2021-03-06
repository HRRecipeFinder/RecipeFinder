package nl.hr.recipefinder.repository;

import nl.hr.recipefinder.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {


  List<Recipe> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
