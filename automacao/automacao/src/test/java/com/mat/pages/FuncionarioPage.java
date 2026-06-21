package com.mat.pages;

import com.mat.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FuncionarioPage extends BasePage {

    public FuncionarioPage(WebDriver driver) {
        super(driver);
    }

    // ── Botão "+ Adicionar Funcionário" na lista ───────────────────────────────
    private final By BTN_ADICIONAR_FUNCIONARIO = By.xpath(
            "//*[contains(text(),'Adicionar Funcionário')]");

    // ── Toggle Ativo/Inativo — ant-switch ─────────────────────────────────────
    private final By TOGGLE_STATUS_BUTTON = By.xpath(
            "//button[@role='switch'] | //button[contains(@class,'ant-switch')]");

    // ── Campos de texto do formulário ──────────────────────────────────────────
    private final By CAMPO_NOME = By.xpath(
            "//label[normalize-space(text())='Nome']/following::input[1]");
    private final By CAMPO_CPF = By.xpath(
            "//label[normalize-space(text())='CPF']/following::input[1]");
    private final By CAMPO_RG = By.xpath(
            "//label[normalize-space(text())='RG']/following::input[1]");
    private final By CAMPO_DATA_NASCIMENTO = By.xpath(
            "//input[@type='date'] | //input[contains(@placeholder,'aaaa')]");

    // ── Checkbox "O trabalhador não usa EPI." ─────────────────────────────────
    private final By CHECKBOX_SEM_EPI = By.xpath(
            "//label[contains(.,'não usa EPI')]//input[@type='checkbox']"
                    + " | //input[@type='checkbox'][following-sibling::*[contains(.,'não usa EPI')]]"
                    + " | //input[@type='checkbox'][../following-sibling::*[contains(.,'não usa EPI')]]"
                    + " | (//input[@type='checkbox'])[1]");

    // ── Seção de EPI ───────────────────────────────────────────────────────────
    // "Adicionar EPI" — texto clicável no canto direito da linha de EPI
    private final By BTN_ADICIONAR_EPI = By.xpath(
            "//*[normalize-space(text())='Adicionar EPI' or normalize-space(.)='Adicionar EPI']");

    // Campo "Informe o número do CA" — é OBRIGATÓRIO antes de clicar Adicionar EPI
    // Busca todos os inputs de CA na tela para pegar sempre o último (suporta
    // múltiplos EPIs)
    private final By TODOS_CAMPOS_CA = By.xpath(
            "//label[contains(.,'número do CA') or normalize-space(text())='CA']/following::input[1]"
                    + " | //input[contains(@placeholder,'CA') or contains(@id,'ca') or contains(@name,'ca')]");

    // ── Upload de atestado ─────────────────────────────────────────────────────
    private final By INPUT_ATESTADO = By.xpath("//input[@type='file']");

    // ── Botão Salvar ───────────────────────────────────────────────────────────
    private final By BTN_SALVAR = By.xpath(
            "//*[normalize-space(text())='Salvar' or normalize-space(.)='Salvar']"
                    + "[self::button or self::span or self::a]");

    // ── Mensagem de sucesso ────────────────────────────────────────────────────
    private final By MSG_SUCESSO = By.xpath(
            "//*[contains(@class,'success') or contains(@class,'ant-message-success')"
                    + " or contains(@class,'ant-notification-notice-success')"
                    + " or contains(@class,'swal2-success')"
                    + " or contains(text(),'sucesso') or contains(text(),'cadastrado') or contains(text(),'criado')]");

    // ── Mensagem de extensão inválida ──────────────────────────────────────────
    private final By MSG_EXTENSAO_INVALIDA = By.xpath(
            "//*[contains(@class,'ant-form-item-explain-error')]"
                    + " | //*[contains(text(),'extensão') or contains(text(),'formato')"
                    + " or contains(text(),'inválido') or contains(text(),'permitido') or contains(text(),'tipo')]");

    // ── Métodos de negócio ─────────────────────────────────────────────────────

    public void navegarParaNovoFuncionario() {
        click(BTN_ADICIONAR_FUNCIONARIO);
    }

    public void preencherDadosBasicos(String nome, String cpf, String rg, String dataNascimento) {
        typeText(CAMPO_NOME, nome);
        typeText(CAMPO_CPF, cpf);
        typeText(CAMPO_RG, rg);
        if (dataNascimento != null && !dataNascimento.isEmpty()) {
            typeText(CAMPO_DATA_NASCIMENTO, dataNascimento);
        }
    }

    public void definirStatusAtivo() {
        WebElement toggle = findElement(TOGGLE_STATUS_BUTTON);
        String checked = toggle.getAttribute("aria-checked");

        if (!"true".equals(checked)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
        }
    }

    /**
     * Garante que o toggle fique em "Inativo".
     * Lê o aria-checked atual e só clica se estiver "true" (= Ativo).
     */
    public void definirStatusInativo() {
        WebElement toggle = findElement(TOGGLE_STATUS_BUTTON);
        String checked = toggle.getAttribute("aria-checked");
        if ("true".equals(checked)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
        }
    }

    public void adicionarEpi() {
        WebElement btnAdicionarEpi = findElement(BTN_ADICIONAR_EPI);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", btnAdicionarEpi);
    }

    public void preencherCA(String numeroCa) {
        List<WebElement> campos = driver.findElements(TODOS_CAMPOS_CA);

        if (campos.isEmpty()) {
            throw new RuntimeException("Nenhum campo CA encontrado na tela");
        }

        WebElement ultimoCa = campos.get(campos.size() - 1);
        ultimoCa.clear();
        ultimoCa.sendKeys(numeroCa);
    }

    /**
     * Marca o checkbox "O trabalhador não usa EPI." via JavaScript.
     */
    public void marcarSemEpi() {
        WebElement cb = findElement(CHECKBOX_SEM_EPI);
        if (!cb.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
        }
    }

    /**
     * Faz upload revelando o input[file] oculto via JavaScript.
     */
    public void fazerUploadAtestado(String caminhoArquivo) {
        WebElement input = findElement(INPUT_ATESTADO);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.display='block';"
                        + "arguments[0].style.visibility='visible';"
                        + "arguments[0].style.opacity='1';",
                input);
        input.sendKeys(caminhoArquivo);
    }

    /**
     * Clica em Salvar via JavaScript.
     */
    public void submeterFormulario() {
        WebElement btn = findElement(BTN_SALVAR);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean formularioEnviadoComSucesso() {
        return isElementVisible(MSG_SUCESSO);
    }

    public boolean erroCampoObrigatorioVisivel() {
        try {
            // 1. Tenta encontrar a classe de erro padrão do Ant Design
            By erroAntD = By.xpath("//div[contains(@class, 'ant-form-item-explain-error')]");
            if (!driver.findElements(erroAntD).isEmpty()) {
                return true;
            }

            // 2. Procura por textos comuns de validação
            By textosErro = By.xpath(
                    "//*[contains(text(),'obrigatório') or contains(text(),'Por favor') or contains(text(),'Preencha')]");
            if (!driver.findElements(textosErro).isEmpty()) {
                return true;
            }

            // 3. Validação nativa de HTML5/Aria-invalid (Fallback)
            List<WebElement> campos = driver.findElements(By.xpath("//input | //select | //textarea"));
            for (WebElement campo : campos) {
                String msg = (String) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0].validationMessage;", campo);
                if (msg != null && !msg.isEmpty()) {
                    return true;
                }
                if ("true".equals(campo.getAttribute("aria-invalid"))) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public boolean erroExtensaoInvalidaVisivel() {
        return isElementVisible(MSG_EXTENSAO_INVALIDA);
    }

    /**
     * Conta quantos blocos de EPI existem na tela.
     * Baseado no label "Selecione o EPI:"
     */
    public int contarEPIsNaTela() {
        return driver.findElements(
                By.xpath("//label[contains(.,'Selecione o EPI')]")).size();
    }
}