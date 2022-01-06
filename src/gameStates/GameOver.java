package gameStates;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import main.GameEngine;
import main.Main;

/**
 *
 * @author aldokimi
 */
public class GameOver {

    /**
     *
     * @param game
     * @throws SQLException
     */
    public GameOver(GameEngine game) throws SQLException {
        String playerName  = JOptionPane.showInputDialog(game, "GAME OVER \n Enter your nake");
        int score = game.getSnake().getBody().size() - 2;
        if( playerName != null || !playerName.equals("")){
            GameStart.database.putHighScore(playerName,  score);
        }
        GameStart.game.dispose();
        Main.gameStart = new GameStart();
    }

}
