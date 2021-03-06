package nl.hr.recipefinder.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.hr.recipefinder.model.entity.Picture;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
  @Size(max = 5)
  private Integer score;

  @Size(min = 1, max = 50)
  private String title;

  @Size(min = 1, max = 500)
  private String message;

  private List<Picture> pictures;

  private UserResponseDto user;
}

