package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.DonanteNoAptoException;
import blood.bank.api.service.ValidacionDonante;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ValidacionTiempoDonacion implements ValidacionDonante {

    private static final int MESES_MINIMOS = 3;

    @Override
    public void validar(Donante donante) {
        if (donante.getFechaUltimaDonacion() != null) {
            Period periodo = Period.between(donante.getFechaUltimaDonacion(), LocalDate.now());
            int mesesTranscurridos = periodo.getYears() * 12 + periodo.getMonths();
            
            if (mesesTranscurridos < MESES_MINIMOS) {
                throw new DonanteNoAptoException("Deben pasar al menos " + MESES_MINIMOS + " meses entre donaciones. Meses transcurridos: " + mesesTranscurridos);
            }
        }
    }
}