package dev.magadiflo.r2dbc.app.validation;

import dev.magadiflo.r2dbc.app.exception.CustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ObjectValidator {

    private final Validator validator;

    /**
     * @SneakyThrows, es una anotación de Lombook. Se utiliza para suprimir la necesidad
     * de incluir una declaración de excepción en un método que arroja una excepción
     * comprobada (checked exception). Básicamente, @SneakyThrows oculta la necesidad
     * de capturar o declarar las excepciones lanzadas por el método anotado,
     * convirtiéndolas en excepciones no verificadas (unchecked exception). Esto puede
     * simplificar la escritura de código en situaciones donde no se quiere lidiar
     * directamente con el manejo de excepciones verificadas.
     */

    @SneakyThrows
    public <T> T validate(T object) {
        Set<ConstraintViolation<T>> errors = this.validator.validate(object);
        if (errors.isEmpty()) {
            return object;
        }
        String message = errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        throw new CustomException(HttpStatus.BAD_REQUEST, message);
    }
}
