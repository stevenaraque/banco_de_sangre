package blood.bank.api.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentimientoResponse {

    private Long id;
    private Long donanteId;
    private Boolean aceptaConsentimiento;
    private String firmaConsentimiento;
    private String versionDocumento;
    private LocalDateTime creadoEn;
}