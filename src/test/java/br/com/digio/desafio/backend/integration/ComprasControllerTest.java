package br.com.digio.desafio.backend.integration;

import br.com.digio.desafio.backend.dto.ClienteFielResponseDTO;
import br.com.digio.desafio.backend.dto.ComprasResponseDTO;
import br.com.digio.desafio.backend.exception.ComprasNotFoundException;
import br.com.digio.desafio.backend.exception.InternalServerErrorException;
import br.com.digio.desafio.backend.model.Produto;
import br.com.digio.desafio.backend.service.ComprasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ComprasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComprasService comprasService;

    @Test
    void testListarCompras_Sucesso() throws Exception {
        when(comprasService.listarCompras()).thenReturn(getComprasResponseDTOS());

        mockMvc.perform(get("/compras")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].cpfCliente", is("1051252612")));
    }

    @Test
    public void testListarCompras_Erro404() throws Exception {
        when(comprasService.listarCompras()).thenThrow(new ComprasNotFoundException("Erro ao buscar dados"));
        mockMvc.perform(get("/compras")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testListarCompras_ErroInterno() throws Exception {
        when(comprasService.listarCompras()).thenThrow(new InternalServerErrorException("Erro interno"));
        mockMvc.perform(get("/compras") // Altere para o seu endpoint
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testMaiorCompraPorAno_Sucesso() throws Exception {
        Integer ano = 2023;
        ComprasResponseDTO responseDTO = new ComprasResponseDTO("Hadassa Daniela Sales", "1051252612", new Produto("12", "Branco", new BigDecimal("106.50"), 2018, 2019), 2, new BigDecimal("213.00"));
        when(comprasService.maiorCompraPorAno(anyInt())).thenReturn(responseDTO);
        mockMvc.perform(get("/compras/maior-compra/{ano}", ano)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valorTotal").value(213.00)); // Verifique o valor esperado
    }

    @Test
    public void testMaiorCompraPorAno_ErroAnoInvalido() throws Exception {
        mockMvc.perform(get("/compras/maior-compra/{ano}", 1800) // Ano inválido
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testClientesFieis_Sucesso() throws Exception {
        when(comprasService.listarCompras()).thenReturn(getComprasResponseDTOS());
        when(comprasService.clientesFieis()).thenReturn(getClienteCompras());

        mockMvc.perform(get("/compras/clientes-fieis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].nome").value("Hadassa Daniela Sales"))
                .andExpect(jsonPath("$[1].nome").value("Fabiana Melissa Nunes"))
                .andExpect(jsonPath("$[2].nome").value("Teresinha Daniela Galvão"));
    }

    @Test
    public void testRecomendacaoVinhoPorCliente_Sucesso() throws Exception {
        String cpf = "12345678901"; // CPF válido
        String recomendacao = "Vinho Tinto"; // Exemplo de recomendação
        when(comprasService.recomendacaoVinhoPorCliente(cpf)).thenReturn(recomendacao);
        mockMvc.perform(get("/compras/recomendacao/cliente/{cpf}/tipo", cpf)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(recomendacao));
    }

    @Test
    public void testRecomendacaoVinhoPorCliente_CpfInvalido() throws Exception {
        String cpfInvalido = "123"; // CPF inválido

        mockMvc.perform(get("/compras/recomendacao/cliente/{cpf}/tipo", cpfInvalido)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRecomendacaoVinhoPorCliente_ErroInterno() throws Exception {
        when(comprasService.recomendacaoVinhoPorCliente("12345678901"))
                .thenThrow(new InternalServerErrorException("Erro interno"));

        mockMvc.perform(get("/compras/recomendacao/cliente/{cpf}/tipo", "12345678901")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    private static List<ComprasResponseDTO> getComprasResponseDTOS() {
        return Arrays.asList(
                new ComprasResponseDTO("Hadassa Daniela Sales", "1051252612", new Produto("12", "Branco", new BigDecimal("106.50"), 2018, 2019), 2, new BigDecimal("213.00")),
                new ComprasResponseDTO("Fabiana Melissa Nunes", "824643755772", new Produto("18", "Rosé", new BigDecimal("120.99"), 2018, 2019), 2, new BigDecimal("241.98")),
                new ComprasResponseDTO("Teresinha Daniela Galvão", "04372012950", new Produto("4", "Espumante", new BigDecimal("134.25"), 2020, 2021), 2, new BigDecimal("268.50")),
                new ComprasResponseDTO("Andreia Emanuelly da Mata", "27737287426", new Produto("3", "Rosé", new BigDecimal("121.75"), 2019, 2020), 2, new BigDecimal("243.5"))
        );
    }

    private static List<ClienteFielResponseDTO> getClienteCompras() {
        return Arrays.asList(
                new ClienteFielResponseDTO("Hadassa Daniela Sales", "1051252612", 2, new BigDecimal("213.00")),
                new ClienteFielResponseDTO("Fabiana Melissa Nunes", "824643755772", 2, new BigDecimal("241.98")),
                new ClienteFielResponseDTO("Teresinha Daniela Galvão", "04372012950", 2, new BigDecimal("268.50"))
        );
    }

}
