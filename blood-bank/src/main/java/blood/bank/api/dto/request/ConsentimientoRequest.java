package blood.bank.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentimientoRequest {

    @NotNull(message = "El ID del donante es obligatorio")
    private Long donanteId;

    @NotNull(message = "Debe aceptar el consentimiento informado")
    private Boolean aceptaConsentimiento;

    private String firmaConsentimiento;

    private String versionDocumento;
}