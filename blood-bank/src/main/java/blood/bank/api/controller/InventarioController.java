package blood.bank.api.controller;

import blood.bank.api.dto.response.InventarioResponse;
import blood.bank.api.enums.TipoSangre;
import blood.bank.api.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Consulta de disponibilidad de sangre")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Consultar inventario completo de sangre")
    public ResponseEntity<List<InventarioResponse>> consultarInventarioCompleto() {
        return ResponseEntity.ok(inventarioService.consultarInventarioCompleto());
    }

    @GetMapping("/{tipoSangre}")
    @Operation(summary = "Consultar inventario por tipo de sangre")
    @ApiResponse(responseCode = "200", description = "Inventario encontrado")
    @ApiResponse(responseCode = "404", description = "Tipo de sangre no encontrado en inventario")
    public ResponseEntity<InventarioResponse> consultarPorTipo(@PathVariable TipoSangre tipoSangre) {
        return ResponseEntity.ok(inventarioService.consultarPorTipo(tipoSangre));
    }
}