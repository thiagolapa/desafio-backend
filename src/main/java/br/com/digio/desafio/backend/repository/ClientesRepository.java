package br.com.digio.desafio.backend.repository;

import br.com.digio.desafio.backend.exception.UnprocessableEntityException;
import br.com.digio.desafio.backend.model.Clientes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ClientesRepository {
    private List<Clientes> clientes;

    public ClientesRepository(RestClient.Builder builder) {
        log.info("Carregando dados da listagem de compras.");
        this.clientes = new ArrayList<>();

        try {
            RestClient restClient = builder
                    .baseUrl("https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json")
                    .build();

            var response = restClient.get().retrieve().toEntity(new ParameterizedTypeReference<List<Clientes>>() {});

            if (response.getStatusCode().isError()) {
                log.info("Erro ao carregar dados da listagem de compra.");
                throw new UnprocessableEntityException("Erro ao carregar a lista de compras dos clientes!");
            }

            this.clientes = response.getBody();

        } catch (Exception e) {
            log.info("Erro interno.");
            throw new UnprocessableEntityException("Erro ao inicializar o mock dos compras de clientes.");
        }
    }

    public List<Clientes> findAll() {
        log.info("Sucesso ao carregar a listagem de compras.");
        return clientes;
    }
}
