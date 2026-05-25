package blood.bank.api.mapper;

import blood.bank.api.domain.entity.InventarioSangre;
import blood.bank.api.dto.response.InventarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventarioMapper {

    @Mapping(target = "etiquetaTipoSangre", source = "tipoSangre.etiqueta")
    InventarioResponse toResponse(InventarioSangre inventario);

    List<InventarioResponse> toResponseList(List<InventarioSangre> inventarios);
}