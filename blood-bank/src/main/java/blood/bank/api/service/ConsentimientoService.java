package blood.bank.api.service;

import blood.bank.api.dto.request.ConsentimientoRequest;
import blood.bank.api.dto.response.ConsentimientoResponse;

import java.util.List;

public interface ConsentimientoService {

    ConsentimientoResponse registrar(ConsentimientoRequest request);

    List<ConsentimientoResponse> listarPorDonante(Long donanteId);
}