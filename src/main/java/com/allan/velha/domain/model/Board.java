package com.allan.velha.domain.model;

public class Board {
    private static final int SIZE = 3;
    private final char[][] board;

    public Board() {
        board = new char[SIZE][SIZE];
        clear();
    }

    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == ' ';
    }

    public void makeMove(int row, int col, char symbol) {
        if (isValidMove(row, col)) {
            board[row][col] = symbol;
        }
    }

    public boolean checkWin(char symbol) {
        // Verificar linhas
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == symbol &&
                    board[i][1] == symbol &&
                    board[i][2] == symbol) {
                return true;
            }
        }

        // Verificar colunas
        for (int j = 0; j < SIZE; j++) {
            if (board[0][j] == symbol &&
                    board[1][j] == symbol &&
                    board[2][j] == symbol) {
                return true;
            }
        }

        // Verificar diagonal principal
        if (board[0][0] == symbol &&
                board[1][1] == symbol &&
                board[2][2] == symbol) {
            return true;
        }

        // Verificar diagonal secundária
        return board[0][2] == symbol &&
                board[1][1] == symbol &&
                board[2][0] == symbol;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getState() {
        char[][] state = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, state[i], 0, SIZE);
        }
        return state;
    }
}