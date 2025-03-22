# Jogo da Velha com IA

Um jogo da velha implementado em JavaFX com interface gráfica e inteligência artificial.

## Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

## Como Executar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/velha.git
cd velha
```

2. Compile e execute o projeto:
```bash
mvn clean javafx:run
```

## Funcionalidades

- Interface gráfica intuitiva
- Jogador humano (X) vs IA (O)
- Alternância automática de quem começa cada partida
- Placar de vitórias e empates
- IA com aprendizado baseado em padrões de jogo

## Como Jogar

1. Ao iniciar o jogo, clique no botão "Iniciar"
2. No primeiro jogo, o jogador humano (X) sempre começa
3. Clique em qualquer célula vazia para fazer sua jogada
4. A IA (O) fará sua jogada automaticamente
5. Ao fim de cada partida:
   - O placar é atualizado
   - Uma mensagem indica o vencedor ou empate
   - Clique na mensagem para iniciar uma nova partida
   - A próxima partida alternará quem começa

## Estrutura do Projeto

```
com.allan.velha
├── VelhaApplication.java         # Classe principal da aplicação
├── domain                        # Camada de domínio
│   ├── model                    # Modelos de negócio
│   │   ├── Board.java          # Tabuleiro do jogo
│   │   ├── Game.java           # Regras e estado do jogo
│   │   ├── GameMemory.java     # Memória de jogadas da IA
│   │   └── Player.java         # Jogador (humano ou IA)
│   └── service                  # Serviços
│       ├── NeuralNetworkService.java      # Interface da IA
│       └── impl
│           └── NeuralNetworkServiceImpl.java  # Implementação da IA
└── presentation                  # Camada de apresentação
    ├── controller
    │   └── VelhaController.java # Controlador da interface
    └── model
        └── Score.java           # Modelo de pontuação
```

## Características Técnicas

- **Arquitetura MVC**: Separação clara entre Model, View e Controller
- **JavaFX**: Interface gráfica moderna e responsiva
- **Maven**: Gerenciamento de dependências e build
- **Modular**: Estrutura organizada e de fácil manutenção
- **IA**: Implementação de rede neural para tomada de decisões

## Regras do Jogo

1. O jogador humano é sempre X
2. A IA é sempre O
3. Vence quem completar uma linha, coluna ou diagonal
4. Se ninguém vencer, o jogo termina em empate
5. A cada nova partida, alterna-se quem começa
6. O placar mantém o registro de vitórias e empates

## Controles

- **Iniciar**: Começa um novo jogo
- **Resetar**: Zera o placar e reinicia o jogo
- **Grid**: Clique nas células para fazer sua jogada
- **Overlay**: Clique na mensagem de fim de jogo para nova partida 