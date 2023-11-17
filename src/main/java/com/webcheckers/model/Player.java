package com.webcheckers.model;

import java.util.logging.Logger;

/**
 * A WebCheckers Player
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class Player {
    private static final Logger LOG = Logger.getLogger(Player.class.getName());

    // Attributes
    private final String name;
    private boolean inGame;

    /**
     * Constructor for the class Piece
     * @param name
     */
    public Player(String name) {
        this.name = name;
        inGame = false;
    }


    /**
     * Getter to get the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter to set whether the player is currently playing a game
     * @param status the current game status of a player
     */
    public void setInGameStatus(boolean status) {
        inGame = status;
    }

    /**
     * Getter to get the name
     * @return inGame true if the player is playing a game
     */
    public boolean isInGame() {
        return inGame;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Player)) return false;
        final Player that = (Player) obj;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
// End of Class
