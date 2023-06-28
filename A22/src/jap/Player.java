package jap;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int health;
    private boolean actor;
    private int maxScore;
    private Match[] matches;

    private List<Boat> boats;

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

}
