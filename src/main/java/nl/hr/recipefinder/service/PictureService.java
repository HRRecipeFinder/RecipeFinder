package nl.hr.recipefinder.service;


import nl.hr.recipefinder.model.entity.Picture;
import nl.hr.recipefinder.repository.PictureRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PictureService {

  private final PictureRepository pictureRepository;

  public PictureService(PictureRepository pictureRepository) {
    this.pictureRepository = pictureRepository;
  }

  public Picture storePicture(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    Picture Picture = new Picture(fileName, file.getContentType(), file.getBytes());

    return pictureRepository.save(Picture);
  }

  public List<Picture> getPictures() {
    return pictureRepository.findAll();
  }

  public Optional<Picture> getPicture(long id) {
    return pictureRepository.findById(id);
  }
}




