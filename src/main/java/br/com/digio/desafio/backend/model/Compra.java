package br.com.digio.desafio.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Compra (
    @JsonProperty("codigo")
    @NotBlank(message = "Código não pode ser nulo.")
    String codigoProduto,
    @NotNull(message = "Quantidade não pode ser nulo.")
    Integer quantidade) {
}