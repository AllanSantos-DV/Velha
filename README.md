# Jogo da Velha

Um jogo da velha implementado em JavaFX com interface gráfica.

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

## Como rodar o projeto

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/velha.git
cd velha
```

2. Compile o projeto:
```bash
mvn clean compile
```

3. Execute o jogo:
```bash
mvn javafx:run
```

## Estrutura do Projeto

```
src/main/java/com/allan/velha/
├── application/
│   └── VelhaApp.java
├── domain/
│   ├── model/
│   │   ├── Board.java
│   │   ├── Game.java
│   │   └── Player.java
│   ├── repository/
│   │   └── AIRepository.java
│   └── service/
│       ├── AIService.java
│       ├── GameService.java
│       └── impl/
│           ├── GameServiceImpl.java
│           └── NeuralNetworkServiceImpl.java
├── infrastructure/
│   └── persistence/
│       └── FileAIRepository.java
└── presentation/
    ├── controller/
    │   └── VelhaController.java
    ├── model/
    │   └── Score.java
    └── view/
        └── Velha-view.fxml
```

## Funcionalidades

- Interface gráfica com JavaFX
- Jogo da velha completo
- Sistema de pontuação
- IA para jogar contra o computador
- Persistência de dados 