package br.com.digio.desafio.backend.dto;

import br.com.digio.desafio.backend.model.Produto;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ComprasResponseDTO(String nomeCliente,
                                 String cpfCliente,
                                 Produto produto,
                                 Integer quantidade,
                                 BigDecimal valorTotal) {
    public ComprasResponseDTO {
        valorTotal = valorTotal.setScale(2);
    }
}
