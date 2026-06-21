package com.mat.tests;

import com.mat.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.mat.pages.FuncionarioPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTest extends BaseTest {

        private String getFixturePath(String fileName) {
                try {
                        URL resource = getClass().getClassLoader().getResource("fixtures/" + fileName);
                        if (resource == null) {
                                return "";
                        }
                        return Paths.get(resource.toURI()).toAbsolutePath().toString();
                } catch (Exception e) {
                        return "";
                }
        }

        private void validarRedirecionamentoParaMenuInicial(String idCasoTeste) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
                boolean voltouParaInicio = wait.until(d -> d.getCurrentUrl()
                                .contains("analista-teste.seatecnologia.com.br") &&
                                d.findElements(By.xpath(
                                                "//*[contains(text(), 'Adicionar Funcionário') or contains(text(), 'Funcionários')]"))
                                                .size() > 0);
                assertTrue(voltouParaInicio,
                                idCasoTeste + " FALHOU: O sistema não retornou para o menu inicial após salvar.");
        }

        // ── CT-08 ───────────────────────────────────────────────────────────────────
        @Test
        @DisplayName("CT-08 - Criar funcionário seguindo o fluxo principal (Caminho Feliz)")
        public void ct08_fluxoPrincipalCaminhoFeliz() {
                FuncionarioPage page = new FuncionarioPage(driver);
                page.navegarParaNovoFuncionario();

                page.definirStatusAtivo();

                String cpfValido = "529.982.247-25";

                page.preencherDadosBasicos(
                                "João Silva",
                                cpfValido,
                                "12.345.678-9",
                                "15/06/1990");

                page.preencherCA("12345");
                page.submeterFormulario();
                validarRedirecionamentoParaMenuInicial("CT-08");
        }

        // ── CT-09 ───────────────────────────────────────────────────────────────────
        @Test
        @DisplayName("CT-09 - Criar funcionário com mais de um EPI vinculado")
        public void ct09_funcionarioComMaisDeUmEpi() {
                FuncionarioPage page = new FuncionarioPage(driver);
                page.navegarParaNovoFuncionario();

                page.preencherDadosBasicos(
                                "Maria Oliveira",
                                "529.982.247-25",
                                "98.765.432-1",
                                "20/03/1985");

                // Preenche o CA do EPI que já vem por padrão no formulário
                page.preencherCA("12345");

                // Adiciona um segundo EPI e preenche o CA dele
                page.adicionarEpi();
                page.preencherCA("12345");

                int quantidadeEPIs = page.contarEPIsNaTela();

                assertEquals(
                                2,
                                quantidadeEPIs,
                                "[BUG ENCONTRADO] CT-09 FALHOU: O sistema NÃO duplicou os EPIs na tela.");

                page.submeterFormulario();
                validarRedirecionamentoParaMenuInicial("CT-09");
        }

        // ── CT-10 ───────────────────────────────────────────────────────────────────
        @Test
        @DisplayName("CT-10 - Criar funcionário com status inicial 'Inativo'")
        public void ct10_funcionarioComStatusInativo() {
                FuncionarioPage page = new FuncionarioPage(driver);
                page.navegarParaNovoFuncionario();

                page.definirStatusInativo();

                page.preencherDadosBasicos(
                                "Carlos Inativo",
                                "111.222.333-44",
                                "11.111.111-1",
                                "10/10/1980");

                page.preencherCA("12345");
                page.submeterFormulario();
                validarRedirecionamentoParaMenuInicial("CT-10");
        }

        // ── CT-11 ───────────────────────────────────────────────────────────────────
        @Test
        @DisplayName("CT-11 - Criar funcionário ATIVO sem EPI e com atestado de saúde")
        public void ct11_funcionarioSemEpiComAtestado() {
                String caminhoAtestado = getFixturePath("atestado.pdf");

                assertFalse(caminhoAtestado.isEmpty(),
                                "\n[ERRO CRÍTICO] O arquivo 'atestado.pdf' não foi encontrado!\n");

                FuncionarioPage page = new FuncionarioPage(driver);
                page.navegarParaNovoFuncionario();

                page.preencherDadosBasicos(
                                "Letícia Ativa Sem EPI",
                                "555.666.777-88",
                                "22.222.222-2",
                                "05/05/1995");

                page.marcarSemEpi();
                page.fazerUploadAtestado(caminhoAtestado);
                page.submeterFormulario();
                validarRedirecionamentoParaMenuInicial("CT-11");
        }

        // ── CT-16 ───────────────────────────────────────────────────────────────────
        @Test
        @DisplayName("CT-16 - Submeter formulário com campos obrigatórios em branco")
        public void ct16_camposObrigatoriosEmBranco() {
                FuncionarioPage page = new FuncionarioPage(driver);
                page.navegarParaNovoFuncionario();

                page.submeterFormulario();

                try {
                        Thread.sleep(1500);
                } catch (InterruptedException ignored) {
                }

                WebElement campoAtivo = driver.switchTo().activeElement();
                String mensagemNativa = campoAtivo.getAttribute("validationMessage");

                assertAll(
                                () -> assertNotNull(mensagemNativa,
                                                "CT-16 FALHOU: O atributo validationMessage não existe."),
                                () -> assertFalse(mensagemNativa.isEmpty(),
                                                "CT-16 FALHOU: O balão de validação do HTML5 não apareceu."),
                                () -> assertTrue(
                                                mensagemNativa.contains("Preencha")
                                                                || mensagemNativa.contains("preencher"),
                                                "CT-16 FALHOU: A mensagem não parece ser de campo obrigatório: "
                                                                + mensagemNativa),
                                () -> assertFalse(page.formularioEnviadoComSucesso(),
                                                "CT-16 FALHOU: O formulário foi enviado em branco."));
        }

        // ── CT-17 ───────────────────────────────────────────────────────────────────
        @ParameterizedTest(name = "CT-17 - Upload com extensão proibida: {0}")
        @ValueSource(strings = { "TESTE.exe", "TESTE-02.xlsx" })
        @DisplayName("CT-17 - Upload de atestado com extensão proibida (.exe e .xlsx)")
        public void ct17_uploadExtensaoProibida(String nomeArquivo) {
                String caminhoArquivo = getFixturePath(nomeArquivo);

                FuncionarioPage page = new FuncionarioPage(driver);
                page.navegarParaNovoFuncionario();

                page.preencherDadosBasicos(
                                "Teste Upload Inválido",
                                "000.000.000-00",
                                "00.000.000-0",
                                "01/01/2000");

                page.marcarSemEpi();
                page.fazerUploadAtestado(caminhoArquivo);

                try {
                        Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

                page.submeterFormulario();

                assertAll(
                                () -> assertTrue(page.erroExtensaoInvalidaVisivel(),
                                                "[BUG ENCONTRADO] CT-17 FALHOU: O sistema aceitou extensão inválida: "
                                                                + nomeArquivo),
                                () -> assertFalse(page.formularioEnviadoComSucesso(),
                                                "[BUG ENCONTRADO] CT-17 FALHOU: O formulário enviado com arquivo inválido."));
        }
}