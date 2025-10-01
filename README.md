Objetivo
Construir um CRUD de Clientes com Front-end em Angular 17 e Back-end em Java 17 (Spring Boot). O foco é avaliar organização, boas práticas e clareza no desenvolvimento.
Escopo funcional
Entidade Cliente:
    • name (obrigatório)

    • email (obrigatório, único)

    • phone (opcional)

    • birthDate (opcional)

    • status (obrigatório)

    • balance (obrigatório, ≥ 0)

    • currency (ex: “BRL”, obrigatório)

    • createdAt / updatedAt (preenchidos no servidor)
Histórias de usuário
    1. Listar clientes com paginação e filtro por name, email

    2. Criar/Editar cliente com validação de formulário.

    3. Excluir cliente com confirmação.

    4. Visualizar detalhes de um cliente.
Tabela com paginação, ordenação
Formulário de criação/edição
Detalhes do cliente em página dedicada ou diálogo.
Confirmação de exclusão.