package br.com.digio.desafio.backend.service;

import br.com.digio.desafio.backend.dto.ClienteFielResponseDTO;
import br.com.digio.desafio.backend.dto.ComprasResponseDTO;
import br.com.digio.desafio.backend.exception.ComprasNotFoundException;
import br.com.digio.desafio.backend.model.Clientes;
import br.com.digio.desafio.backend.model.Produto;
import br.com.digio.desafio.backend.repository.ClientesRepository;
import br.com.digio.desafio.backend.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComprasService {

    @Autowired
    private final ClientesRepository clientesRepository;
    @Autowired
    private final ProdutoRepository produtoRepository;

    public List<ComprasResponseDTO> listarCompras() {
        log.info("Inicio da listagem de compras.");
        List<ComprasResponseDTO> comprasResponseDTOS = new ArrayList<>();

        clientesRepository.findAll().forEach(clientes -> {
            clientes.compras().forEach(compra -> {
                Produto produto = produtoRepository.findByCodigo(compra.codigoProduto())
                        .orElseThrow(() -> new ComprasNotFoundException("Produto não encontrado"));

                comprasResponseDTOS.add(ComprasResponseDTO.builder()
                        .nomeCliente(clientes.nome())
                        .cpfCliente(clientes.cpf())
                        .produto(produto)
                        .quantidade(compra.quantidade())
                        .valorTotal(produto.preco().multiply(BigDecimal.valueOf(compra.quantidade())))
                        .build());
            });
        });

        log.info("Criou com sucesso o response da listagem.");

        return comprasResponseDTOS.stream()
                .sorted(Comparator.comparing(ComprasResponseDTO::valorTotal))
                .collect(Collectors.toList());

    }

    public ComprasResponseDTO maiorCompraPorAno(final Integer ano) {
        log.info("Iniciando o carregamento da maior compra do ano: {}", ano);
        return listarCompras().stream()
                .filter(p -> p.produto().anoCompra().equals(ano))
                .max(Comparator.comparing(ComprasResponseDTO::valorTotal))
                .orElseThrow(() -> new ComprasNotFoundException("Nenhuma compra encontrada para o ano " + ano));
    }

    public List<ClienteFielResponseDTO> clientesFieis() {
        log.info("Iniciando o carregamento dos clientes que mais compram na loja.");
        Map<String, ClienteFielResponseDTO> clienteMap = new HashMap<>();

        clientesRepository.findAll().forEach(clientes -> {
            double totalValue = clientes.compras().stream()
                    .mapToDouble(compra -> {
                        Produto produto = produtoRepository.findByCodigo(compra.codigoProduto())
                                .orElseThrow(() -> new ComprasNotFoundException("Produto não encontrado"));
                        return produto.preco().doubleValue() * compra.quantidade();
                    })
                    .sum();

            clienteMap.put(clientes.cpf(), ClienteFielResponseDTO.builder()
                    .nome(clientes.nome())
                    .cpf(clientes.cpf())
                    .numeroCompras(clientes.compras().size())
                    .valorTotal(new BigDecimal(totalValue))
                    .build());
        });

        log.info("Sucesso ao criar a lista dos clientes que mais compram na loja: {}", clienteMap);

        return clienteMap.values().stream()
                .sorted(Comparator.comparing(ClienteFielResponseDTO::valorTotal).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public String recomendacaoVinhoPorCliente(final String cpf) {
        log.info("Iniciando a recomendação de vinho do cliente por CPF: {}", cpf);
        Clientes clientes = clientesRepository.findAll().stream()
                .filter(c -> c.cpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ComprasNotFoundException("Cliente não encontrado."));

        Map<String, Long> wineTypeCount = clientes.compras().stream()
                .map(purchase -> {
                    Produto product = produtoRepository.findByCodigo(purchase.codigoProduto())
                            .orElseThrow(() -> new ComprasNotFoundException("Nenhuma compra encontrada."));
                    return Map.entry(product.tipoVinho(), (long) purchase.quantidade());
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingLong(Map.Entry::getValue)
                ));

        log.info("Tipo do vinho mais comprado e quantidade: {}", wineTypeCount);

        return "Vinho recomendado: ".concat(wineTypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new ComprasNotFoundException("Não foi possível gerar recomendação.")));

    }

}
