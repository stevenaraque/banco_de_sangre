package blood.bank.api.controller;

import blood.bank.api.dto.request.ConsentimientoRequest;
import blood.bank.api.dto.response.ConsentimientoResponse;
import blood.bank.api.service.ConsentimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consentimientos")
@RequiredArgsConstructor
@Tag(name = "Consentimientos", description = "Gestión de consentimientos informados de donantes")
public class ConsentimientoController {

    private final ConsentimientoService consentimientoService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo consentimiento informado")
    @ApiResponse(responseCode = "201", description = "Consentimiento registrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Donante no encontrado")
    public ResponseEntity<ConsentimientoResponse> registrar(@Valid @RequestBody ConsentimientoRequest request) {
        ConsentimientoResponse response = consentimientoService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{donanteId}")
    @Operation(summary = "Consultar historial de consentimientos de un donante")
    @ApiResponse(responseCode = "200", description = "Historial encontrado")
    @ApiResponse(responseCode = "404", description = "Donante no encontrado")
    public ResponseEntity<List<ConsentimientoResponse>> listarPorDonante(
            @Parameter(description = "ID del donante") @PathVariable Long donanteId) {
        return ResponseEntity.ok(consentimientoService.listarPorDonante(donanteId));
    }
}