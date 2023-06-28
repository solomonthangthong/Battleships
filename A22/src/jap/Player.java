package jap;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int health;
    private boolean actor;
    private int maxScore;
    private Match[] matches;

    private List<Boat> boats;

    //TODO get player name as string and set it
    /**
     * Constructor for Player
     * @param name
     * @param actor
     */
    public Player(String name, Boolean actor){
        this.playerName = name;
        this.actor = actor;
        // Initialize max score to 0
        this.maxScore = 0;
        boats = new ArrayList<>();
    }

    protected boolean isActor(){
        return actor;
    }

    /**
     * Purpose: Add instance of Boat object to Players boat List.
     * @param boatInstance
     */
    protected void addBoat(Boat boatInstance){
        boats.add(boatInstance);
    }

    //TODO Add match list to hold # of matches if same player plays more than once
}
