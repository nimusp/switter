package inc.myself.fo.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUtils {

    public static Map<String, String> getErrorMap(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(Collectors.toMap(
                field -> field.getField() + "Error",
                FieldError::getDefaultMessage
                )
        );
    }
}
