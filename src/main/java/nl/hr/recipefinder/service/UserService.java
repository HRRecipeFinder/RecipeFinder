package nl.hr.recipefinder.service;

import nl.hr.recipefinder.model.entity.User;
import nl.hr.recipefinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public User findUserByUsername(String username){
    return userRepository.findUserByUsername(username);
  }

  public User findUserByUsernameAndPassword(String username, String password){
    return userRepository.findUserByUsernameAndPassword(username, password);
  }

  public List<User> findAll(){
    return userRepository.findAll();
  }

  public void save(User user){
    userRepository.save(user);
  }
}
