package blood.bank.api.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "consents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consentimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "donante_id", nullable = false)
    private Long donanteId;

    @Column(name = "acepta_consentimiento", nullable = false)
    private Boolean aceptaConsentimiento;

    @Column(name = "firma_consentimiento", columnDefinition = "TEXT")
    private String firmaConsentimiento;

    @Column(name = "version_documento", length = 20)
    private String versionDocumento;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}