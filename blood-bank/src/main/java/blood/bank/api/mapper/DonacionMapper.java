package blood.bank.api.mapper;

import blood.bank.api.domain.entity.Donacion;
import blood.bank.api.dto.request.DonacionRequest;
import blood.bank.api.dto.response.DonacionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DonacionMapper {

    @Mapping(target = "donanteId", source = "donante.id")
    @Mapping(target = "nombreDonante", source = "donante.nombres")
    @Mapping(target = "documentoDonante", source = "donante.documento")
    DonacionResponse toResponse(Donacion donacion);

    List<DonacionResponse> toResponseList(List<Donacion> donaciones);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoDonacion", ignore = true)
    @Mapping(target = "fechaDonacion", ignore = true)
    @Mapping(target = "donante", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    Donacion toEntity(DonacionRequest request);
}