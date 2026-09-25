## Descrição do Projeto
Aplicação de gerência de assinaturas, desenvolvido com Java Spring e React para a disciplina de Processos de Software (DIM0510), ministrada pelo professor Dr. Fernando Marques Figueira, no curso de Bacharelado em Engenharia de Software da Universidade Federal do Rio Grande do Norte (UFRN).

## Como rodar a aplicação

### 1) Configure as variáveis de ambiente
Crie uma cópia do arquivo `.env.example` e renomeie para `.env`:

```bash
cp .env.example .env
```

Depois, gere uma chave JWT forte e substitua o valor de `JWT_SECRET` no arquivo `.env`.

Exemplo de geração no Linux/macOS:

```bash
openssl rand -base64 64
```

Ou em Python:

```bash
python -c "import secrets; print(secrets.token_urlsafe(64))"
```

### 2) Suba o banco de dados e a aplicação
No diretório do projeto:

```bash
docker compose up -d --build
```

### 3) Acesse a API
A aplicação Spring Boot fica disponível em:

```text
http://localhost:8080
```

O PostgreSQL fica em:

```text
localhost:5432
```

## Equipe

**Nome do Grupo:** AJ Spring


| Nome | Matrícula | Usuário do GitHub
|---|---|---|
| André Lucas Gonçalves Gomes | 20240019775 | [AndreGGomes](https://github.com/AndreGGomes)
| Jorge William Câmara Sales | 20210050012 | [Jorgelino328](https://github.com/Jorgelino328)

## Informações Adicionais

* **Coorte de apresentação:** B 
* **Integração com outras matérias:** N/A
* **Link do quadro no GitHub Projects:** [Subminder Kanban](https://github.com/users/AndreGGomes/projects/1)
* **Link do vídeo de apresentação:** https://youtu.be/r4zvOKh_s_g

## Licença

Material licenciado sob CC BY-NC-SA 4.0.
