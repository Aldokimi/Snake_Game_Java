package gameStates;

import static gameStates.GameStart.database;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import main.GameEngine;

/**
 *
 * @author aldokimi
 */
@SuppressWarnings("serial")
public class Game extends JFrame {

    private final JMenuBar menuBar;
    private final JMenu menu;
    private GameEngine gameEngine;
    private JFrame highscoresFrame;
    
    /**
     *
     * @param level
     * @throws SQLException
     */
    public Game(int level) throws SQLException {
        gameEngine = new GameEngine(level);
        this.add(gameEngine);
        
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menu = new JMenu("Menu");
        
        // Show high scores
        JMenuItem highscores = new JMenuItem("Highscores");
        highscores.addActionListener((ActionEvent e) -> {
            try {
                highscoresFrame = new JFrame("Highscores");
                JTable table = new JTable(database.getDataMatrix(),database.getColumnNamesArray());
                table.setEnabled(false);
                table.setRowHeight(50);
                JScrollPane sp = new JScrollPane(table);
                TableColumnModel columnModel = table.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(50);
                columnModel.getColumn(1).setPreferredWidth(230);
                columnModel.getColumn(2).setPreferredWidth(120);
                columnModel.getColumn(3).setPreferredWidth(200);
                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
                cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
                columnModel.getColumn(0).setCellRenderer(cellRenderer);
                columnModel.getColumn(1).setCellRenderer(cellRenderer);
                columnModel.getColumn(2).setCellRenderer(cellRenderer);
                columnModel.getColumn(3).setCellRenderer(cellRenderer);
                highscoresFrame.add(sp);
                highscoresFrame.setSize(new Dimension(600, 400));
                highscoresFrame.setVisible(true);
                highscoresFrame.setLocationRelativeTo(null);
            } catch (SQLException ex) {
                Logger.getLogger(GameStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menu.add(highscores);
        
        // Paues the game
        JMenuItem pause = new JMenuItem("Pause");
        pause.addActionListener((ActionEvent e) -> {
            gameEngine.pause();
        });
        menu.add(pause);
        
        // Restart the game
        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener((ActionEvent e) -> {
            GameStart.game.dispose();
            try {
                GameStart.game  = new Game(level);
            } catch (SQLException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menu.add(restart);
        
        // Exit the game
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        menu.add(exit);
        menuBar.add(menu);
        
        
        this.setTitle("Snake game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
