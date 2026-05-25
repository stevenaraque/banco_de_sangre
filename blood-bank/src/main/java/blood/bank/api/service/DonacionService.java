package blood.bank.api.service;

import blood.bank.api.dto.request.DonacionRequest;
import blood.bank.api.dto.response.DonacionResponse;

import java.util.List;

public interface DonacionService {

    DonacionResponse registrar(DonacionRequest request);

    List<DonacionResponse> listarTodos();

    DonacionResponse buscarPorId(Long id);

    DonacionResponse buscarPorCodigo(String codigo);
    
    byte[] generarHistorialPDF(Long donanteId);
}