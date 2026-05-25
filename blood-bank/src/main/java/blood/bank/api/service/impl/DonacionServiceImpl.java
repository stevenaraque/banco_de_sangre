package blood.bank.api.service.impl;

import blood.bank.api.domain.entity.Donacion;
import blood.bank.api.domain.entity.Donante;
import blood.bank.api.domain.entity.InventarioSangre;
import blood.bank.api.dto.request.DonacionRequest;
import blood.bank.api.dto.response.DonacionResponse;
import blood.bank.api.exception.BusinessException;
import blood.bank.api.exception.ResourceNotFoundException;
import blood.bank.api.mapper.DonacionMapper;
import blood.bank.api.repository.DonacionRepository;
import blood.bank.api.repository.DonanteRepository;
import blood.bank.api.repository.InventarioRepository;
import blood.bank.api.service.DonacionService;
import blood.bank.api.service.DonanteService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonacionServiceImpl implements DonacionService {

    private final DonacionRepository donacionRepository;
    private final DonanteRepository donanteRepository;
    private final InventarioRepository inventarioRepository;
    private final DonanteService donanteService;
    private final DonacionMapper donacionMapper;

    @Override
    @Transactional
    public DonacionResponse registrar(DonacionRequest request) {
        log.info("Procesando donación para donante ID: {}", request.getDonanteId());

        Donante donante = donanteRepository.findById(request.getDonanteId())
            .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + request.getDonanteId()));

        donanteService.validarAptitud(donante.getId());

        String codigo = generarCodigoUnico();

        Donacion donacion = donacionMapper.toEntity(request);
        donacion.setDonante(donante);
        donacion.setCodigoDonacion(codigo);
        donacion.setFechaDonacion(LocalDate.now());

        Donacion guardada = donacionRepository.save(donacion);

        donante.setFechaUltimaDonacion(LocalDate.now());
        donanteRepository.save(donante);

        actualizarInventario(donante.getTipoSangre(), request.getCantidadML());

        log.info("Donación registrada exitosamente. Código: {}", codigo);
        return donacionMapper.toResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonacionResponse> listarTodos() {
        return donacionMapper.toResponseList(donacionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public DonacionResponse buscarPorId(Long id) {
        Donacion donacion = donacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada con ID: " + id));
        return donacionMapper.toResponse(donacion);
    }

    @Override
    @Transactional(readOnly = true)
    public DonacionResponse buscarPorCodigo(String codigo) {
        Donacion donacion = donacionRepository.findByCodigoDonacion(codigo)
            .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada con código: " + codigo));
        return donacionMapper.toResponse(donacion);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarHistorialPDF(Long donanteId) {
        log.info("Generando historial PDF para donante ID: {}", donanteId);

        Donante donante = donanteRepository.findById(donanteId)
            .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + donanteId));

        List<Donacion> donaciones = donacionRepository.findByDonanteIdOrderByFechaDonacionDesc(donanteId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
            Paragraph titulo = new Paragraph("Historial de Donaciones de Sangre", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // Datos del donante
            Font datosFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph("Donante: " + donante.getNombres() + " " + donante.getApellidos(), datosFont));
            document.add(new Paragraph("Documento: " + donante.getDocumento(), datosFont));
            document.add(new Paragraph("Tipo de Sangre: " + donante.getTipoSangre().getEtiqueta(), datosFont));
            document.add(new Paragraph(" "));

            // Tabla de donaciones
            if (donaciones.isEmpty()) {
                document.add(new Paragraph("No hay donaciones registradas.", datosFont));
            } else {
                PdfPTable tabla = new PdfPTable(4);
                tabla.setWidthPercentage(100);
                tabla.setSpacingBefore(10);

                String[] headers = { "Código", "Fecha", "Cantidad (mL)", "Observaciones" };
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE)));
                    cell.setBackgroundColor(new Color(200, 16, 46));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    tabla.addCell(cell);
                }

                int totalML = 0;
                for (Donacion d : donaciones) {
                    tabla.addCell(d.getCodigoDonacion());
                    tabla.addCell(d.getFechaDonacion().toString());
                    tabla.addCell(d.getCantidadML().toString());
                    tabla.addCell(d.getObservaciones() != null ? d.getObservaciones() : "-");
                    totalML += d.getCantidadML();
                }

                document.add(tabla);
                document.add(new Paragraph(" "));

                Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD);
                document.add(new Paragraph("Total de donaciones: " + donaciones.size(), totalFont));
                document.add(new Paragraph("Total de sangre donada: " + totalML + " mL", totalFont));
            }

            // Pie de página
            document.add(new Paragraph(" "));
            Font pieFont = new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY);
            Paragraph pie = new Paragraph("Generado el " + LocalDate.now() + " por Blood Bank API", pieFont);
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();

            log.info("PDF generado exitosamente. Donaciones incluidas: {}", donaciones.size());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error al generar PDF: {}", e.getMessage());
            throw new BusinessException("Error al generar el PDF: " + e.getMessage());
        }
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = "DON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (donacionRepository.existsByCodigoDonacion(codigo));
        return codigo;
    }

    private void actualizarInventario(blood.bank.api.enums.TipoSangre tipoSangre, Integer cantidadML) {
        InventarioSangre inventario = inventarioRepository.findByTipoSangre(tipoSangre)
            .orElseGet(() -> {
                log.info("Creando nuevo registro de inventario para tipo: {}", tipoSangre.getEtiqueta());
                return InventarioSangre.builder()
                    .tipoSangre(tipoSangre)
                    .cantidadML(0)
                    .unidadesDisponibles(0)
                    .build();
            });

        inventario.setCantidadML(inventario.getCantidadML() + cantidadML);
        inventario.setUnidadesDisponibles(inventario.getUnidadesDisponibles() + 1);
        inventarioRepository.save(inventario);

        log.info("Inventario actualizado. Tipo: {} | Total mL: {} | Unidades: {}",
            tipoSangre.getEtiqueta(), inventario.getCantidadML(), inventario.getUnidadesDisponibles());
    }
}