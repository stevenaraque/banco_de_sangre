package blood.bank.api.repository;

import blood.bank.api.domain.entity.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    Optional<Donacion> findByCodigoDonacion(String codigoDonacion);

    boolean existsByCodigoDonacion(String codigoDonacion);

    List<Donacion> findByDonanteIdOrderByFechaDonacionDesc(Long donanteId);
}