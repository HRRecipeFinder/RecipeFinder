package nl.hr.recipefinder.controller;

import nl.hr.recipefinder.model.dto.ReportRequestDto;
import nl.hr.recipefinder.model.dto.ReportResponseDto;
import nl.hr.recipefinder.model.entity.Recipe;
import nl.hr.recipefinder.model.entity.Report;
import nl.hr.recipefinder.model.entity.ReportKey;
import nl.hr.recipefinder.model.entity.User;
import nl.hr.recipefinder.model.httpexception.clienterror.HttpBadRequestError;
import nl.hr.recipefinder.model.httpexception.clienterror.HttpConflictError;
import nl.hr.recipefinder.model.httpexception.clienterror.HttpNotFoundError;
import nl.hr.recipefinder.model.httpexception.servererror.HttpInternalServerError;
import nl.hr.recipefinder.service.RecipeService;
import nl.hr.recipefinder.service.ReportService;
import nl.hr.recipefinder.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "localhost:4200",
  allowedHeaders = {"x-auth-token", "x-requested-with", "x-xsrf-token", "authorization", "content-type", "accept"})
@RequestMapping("/report")
public class ReportController {

  private final ReportService reportService;
  private final UserService userService;
  private final RecipeService recipeService;
  private final ModelMapper modelMapper;

  @Autowired
  public ReportController(
    ReportService reportService,
    UserService userService,
    RecipeService recipeService,
    ModelMapper modelMapper
  ) {
    this.reportService = reportService;
    this.userService = userService;
    this.recipeService = recipeService;
    this.modelMapper = modelMapper;
  }

  @GetMapping()
  public ResponseEntity<List<Report>> getAllReports() {
    List<Report> reports = reportService.findAll();
    return new ResponseEntity<>(reports, HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto reportRequestDto) {
    try {
      Optional<User> foundReporter = userService.findUserById(reportRequestDto.getReportingUserId());
      if (foundReporter.isEmpty()) throw new HttpNotFoundError();

      Optional<Recipe> foundRecipe = recipeService.findById(reportRequestDto.getReportedRecipeId());
      if (foundRecipe.isEmpty()) throw new HttpNotFoundError();

      ReportKey id =  new ReportKey(foundReporter.get().getId(), foundRecipe.get().getUser().getId());
      Optional<Report> existingReport = reportService.findById(id);

      if (existingReport.isPresent()) throw new HttpBadRequestError("You have already reported the user");

      Report report = reportService.save(
        new Report(
         id,
          null, null, reportRequestDto.getMessage(),
          foundReporter.get(), foundRecipe.get().getUser(), foundRecipe.get()
        )
      );
      return new ResponseEntity<>(modelMapper.map(report, ReportResponseDto.class), HttpStatus.CREATED);
    } catch (DataIntegrityViolationException e) {
      throw new HttpConflictError(e);
    } catch (DataAccessException e) {
      throw new HttpInternalServerError(e);
    }
  }
}
