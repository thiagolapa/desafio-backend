package br.com.digio.desafio.backend.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
public record ClienteFielResponseDTO(
        String nome,
        String cpf,
        Integer numeroCompras,
        BigDecimal valorTotal) {

    public ClienteFielResponseDTO {
        valorTotal = valorTotal.setScale(2, RoundingMode.HALF_DOWN);
    }
}
