package blood.bank.api.controller;

import blood.bank.api.dto.request.DonacionRequest;
import blood.bank.api.dto.response.DonacionResponse;
import blood.bank.api.service.DonacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
@Tag(name = "Donaciones", description = "Registro y consulta de donaciones de sangre")
public class DonacionController {

    private final DonacionService donacionService;

    @PostMapping
    @Operation(summary = "Registrar una nueva donación")
    @ApiResponse(responseCode = "201", description = "Donación registrada exitosamente")
    @ApiResponse(responseCode = "400", description = "Donante no apto o datos inválidos")
    public ResponseEntity<DonacionResponse> registrar(@Valid @RequestBody DonacionRequest request) {
        DonacionResponse response = donacionService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas las donaciones")
    public ResponseEntity<List<DonacionResponse>> listarTodos() {
        return ResponseEntity.ok(donacionService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar donación por ID")
    public ResponseEntity<DonacionResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(donacionService.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar donación por código único")
    public ResponseEntity<DonacionResponse> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(donacionService.buscarPorCodigo(codigo));
    }

    @GetMapping("/historial/{donanteId}/pdf")
    @Operation(summary = "Exportar historial de donaciones a PDF")
    @ApiResponse(responseCode = "200", description = "PDF generado exitosamente")
    @ApiResponse(responseCode = "404", description = "Donante no encontrado")
    public ResponseEntity<byte[]> exportarHistorialPDF(
            @Parameter(description = "ID del donante") @PathVariable Long donanteId) {
        
        byte[] pdfBytes = donacionService.generarHistorialPDF(donanteId);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=historial_donante_" + donanteId + ".pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfBytes);
    }
}