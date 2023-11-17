package com.webcheckers.application;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerLobbyTest {

    private String playerName;
    private Player player;
    private PlayerLobby CuT;

    @BeforeEach
    void setup() {
        playerName = "John";
        player = new Player(playerName);
        CuT = new PlayerLobby();
    }

    @Test
    void initialListOfPlayers() {
        List players = CuT.getPlayers();
        assertNotNull(players);
        assertEquals(0,players.size());
    }

    @Test
    void addPlayerToLobby() {
        CuT.addPlayerToLobby(player);
        assertEquals(1 , CuT.getPlayers().size());
    }

    @Test
    void removePlayerFromLobby() {
        CuT.addPlayerToLobby(player);
        assertEquals(1 , CuT.getPlayers().size());
        CuT.removePlayerFromLobby(player);
        assertEquals(0 , CuT.getPlayers().size());
    }

    @Test
    void getPlayer() {
        CuT.addPlayerToLobby(player);
        assertEquals("John", CuT.getPlayer(playerName).getName());
    }

    @Test
    void getPlayers() {
        assertEquals("ArrayList", CuT.getPlayers().getClass().getSimpleName());
    }

    @Test
    void getNumberOfActivePlayers() {
        CuT.addPlayerToLobby(player);
        assertEquals(1, CuT.getNumberOfActivePlayers());
    }

    @Test
    void isSignedIn() {
        Player player2 = new Player("Mark$%");
        CuT.addPlayerToLobby(player);
        assertTrue(CuT.isSignedIn(player));
        assertFalse(CuT.isSignedIn(player2));
    }

    @Test
    void nameIsTaken() {
        CuT.addPlayerToLobby(player);
        Player player2 = new Player("Mark");

        assertTrue(CuT.nameIsTaken(player.getName()));
        assertFalse(CuT.nameIsTaken(player2.getName()));
    }

    @Test
    void nameIsInvalid() {
        Player player2 = new Player("Mark$%");
        Player player3 = new Player("");

        assertTrue(CuT.nameIsInvalid(player2.getName()));
        assertTrue(CuT.nameIsInvalid(player3.getName()));
        assertFalse(CuT.nameIsInvalid(player.getName()));
    }
}