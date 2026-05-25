package blood.bank.api.service;

import blood.bank.api.dto.request.DonanteRequest;
import blood.bank.api.dto.response.DonanteResponse;
import blood.bank.api.dto.response.FirmaUploadResponse;
import blood.bank.api.enums.TipoSangre; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonanteService {
    
    DonanteResponse registrar(DonanteRequest request);
    
    List<DonanteResponse> listarTodos();
    
    Page<DonanteResponse> listarPaginado(Pageable pageable, TipoSangre tipoSangre);
    
    DonanteResponse buscarPorId(Long id);
    
    DonanteResponse actualizar(Long id, DonanteRequest request);
    
    void eliminar(Long id);
    
    void validarAptitud(Long donanteId);
    FirmaUploadResponse subirFirma(Long donanteId, org.springframework.web.multipart.MultipartFile archivo);
}