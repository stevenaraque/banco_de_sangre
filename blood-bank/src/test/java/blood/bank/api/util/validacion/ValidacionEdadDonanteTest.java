package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.DonanteNoAptoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidacionEdadDonanteTest {

    private final ValidacionEdadDonante validacion = new ValidacionEdadDonante();

    @Test
    void validar_deberiaLanzarExcepcionCuandoEsMenorDeEdad() {
        Donante donante = Donante.builder()
                .fechaNacimiento(LocalDate.now().minusYears(16))
                .build();

        DonanteNoAptoException exception = assertThrows(DonanteNoAptoException.class, () -> {
            validacion.validar(donante);
        });

        assertTrue(exception.getMessage().contains("mayor de 18"));
    }

    @Test
    void validar_noDeberiaLanzarExcepcionCuandoEsMayorDeEdad() {
        Donante donante = Donante.builder()
                .fechaNacimiento(LocalDate.now().minusYears(25))
                .build();

        assertDoesNotThrow(() -> validacion.validar(donante));
    }
}