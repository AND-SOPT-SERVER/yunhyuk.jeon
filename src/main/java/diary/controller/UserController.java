package diary.controller;

import diary.dto.LoginRequest;
import diary.dto.RegisterRequest;
import diary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/auth/login")
    ResponseEntity<Long> login(@RequestBody LoginRequest loginRequest) {
       Long token = userService.login(loginRequest.username(), loginRequest.password());
       return ResponseEntity.ok(token);
    }

    @PostMapping("/api/user/auth/register")
    ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest.username(), registerRequest.password(), registerRequest.nickname());
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
