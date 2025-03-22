package com.allan.velha.domain.service;

import com.allan.velha.domain.model.Game;

public interface NeuralNetworkService {
    int[] predictNextMove(Game game);
}