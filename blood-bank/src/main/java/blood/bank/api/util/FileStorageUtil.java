package blood.bank.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitario para almacenamiento de archivos en disco.
 * 
 * Se encarga de:
 * - Validar que el archivo sea una imagen (PNG, JPG, JPEG)
 * - Generar nombres únicos para evitar colisiones
 * - Crear directorios si no existen
 * - Guardar el archivo en la ruta configurada
 */
@Component
public class FileStorageUtil {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private static final String[] EXTENSIONES_PERMITIDAS = { "png", "jpg", "jpeg" };
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Guarda un archivo de imagen en el directorio configurado.
     * 
     * @param donanteId ID del donante (para incluirlo en el nombre)
     * @param archivo Archivo recibido del cliente
     * @return Ruta relativa del archivo guardado (ej: uploads/firmas/firma_1_20260521_143022.png)
     * @throws IOException Si hay error al escribir en disco
     * @throws IllegalArgumentException Si el archivo no es una imagen válida
     */
    public String guardarFirma(Long donanteId, MultipartFile archivo) throws IOException {
        // 1. Validar que no esté vacío
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // 2. Validar extensión
        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!esExtensionPermitida(extension)) {
            throw new IllegalArgumentException(
                "Formato no permitido: " + extension + ". Solo se aceptan: PNG, JPG, JPEG");
        }

        // 3. Crear directorio si no existe
        Path directorio = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(directorio);

        // 4. Generar nombre único: firma_{id}_{fecha}.{ext}
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String nombreArchivo = String.format("firma_%d_%s.%s", donanteId, timestamp, extension);
        Path destino = directorio.resolve(nombreArchivo);

        // 5. Guardar archivo
        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

        // 6. Devolver ruta relativa
        return uploadDir + "/" + nombreArchivo;
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            throw new IllegalArgumentException("El archivo no tiene extensión");
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean esExtensionPermitida(String extension) {
        for (String ext : EXTENSIONES_PERMITIDAS) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}