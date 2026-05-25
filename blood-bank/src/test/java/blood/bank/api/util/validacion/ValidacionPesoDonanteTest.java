package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.DonanteNoAptoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidacionPesoDonanteTest {

    private final ValidacionPesoDonante validacion = new ValidacionPesoDonante();

    @Test
    void validar_deberiaLanzarExcepcionCuandoPesaMenosDe50kg() {
        Donante donante = Donante.builder()
                .peso(45.0)
                .build();

        DonanteNoAptoException exception = assertThrows(DonanteNoAptoException.class, () -> {
            validacion.validar(donante);
        });

        assertTrue(exception.getMessage().contains("50.0kg"));
    }

    @Test
    void validar_noDeberiaLanzarExcepcionCuandoPesa50kgOMas() {
        Donante donante = Donante.builder()
                .peso(75.0)
                .build();

        assertDoesNotThrow(() -> validacion.validar(donante));
    }
}