package com.swconstruction.backend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;



@RestController
public class UserController{

    /* User Database */
    private final UserRepository userRepository;
    /* Password Encoder */
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * This method returns all users.
     * @return Iterable<User>  List of all users.
     */
    @GetMapping(path="/users")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    /**
     * This method adds a new user to the database.
     * @param newUser   User to be added
     * @return ResponseEntity  201 Created
     * @throws UserAlreadyExistsException if a user exists in the database with the same username.
     * @throws BadRequestException if username is greater than 12 characters.
     * Username or password is null or empty string.
     */
    @PostMapping(path="/sign_up")
    public @ResponseBody ResponseEntity<Void> createUser(@RequestBody User newUser)  throws UserAlreadyExistsException
    {
        if(newUser == null || newUser.getUsername() == null || newUser.getPassword() == null ||
                newUser.getPassword().isEmpty() || newUser.getUsername().isEmpty() || newUser.getUsername().length() > 12)
        {
            throw new BadRequestException("Invalid username or password");
        }


        /* Check whether or not a user exists with the same username */
        Optional<User> user = userRepository.findByUsername(newUser.getUsername());
        /* Throws an exception if user exists with the same username*/
        if(user.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists!");
        }
        /* If user doesn't exist, create and insert new user to the database */
        else {
            /* Encode the password */
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setLevel(1);
            /* Insert user to the database */
            userRepository.save(newUser);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                    "/{id}").buildAndExpand(newUser.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
    }


     /**
      * This method authenticate user's username and password
      * then returns user id if authentication is valid.
      * @param user  user requested to login
      * @return id   user id
      * @throws UserNotFoundException if incorrect username or password is entered.
      */
     @PostMapping(path="/login")
     public @ResponseBody User login(@RequestBody User user) throws UserNotFoundException {

        Optional<User> opt_user = userRepository.findByUsername(user.getUsername());

        /* If username and  password  true ,returns user id */
        if(opt_user.isPresent()
        && passwordEncoder.matches(user.getPassword(),opt_user.get().getPassword())) {
            User loggedUser = opt_user.get();
            loggedUser.setPassword(null);
            return loggedUser;
        }

        /* Throws an exception if the username or password is wrong, not exist */
        else{
            throw new UserNotFoundException("Incorrect username or password!");

        }
    }


    /**
     * This method returns the user with the given id.
     * @param id        user id
     * @return user     user with the given id
     * @throws UserNotFoundException if a user doesn't exist with the given id.
     */
    @GetMapping("/users/{id}")
    public @ResponseBody User retrieveUser(@PathVariable Integer id)  throws UserNotFoundException{

        Optional<User> user = userRepository.findById(id);
        /* Check whether or not a user exists with the given id */
        if(!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        /* If user exists, return user id */
        return user.get();
    }

    /**
     * This method deletes the user with the given id
     * @param id    id of the user to be deleted
     * @throws UserNotFoundException if the user doesn't exist with the given id
     */
    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable Integer id)  throws UserNotFoundException {

        Optional<User> user = userRepository.findById(id);
        /* Throws an exception if the user doesn't exist with the given id */
        if(!user.isPresent()) {
            throw new UserNotFoundException("User not found!");
        }
        /* Delete user */
        userRepository.deleteById(id);
    }
}