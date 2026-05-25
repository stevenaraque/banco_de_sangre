package blood.bank.api.service.impl;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.dto.request.DonanteRequest;
import blood.bank.api.dto.response.DonanteResponse;
import blood.bank.api.enums.TipoSangre;
import blood.bank.api.exception.BusinessException;
import blood.bank.api.exception.ResourceNotFoundException;
import blood.bank.api.mapper.DonanteMapper;
import blood.bank.api.repository.DonanteRepository;
import blood.bank.api.service.ValidacionDonante;
import blood.bank.api.util.FileStorageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonanteServiceImplTest {

    @Mock
    private DonanteRepository donanteRepository;

    @Mock
    private DonanteMapper donanteMapper;

    @Mock
    private ValidacionDonante validacionDonante;

    @Mock
    private FileStorageUtil fileStorageUtil;

    private DonanteServiceImpl donanteService;

    private DonanteRequest request;
    private Donante donante;
    private DonanteResponse response;

    @BeforeEach
    void setUp() {
        List<ValidacionDonante> validaciones = new ArrayList<>();
        validaciones.add(validacionDonante);

        donanteService = new DonanteServiceImpl(donanteRepository, donanteMapper, validaciones, fileStorageUtil);

        // Crear request con new + setters
        request = new DonanteRequest();
        request.setNombres("Juan Carlos");
        request.setApellidos("Perez");
        request.setDocumento("1234567890");
        request.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        request.setTipoSangre(TipoSangre.O_POSITIVO);
        request.setPeso(75.5);
        request.setAceptaConsentimiento(true);
        request.setFirmaConsentimiento("firma123");

        // Crear entidad con new + setters
        donante = new Donante();
        donante.setId(1L);
        donante.setNombres("Juan Carlos");
        donante.setApellidos("Perez");
        donante.setDocumento("1234567890");

        // Crear response con new + setters
        response = new DonanteResponse();
        response.setId(1L);
        response.setNombres("Juan Carlos");
        response.setApellidos("Perez");
    }

    @Test
    void registrar_deberiaCrearDonanteCuandoEsValido() {
        when(donanteRepository.existsByDocumento("1234567890")).thenReturn(false);
        when(donanteMapper.toEntity(request)).thenReturn(donante);
        when(donanteRepository.save(any(Donante.class))).thenReturn(donante);
        when(donanteMapper.toResponse(donante)).thenReturn(response);

        DonanteResponse resultado = donanteService.registrar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Carlos", resultado.getNombres());
        
        verify(donanteRepository).existsByDocumento("1234567890");
        verify(donanteRepository).save(any(Donante.class));
        verify(validacionDonante).validar(any(Donante.class));
    }

    @Test
    void registrar_deberiaLanzarExcepcionCuandoDocumentoExiste() {
        when(donanteRepository.existsByDocumento("1234567890")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            donanteService.registrar(request);
        });

        assertEquals("Ya existe un donante con el documento: 1234567890", exception.getMessage());
        verify(donanteRepository, never()).save(any());
    }

    @Test
    void buscarPorId_deberiaRetornarDonanteCuandoExiste() {
        when(donanteRepository.findById(1L)).thenReturn(Optional.of(donante));
        when(donanteMapper.toResponse(donante)).thenReturn(response);

        DonanteResponse resultado = donanteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(donanteRepository).findById(1L);
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(donanteRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            donanteService.buscarPorId(99L);
        });

        assertEquals("Donante no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    void eliminar_deberiaEliminarDonanteCuandoExiste() {
        when(donanteRepository.existsById(1L)).thenReturn(true);

        donanteService.eliminar(1L);

        verify(donanteRepository).deleteById(1L);
    }
}