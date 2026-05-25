package blood.bank.api.domain.entity;

import blood.bank.api.enums.TipoSangre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioSangre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sangre", nullable = false, unique = true, length = 10)
    private TipoSangre tipoSangre;

    @Column(name = "cantidad_ml", nullable = false)
    private Integer cantidadML;

    @Column(name = "unidades_disponibles", nullable = false)
    private Integer unidadesDisponibles;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}