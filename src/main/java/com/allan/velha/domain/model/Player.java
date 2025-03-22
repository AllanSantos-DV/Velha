package com.allan.velha.domain.model;

public record Player(String name) {

    public void incrementWins() {
    }

    public void incrementLosses() {
    }

    public void incrementDraws() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Player player = (Player) obj;
        return name.equals(player.name);
    }

}