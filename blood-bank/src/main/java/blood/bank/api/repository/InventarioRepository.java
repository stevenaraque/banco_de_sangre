package blood.bank.api.repository;

import blood.bank.api.domain.entity.InventarioSangre;
import blood.bank.api.enums.TipoSangre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioSangre, Long> {

    Optional<InventarioSangre> findByTipoSangre(TipoSangre tipoSangre);

    boolean existsByTipoSangre(TipoSangre tipoSangre);
}