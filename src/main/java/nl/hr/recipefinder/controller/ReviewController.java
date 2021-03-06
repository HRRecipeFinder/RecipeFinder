package nl.hr.recipefinder.controller;

import lombok.RequiredArgsConstructor;
import nl.hr.recipefinder.model.dto.ReviewDto;
import nl.hr.recipefinder.model.dto.ReviewResponseDto;
import nl.hr.recipefinder.model.dto.UserResponseDto;
import nl.hr.recipefinder.model.entity.Review;
import nl.hr.recipefinder.model.entity.User;
import nl.hr.recipefinder.model.httpexception.clienterror.HttpBadRequestException;
import nl.hr.recipefinder.service.AuthenticationService;
import nl.hr.recipefinder.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:4200",
  allowedHeaders = {"x-auth-token", "x-requested-with", "x-xsrf-token", "authorization", "content-type", "accept"})
@RequestMapping()
public class ReviewController {
  private final ReviewService reviewService;
  private final AuthenticationService authenticationService;
  private final ModelMapper modelMapper;

  @GetMapping("/recipe/{recipeId}/review")
  public ResponseEntity<List<ReviewResponseDto>> getReviewsForOneRecipe(@PathVariable("recipeId") Long recipeId) {
    List<Review> reviews = reviewService.findAllByRecipeId(recipeId);
    List<ReviewResponseDto> reviewsDto = new ArrayList<>();
    for (Review review : reviews) reviewsDto.add(modelMapper.map(review, ReviewResponseDto.class));
    return new ResponseEntity<>(reviewsDto, HttpStatus.OK);
  }

  @PostMapping("/recipe/{recipeId}/review")
  public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewDto reviewDto, @PathVariable("recipeId") Long recipeId) {
    try {
      User currentUser = authenticationService.getAuthenticatedUser();

      Review mappedReview = modelMapper.map(reviewDto, Review.class);
      mappedReview.setUser(currentUser);
      Review savedReview = reviewService.save(mappedReview, recipeId);
      ReviewResponseDto reviewResponseDto = modelMapper.map(savedReview, ReviewResponseDto.class);
      reviewResponseDto.setUser(modelMapper.map(currentUser, UserResponseDto.class));

      return new ResponseEntity<>(reviewResponseDto, HttpStatus.CREATED);
    } catch (ConstraintViolationException e) {
      throw new HttpBadRequestException(e);
    }
  }

  @GetMapping("/review")
  public ResponseEntity<List<Review>> getReviewsForAllRecipes() {
    List<Review> reviews = reviewService.findAll();
    return new ResponseEntity<>(reviews, HttpStatus.OK);
  }
}
