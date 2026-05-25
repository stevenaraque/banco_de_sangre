package blood.bank.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirmaUploadResponse {

    private Long donanteId;
    private String rutaFirma;
    private String mensaje;
}