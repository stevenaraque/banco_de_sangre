package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.ConsentimientoNoFirmadoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidacionConsentimientoTest {

    private final ValidacionConsentimiento validacion = new ValidacionConsentimiento();

    @Test
    void validar_deberiaLanzarExcepcionCuandoNoAcepta() {
        Donante donante = Donante.builder()
                .aceptaConsentimiento(false)
                .build();

        ConsentimientoNoFirmadoException exception = assertThrows(ConsentimientoNoFirmadoException.class, () -> {
            validacion.validar(donante);
        });

        assertEquals("El donante debe aceptar el consentimiento informado", exception.getMessage());
    }

    @Test
    void validar_noDeberiaLanzarExcepcionCuandoAcepta() {
        Donante donante = Donante.builder()
                .aceptaConsentimiento(true)
                .build();

        assertDoesNotThrow(() -> validacion.validar(donante));
    }
}