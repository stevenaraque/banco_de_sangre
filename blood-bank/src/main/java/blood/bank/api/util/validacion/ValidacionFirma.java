package blood.bank.api.util.validacion;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.exception.ConsentimientoNoFirmadoException;
import blood.bank.api.service.ValidacionDonante;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class ValidacionFirma implements ValidacionDonante {

    @Override
    public void validar(Donante donante) {
        String firma = donante.getFirmaConsentimiento();

        // 1. Verificar que no esté vacío
        if (!StringUtils.hasText(firma)) {
            throw new ConsentimientoNoFirmadoException(
                "El donante debe registrar su firma de consentimiento"
            );
        }

        // 2. Verificar que sea una de las 3 formas válidas según el taller
        boolean esBase64 = firma.startsWith("data:image");
        boolean esURL = firma.startsWith("http://") || firma.startsWith("https://");
        boolean esRutaLocal = firma.startsWith("uploads/firmas/");

        if (!esBase64 && !esURL && !esRutaLocal) {
            throw new ConsentimientoNoFirmadoException(
                "La firma debe ser: Base64 (data:image...), URL (http://...) o ruta de archivo subido (uploads/firmas/...)"
            );
        }

        // 3. Si es ruta local, verificar que el archivo exista físicamente
        if (esRutaLocal) {
            try {
                if (!Files.exists(Paths.get(firma))) {
                    throw new ConsentimientoNoFirmadoException(
                        "El archivo de firma no existe en el servidor: " + firma
                    );
                }
            } catch (Exception e) {
                throw new ConsentimientoNoFirmadoException(
                    "Error al verificar la firma: " + e.getMessage()
                );
            }
        }

        // 4. Si es Base64, verificar que sea formato válido (opcional, más estricto)
        if (esBase64) {
            try {
                String base64Data = firma.substring(firma.indexOf(",") + 1);
                Base64.getDecoder().decode(base64Data);
            } catch (IllegalArgumentException e) {
                throw new ConsentimientoNoFirmadoException(
                    "La firma Base64 no tiene formato válido"
                );
            }
        }
    }
}