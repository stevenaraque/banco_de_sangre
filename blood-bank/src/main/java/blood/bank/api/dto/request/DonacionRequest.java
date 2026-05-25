package blood.bank.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonacionRequest {

    @NotNull(message = "El ID del donante es obligatorio")
    private Long donanteId;

    @NotNull(message = "La cantidad en mL es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidadML;

    private String observaciones;
}