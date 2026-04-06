# Basket Service (Carrinho de Compras)

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

Microserviço responsável pelo gerenciamento de carrinhos de compras de um ecossistema de E-commerce. Ele permite a criação, atualização, consulta e finalização (pagamento) de cestas de produtos, garantindo alta performance com cache em memória e integração robusta com APIs externas de catálogo.

## Tecnologias Utilizadas

O projeto foi desenvolvido utilizando as seguintes tecnologias e padrões:

* **Java 17**
* **Spring Boot 4.0.5**
* **Spring Cloud OpenFeign:** Para comunicação HTTP declarativa com o microsserviço de catálogo externo.
* **MongoDB (Spring Data MongoDB):** Banco de dados NoSQL utilizado para persistir os carrinhos de compras de forma flexível.
* **Redis (Spring Data Redis):** Utilizado para gerenciamento de tempo de vida do carrinho (TTL configurado para 60 segundos) e alta performance.
* **Lombok:** Para redução de boilerplate (Getters, Setters, Builders).
* **Spring WebMVC:** Criação da API RESTful.

## Decisões Arquiteturais e Destaques

Durante o desenvolvimento, aplicamos conceitos avançados de arquitetura e Clean Code:

* **Integração com API Externa (Platzi):** utilizei o `@FeignClient` para consumir a API pública da Platzi (`https://api.escuelajs.co/api/v1`). O Service valida se os IDs dos produtos inseridos no carrinho realmente existem no catálogo antes de prosseguir.
* **Global Exception Handling:** A API utiliza `@RestControllerAdvice` e exceções customizadas (como `DataNotFoundException`). Isso mantém os Controllers limpos (focados apenas no *Happy Path*) e garante que erros (como buscar um carrinho que não existe) retornem respostas HTTP consistentes (ex: `404 Not Found` com mensagens claras), em vez de falhas internas `500`.
* **Cálculo com BigDecimal e Streams:** O valor total do carrinho (`totalPrice`) é calculado de forma dinâmica, segura e imutável utilizando a API de Streams do Java 8+ e operações atômicas da classe `BigDecimal`.
* **Serialização de Records:** Respostas de API modeladas utilizando `record` do Java 14+, implementando a interface `Serializable` para garantir a conversão perfeita de bytes exigida pelo motor de cache do Redis.
* **Fat Service, Thin Controller:** Todas as regras de negócio e validações (se o usuário já possui carrinho aberto, cálculo de total, verificação de estoque) residem exclusivamente na camada de `@Service`.

## Pré-requisitos para rodar localmente

Certifique-se de ter as seguintes ferramentas instaladas e rodando na sua máquina:
* **Java 17** ou superior.
* **Maven**
* **MongoDB:** Rodando na porta padrão `27017`.
* **Redis:** Rodando na porta padrão `6379` (com a senha configurada como `sa` no `application.yml`).
  *(Dica para usuários de macOS com Apple Silicon: utilize o Homebrew com a flag `arch -arm64` ou imagens Docker nativas arm64 para melhor performance do Redis).*

## Como executar a aplicação

1. Clone o repositório.
2. Certifique-se de que os serviços do MongoDB e Redis estejam iniciados na sua máquina (ou via Docker).
3. Na raiz do projeto, execute o comando Maven para baixar as dependências e compilar o projeto:
```bash
mvn clean compile
```
4. Inicie a aplicação Spring Boot:
```bash
mvn spring-boot:run
```
5. A aplicação estará disponível em `http://localhost:8080`.

## Endpoints da API

Abaixo estão as principais rotas disponíveis no `BasketController`.

### 1. Criar um novo carrinho (POST `/basket`)
**Corpo da Requisição (JSON):**
```json
{
    "clientId": 10,
    "products": [
        {
            "id": 1,
            "quantity": 2
        },
        {
            "id": 2,
            "quantity": 1
        }
    ]
}
```
*(Certifique-se de enviar IDs que existam na API da Platzi no momento do teste).*

### 2. Buscar Carrinho por ID (GET `/basket/{id}`)
Retorna o JSON completo do carrinho, com status `200 OK` caso encontrado, ou `404 Not Found` caso o ID não exista.

### 3. Atualizar Carrinho (PUT `/basket/{id}`)
Substitui os itens do carrinho atual por uma nova lista, recalculando o valor total.

### 4. Pagar/Finalizar Carrinho (PUT `/basket/{id}/payment`)
**Corpo da Requisição (JSON):**
```json
{
    "paymentMethod": "CREDIT_CARD"
}
```
Altera o status do carrinho para `SOLD` (Vendido).

### 5. Deletar Carrinho (DELETE `/basket/{id}`)
Exclui o carrinho do banco de dados (Retorna `204 No Content`).

---
Desenvolvido com ☕ e 💻 como parte dos meus estudos em Spring Boot.