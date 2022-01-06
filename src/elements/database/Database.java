package elements.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import main.GameEngine;

/**
 *
 * @author aldokimi
 */
public class Database {
    
    int maxScores;
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;
    Connection connection;

    /**
     *
     * @param maxScores
     * @throws SQLException
     * Initialize a Database object from a maximum score that can be recorded.
     */
    public Database(int maxScores) throws SQLException {
        this.maxScores = maxScores;
        String dbURL = "jdbc:derby://localhost:1527/highScores;";
        connection = DriverManager.getConnection(dbURL);
        String insertQuery = "INSERT INTO HIGHSCORES (TYPESTAMP, NAME, LEVEL, SCORE) VALUES (?, ?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        String deleteQuery = "DELETE FROM HIGHSCORES WHERE SCORE=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    /**
     *
     * @return highScores
     * @throws SQLException
     * Get all scored HighScore's.
     */
    public ArrayList<HighScore> getHighScores() throws SQLException {
        String query = "SELECT * FROM HIGHSCORES";
        ArrayList<HighScore> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            int level = results.getInt("LEVEL");
            highScores.add(new HighScore(name, score, level));
        }
        sortHighScores(highScores);
        return highScores;
    }

    /**
     *
     * @param name
     * @param score
     * @throws SQLException
     * 
     * Put a new HighScore into the database.
     */
    public void putHighScore(String name, int score) throws SQLException {
        ArrayList<HighScore> highScores = getHighScores();
        if (highScores.size() < maxScores) {
            insertScore(name, score, GameEngine.level);
        } else {
            int leastScore = highScores.get(highScores.size() - 1).getScore();
            if (leastScore < score) {
                deleteScores(leastScore);
                insertScore(name, score, GameEngine.level);
            }
        }
    }

    /**
     * Sort the high scores in descending order.
     *
     * @param highScores
     */
    private void sortHighScores(ArrayList<HighScore> highScores) {
        Collections.sort(highScores, (HighScore t, HighScore t1) -> t1.getScore() - t.getScore());
    }

    
    /**
     * Inserts a new highScore into the database.
     */
    private void insertScore(String name, int score, int level) throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        insertStatement.setTimestamp(1, ts);
        insertStatement.setString(2, name);
        insertStatement.setInt(3, level);
        insertStatement.setInt(4, score);
        insertStatement.executeUpdate();
    }

    /**
     * Deletes all the highScores with score.
     *
     * @param score
     */
    private void deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        deleteStatement.executeUpdate();
    }
    
    /**
     * @return a String array with the names of column headers.
     */
    public String[] getColumnNamesArray (){
        String[] columnNames = {"#", "Name", "Level", "Score"};
        return columnNames;
    }
    
    /**
     * @return a String matrix with all the data in the database.
     * @throws java.sql.SQLException
     */
    public String[][] getDataMatrix () throws SQLException{
        String[][] columnNames = new String[10][4];
        ArrayList<HighScore> highscores = getHighScores();
        int cnt = 0;
        for(HighScore hs : highscores){
            if(cnt == 10) break;
            columnNames[cnt][0] = String.valueOf(cnt+1);
            columnNames[cnt][1] = hs.getName();
            columnNames[cnt][2] = "" + hs.getLevel();
            columnNames[cnt][3] = "" + hs.getScore();
            
            cnt++;
        }
        while(cnt < 10){
            if(cnt == 11) break;
            columnNames[cnt][0] = String.valueOf(cnt+1);
            columnNames[cnt][1] = "";
            columnNames[cnt][2] = "";
            columnNames[cnt][3] = "";
            cnt++;
        }
        return columnNames;
    }
}
