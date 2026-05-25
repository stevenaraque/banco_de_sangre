package blood.bank.api.domain.entity;

import blood.bank.api.enums.TipoSangre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sangre", nullable = false, length = 10)
    private TipoSangre tipoSangre;

    @Column(nullable = false)
    private Double peso;

    @Column(length = 20)
    private String telefono;

    @Column(length = 150)
    private String correo;

    @Column(length = 255)
    private String direccion;

    @Column(name = "fecha_ultima_donacion")
    private LocalDate fechaUltimaDonacion;

    @Column(name = "acepta_consentimiento", nullable = false)
    private Boolean aceptaConsentimiento;

    @Column(name = "firma_consentimiento", columnDefinition = "TEXT")
    private String firmaConsentimiento; // Base64, URL o ruta PDF

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}