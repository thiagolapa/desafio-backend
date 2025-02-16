package br.com.digio.desafio.backend.service;

import br.com.digio.desafio.backend.dto.ClienteFielResponseDTO;
import br.com.digio.desafio.backend.dto.ComprasResponseDTO;
import br.com.digio.desafio.backend.exception.ComprasNotFoundException;
import br.com.digio.desafio.backend.model.Clientes;
import br.com.digio.desafio.backend.model.Compra;
import br.com.digio.desafio.backend.model.Produto;
import br.com.digio.desafio.backend.repository.ClientesRepository;
import br.com.digio.desafio.backend.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComprasServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ClientesRepository clientesRepository;

    @InjectMocks
    private ComprasService comprasService;

    @Test
    void listaTodasCompras_DeveRetornarComprasOrdemCrescentePorValor() {

        when(clientesRepository.findAll()).thenReturn(getClienteCompras());
        when(produtoRepository.findByCodigo(anyString())).thenAnswer(i ->
                getProdutos().stream()
                        .filter(p -> p.codigo().equals(i.getArgument(0)))
                        .findFirst());

        List<ComprasResponseDTO> result = comprasService.listarCompras();

        assertEquals(4, result.size());
        assertEquals("1051252612", result.get(0).cpfCliente());
        assertTrue(result.get(0).quantidade() <= result.get(1).quantidade());
    }


    @Test
    public void testMaiorCompraPorAno_Sucesso() {

        when(clientesRepository.findAll()).thenReturn(getClienteCompras());
        when(produtoRepository.findByCodigo(anyString())).thenAnswer(i ->
                getProdutos().stream()
                        .filter(p -> p.codigo().equals(i.getArgument(0)))
                        .findFirst());

        List<ComprasResponseDTO> result = comprasService.listarCompras();

        ComprasResponseDTO resultado = comprasService.maiorCompraPorAno(2019);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("241.98"), resultado.valorTotal());
    }


    @Test
    public void testMaiorCompraPorAno_NaoEncontrado() {

        when(clientesRepository.findAll()).thenReturn(getClienteCompras());
        when(produtoRepository.findByCodigo(anyString())).thenAnswer(i ->
                getProdutos().stream()
                        .filter(p -> p.codigo().equals(i.getArgument(0)))
                        .findFirst());

        ComprasNotFoundException exception = assertThrows(ComprasNotFoundException.class, () -> {
            comprasService.maiorCompraPorAno(2022);
        });

        assertEquals("Nenhuma compra encontrada para o ano 2022", exception.getMessage());
    }

    @Test
    public void testClientesFieis() {
        when(clientesRepository.findAll()).thenReturn(getClienteCompras());
        when(produtoRepository.findByCodigo(anyString())).thenAnswer(i ->
                getProdutos().stream()
                        .filter(p -> p.codigo().equals(i.getArgument(0)))
                        .findFirst());

        List<ClienteFielResponseDTO> result = comprasService.clientesFieis();

        assertEquals(3, result.size());
        assertEquals("Teresinha Daniela Galvão", result.get(0).nome());
        assertEquals("Andreia Emanuelly da Mata", result.get(1).nome());

        verify(clientesRepository).findAll();
    }

    @Test
    public void testRecomendacaoVinhoPorCliente() {
        when(clientesRepository.findAll()).thenReturn(getClienteCompras());
        when(produtoRepository.findByCodigo(anyString())).thenAnswer(i ->
                getProdutos().stream()
                        .filter(p -> p.codigo().equals(i.getArgument(0)))
                        .findFirst());

        String resultado = comprasService.recomendacaoVinhoPorCliente("1051252612");

        assertEquals("Branco", resultado);
    }


    @Test
    public void testRecomendacaoVinhoPorClienteNaoEncontrado() {
        when(clientesRepository.findAll()).thenReturn(List.of());

        ComprasNotFoundException exception = assertThrows(
                ComprasNotFoundException.class,
                () -> comprasService.recomendacaoVinhoPorCliente("12345678900")
        );

        assertEquals("Cliente não encontrado.", exception.getMessage());
    }

    @Test
    public void testRecomendacaoVinhoPorCliente_RecomendacaoNaoEncontrada() {

        when(clientesRepository.findAll()).thenReturn(getClienteCompras());
        when(produtoRepository.findByCodigo(any())).thenReturn(Optional.empty());

        ComprasNotFoundException exception = assertThrows(
                ComprasNotFoundException.class,
                () -> comprasService.recomendacaoVinhoPorCliente("1051252612")
        );
        assertEquals("Nenhuma compra encontrada.", exception.getMessage());
    }

    private static List<ComprasResponseDTO> getComprasResponseDTOS() {
        return Arrays.asList(
                new ComprasResponseDTO("Hadassa Daniela Sales", "1051252612", new Produto("12", "Branco", new BigDecimal("106.50"), 2018, 2019), 2, new BigDecimal("213.00")),
                new ComprasResponseDTO("Fabiana Melissa Nunes", "824643755772", new Produto("18", "Rosé", new BigDecimal("120.99"), 2018, 2019), 2, new BigDecimal("241.98")),
                new ComprasResponseDTO("Teresinha Daniela Galvão", "04372012950", new Produto("4", "Espumante", new BigDecimal("134.25"), 2020, 2021), 2, new BigDecimal("268.50")),
                new ComprasResponseDTO("Andreia Emanuelly da Mata", "27737287426", new Produto("3", "Rosé", new BigDecimal("121.75"), 2019, 2020), 2, new BigDecimal("243.5"))
        );
    }

    private static List<Clientes> getClienteCompras() {
        return Arrays.asList(
                new Clientes("Hadassa Daniela Sales", "1051252612",List.of(new Compra("12",2))),
                new Clientes("Fabiana Melissa Nunes", "824643755772",List.of(new Compra("18",2))),
                new Clientes("Teresinha Daniela Galvão", "04372012950",List.of(new Compra("4",2))),
                new Clientes("Andreia Emanuelly da Mata", "27737287426",List.of(new Compra("3",2)))
        );
    }

    private static List<Produto> getProdutos() {
        return Arrays.asList(
                new Produto("12", "Branco", new BigDecimal("106.50"), 2018, 2019),
                new Produto("18", "Rosé", new BigDecimal("120.99"), 2018, 2019),
                new Produto("4", "Espumante", new BigDecimal("134.25"), 2020, 2021),
                new Produto("3", "Rosé", new BigDecimal("121.75"), 2019, 2020)
        );
    }
}
