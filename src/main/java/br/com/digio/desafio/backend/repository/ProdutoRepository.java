package br.com.digio.desafio.backend.repository;

import br.com.digio.desafio.backend.exception.UnprocessableEntityException;
import br.com.digio.desafio.backend.model.Produto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ProdutoRepository {
    private List<Produto> produtos;

    public ProdutoRepository(RestClient.Builder builder) {
        log.info("Iniciando carregamento da listagem de produtos da loja.");
        this.produtos = new ArrayList<>();
        try {
            RestClient restClient = builder
                    .baseUrl("https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json")
                    .build();

            var response = restClient.get().retrieve().toEntity(new ParameterizedTypeReference<List<Produto>>() {});

            if (response.getStatusCode().isError()) {
                log.info("Erro ao carregar dados da listagem de produtos.");
                throw new UnprocessableEntityException("Erro ao carregar a lista de produtos!");
            }

            this.produtos = response.getBody();
        } catch (Exception e) {
            log.info("Erro interno.");
            throw new UnprocessableEntityException("Erro ao carregar a lista de produtos!");
        }
    }

    public List<Produto> findAll() {
        log.info("Sucesso ao carregar a lista de produtos da loja.");
        return produtos;
    }

    public Optional<Produto> findByCodigo(String codigo) {
        log.info("Sucesso ao carregar a buscar de produto por código: {}", codigo);
        return produtos.stream()
                .filter(p -> p.codigo().equals(codigo))
                .findFirst();
    }
}
