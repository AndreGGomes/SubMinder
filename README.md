# Proposta de Projeto: SubMinder

## 1. Visão do Produto

```text
Para jovens adultos e estudantes
Que perdem o controle de múltiplas assinaturas de software e streaming
O SubMinder é um aplicativo de gestão financeira focado em recorrências
Que centraliza os gastos e calcula o custo fixo mensal
Diferente de planilhas manuais ou apps genéricos de orçamento
Nosso produto foca exclusivamente em dar visibilidade aos serviços por assinatura para evitar cobranças indesejadas
```

## 2. Definição do MVP

**O que entra e o que fica fora do MVP:**

| No MVP | Fora do MVP |
|---|---|
| Cadastro de usuário simples | Integração direta com bancos ou cartões via API |
| Adicionar, editar e remover assinaturas (nome, valor, ciclo de cobrança) | Conversão de múltiplas moedas |
| Dashboard com o cálculo do gasto total mensal e anual | Notificações push ou integração com WhatsApp |
| Lista de assinaturas ordenada pela próxima data de cobrança | Histórico de gastos de meses anteriores |

**Hipótese de valor:** 
*Acreditamos que jovens adultos e estudantes vão registrar e centralizar suas assinaturas no aplicativo porque isso lhes dará clareza imediata sobre seu custo fixo mensal, evitando surpresas no cartão de crédito.*

## 3. Backlog e Quadro Kanban

**Link para o quadro:** [Link para o GitHub Projects]

**Backlog Inicial Priorizado:**

| Prio | História | Critérios de aceitação | Sprint |
|---|---|---|---|
| P1 | Como usuário, quero me cadastrar e fazer login para manter meus dados salvos de forma privada. | O sistema deve exigir email e senha; persistência no banco de dados. | 1 |
| P1 | Como usuário, quero adicionar uma nova assinatura informando nome, valor e ciclo para acompanhar esse gasto. | Deve permitir valores positivos e seleção de ciclo (mensal/anual). | 1 |
| P1 | Como usuário, quero visualizar um dashboard com o gasto total (mensal e anual) para saber quanto da minha renda está comprometida. | O cálculo deve somar todas as assinaturas ativas e converter ciclos anuais para impacto mensal (e vice-versa). | 2 |
| P2 | Como usuário, quero ver uma lista das minhas assinaturas ordenada pela próxima data de cobrança para me preparar para os próximos pagamentos. | A lista deve exibir primeiro os pagamentos mais próximos do dia atual. | 2 |
| P3 | Como usuário, quero poder editar ou remover uma assinatura para manter meus registros atualizados quando eu cancelar um serviço. | O total do dashboard deve ser recalculado automaticamente após a exclusão/edição. | 3 |

*(P1 é essencial ao MVP, P2 é importante, P3 é desejável).*

## 4. Stack Tecnológico e Justificativa

* **Back-end:** TODO (Tenho preferencia por Java Spring)
* **Front-end:** TODO (Tenho preferencia por React, mas acho que não vamos ter o frontend no projeto né, já que vai ser um mvp)
* **Banco de Dados:** PostgreSQL. Confiável, de código aberto e possui excelente integração com a stack do back-end.

## 5. Acordo de Processo

* **Cadência:** Sprints de 2 semanas (planejamento às segundas-feiras, fechamento às sextas-feiras da semana seguinte).
* **Cerimônias:** 
  * *Planning:* 1 hora no primeiro dia da sprint.
  * *Daily:* Assíncrona no [Discord/WhatsApp] até as 12h, ou reuniões rápidas de 15 minutos em dias de pareamento.
  * *Review e Retrospectiva:* 1 hora no último dia da sprint para avaliar o incremento e melhorar o processo.
* **Definição de Pronto (DoD):** Um item sai de "Em revisão" para "Pronto" quando:
  * O código foi revisado por pelo menos um colega da equipe (Pull Request aprovado).
  * O código passou nos testes automatizados (CI).
  * A funcionalidade atende a todos os critérios de aceitação estipulados na história.
* **Papéis:** A equipe é estritamente horizontal, sem hierarquia fixa entre os integrantes. Ambos os membros atuam como desenvolvedores full-stack co-responsáveis por todas as etapas (desenvolvimento, testes e revisão de código). Haverá um rodízio a cada Sprint para a liderança e facilitação das cerimônias (Scrum Master), garantindo que ambos exercitem a gestão do processo de forma idêntica.
* **Ferramentas:** 
  * Gestão e Kanban: GitHub Projects.
  * Versionamento e Code Review: GitHub.
  * Comunicação diária: [Discord ou WhatsApp].
* **WIP Limits (Work in Progress):** Limite máximo de **2** itens na coluna "Em progresso" por pessoa simultaneamente para garantir o foco em terminar entregas antes de começar novas.

## 6. Equipe

| Nome | Matrícula | Papel |
|---|---|---|
| André Lucas Gonçalves Gomes | 20240019775 | Desenvolvedor |
| Jorge William Câmara Sales | 20210050012 | Desenvolvedor |

## 7. Informações Adicionais

* **Coorte de apresentação:** TODO
* **Link do quadro no GitHub Projects:** [https://github.com/users/AndreGGomes/projects/1](https://github.com/users/AndreGGomes/projects/1)
