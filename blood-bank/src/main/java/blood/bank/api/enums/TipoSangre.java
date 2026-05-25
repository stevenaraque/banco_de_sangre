package blood.bank.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoSangre {
    O_POSITIVO("O+"),
    O_NEGATIVO("O-"),
    A_POSITIVO("A+"),
    A_NEGATIVO("A-"),
    B_POSITIVO("B+"),
    B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"),
    AB_NEGATIVO("AB-");

    private final String etiqueta;

    TipoSangre(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    public String getEtiqueta() {
        return etiqueta;
    }

    @JsonCreator
    public static TipoSangre fromValue(String value) {
        for (TipoSangre tipo : values()) {
            // Acepta tanto "O_NEGATIVO" como "O-"
            if (tipo.name().equals(value) || tipo.etiqueta.equals(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de sangre inválido: " + value);
    }
}