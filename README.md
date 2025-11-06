## Introdução

O **DsLearn** é um projeto de engenharia de software (MC656) que visa criar uma plataforma educacional completa focada no ensino e aprendizado de Estruturas de Dados e Algoritmos.

A solução é desenvolvida como uma aplicação *full-stack* que combina um backend robusto com um aplicativo móvel. O objetivo é fornecer aos usuários uma ferramenta acessível e interativa para estudar conceitos teóricos e, potencialmente, praticar exercícios.

## Arquitetura da Aplicação

A arquitetura do projeto DsLearn segue uma abordagem híbrida, combinando estilos arquiteturais de alto nív (para o sistema) e padrões de design específicos (para os componentes).

### 1. Estilos Arquiteturais Adotados

* **Cliente-Servidor (Client-Server):** Esse é o estilo principal que define a macro-arquitetura do sistema. As responsabilidades são claramente separadas entre dois executáveis distintos:
    * **Servidor (`backend/dslearn`):** Uma API RESTful, desenvolvida com Java e Spring Boot, responsável por centralizar toda a lógica de negócio e o acesso aos dados.
    * **Cliente (`app/DSLearn`):** Um aplicativo nativo para Android, desenvolvido em Kotlin, que atua como consumidor dos serviçs e dados fornecidos pelo backend.
* **Arquitetura em Camadas (Backend):** O servidor Spring Boot é estruturado internamente usando uma **Arquitetura em Camadas (Layered Architecture)**. Esse padrão promove uma forte separação de responsabilidades, dividindo o código em camadas horizontais com responsabilidades bem definidas (Apresentação, Negócio e Dados).
* **Arquitetura em camadas e MVVM (Frontend)** No **Frontend**, o app Android utiliza o padrão **MVVM (Model-View-ViewModel)** com **Clean Architecture**, separando os componentes em três camadas principais: _data_, _domain_ e _presentation_ (ViewModel).

### 2. Diagramas C4 (Contexto, Container e Componentes)

Abaixo estão os diagramas C4 representando visualmente a arquitetura da aplicação.

1.  Diagrama de Contexto

    ![Diagrama de Contexto](/diagrams/contexto.png)

2.  Diagrama de Container

    ![Diagrama de Container](/diagrams/container.png)

3.  Diagrama de Componentes

    ![Diagrama de Componentes](/diagrams/componentes.png)

### 3. Principais Componentes e Responsabilidades

Abaixo está uma descrição sucinta dos principais componentes que formam a arquitetura da aplicação.

#### 1. Frontend (Cliente Android)
Organizado internamente usando **Clean Architecture** dividindo os componentes em três camadas principais:
* **presentation:**
   * **Responsabilidade:** Componentes visuais (Activities, Composable functions, ViewModels) responsáveis por renderizar o estado da aplicação e capturar as interações do usuário.
      * ViewModel: Gerenciar e armazenar o estado da UI de forma reativa. Ele expõe os dados para a View e processa as ações do usuário, chamando o UseCases.
* **domain:**
   * **Responsabilidade:** Encapsula a lógica de negócios complexa ou mais simples que é reutilizada por vários ViewModels em UseCases. Abstrai a camada data, tornando o código extremamente reutilizável.
* **data:**
   * **Responsabilidade:** Abstrai fontes de dados do restante do App, decidindo se busca dados da rede ou de um cache local. Lida com a comunicação HTTP real com o backend (API REST) usando bibliotecas como Retrofit e OkHttp.

A camada **_presentation_** utiliza a arquitetura **MVVM** promovendo a separação de responsabilidades entre seus componentes, facilitando o gerenciamento e a escalabilidade do App seguindo as separações:
* Modelo: Representa os dados (localizado em `:core:model`).
* View: Representa a interface do usuário (Activities, Composable functions) e observa as alterações feitas pelo ViewModel (localizado em `:presentation`).
* ViewModel: Atua como uma ponte entre a View e o Model, armazenando dados relacionados à interface do usuário. Implementa os UseCases da camada de domain (localizado em `:presentation`).

#### 2. Backend (Servidor Spring Boot)

Organizado internamente usando uma **Arquitetura em Camadas**, este componente é a **Camada de Lógica de Negócio** (Application Tier) do sistema.

* **Controllers (Camada de Apresentação/API):**
    * **Responsabilidade:** Expor os *endpoints* da API REST. Recebe as requisições HTTP do cliente Android e delega a execução para a camada de serviço.
* **Services (Camada de Negócio):**
    * **Responsabilidade:** Orquestrar e executar a lógica de negócio central da aplicação (requisitos funcionais), de forma independente da API ou do banco de dados.
* **Repositories (Camada de Acesso a Dados):**
    * **Responsabilidade:** Abstrair a comunicação com a **Camada de Dados** (o banco de dados). Define as interfaces para operações de persistência (CRUD).
* **Entities & DTOs (Camada de Modelo):**
    * **Responsabilidade:** As `Entities` mapeiam os objetos para as tabelas do banco de dados. Os `DTOs` (Data Transfer Objects) definem o formato JSON que é enviado e recebido pela API.

#### 3. Banco de Dados (PostgreSQL)

Este componente é a **Camada de Dados (Data Tier)** do sistema.

* **Responsabilidade:**
    * **Persistência:** Armazenar e gerenciar de forma segura e eficiente todos os dados da aplicação (informações de usuários, conteúdos dos cursos, progresso, etc.).
    * **Integridade:** Garantir a integridade e a consistência dos dados através de esquemas, chaves e transações.

    * **Disponibilidade:** Servir os dados para a Camada de Lógica de Negócio (Backend) quando solicitado através das consultas (gerenciadas pelos `Repositories`).
