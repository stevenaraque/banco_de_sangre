package blood.bank.api.service.impl;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.dto.request.DonanteRequest;
import blood.bank.api.dto.response.DonanteResponse;
import blood.bank.api.enums.TipoSangre;
import blood.bank.api.exception.BusinessException;
import blood.bank.api.exception.ResourceNotFoundException;
import blood.bank.api.mapper.DonanteMapper;
import blood.bank.api.repository.DonanteRepository;
import blood.bank.api.service.DonanteService;
import blood.bank.api.service.ValidacionDonante;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import blood.bank.api.dto.response.FirmaUploadResponse;
import blood.bank.api.util.FileStorageUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonanteServiceImpl implements DonanteService {

    private final DonanteRepository donanteRepository;
    private final DonanteMapper donanteMapper;
    private final List<ValidacionDonante> validaciones;

    @Override
    @Transactional
    public DonanteResponse registrar(DonanteRequest request) {
        log.info("Registrando donante con documento: {}", request.getDocumento());
        
        if (donanteRepository.existsByDocumento(request.getDocumento())) {
            throw new BusinessException("Ya existe un donante con el documento: " + request.getDocumento());
        }

                Donante donante = donanteMapper.toEntity(request);
        validaciones.stream()
            .filter(v -> !(v instanceof blood.bank.api.util.validacion.ValidacionFirma))
            .forEach(v -> v.validar(donante));
        
        Donante guardado = donanteRepository.save(donante);
        
        log.info("Donante registrado con ID: {}", guardado.getId());
        return donanteMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonanteResponse> listarTodos() {
        return donanteMapper.toResponseList(donanteRepository.findAll());
    }

        @Override
    @Transactional(readOnly = true)
    public Page<DonanteResponse> listarPaginado(Pageable pageable, TipoSangre tipoSangre) {
        log.info("Listando donantes paginados. Página: {}, Tamaño: {}, TipoSangre: {}", 
            pageable.getPageNumber(), pageable.getPageSize(), tipoSangre);
        
        Page<Donante> page;
        if (tipoSangre != null) {
            page = donanteRepository.findByTipoSangre(tipoSangre, pageable);
        } else {
            page = donanteRepository.findAll(pageable);
        }
        return page.map(donanteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DonanteResponse buscarPorId(Long id) {
        Donante donante = donanteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + id));
        return donanteMapper.toResponse(donante);
    }

    @Override
    @Transactional
    public DonanteResponse actualizar(Long id, DonanteRequest request) {
        log.info("Actualizando donante ID: {}", id);
        
        Donante existente = donanteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + id));

        if (!existente.getDocumento().equals(request.getDocumento()) 
            && donanteRepository.existsByDocumento(request.getDocumento())) {
            throw new BusinessException("Ya existe otro donante con el documento: " + request.getDocumento());
        }

        existente.setNombres(request.getNombres());
        existente.setApellidos(request.getApellidos());
        existente.setDocumento(request.getDocumento());
        existente.setFechaNacimiento(request.getFechaNacimiento());
        existente.setTipoSangre(request.getTipoSangre());
        existente.setPeso(request.getPeso());
        existente.setTelefono(request.getTelefono());
        existente.setCorreo(request.getCorreo());
        existente.setDireccion(request.getDireccion());
        existente.setFechaUltimaDonacion(request.getFechaUltimaDonacion());
        existente.setAceptaConsentimiento(request.getAceptaConsentimiento());
        existente.setFirmaConsentimiento(request.getFirmaConsentimiento());

        Donante actualizado = donanteRepository.save(existente);
        return donanteMapper.toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!donanteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donante no encontrado con ID: " + id);
        }
        donanteRepository.deleteById(id);
        log.info("Donante eliminado ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public void validarAptitud(Long donanteId) {
        Donante donante = donanteRepository.findById(donanteId)
            .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + donanteId));
        
        log.info("Validando aptitud médica del donante ID: {}", donanteId);
        validarAptitud(donante);
        log.info("Donante ID: {} apto para donar", donanteId);
    }

    private void validarAptitud(Donante donante) {
        validaciones.forEach(v -> v.validar(donante));
    }
        private final FileStorageUtil fileStorageUtil;

    @Override
    @Transactional
    public FirmaUploadResponse subirFirma(Long donanteId, MultipartFile archivo) {
        log.info("Subiendo firma para donante ID: {}", donanteId);

        // Verificar que el donante existe
        Donante donante = donanteRepository.findById(donanteId)
            .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + donanteId));

        try {
            // Guardar archivo en disco
            String rutaFirma = fileStorageUtil.guardarFirma(donanteId, archivo);

            // Actualizar la ruta en la base de datos
            donante.setFirmaConsentimiento(rutaFirma);
            donanteRepository.save(donante);

            log.info("Firma guardada en: {}", rutaFirma);

            return FirmaUploadResponse.builder()
                    .donanteId(donanteId)
                    .rutaFirma(rutaFirma)
                    .mensaje("Firma subida exitosamente")
                    .build();

        } catch (IOException e) {
            log.error("Error al guardar firma: {}", e.getMessage());
            throw new BusinessException("Error al guardar la firma: " + e.getMessage());
        }
    }
}