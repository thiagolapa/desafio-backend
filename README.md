<h1 align="center">
  Loja de Vinhos Service
</h1>

<p align="center">
 Este é um microserviço desenvolvido em Java Spring Boot para gerenciamento de uma loja de vinhos.
</p>

<p align="center">
 <img src="https://img.shields.io/static/v1?label=GitHub&message=https://github.com/thiagolapa&color=8257E5&labelColor=000000" alt="@giulianabezerra" />
 <img src="https://img.shields.io/static/v1?label=Tipo&message=Desafio&color=8257E5&labelColor=000000" alt="Desafio" />
</p>

Criar um microserviço após consumir os dados de mocks, retornando as informações através dos segintes endpoints:

1. GET: /compras - Retornar uma lista das compras ordenadas de forma
   crescente por valor, deve conter o nome dos clientes, cpf dos clientes,
   dado dos produtos, quantidade das compras e valores totais de cada
   compra.
2. GET: /maior-compra/ano - (Exemplo: /maior_compra/2016) - Retornar a
   maior compra do ano informando os dados da compra disponibilizados,
   deve ter o nome do cliente, cpf do cliente, dado do produto, quantidade
   da compra e seu valor total.
3. GET: /clientes-fieis - Retornar o Top 3 clientes mais fieis, clientes que
   possuem mais compras recorrentes com maiores valores.
4. GET: /recomendacao/cliente/tipo - Retornar uma recomendação de vinho
   baseado nos tipos de vinho que o cliente mais compra.

Os mocks estão disponíveis através dos links:

Lista de Produtos: https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json

Lista de Clientes e Compras: https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json

## Requisitos

- Java 21
- Docker
- Gradle

## Estrutura do Projeto

- `domain/`: Classes de domínio
- `dto/`: Objetos de transferência de dados
- `repository/`: Camada de acesso aos dados
- `service/`: Lógica de negócios
- `controller/`: Endpoints da API
- `exception/`: Tratamento de exceções

## Princípios SOLID Aplicados

1. **Single Responsibility Principle**: Cada classe tem uma única responsabilidade
2. **Open/Closed Principle**: As classes são abertas para extensão mas fechadas para modificação
3. **Liskov Substitution Principle**: As classes derivadas podem substituir suas classes base
4. **Interface Segregation Principle**: As interfaces são específicas para cada cliente
5. **Dependency Inversion Principle**: Dependência em abstrações, não em implementações

## Como Executar

### Localmente
- Clonar repositório git
- Construir o projeto:
```
./gradlew clean build
```
- Executar:
```
java -jar build/libs/desafio-backend-0.0.1-SNAPSHOT.jar
```

A API poderá ser acessada em [localhost:8080](http://localhost:8080).
O Swagger poderá ser visualizado em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Usando Docker

- Clonar repositório git
- Construir o projeto:
```
./gradlew clean build
```
- Construir a imagem:
```
./gradlew bootBuildImage
```
- Executar o container:
```
docker run --name desafio-backend -p 8080:8080  -d desafio-backend:0.0.1-SNAPSHOT
```

A API poderá ser acessada em [localhost:8080](http://localhost:8080).
O Swagger poderá ser visualizado em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Endpoints

### 1. Lista de Compras
```
GET /compras
```
Retorna todas as compras ordenadas por valor crescente.
```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "nomeCliente": "Hadassa Daniela Sales",
    "cpfCliente": "1051252612",
    "produto": {
      "codigo": "12",
      "tipo_vinho": "Branco",
      "preco": 106.5,
      "safra": 2018,
      "ano_compra": 2019
    },
    "quantidade": 2,
    "valorTotal": 213
  },
  {
    "nomeCliente": "Fabiana Melissa Nunes",
    "cpfCliente": "824643755772",
    "produto": {
      "codigo": "18",
      "tipo_vinho": "Rosé",
      "preco": 120.99,
      "safra": 2018,
      "ano_compra": 2019
    },
    "quantidade": 2,
    "valorTotal": 241.98
  }
]
```

### 2. Maior Compra do Ano
```
GET /compras/maior-compra/{ano}
```
Retorna a maior compra do ano especificado.
```

HTTP/1.1 200 OK
Content-Type: application/json

{
  "nomeCliente": "Ian Joaquim Giovanni Santos",
  "cpfCliente": "96718391344",
  "produto": {
    "codigo": "2",
    "tipo_vinho": "Branco",
    "preco": 126.5,
    "safra": 2018,
    "ano_compra": 2019
  },
  "quantidade": 15,
  "valorTotal": 1897.5
}
```

### 3. Clientes Fiéis
```
GET /compras/clientes-fieis
```
Retorna o top 3 clientes mais fiéis.
```

HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "nome": "Ian Joaquim Giovanni Santos",
    "cpf": "96718391344",
    "numeroCompras": 5,
    "valorTotal": 7631.69
  },
  {
    "nome": "Geraldo Pedro Julio Nascimento",
    "cpf": "05870189179",
    "numeroCompras": 5,
    "valorTotal": 3416.87
  },
  {
    "nome": "Andreia Emanuelly da Mata",
    "cpf": "27737287426",
    "numeroCompras": 6,
    "valorTotal": 3210.19
  }
]
```

### 4. Recomendação de Vinho
```
GET /compras/recomendacao/cliente/{cpf}/tipo
```
Retorna uma recomendação de vinho baseada no histórico de compras do cliente.
```

HTTP/1.1 200 OK
Content-Type: application/json

Vinho recomendado: Tinto
```