package br.com.digio.desafio.backend.controller;

import br.com.digio.desafio.backend.dto.ClienteFielResponseDTO;
import br.com.digio.desafio.backend.dto.ComprasResponseDTO;
import br.com.digio.desafio.backend.service.ComprasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
@Tag(name = "Compras", description = "Loja de Vinhos Service")
public class ComprasController {

    @Autowired
    private ComprasService comprasService;

    @GetMapping
    @Operation(
            summary = "Responsavel por listar compras de forma crescente por valor.", tags = "Compras",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ComprasResponseDTO.class))}),
                    @ApiResponse(responseCode = "400", description = "Erro ao buscar dados.",
                            content = {@Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500", description = "Erro interno.",
                            content = {@Content(mediaType = "application/json")})
            }
    )
    public ResponseEntity<List<ComprasResponseDTO>> listarCompras() {
        return ResponseEntity.ok(comprasService.listarCompras());
    }

    @GetMapping("/maior-compra/{ano}")
    @Operation(
            summary = "Responsavel por listar a maior compra por ano.", tags = "Compras",
            responses = {
                @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ComprasResponseDTO.class))}),
                @ApiResponse(responseCode = "400", description = "Erro ao buscar dados."),
                @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    public ResponseEntity<ComprasResponseDTO> maiorCompraPorAno(@PathVariable @Min(1900) final Integer ano) {
        return ResponseEntity.ok(comprasService.maiorCompraPorAno(ano));
    }

    @GetMapping("/clientes-fieis")
    @Operation(
            summary = "Responsavel por retornar o Top 3 clientes mais fieis.", tags = "Compras",
            responses = {
                @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClienteFielResponseDTO.class))}),
                @ApiResponse(responseCode = "400", description = "Erro ao buscar dados."),
                @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    public ResponseEntity<List<ClienteFielResponseDTO>> clientesFieis() {
        return ResponseEntity.ok(comprasService.clientesFieis());
    }

    @GetMapping("/recomendacao/cliente/{cpf}/tipo")
    @Operation(
            summary = "Retornar uma recomendação de vinho baseado nos tipos de vinho que o cliente mais compra.", tags = "Compras",
            responses = {
                @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.",
                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
                @ApiResponse(responseCode = "400", description = "Erro ao buscar dados."),
                @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    public ResponseEntity<String> recomendacaoVinhoPorCliente(
            @PathVariable @Pattern(regexp = "\\d{11}") final String cpf) {
        return ResponseEntity.ok(comprasService.recomendacaoVinhoPorCliente(cpf));
    }
}
