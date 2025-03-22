package com.allan.velha.domain.model;

import java.io.*;
import java.util.*;

/**
 * Classe responsável por armazenar e gerenciar a memória de jogadas do jogo.
 * Mantém um registro das jogadas que levaram a derrotas para evitar repeti-las.
 */
public class GameMemory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MEMORY_FILE = "src/main/resources/data/game_memory.dat";
    private final Map<String, List<BoardState>> losingMoves;

    public GameMemory() {
        this.losingMoves = loadMemory();
    }

    /**
     * Adiciona uma jogada perdedora à memória.
     * 
     * @param boardState Estado do tabuleiro
     * @param row        Linha da jogada
     * @param col        Coluna da jogada
     */
    public void addLosingMove(char[][] boardState, int row, int col) {
        String key = getBoardKey(boardState);
        losingMoves.computeIfAbsent(key, k -> new ArrayList<>())
                .add(new BoardState(boardState, row, col));
        saveMemory();
    }

    /**
     * Verifica se uma jogada é perdedora baseado na memória.
     * 
     * @param boardState Estado do tabuleiro
     * @param row        Linha da jogada
     * @param col        Coluna da jogada
     * @return true se a jogada é perdedora
     */
    public boolean isLosingMove(char[][] boardState, int row, int col) {
        Set<String> keys = getAllBoardKeys(boardState);
        for (String key : keys) {
            List<BoardState> moves = losingMoves.get(key);
            if (moves != null) {
                for (BoardState move : moves) {
                    if (move.isEquivalentMove(boardState, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Set<String> getAllBoardKeys(char[][] board) {
        Set<String> keys = new HashSet<>();
        char[][] currentBoard = board;

        // Rotações
        for (int i = 0; i < 4; i++) {
            keys.add(getBoardKey(currentBoard));
            currentBoard = rotate90(currentBoard);
        }

        // Reflexões
        currentBoard = flipHorizontal(board);
        keys.add(getBoardKey(currentBoard));
        currentBoard = flipVertical(board);
        keys.add(getBoardKey(currentBoard));

        return keys;
    }

    private char[][] rotate90(char[][] board) {
        int n = board.length;
        char[][] rotated = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = board[i][j];
            }
        }
        return rotated;
    }

    private char[][] flipHorizontal(char[][] board) {
        int n = board.length;
        char[][] flipped = new char[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(board[i], 0, flipped[n - 1 - i], 0, n);
        }
        return flipped;
    }

    private char[][] flipVertical(char[][] board) {
        int n = board.length;
        char[][] flipped = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                flipped[i][j] = board[i][n - 1 - j];
            }
        }
        return flipped;
    }

    private String getBoardKey(char[][] board) {
        StringBuilder key = new StringBuilder();
        for (char[] row : board) {
            for (char cell : row) {
                key.append(cell == ' ' ? '-' : cell);
            }
        }
        return key.toString();
    }

    private Map<String, List<BoardState>> loadMemory() {
        File file = new File(MEMORY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, List<BoardState>> map = (Map<String, List<BoardState>>) obj;
                    return map;
                }
                System.err.println("Arquivo de memória corrompido. Criando nova memória.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar memória: " + e.getMessage());
            }
        }
        return new HashMap<>();
    }

    private void saveMemory() {
        File dir = new File(MEMORY_FILE).getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Não foi possível criar o diretório para salvar a memória.");
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMORY_FILE))) {
            oos.writeObject(losingMoves);
        } catch (IOException e) {
            System.err.println("Erro ao salvar memória: " + e.getMessage());
        }
    }

    /**
         * Classe interna que representa um estado do tabuleiro e uma jogada específica.
         */
        private record BoardState(char[][] board, int row, int col) implements Serializable {
            @Serial
            private static final long serialVersionUID = 1L;

        private BoardState(char[][] board, int row, int col) {
                this.board = new char[3][3];
                for (int i = 0; i < 3; i++) {
                    System.arraycopy(board[i], 0, this.board[i], 0, 3);
                }
                this.row = row;
                this.col = col;
            }

            boolean isEquivalentMove(char[][] currentBoard, int moveRow, int moveCol) {
                // Verifica se a posição é a mesma
                if (row != moveRow || col != moveCol) {
                    return false;
                }

                // Conta o número total de peças em cada tabuleiro
                int totalPieces = 0;
                int totalCurrentPieces = 0;
                int matchingPositions = 0;

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] != ' ')
                            totalPieces++;
                        if (currentBoard[i][j] != ' ')
                            totalCurrentPieces++;
                        if (board[i][j] == currentBoard[i][j])
                            matchingPositions++;
                    }
                }

                // Verifica se o número de peças é similar e se a maioria das posições
                // corresponde
                return totalPieces == totalCurrentPieces && matchingPositions >= 7;
            }
        }
}