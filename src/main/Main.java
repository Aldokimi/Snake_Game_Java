package main;

import java.sql.SQLException;

import gameStates.GameStart;

/**
 *
 * @author mnd77
 */
public class Main {
    public static GameStart gameStart;

    /**
     *
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
		gameStart = new GameStart();
    }
}
