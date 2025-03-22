package com.allan.velha.presentation.model;

/**
 * Classe que representa a pontuação de um jogador.
 */
public class Score {
    private final String player;
    private final int wins;

    /**
     * Cria uma nova pontuação.
     * 
     * @param player Nome do jogador
     * @param wins   Número de vitórias
     */
    public Score(String player, int wins) {
        this.player = player;
        this.wins = wins;
    }

    /**
     * @return Nome do jogador
     */
    public String getPlayer() {
        return player;
    }

    /**
     * @return Número de vitórias
     */
    public int getWins() {
        return wins;
    }
}