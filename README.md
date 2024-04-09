# 🏅 Certificate Generator

Este projeto consiste em uma aplicação para geração automatizada de certificados para eventos, utilizando JavaFX para a interface gráfica e Apache POI e PDFBox para manipulação de planilhas Excel e criação de arquivos PDF, respectivamente.

## 📁 Estrutura do Projeto

O projeto está dividido em quatro pacotes principais:

- `br.ucsal.certificateGenerator.presentation`: Contém a classe Main, responsável pela inicialização da aplicação JavaFX e definição da interface gráfica.
- `br.ucsal.certificateGenerator.application`: Contém as classes que realizam as principais funcionalidades da aplicação, como leitura de planilhas, envio de e-mails e geração de certificados.
- `br.ucsal.certificateGenerator.domain`: Contém as classes de domínio que representam os participantes e o conteúdo dos e-mails.
- `br.ucsal.certificateGenerator.infra`: Contém os certificados digitais gerados e armazenados localmente.
  
![UML](https://github.com/armentanoc/CertificateGenerator/assets/88147887/1d9f32f8-2395-4e3a-be9c-339e00c9511a)
<!--
UML Code
@startuml
class EmailContent {
    - participante: Participante
    - subject: String
    - body: String
    - certificate: File
    + EmailContent(Participante)
    + getSubject(): String
    + getBody(): String
    + getCertificate(): File
}

class EmailService {
    - host: String
    - username: String
    - password: String
    - result: String
    + enviarEmails(List<Participante>): String
}

class ParticipanteService {
    + lerPlanilhaDeParticipantes(File, String, int): List<Participante>
    - processRow(Row, List<Participante>, String, int): void
}

class CertificateGeneratorService {
    - listaParticipantes: List<Participante>
    + CertificateGeneratorService(List<Participante>)
    + gerarCertificados(): List<Participante>
    - criarDocumentoVazio(Participante): Participante
    - customizeCertificate(PDDocument, PDPage, Participante): void
}

class EmailManager {
    - participante: Participante
    - emailContent: EmailContent
    - emailSender: EmailSender
    - host: String
    - username: String
    - password: String
    + enviarEmail(): String
}

class EmailSender {
    - host: String
    - username: String
    - password: String
    - returnMessage: String
    + sendEmail(String, EmailContent): String
}

class Participante {
    - nome: String
    - cpf: String
    - email: String
    - certificate: File
    - nomeEvento: String
    - cargaHorariaEvento: int
    + getNome(): String
    + getEmail(): String
    + getNomeEvento(): String
    + getCargaHorariaEvento(): int
    + setCertificate(File): void
}

class File {
    // attributes and methods of the File class
}

EmailContent -- Participante
EmailService -- EmailManager
EmailManager -- EmailSender
EmailManager -- Participante
ParticipanteService -- Participante
CertificateGeneratorService -- Participante
@enduml

-->

## Fluxo da Aplicação

<div align="center" display="flex">
<img width="300px" src="https://github.com/armentanoc/CertificateGenerator/assets/88147887/a402f037-fe33-4643-a539-dd5a44e9d04c">
</div>

### `ParticipanteService`
<div align="center" display="flex">
<img width="600px" src="https://github.com/armentanoc/CertificateGenerator/assets/88147887/71a0b6b7-45f1-4718-803a-b70c54d4869d">
</div>

```
graph TD
    A[Ler Planilha] --> B[Processar Linhas]
    B --> C[Processar Células]
    C -->|0| D[Ler Nome]
    C -->|1| E[Ler CPF]
    C -->|2| F[Ler Email]
    F --> G[Criar Participante]
    G --> H[Add Participante a Lista]
    H --> B
    B --> I[Gerar Certificados]
```

### `CertificateGeneratorService`
<div align="center" display="flex">
<img width="600px" src="https://github.com/armentanoc/CertificateGenerator/assets/88147887/92215b7e-7f95-4924-941a-90cf5b19b65e">
</div>

```
graph TD
    A[Início] --> B[Iterar sobre Participantes]
    B --> C[Criar Documento Vazio]
    C --> D[Personalizar Certificado]
    D --> E[Salvar Documento]
    E --> F[Adicionar Certificado ao Participante]
    F --> B
    B --> H[Finalizar Participantes]
    H --> I[EmailService]
```

### `EmailService`
<div align="center" display="flex">
<img width="600px" src="https://github.com/armentanoc/CertificateGenerator/assets/88147887/50d52013-9fcd-420a-acea-2b061ea74698">
</div>  

```
graph TD
    A[Início] --> B[Iterar sobre Participantes]
    B --> C[Escrever Email]
    C --> D[Enviar Email]
    D --> E[Obter Resultado do Envio]
    E --> F[Adicionar Resultado ao Resultado Geral]
    F --> C
    C --> H[Finalizar e Exibir Relatório de Resultado]
```

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
