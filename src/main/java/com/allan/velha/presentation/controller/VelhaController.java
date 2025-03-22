package com.allan.velha.presentation.controller;

import com.allan.velha.domain.model.Game;
import com.allan.velha.domain.model.Player;
import com.allan.velha.domain.service.NeuralNetworkService;
import com.allan.velha.domain.service.impl.NeuralNetworkServiceImpl;
import com.allan.velha.presentation.model.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

/**
 * Controlador principal do jogo da velha.
 * Gerencia a interface do usuário e a lógica do jogo.
 */
public class VelhaController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML
    private Label lblPlayer;
    @FXML
    private Label lblOverlay;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnRestart;
    @FXML
    private TableView<Score> tblScore;
    @FXML
    private TableColumn<Score, String> colPlayer;
    @FXML
    private TableColumn<Score, Integer> colWins;
    @FXML
    private Label lblDraws;

    private final NeuralNetworkService neuralNetworkService;
    private Game game;
    private Button[][] buttons;
    private final ObservableList<Score> scores = FXCollections.observableArrayList();
    private boolean isGameStarted = false;
    private boolean isHumanFirst = true; // Controla quem começa o próximo jogo

    /**
     * Construtor do controlador.
     * 
     * @param neuralNetworkService Serviço de IA para o jogo
     */
    public VelhaController(NeuralNetworkService neuralNetworkService) {
        this.neuralNetworkService = neuralNetworkService;
    }

    /**
     * Inicializa o controlador após o FXML ser carregado.
     */
    @FXML
    public void initialize() {
        setupButtons();
        setupTable();
        resetGame();
    }

    private void setupButtons() {
        buttons = new Button[][] {
                { btn00, btn01, btn02 },
                { btn10, btn11, btn12 },
                { btn20, btn21, btn22 }
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setOnAction(this::handleButtonClick);
            }
        }
    }

    private void setupTable() {
        colPlayer.setCellValueFactory(new PropertyValueFactory<>("player"));
        colWins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        tblScore.setItems(scores);
        scores.add(new Score("X", 0));
        scores.add(new Score("O", 0));
        lblDraws.setText("0");
    }

    /**
     * Inicia um novo jogo.
     */
    @FXML
    public void startNewGame() {
        if (!isGameStarted) {
            isGameStarted = true;

            // Humano sempre X, IA sempre O
            Player humanPlayer = new Player("X");
            Player aiPlayer = new Player("O");

            clearBoard();
            enableButtons(true);

            // Sempre criamos o jogo com X primeiro e O segundo para manter os símbolos
            game = new Game(humanPlayer, aiPlayer);

            // Se a IA começa, definimos ela como jogador atual e fazemos sua jogada
            if (!isHumanFirst) {
                game.setCurrentPlayer(aiPlayer);
                makeAIMove();
            }

            updateStatusLabel();
            btnStart.setDisable(true);
            btnRestart.setDisable(false);
            lblOverlay.setVisible(false);
        }
    }

    /**
     * Reinicia o placar do jogo.
     */
    @FXML
    public void resetGame() {
        scores.clear();
        scores.add(new Score("X", 0));
        scores.add(new Score("O", 0));
        lblDraws.setText("0");
        clearBoard();
        enableButtons(false);
        isGameStarted = false;
        isHumanFirst = true; // Primeiro jogo sempre começa com o humano
        btnStart.setDisable(false);
        btnRestart.setDisable(true);
        lblOverlay.setVisible(true);
        lblOverlay.setText("Clique em Iniciar para começar!");
    }

    /**
     * Manipula o clique em um botão do tabuleiro.
     */
    @FXML
    public void handleButtonClick(javafx.event.ActionEvent event) {
        if (!isGameStarted)
            return;

        // Só aceita cliques quando for a vez do jogador X (humano)
        if (!game.getCurrentPlayer().getName().equals("X"))
            return;

        Button clickedButton = (Button) event.getSource();
        int[] position = findButtonPosition(clickedButton);

        if (position != null && isValidMove(position)) {
            makePlayerMove(position);
        }
    }

    private int[] findButtonPosition(Button button) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    private boolean isValidMove(int[] position) {
        return game.getBoard().getState()[position[0]][position[1]] == ' ';
    }

    private void makePlayerMove(int[] position) {
        // Garante que o jogador humano só joga como X
        if (!game.getCurrentPlayer().getName().equals("X"))
            return;

        game.makeMove(position[0], position[1]);
        updateBoard();
        updateStatusLabel();

        // Verifica se o jogo acabou após a jogada do jogador
        if (!game.isGameOver()) {
            makeAIMove();
        } else {
            checkGameEnd();
        }
    }

    private void makeAIMove() {
        // Garante que a IA só joga como O
        if (!game.getCurrentPlayer().getName().equals("O"))
            return;

        int[] aiMove = neuralNetworkService.predictNextMove(game);
        if (aiMove != null) {
            game.makeMove(aiMove[0], aiMove[1]);
            updateBoard();
            updateStatusLabel();

            // Verifica se o jogo acabou após a jogada da IA
            if (game.isGameOver()) {
                checkGameEnd();
            }
        }
    }

    /**
     * Manipula o clique no overlay.
     */
    @FXML
    public void handleOverlayClick() {
        if (game != null && game.isGameOver()) {
            isHumanFirst = !isHumanFirst; // Alterna quem começa o próximo jogo
            startNewGame();
        }
    }

    private void checkGameEnd() {
        if (game.isGameOver()) {
            isGameStarted = false;
            enableButtons(false);
            btnStart.setDisable(false);
            updateScore();

            String message;
            if (game.getWinner() != null) {
                message = "Jogador " + game.getWinner().getName() + " venceu!\nClique para jogar novamente.";
                if (neuralNetworkService instanceof NeuralNetworkServiceImpl) {
                    ((NeuralNetworkServiceImpl) neuralNetworkService).registerGameResult(game);
                }
            } else {
                message = "Empate!\nClique para jogar novamente.";
            }

            lblOverlay.setText(message);
            lblOverlay.setVisible(true);
        }
    }

    private void updateBoard() {
        char[][] state = game.getBoard().getState();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(state[i][j] == ' ' ? "" : String.valueOf(state[i][j]));
            }
        }
    }

    private void updateStatusLabel() {
        if (game.isGameOver()) {
            Player winner = game.getWinner();
            lblPlayer.setText(winner != null ? "Jogador " + winner.getName() + " venceu!" : "Empate!");
        } else {
            lblPlayer.setText("Vez do jogador " + game.getCurrentPlayer().getName());
        }
    }

    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void enableButtons(boolean enable) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setDisable(!enable);
            }
        }
    }

    private void updateScore() {
        Player winner = game.getWinner();
        if (winner != null) {
            String playerName = winner.getName();
            int currentIndex = playerName.equals("X") ? 0 : 1;
            Score currentScore = scores.get(currentIndex);
            scores.set(currentIndex, new Score(playerName, currentScore.getWins() + 1));
        } else {
            int draws = Integer.parseInt(lblDraws.getText());
            lblDraws.setText(String.valueOf(draws + 1));
        }
    }
}