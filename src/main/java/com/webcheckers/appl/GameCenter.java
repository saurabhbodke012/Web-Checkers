package com.webcheckers.appl;

import com.webcheckers.model.Game;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * GameCenter class stores a map of all games currently happening on the server
 */
public class GameCenter {
    // declaring the hashmap with key as String and value as Game
    private HashMap<String, Game> gameMap;
    // Another hashmap to connect gameID's with their associated Game
    private HashMap<String, Game> gameIDMap;
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    /**
     * A constructor for the game center
     */
    public GameCenter(){
        gameMap = new HashMap<>();
        gameIDMap = new HashMap<>();
    }

    /**
     * add a game to the game map
     * @param name the name of the player to be used as the key
     * @param game the game that is being added
     */
    public void addGame(String name, Game game){
        String gameID = game.getGameID();

        // First add the game and associate it with the player being added to the game
        gameMap.put(name, game);

        // Add the game to the ID map if it had not already (a player already joined the game first)
        if(getGameFromID(gameID) == null) {
            gameIDMap.put(gameID, game);
        }

        LOG.config(String.format("%s added to game %s.", name, gameID));
    }

    /**
     * remove a name,game pair from the map
     * @param name the name of the player that is the key to be removed
     */
    public void removeGameBoard(String name){
        gameMap.remove(name, getGame(name));
        LOG.config(String.format("%s removed from a game.", name));
    }

    /**
     * Get a game from the map.
     * @param name -  the name of the player in the game.
     * @return
     */
    public Game getGame(String name) {
        return gameMap.get(name);
    }

    public Game getGameFromID(String gameID) {
        return gameIDMap.get(gameID);
    }

    /**
     * Used for unit testing, to het the gameMap
     */
    public HashMap<String, Game> getGameMap() {return gameMap;}
}
// End of Class
