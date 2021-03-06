package nl.hr.recipefinder.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesListRequestDto {

  @Size(min = 1, max = 50)
  private String name;

  @Size(min = 1, max = 500)
  private String description;

  public UserResponseDto user;

  @OneToMany(cascade = CascadeType.ALL)
  private List<RecipeDto> recipes;
}
