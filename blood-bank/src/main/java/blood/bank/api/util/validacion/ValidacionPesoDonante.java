package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.DonanteNoAptoException;
import blood.bank.api.service.ValidacionDonante;
import org.springframework.stereotype.Component;

@Component
public class ValidacionPesoDonante implements ValidacionDonante {

    private static final double PESO_MINIMO = 50.0;

    @Override
    public void validar(Donante donante) {
        if (donante.getPeso() < PESO_MINIMO) {
            throw new DonanteNoAptoException("El peso mínimo para donar es " + PESO_MINIMO + "kg. Peso actual: " + donante.getPeso() + "kg");
        }
    }
}