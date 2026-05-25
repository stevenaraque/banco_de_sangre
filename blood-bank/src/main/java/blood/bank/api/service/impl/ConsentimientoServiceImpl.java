package blood.bank.api.service.impl;

import blood.bank.api.domain.entity.Consentimiento;
import blood.bank.api.domain.entity.Donante;
import blood.bank.api.dto.request.ConsentimientoRequest;
import blood.bank.api.dto.response.ConsentimientoResponse;
import blood.bank.api.exception.ResourceNotFoundException;
import blood.bank.api.mapper.ConsentimientoMapper;
import blood.bank.api.repository.ConsentimientoRepository;
import blood.bank.api.repository.DonanteRepository;
import blood.bank.api.service.ConsentimientoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsentimientoServiceImpl implements ConsentimientoService {

    private final ConsentimientoRepository consentimientoRepository;
    private final DonanteRepository donanteRepository;
    private final ConsentimientoMapper consentimientoMapper;

    @Override
    @Transactional
    public ConsentimientoResponse registrar(ConsentimientoRequest request) {
        log.info("Registrando consentimiento para donante ID: {}", request.getDonanteId());

        // Verificar que el donante existe
        Donante donante = donanteRepository.findById(request.getDonanteId())
            .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + request.getDonanteId()));

        // Actualizar la firma en el donante (último consentimiento vigente)
        donante.setAceptaConsentimiento(request.getAceptaConsentimiento());
        donante.setFirmaConsentimiento(request.getFirmaConsentimiento());
        donanteRepository.save(donante);

        // Guardar el consentimiento en el historial
        Consentimiento consentimiento = consentimientoMapper.toEntity(request);
        Consentimiento guardado = consentimientoRepository.save(consentimiento);

        log.info("Consentimiento registrado con ID: {}", guardado.getId());
        return consentimientoMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsentimientoResponse> listarPorDonante(Long donanteId) {
        log.info("Consultando historial de consentimientos para donante ID: {}", donanteId);

        if (!donanteRepository.existsById(donanteId)) {
            throw new ResourceNotFoundException("Donante no encontrado con ID: " + donanteId);
        }

        return consentimientoMapper.toResponseList(
            consentimientoRepository.findByDonanteIdOrderByCreadoEnDesc(donanteId)
        );
    }
}