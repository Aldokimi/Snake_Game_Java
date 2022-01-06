package gameStates;

import elements.database.Database;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import helpers.CONSTANTS;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import main.Main;

/**
 *
 * @author aldokimi
 */
@SuppressWarnings("serial")
public class GameStart extends JFrame {

    public static Game game;
    public static Database database;
    private JFrame highscoresFrame;
    private int level;

    /**
     *
     * @throws SQLException
     * Initialize a new gameStart object with 3 buttons for starting the game, showing the highest scores, and exiting the program!
     */
    public GameStart() throws SQLException {
        super("Snake Game");
        database  = new Database(50);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(CONSTANTS.SCREAN_WIDTH, CONSTANTS.SCREAN_HEIGHT));
        JButton playButton = new JButton();
        playButton.setBounds(40,100,100,60);
        JButton scoresButton = new JButton();
        scoresButton.setBounds(80,200,200,100);
        JButton quitButton = new JButton();
        quitButton.setBounds(120,300,300,140);
        
        
        
        
        JLabel lbl = new JLabel("Select one of the possible Levels: ");
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lbl);

        String[] choices = { "LEVEL 1", "LEVEL 2", "LEVEL 3", "LEVEL 4",
                             "LEVEL 5", "LEVEL 6", "LEVEL 7", "LEVEL 8", "LEVEL 9", "LEVEL 10" };

        final JComboBox<String> cb = new JComboBox<>(choices);

        cb.setMaximumSize(cb.getPreferredSize()); // added code
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);// added code
        panel.add(cb);
        
        

        playButton.setText("PLAY");
        scoresButton.setText("Scores");
        quitButton.setText("Quit");

        
        // play button
        playButton.addActionListener((ActionEvent e) -> {
            try {
                Main.gameStart.dispose();
                String chosenLevel = cb.getSelectedItem().toString();
                if(!"LEVEL 10".equals(chosenLevel)){
                    level = Integer.parseInt(chosenLevel.substring(chosenLevel.length() - 1));
                }else{
                    level = Integer.parseInt(chosenLevel.substring(chosenLevel.length() - 2));
                }
                game = new Game(level);
            } catch (SQLException e1) {
            }
        });
        
        // show highscores button
        scoresButton.addActionListener((var e) -> {
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
        
        // exit button
        quitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        panel.add(playButton);
        panel.add(scoresButton);
        panel.add(quitButton);

        this.add(panel);

        this.setTitle("Snake game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
