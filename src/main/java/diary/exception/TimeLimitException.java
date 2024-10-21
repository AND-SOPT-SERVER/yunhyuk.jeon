package diary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class TimeLimitException extends RuntimeException{
    public TimeLimitException(String message) {
        super(message);
    }
}

