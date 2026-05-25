package blood.bank.api.mapper;

import blood.bank.api.domain.entity.Donante;
import blood.bank.api.dto.request.DonanteRequest;
import blood.bank.api.dto.response.DonanteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DonanteMapper {

    // Entity → Response
    DonanteResponse toResponse(Donante donante);

    List<DonanteResponse> toResponseList(List<Donante> donantes);

    // Request → Entity (ignoramos campos que maneja la BD)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    Donante toEntity(DonanteRequest request);
}