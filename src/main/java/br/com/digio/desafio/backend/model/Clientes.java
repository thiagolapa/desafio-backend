package br.com.digio.desafio.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Clientes(
        @NotBlank(message = "Nome não pode ser nulo.")
        String nome,
        @NotBlank(message = "Cpf não pode ser nulo.")
        String cpf,
        List<Compra> compras) {
}