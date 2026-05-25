package blood.bank.api.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonacionResponse {

    private Long id;
    private String codigoDonacion;
    private Long donanteId;
    private String nombreDonante;
    private String documentoDonante;
    private Integer cantidadML;
    private LocalDate fechaDonacion;
    private String observaciones;
    private LocalDateTime creadoEn;
}