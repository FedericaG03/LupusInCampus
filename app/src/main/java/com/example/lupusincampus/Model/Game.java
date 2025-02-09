package com.example.lupusincampus.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Game {
    private int id;
    private int creatorId;
    private LocalDateTime localDateTime;
    private List<Player> participants;
    private int winningPlayerId;


    public Game(int id, int creatorId, LocalDateTime localDateTime, List<Player> participants, int winningPlayerId) {
        this.id = id;
        this.creatorId = creatorId;
        this.localDateTime = localDateTime;
        this.participants = participants;
        this.winningPlayerId = winningPlayerId;
    }

    public List<Player> getPartecipants() {
        return participants;
    }

    public void setPartecipants(List<Player> partecipants) {
        this.participants = partecipants;
    }

    // Costruttore vuoto (necessario per ObjectMapper)
    public Game() {
    }

    // Costruttore completo
    public Game(int id, int creatorId, LocalDateTime gameDate, int winningPlayerId, List<Player> participants) {
        this.id = id;
        this.creatorId = creatorId;
        this.localDateTime = gameDate;
        this.winningPlayerId = winningPlayerId;
        this.participants = participants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getWinningPlayerId() {
        return winningPlayerId;
    }

    public void setWinningPlayerId(int winningPlayerId) {
        this.winningPlayerId = winningPlayerId;
    }

}
