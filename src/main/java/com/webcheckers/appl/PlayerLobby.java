package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A collection of signed-in WebCheckers players
 *
 * @author <a href='mailto:pjw7904@rit.edu'>Peter Willis</a>
 */
public class PlayerLobby {
    // Attributes
    private static HashMap<String, Player> playersMap;
    private static ArrayList<Player> players;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());


    /**
     * Constructor for class PlayerLobby
     * Create a lobby to hold all the logged-in players
     */
    public PlayerLobby() {
        players = new ArrayList<Player>();
        playersMap = new HashMap<>();
    }

    /**
     * The players are added with the username provided by them.
     * @param newPlayer
     * @return - true if the username is valid and the player was added to the lobby
     */
    public synchronized void addPlayerToLobby(Player newPlayer) {
        players.add(newPlayer);
        playersMap.put(newPlayer.getName(), newPlayer);
        LOG.config(String.format("Player \'%s\' added to the lobby", newPlayer.getName()));
    }

    /**
     * A given player is removed
     * @param playerToRemove
     */
    public synchronized void removePlayerFromLobby(Player playerToRemove) {
        players.remove(playerToRemove);
        playersMap.remove(playerToRemove.getName());
        LOG.config(String.format("Player \'%s\' removed from the lobby", playerToRemove.getName()));
    }

    /**
     * Get a player based on their name.
     * @param name
     * @return
     */
    public synchronized Player getPlayer(String name){
        return playersMap.get(name);
    }

    /**
     * Ge the list of the Players.
     * @return the list of players
     */
    public synchronized ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * The number of Active Players.
     * @return the no of logged in players.
     */
    public synchronized int getNumberOfActivePlayers() {
        return players.size();
    }

    /**
     * Method to check if the player is already signed in or not.
     * @param player
     * @return - true: if the player is already in the list.
     *         - false: if the player is not in the list.
     */
    public synchronized boolean isSignedIn(Player player) {
        for (Player otherPlayer: players) {
            if (player.equals(otherPlayer)) return true;
        }
        return false;
    }

    /**
     * Method to check if the player name is Taken or not.
     * @param potentialPlayerName
     * @return true: if the player name is taken or not.
     */
    public synchronized boolean nameIsTaken(String potentialPlayerName) {
        for(Player player : players) {
            if(player.getName().equalsIgnoreCase(potentialPlayerName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if the name is valid alphanumerically.
     * @param potentialPlayerName
     * @return true: if the name is not atleast 1 character or includes special characters.
     */
    public boolean nameIsInvalid(String potentialPlayerName) {
        String modifiedName = potentialPlayerName.replaceAll("\\s", "");
        if(modifiedName.length() == 0) {
            return true;
        }
        else {
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            return p.matcher(modifiedName).find();
        }
    }
}