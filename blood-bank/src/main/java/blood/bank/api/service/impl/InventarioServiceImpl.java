package blood.bank.api.service.impl;

import blood.bank.api.domain.entity.InventarioSangre;
import blood.bank.api.dto.response.InventarioResponse;
import blood.bank.api.enums.TipoSangre;
import blood.bank.api.exception.ResourceNotFoundException;
import blood.bank.api.mapper.InventarioMapper;
import blood.bank.api.repository.InventarioRepository;
import blood.bank.api.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponse> consultarInventarioCompleto() {
        return inventarioMapper.toResponseList(inventarioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponse consultarPorTipo(TipoSangre tipoSangre) {
        InventarioSangre inventario = inventarioRepository.findByTipoSangre(tipoSangre)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No hay registros de inventario para el tipo de sangre: " + tipoSangre.getEtiqueta()));
        return inventarioMapper.toResponse(inventario);
    }
}