# 🏅 Certificate Generator

Este projeto consiste em uma aplicação para geração automatizada de certificados para eventos, utilizando JavaFX para a interface gráfica e Apache POI e PDFBox para manipulação de planilhas Excel e criação de arquivos PDF, respectivamente.

## 📁 Estrutura do Projeto

O projeto está dividido em quatro pacotes principais:

- `br.ucsal.certificateGenerator.presentation`: Contém a classe Main, responsável pela inicialização da aplicação JavaFX e definição da interface gráfica.
- `br.ucsal.certificateGenerator.application`: Contém as classes que realizam as principais funcionalidades da aplicação, como leitura de planilhas, envio de e-mails e geração de certificados.
- `br.ucsal.certificateGenerator.domain`: Contém as classes de domínio que representam os participantes e o conteúdo dos e-mails.
- `br.ucsal.certificateGenerator.infra`: Contém os certificados digitais gerados e armazenados localmente.

## ⚙️ Ambiente Local

Certifique-se de ter o Java Development Kit (JDK) 17+ e o Maven instalados e configurados em seu sistema.

## 🚀 Utilização

Para utilizar a aplicação, siga os passos abaixo:

1. Clone este repositório em sua máquina local.
2. Abra o projeto em sua IDE de preferência.
3. No terminal, execute o seguinte comando: 
```
mvn clean javafx:run
```
4. A interface gráfica da aplicação será exibida.
5. Realize o login com um email gmail configurado para utilização de aplicação e a respectiva senha.
6. Informe nome do evento, carga horária e selecione a planilha contendo os participantes.
7. Clique no botão para iniciar o processo de geração de certificados e envio de e-mails.

## 📚 Dependências

Este projeto utiliza as seguintes bibliotecas externas, abstraídas através do Maven:

- Commons Validator
- Jakarta Activation API
- JavaMail (Jakarta Mail)
- Dom4j
- Apache POI
- Apache POI - OOXML
- PDFBox
- JavaFX - Controls
- JavaFX - FXML
- Log4j Core
- FontAwesomeFX FontAwesome
