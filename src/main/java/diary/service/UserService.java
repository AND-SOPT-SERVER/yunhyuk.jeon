package diary.service;

import diary.repository.UserEntity;
import diary.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Long login(String username, String password){
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);
        if (userEntity == null){
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }
        if (userEntity.getPassword().equals(password)){
            return userEntity.getId();
        }
        else {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    public void register(String username, String password, String nickname){
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);
        if (userEntity != null){
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }
        UserEntity newUserEntity = new UserEntity(username, password, nickname);
        userRepository.save(newUserEntity);
    }
}
