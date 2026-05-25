package blood.bank.api.controller;

import blood.bank.api.dto.request.DonanteRequest;
import blood.bank.api.dto.response.DonanteResponse;
import blood.bank.api.enums.TipoSangre;
import blood.bank.api.service.DonanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import blood.bank.api.dto.response.FirmaUploadResponse;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/donantes")
@RequiredArgsConstructor
@Tag(name = "Donantes", description = "Gestión de donantes de sangre")
public class DonanteController {

    private final DonanteService donanteService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo donante")
    @ApiResponse(responseCode = "201", description = "Donante creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o donante ya existe")
    public ResponseEntity<DonanteResponse> registrar(@Valid @RequestBody DonanteRequest request) {
        DonanteResponse response = donanteService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos los donantes (sin paginación)")
    public ResponseEntity<List<DonanteResponse>> listarTodos() {
        return ResponseEntity.ok(donanteService.listarTodos());
    }

    @GetMapping
    @Operation(summary = "Listar donantes paginados (opcional: filtrar por tipo de sangre)")
    public ResponseEntity<Page<DonanteResponse>> listarPaginados(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) TipoSangre tipoSangre) {
        return ResponseEntity.ok(donanteService.listarPaginado(pageable, tipoSangre));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar donante por ID")
    @ApiResponse(responseCode = "200", description = "Donante encontrado")
    @ApiResponse(responseCode = "404", description = "Donante no encontrado")
    public ResponseEntity<DonanteResponse> buscarPorId(
            @Parameter(description = "ID del donante") @PathVariable Long id) {
        return ResponseEntity.ok(donanteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un donante")
    @ApiResponse(responseCode = "200", description = "Donante actualizado")
    @ApiResponse(responseCode = "404", description = "Donante no encontrado")
    public ResponseEntity<DonanteResponse> actualizar(
            @Parameter(description = "ID del donante") @PathVariable Long id,
            @Valid @RequestBody DonanteRequest request) {
        return ResponseEntity.ok(donanteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un donante")
    @ApiResponse(responseCode = "204", description = "Donante eliminado")
    @ApiResponse(responseCode = "404", description = "Donante no encontrado")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del donante") @PathVariable Long id) {
        donanteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
        @PostMapping(
        value = "/{id}/firma",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Subir firma de consentimiento como imagen")
    @ApiResponse(responseCode = "200", description = "Firma subida exitosamente")
    @ApiResponse(responseCode = "400", description = "Archivo inválido")
    @ApiResponse(responseCode = "404", description = "Donante no encontrado")
    public ResponseEntity<FirmaUploadResponse> subirFirma(
            @Parameter(description = "ID del donante") @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(donanteService.subirFirma(id, archivo));
    }
}