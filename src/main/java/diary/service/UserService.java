package diary.service;

import diary.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
}
