package jap;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: Player
 * Method List:
 * Constants List:
 * Player Object containing Player information
 *
 * @author Andrew Lorimer, Solomon Thangthong
 * @version 1.0
 * @see JButton
 * @since 11.0.19
 */
public class Player {
    private final String playerName;
    private int health;
    private boolean actor;
    private int maxScore;
    private Match[] matches;

    private List<Boat> boats;


    /**
     * Method Name:
     * Purpose: Constructor for Player
     * Algorithm:
     *
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

    /**
     * Method Name:
     * Purpose:
     * Algorithm:
     *
     * @return
     */
    protected boolean getActor(){
        return actor;
    }

    /**
     * Method Name:
     * Purpose: Add instance of Boat object to Players boat List.
     * Algorithm:
     *
     * @param boatInstance
     */
    protected void addBoat(Boat boatInstance){
        boats.add(boatInstance);
    }

}
