package blood.bank.api.config;

import blood.bank.api.domain.entity.InventarioSangre;
import blood.bank.api.enums.TipoSangre;
import blood.bank.api.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final InventarioRepository inventarioRepository;

    @Override
    public void run(String... args) {
        log.info("Inicializando inventario de sangre...");

        for (TipoSangre tipo : TipoSangre.values()) {
            if (!inventarioRepository.existsByTipoSangre(tipo)) {
                InventarioSangre inventario = InventarioSangre.builder()
                    .tipoSangre(tipo)
                    .cantidadML(0)
                    .unidadesDisponibles(0)
                    .build();
                inventarioRepository.save(inventario);
                log.info("Creado inventario para tipo: {} ({})", tipo, tipo.getEtiqueta());
            } else {
                log.info("Inventario ya existe para tipo: {} ({})", tipo, tipo.getEtiqueta());
            }
        }

        log.info("Inventario inicializado correctamente. Total tipos: {}", TipoSangre.values().length);
    }
}