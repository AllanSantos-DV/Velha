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
    private final List<char[][]> gameStates;
    private final List<int[]> gameMoves;

    public Game(Player playerX, Player playerO) {
        this.board = new Board();
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = playerX; // Primeiro jogador começa
        this.isGameOver = false;
        this.winner = null;
        this.gameStates = new ArrayList<>();
        this.gameMoves = new ArrayList<>();
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

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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