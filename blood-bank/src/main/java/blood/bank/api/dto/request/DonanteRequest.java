package blood.bank.api.dto.request;

import blood.bank.api.enums.TipoSangre;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data 

@NoArgsConstructor
@AllArgsConstructor
public class DonanteRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El tipo de sangre es obligatorio")
    private TipoSangre tipoSangre;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a 0")
    private Double peso;

    private String telefono;

    @Email(message = "El correo no es válido")
    private String correo;

    private String direccion;

    private LocalDate fechaUltimaDonacion;

    @NotNull(message = "Debe aceptar el consentimiento informado")
    private Boolean aceptaConsentimiento;

    private String firmaConsentimiento;
}