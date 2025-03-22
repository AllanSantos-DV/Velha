package com.allan.velha.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final Player playerX;
    private final Player playerO;
    private Player currentPlayer;
    private boolean isGameOver;
    private Player winner;
    private List<char[][]> gameStates;
    private List<int[]> gameMoves;
    private int draws;

    public Game(Player playerX, Player playerO) {
        this.board = new Board();
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = playerX; // Primeiro jogador começa
        this.isGameOver = false;
        this.winner = null;
        this.gameStates = new ArrayList<>();
        this.gameMoves = new ArrayList<>();
        this.draws = 0;
        saveGameState();
    }

    public void start() {
        board.clear();
        gameStates.clear();
        gameMoves.clear();
        isGameOver = false;
        winner = null;
        currentPlayer = playerX; // Primeiro jogador começa
        saveGameState();
    }

    public void makeMove(int row, int col) {
        if (isGameOver || !board.isValidMove(row, col)) {
            return;
        }

        char symbol = currentPlayer == playerX ? 'X' : 'O';
        board.makeMove(row, col, symbol);
        saveGameState();
        saveMove(row, col);

        if (board.checkWin(symbol)) {
            isGameOver = true;
            winner = currentPlayer;
            currentPlayer.incrementWins();
            (currentPlayer == playerX ? playerO : playerX).incrementLosses();
        } else if (board.isFull()) {
            isGameOver = true;
            draws++;
            playerX.incrementDraws();
            playerO.incrementDraws();
        } else {
            currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
        }
    }

    private void saveGameState() {
        gameStates.add(board.getState());
    }

    private void saveMove(int row, int col) {
        gameMoves.add(new int[] { row, col });
    }

    public boolean checkWinner() {
        // Verificar linhas
        for (int i = 0; i < 3; i++) {
            if (checkLine(board.getCell(i, 0), board.getCell(i, 1), board.getCell(i, 2))) {
                return true;
            }
        }

        // Verificar colunas
        for (int i = 0; i < 3; i++) {
            if (checkLine(board.getCell(0, i), board.getCell(1, i), board.getCell(2, i))) {
                return true;
            }
        }

        // Verificar diagonais
        return checkLine(board.getCell(0, 0), board.getCell(1, 1), board.getCell(2, 2)) ||
                checkLine(board.getCell(0, 2), board.getCell(1, 1), board.getCell(2, 0));
    }

    private boolean checkLine(char a, char b, char c) {
        return a != ' ' && a == b && b == c;
    }

    public void resetScores() {
        playerX.resetScores();
        playerO.resetScores();
        draws = 0;
    }

    public int getDraws() {
        return draws;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<char[][]> getGameStates() {
        return gameStates;
    }

    public List<int[]> getMoves() {
        return gameMoves;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Player getWinner() {
        return winner;
    }

    public void setCurrentPlayer(Player player) {
        if (player == playerX || player == playerO) {
            this.currentPlayer = player;
        }
    }
}