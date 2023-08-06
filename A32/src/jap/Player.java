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
    private String playerName;

    private final boolean actor;
    private int maxScore;

    private int gameLength;
    private List<Boat> boats;


    /**
     * Method Name:
     * Purpose: Constructor for Player
     * Algorithm: Initialize variables
     *
     * @param name - Future implementation if User types name
     * @param actor - Boolean true = user, false = machine
     */
    public Player(String name, Boolean actor){
        this.playerName = name;
        this.actor = actor;
        // Initialize max score to 0
        this.maxScore = 0;
        boats = new ArrayList<>();
    }

    /**
     * Method Name: setPlayerName
     * Purpose: setter method for player name
     * Algorithm: this variable = argument
     *
     * @param name - Player name
     */
    protected void setPlayerName(String name){
        this.playerName = name;
    }

    /**
     * Method Name: getPlayerName
     * Purpose: Getter method
     * Algorithm: Return player name
     *
     * @return - String for player name
     */
    protected String getPlayerName(){
        return playerName;
    }

    /**
     * Method Name: setMaxScore
     * Purpose: Setter method
     * Algorithm: Set maxscore to passed argument
     *
     * @param score - points accumulated during game
     */
    protected void setMaxScore(Integer score){
        this.maxScore = score;
    }

    /**
     * Method Name: getPoints
     * Purpose: Getter method for points
     * Algorithm: Return points
     *
     * @return - Score player accumulated during game
     */
    protected Integer getPoints(){
        return maxScore;
    }

    /**
     * Method Name: setGameLength
     * Purpose: Setter method for time
     * Algorithm: global variable = argument passed
     *
     * @param time - Game length
     */
    protected void setGameLength(Integer time){
        this.gameLength = time;
    }

    /**
     * Method Name: getTime
     * Purpose: Getter method for time
     * Algorithm: Return variable
     *
     * @return - time
     */
    protected Integer getTime(){
        return gameLength;
    }

    /**
     * Method Name: getActor
     * Purpose: Getter method
     * Algorithm: return variable
     *
     * @return Boolean actor
     */
    protected boolean getActor(){
        return actor;
    }

    /**
     * Method Name: addBoat
     * Purpose: Add instance of Boat object to Players boat List.
     * Algorithm: add Boat instance to List
     *
     * @param boatInstance  boat Object
     */
    protected void addBoat(Boat boatInstance){
        boats.add(boatInstance);
    }

}
