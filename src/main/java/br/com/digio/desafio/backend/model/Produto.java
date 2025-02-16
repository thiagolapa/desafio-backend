package br.com.digio.desafio.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Produto(
        @NotBlank(message = "Código não pode ser nulo.")
        String codigo,
        @JsonProperty("tipo_vinho")
        @NotBlank(message = "Tipo não pode ser nulo.")
        String tipoVinho,
        BigDecimal preco,
        @NotNull(message = "Safra não pode ser nulo.")
        Integer safra,
        @JsonProperty("ano_compra")
        @NotNull(message = "Ano não pode ser nulo.")
        Integer anoCompra) {

    public Produto {
        preco = preco.setScale(2);
    }
}
