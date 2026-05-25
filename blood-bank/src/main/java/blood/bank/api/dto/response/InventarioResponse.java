package blood.bank.api.dto.response;

import blood.bank.api.enums.TipoSangre;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponse {

    private Long id;
    private TipoSangre tipoSangre;
    private String etiquetaTipoSangre;
    private Integer cantidadML;
    private Integer unidadesDisponibles;
    private LocalDateTime actualizadoEn;
}