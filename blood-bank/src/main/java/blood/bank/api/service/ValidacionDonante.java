package blood.bank.api.service;

import blood.bank.api.domain.entity.Donante;

public interface ValidacionDonante {
    void validar(Donante donante);
}