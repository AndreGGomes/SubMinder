# Retrospectiva da Sprint 1

## Métricas de Fluxo

- **Throughput (Vazão):** 4 cartões concluídos na sprint (aprox. 1.3 cartões por semana, considerando 3 semanas úteis).
  - **Cycle Time Médio:** 1 dia. O tempo de execução foi baixo porque o trabalho foi feito em "janelas curtas" de esforço concentrado.
  - **Lead Time Médio:** 12 dias. Alto em relação ao Cycle Time, pois os cartões passaram muito tempo criados na coluna "Ready" ou "Backlog" à espera de disponibilidade de tempo da dupla para serem puxados.
  - **WIP (Trabalho em Andamento):** O limite configurado foi respeitado (máximo de 3 em "In progress" e 5 em "In review")[cite: 1].
  - **Cartão mais demorado:** O cartão *"SubMinder #10 - Sistema de Login/Cadastro com persistência de dados"* foi o que teve o maior Cycle Time. Ele ficou parado na coluna de revisão ("In review") durante alguns dias à espera que ambos os integrantes tivessem disponibilidade simultânea para a call no Discord onde a revisão do PR foi feita.

## 1. Fatos
- **Fato 1: Entregas não contínuas.** Embora todas as features propostas para a sprint tenham sido entregues no prazo estabelecido, o ritmo de envios (commits e PRs) não ocorreu de maneira gradual ao longo das semanas da sprint.
- **Fato 2: Falta de clareza nos comentários de Pull Requests.** Todos os PRs abertos por um integrante foram obrigatoriamente revisados pelo outro, satisfazendo a prática de Integração/Revisão de Código. No entanto, os comentários deixados no GitHub foram curtos e pouco esclarecedores para quem analisa o repositório externamente. 

## 2. Causas (5 Porquês)
- **Sobre o fluxo não contínuo de entregas:**
  1. *Por que as entregas não foram graduais?* Porque o trabalho de desenvolvimento acabou se concentrando em janelas curtas e pontuais de tempo.
  2. *Por que o trabalho se concentrou em janelas curtas de tempo?* Devido à concorrência com outros compromissos acadêmicos e pessoais da dupla durante a sprint.
  3. *Por que esses compromissos impactaram o fluxo da sprint de forma tão severa?* Porque a sobrecarga de outras disciplinas tomou o tempo dos dois integrantes de maneira muito abrupta e intensa.
  4. *Por que fomos pegos de surpresa por essa sobrecarga?* Porque, embora tivéssemos mapeado previamente que seriam semanas de pico e de provas, nós subestimamos a exaustão e o volume de esforço que essas outras avaliações exigiriam de nós.
  5. *Por que essa exaustão travou nosso fluxo contínuo?* Porque, mesmo as tarefas do projeto já sendo relativamente pequenas, simplesmente nos faltou o tempo e a energia mental mínima para fazer a mudança de contexto e focar no desenvolvimento, empurrando a execução para os raros momentos de alívio na reta final.

- **Sobre os comentários superficiais nos PRs:**
  1. *Por que os comentários no GitHub não foram claros?* Porque os detalhes, debates arquiteturais e aprovações de código foram resolvidos verbalmente durante reuniões no Discord.
  2. *Por que o debate ficou centralizado apenas no Discord?* Porque pareceu mais rápido e dinâmico alinhar as mudanças por voz do que digitar textos longos e formais no GitHub.
  3. *Por que optamos exclusivamente pela rapidez da voz, ignorando os comentários no GitHub?* Porque, devido ao prazo e à nossa janela curta de tempo, priorizamos a velocidade da integração do código sobre a documentação do processo.
  4. *Por que a prioridade na velocidade justificou a total falta de registro?* Porque, como a equipe tem apenas dois integrantes e a comunicação é muito próxima, o contexto técnico já estava totalmente claro para ambos, fazendo com que documentar as decisões no PR parecesse um esforço redundante.
  5. *Por que essa percepção de "esforço redundante" é um erro no nosso processo?* Porque ignoramos que o repositório precisa ser a fonte de verdade para qualquer pessoa externa (como os avaliadores), e não formalizamos a prática de escrever um breve resumo no GitHub assim que a discussão verbal terminasse.

## 3. Ações
| Ação | Responsável | Prazo |
| :--- | :--- | :--- |
| **1. Registrar resumo das decisões verbais nos PRs:** Sempre que um PR for discutido e aprovado em call (Discord), o revisor deve escrever um breve resumo da conclusão no comentário de aprovação do GitHub para gerar histórico documental. | André e Jorge | Ao longo da Sprint 2 |
| **2. Reserva de horários fixos de foco:** Garantir um alinhamento prévio no Discord para bloquear horários fixos e inegociáveis para pareamento e foco no projeto, mitigando a falta de energia gerada pela troca de contexto com outras matérias e garantindo entregas mais cedo independentemente do fluxo das outras disciplinas. | André e Jorge | Final da Sprint 2 |
| **3. Estabelecer meta mínima de entregas semanais da dupla:** Garantir o fechamento de pelo menos uma entrega funcional (PR integrado) por semana por parte da equipe. Essa entrega pode ser alternada (ex: um integrante assume o envio na primeira semana, e o outro na semana seguinte), forçando a continuidade do fluxo de trabalho sem sobrecarregar individualmente e evitando o acúmulo de tarefas para os dias finais da sprint. | André e Jorge | Ao longo da Sprint 2 |

## 4. Revisão do Acordo de Processo
- **Comunicação:** A comunicação rápida por Discord se provou eficaz para a nossa dupla e continuará sendo a principal via de daily meeting informal e pair programming, desde que comecemos a documentar os resultados dessa comunicação no repositório (conforme Ação 1).