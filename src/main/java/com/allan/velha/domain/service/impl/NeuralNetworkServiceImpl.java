package com.allan.velha.domain.service.impl;

import com.allan.velha.domain.model.Game;
import com.allan.velha.domain.model.GameMemory;
import com.allan.velha.domain.service.NeuralNetworkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetworkServiceImpl implements NeuralNetworkService {
    private final Random random = new Random();
    private final GameMemory gameMemory = new GameMemory();
    private final List<BoardState> moveHistory = new ArrayList<>();

    @Override
    public int[] predictNextMove(Game game) {
        char[][] board = game.getBoard().getState();

        // 1. Verificar se pode vencer no próximo movimento
        int[] winningMove = findWinningMove(board, 'O');
        if (winningMove != null) {
            recordMove(board, winningMove);
            return winningMove;
        }

        // 2. Verificar se precisa bloquear uma vitória do jogador
        int[] blockingMove = findWinningMove(board, 'X');
        if (blockingMove != null) {
            recordMove(board, blockingMove);
            return blockingMove;
        }

        // 3. Obter jogadas disponíveis e remover as que levaram a derrotas anteriores
        List<int[]> availableMoves = getAvailableMoves(board);
        List<int[]> safeMoves = new ArrayList<>();

        // Analisar cada jogada disponível
        for (int[] move : availableMoves) {
            if (!gameMemory.isLosingMove(board, move[0], move[1])) {
                // Simular a jogada
                board[move[0]][move[1]] = 'O';

                // Verificar se o jogador terá uma jogada vencedora após esta
                boolean isRisky = false;

                // Simular todas as possíveis respostas do jogador
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == ' ') {
                            board[i][j] = 'X';
                            if (checkWin(board, 'X')) {
                                isRisky = true;
                            }
                            board[i][j] = ' ';
                        }
                    }
                }

                // Desfazer a simulação
                board[move[0]][move[1]] = ' ';

                if (!isRisky) {
                    safeMoves.add(move);
                }
            }
        }

        // Se todas as jogadas são ruins, usar as disponíveis
        if (safeMoves.isEmpty()) {
            safeMoves = availableMoves;
        }

        // 4. Verificar pontos quentes do tabuleiro
        List<int[]> hotMoves = new ArrayList<>();
        for (int[] move : safeMoves) {
            if (isHotSpot(board, move)) {
                hotMoves.add(move);
            }
        }

        // Se houver pontos quentes seguros, escolher um deles
        if (!hotMoves.isEmpty()) {
            int[] move = hotMoves.get(random.nextInt(hotMoves.size()));
            recordMove(board, move);
            return move;
        }

        // 5. Se nenhuma estratégia anterior funcionar, escolher aleatoriamente
        if (!safeMoves.isEmpty()) {
            int[] move = safeMoves.get(random.nextInt(safeMoves.size()));
            recordMove(board, move);
            return move;
        }

        return null;
    }

    private void recordMove(char[][] board, int[] move) {
        char[][] boardCopy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, 3);
        }
        moveHistory.add(new BoardState(boardCopy, move[0], move[1]));
    }

    private boolean isHotSpot(char[][] board, int[] move) {
        // Centro é o ponto mais quente
        if (move[0] == 1 && move[1] == 1) {
            return true;
        }

        // Cantos são o segundo ponto mais quente
        if ((move[0] == 0 || move[0] == 2) && (move[1] == 0 || move[1] == 2)) {
            // Verificar se há símbolos adjacentes
            int row = move[0];
            int col = move[1];

            // Verificar horizontalmente
            if (col == 0 && board[row][1] != ' ')
                return true;
            if (col == 2 && board[row][1] != ' ')
                return true;

            // Verificar verticalmente
            if (row == 0 && board[1][col] != ' ')
                return true;
            if (row == 2 && board[1][col] != ' ')
                return true;

            // Verificar diagonalmente
            if ((row == 0 && col == 0) && (board[1][1] != ' ' || board[1][0] != ' ' || board[0][1] != ' '))
                return true;
            if ((row == 0 && col == 2) && (board[1][1] != ' ' || board[1][2] != ' ' || board[0][1] != ' '))
                return true;
            if ((row == 2 && col == 0) && (board[1][1] != ' ' || board[1][0] != ' ' || board[2][1] != ' '))
                return true;
            return (row == 2 && col == 2) && (board[1][1] != ' ' || board[1][2] != ' ' || board[2][1] != ' ');
        }

        return false;
    }

    private int[] findWinningMove(char[][] board, char symbol) {
        // Testar cada posição vazia
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    // Simular a jogada
                    board[i][j] = symbol;

                    // Verificar se é uma jogada vencedora
                    if (checkWin(board, symbol)) {
                        // Desfazer a simulação
                        board[i][j] = ' ';
                        return new int[] { i, j };
                    }

                    // Desfazer a simulação
                    board[i][j] = ' ';
                }
            }
        }
        return null;
    }

    private boolean checkWin(char[][] board, char symbol) {
        // Verificar linhas
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
        }

        // Verificar colunas
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == symbol && board[1][j] == symbol && board[2][j] == symbol) {
                return true;
            }
        }

        // Verificar diagonais
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        return board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol;
    }

    private List<int[]> getAvailableMoves(char[][] board) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    moves.add(new int[] { i, j });
                }
            }
        }
        return moves;
    }

    public void registerGameResult(Game game) {
        if (game.getWinner() != null && game.getWinner().name().equals("X")) {
            // Se o jogador humano (X) ganhou, registrar todas as jogadas como perdedoras
            for (BoardState state : moveHistory) {
                gameMemory.addLosingMove(state.board, state.row, state.col);
            }
        }
        moveHistory.clear();
    }

    private record BoardState(char[][] board, int row, int col) {
    }
}