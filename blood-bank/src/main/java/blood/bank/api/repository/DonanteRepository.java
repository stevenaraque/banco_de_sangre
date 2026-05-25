package blood.bank.api.repository;

import blood.bank.api.domain.entity.Donante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import blood.bank.api.enums.TipoSangre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface DonanteRepository extends JpaRepository<Donante, Long> {

    Optional<Donante> findByDocumento(String documento);

    boolean existsByDocumento(String documento);
    
    Page<Donante> findByTipoSangre(TipoSangre tipoSangre, Pageable pageable);
}