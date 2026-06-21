# 📋 Resumo — Desafio Sea Tecnologia (QA)

> **Autor:** Mateus Cerqueira
> **Veredito final:** ❌ Não Recomendado para Homologação

---

## 🎯 Objetivo

Validar se a aplicação de **gerenciamento de funcionários e EPIs** está aderente ao protótipo fornecido, por meio de testes manuais e automatizados (Selenium WebDriver) nos navegadores **Vivaldi, Chrome e Edge**.

---

## 📊 Resultado Geral

| Métrica | Valor |
|---|---|
| Casos de teste executados | 19 |
| ✅ Aprovados | 6 (31,6%) |
| ❌ Reprovados | 13 (68,4%) |
| Bugs documentados | 18 |
| Bugs críticos | 2 |

**CTs Aprovados:** CT-06, CT-07, CT-08, CT-10, CT-11, CT-16
**CTs Reprovados:** CT-01, CT-02, CT-03, CT-04, CT-05, CT-09, CT-12, CT-13, CT-14, CT-15, CT-17, CT-18, CT-19

---

## 🐛 Bugs por Severidade

### 🔴 Críticos (bloqueiam homologação)
| # | Bug | Descrição |
|---|---|---|
| 17 | CPF duplicado | Sistema permite cadastrar funcionários com Nome/CPF repetidos, sem validação de unicidade |
| 18 | Upload de extensões proibidas | Aceita `.exe` e `.xlsx` no upload de atestado (deveria aceitar só `.png`, `.docx`, `.pdf`) — risco de malware |

### 🟠 Altos (quebram o fluxo principal)
| # | Bug | Descrição |
|---|---|---|
| 04 | Numeração/estado do menu superior incorretos | Todas as etapas aparecem como "1" e ativas, sem seguir a lógica progressiva do protótipo |
| 05 | Botão "Próximo passo" inoperante | Habilitado prematuramente; não avança o fluxo após concluir etapa |
| 07 | Falta de máscara/validação | CPF, RG e Data de Nascimento aceitam letras e datas impossíveis (ex: ano 3050) |
| 09 | Botão "Adicionar EPI" inoperante | Impede vincular mais de um EPI ao funcionário; quebra teste automatizado CT-09 |
| 13 | "Adicionar outra atividade" dá submit indevido | Cria o funcionário prematuramente em vez de só adicionar campo |
| 15 | Tecla Enter expulsa o usuário | Se houver campo obrigatório vazio, redireciona ao menu inicial e perde os dados preenchidos |

### 🟡 Médios
| # | Bug |
|---|---|
| 01 | Menu lateral não executa ações de navegação |
| 06 | Cores/emblemas de status (Ativo/Inativo) não diferenciados na listagem |
| 08 | Campo CA aceita letras e reseta a seleção de EPI |
| 12 | Falta de scroll na listagem — registros ficam ocultos |
| 14 | Emblemas de "Cargo 01"/"Atividade 01" não renderizam |

### 🟢 Baixos (cosméticos)
| # | Bug |
|---|---|
| 02 | Avatar do menu inicial extrapola o card |
| 03 | Texto "Lorem Ipsum" não substituído |
| 10 | Fonte usada é Ubuntu em vez de Roboto (conforme protótipo) |
| 11 | Erro de digitação: "Ativid 01" em vez de "Atividade 01" |
| 16 | Título/favicon da aba ainda no padrão Vite + React + TS |

---

## 🔍 Principais Descobertas

- 🔓 **Segurança:** upload aceita arquivos potencialmente maliciosos (`.exe`)
- 🗃️ **Integridade de dados:** permite CPF duplicado; campos sensíveis sem validação
- 🧩 **Funcionalidades centrais quebradas:** adicionar EPI, adicionar atividade, editar, excluir, avançar etapa
- 🧭 **Navegação divergente do protótipo:** menu lateral e superior não funcionam como esperado
- 🎨 **Inconsistências visuais:** fonte, cores de status, emblemas, textos, favicon

---

## ✅ Critérios para Nova Avaliação

- [ ] Nenhum bug crítico pendente
- [ ] Nenhum bug alto bloqueando o fluxo principal
- [ ] Validação correta de CPF, RG, Data de Nascimento, CA e campos obrigatórios
- [ ] Bloqueio de CPF duplicado
- [ ] Upload restrito aos formatos permitidos (.png, .docx, .pdf)
- [ ] Navegação e etapas conforme o protótipo
- [ ] Interface ajustada aos elementos visuais do protótipo

---

## 🏁 Conclusão

A aplicação possui uma **base funcional inicial**, mas apresenta falhas relevantes em **segurança, integridade de dados, fluxo principal e fidelidade ao protótipo**. A recomendação é **corrigir os itens críticos e altos antes de qualquer nova homologação**, seguida de testes de regressão completos.

> **Veredito:** Não Recomendado para Homologação