package blood.bank.api.service;

import blood.bank.api.dto.response.InventarioResponse;
import blood.bank.api.enums.TipoSangre;

import java.util.List;

public interface InventarioService {

    List<InventarioResponse> consultarInventarioCompleto();

    InventarioResponse consultarPorTipo(TipoSangre tipoSangre);
}