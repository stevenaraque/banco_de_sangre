package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.DonanteNoAptoException;
import blood.bank.api.service.ValidacionDonante;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ValidacionEdadDonante implements ValidacionDonante {

    private static final int EDAD_MINIMA = 18;

    @Override
    public void validar(Donante donante) {
        int edad = Period.between(donante.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < EDAD_MINIMA) {
            throw new DonanteNoAptoException("El donante debe ser mayor de " + EDAD_MINIMA + " años. Edad actual: " + edad);
        }
    }
}