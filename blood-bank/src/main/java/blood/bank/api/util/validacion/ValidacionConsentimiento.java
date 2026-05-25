package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.ConsentimientoNoFirmadoException;
import blood.bank.api.service.ValidacionDonante;
import org.springframework.stereotype.Component;

@Component
public class ValidacionConsentimiento implements ValidacionDonante {

    @Override
    public void validar(Donante donante) {
        if (Boolean.FALSE.equals(donante.getAceptaConsentimiento())) {
            throw new ConsentimientoNoFirmadoException("El donante debe aceptar el consentimiento informado");
        }
    }
}