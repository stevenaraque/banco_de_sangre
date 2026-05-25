package blood.bank.api.mapper;

import blood.bank.api.domain.entity.Consentimiento;
import blood.bank.api.dto.request.ConsentimientoRequest;
import blood.bank.api.dto.response.ConsentimientoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsentimientoMapper {

    ConsentimientoResponse toResponse(Consentimiento consentimiento);

    List<ConsentimientoResponse> toResponseList(List<Consentimiento> consentimientos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    Consentimiento toEntity(ConsentimientoRequest request);
}