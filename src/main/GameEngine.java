package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import helpers.*;
import elements.entity.Snake;
import elements.enviroment.Food;
import elements.enviroment.Rock;
import gameStates.GameOver;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aldokimi
 */
public class GameEngine extends JPanel implements ActionListener{

    private boolean 	    running = false;
    private Timer 	    timer;
    private Random 	    random;
    private Food 	    food;
    private Rock 	    rock;
    private Snake 	    snake;
    private String 	    direction;
    private BufferedImage   snakeHead = null;
    private boolean         pause;
    public static int 	    level;
    private int             deley;
    private BufferedImage snakeHeadImage ;
    /**
     *
     * @param level
     * @throws SQLException
     */
    public GameEngine(int level) throws SQLException {
        GameEngine.level = level;
        this.setPreferredSize(new Dimension(CONSTANTS.SCREAN_WIDTH, CONSTANTS.SCREAN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapterClass());
        
        startGame();
    }

    /**
     *
     * Starts a new game.
     */
    private void startGame() throws SQLException {
        this.random = new Random();
        this.running = true;
        this.pause  = false;
        this.rock = new Rock();
        setDirection();
        newSnake();
        setLevel();
        newFood();
        this.timer = new Timer(this.deley , this);
        this.timer.start();
    }	

    /**
     *
     * @see direction 
     * Sets a direction and a head image for the snake.
     */
    private void setDirection() {
        try {
            snakeHeadImage = ImageIO.read(new File("res/snake.png"));
        } catch (IOException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        int randDirection = random.nextInt(3) + 1;
        snakeHead = snakeHeadImage;
        switch (randDirection) {
        case 1 -> {
            this.direction = "UP";
            snakeHead = rotateImage(snakeHead, 90);
        }
        case 2 -> {
            this.direction = "DOWN";
            snakeHead = rotateImage(snakeHead, 270);
        }
        case 3 -> {
            this.direction = "LEFT";
            snakeHead = rotateImage(snakeHead, 0);
        }
        case 4 -> {
            this.direction = "RIGHT";
            snakeHead = rotateImage(snakeHead, 180);
        }
        default -> {}
        }
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try { draw(g); } catch (SQLException e) { }
    }

    /**
     * @throws SQLException
     * Draws all the game components.
     */
    private void draw(Graphics g) throws SQLException {
        if(running) {
            for (int i = 0; i < CONSTANTS.SCREAN_HEIGHT/CONSTANTS.UNIT_SIZE; i++) {
                g.drawLine(i* CONSTANTS.UNIT_SIZE , 0, i* CONSTANTS.UNIT_SIZE ,  CONSTANTS.SCREAN_HEIGHT );
                g.drawLine(0 , i* CONSTANTS.UNIT_SIZE, CONSTANTS.SCREAN_WIDTH, i* CONSTANTS.UNIT_SIZE );
            }
            // Draw food
            try {
                g.drawImage(ImageIO.read(new File("res/apple.png")), food.getPosition().x, food.getPosition().y,
                        CONSTANTS.UNIT_SIZE, CONSTANTS.UNIT_SIZE, null);
            } catch (IOException ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Draw rocks
            for(int i = 0 ; i < rock.getRocks().size(); i++){
                try {
                    g.drawImage(ImageIO.read(new File("res/rock.png")), rock.getPosition(i).x, rock.getPosition(i).y,
                            CONSTANTS.UNIT_SIZE, CONSTANTS.UNIT_SIZE, null);
                } catch (IOException ex) {
                    Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            // Draw snake
            for (int i = 0; i < snake.getBody().size(); i++) {
                if(i == 0) {
                    g.drawImage(snakeHead, snake.getBody().get(i).x, snake.getBody().get(i).y, 
                                    CONSTANTS.UNIT_SIZE, CONSTANTS.UNIT_SIZE, null);
                }else {
                    g.setColor(Color.green);
                    g.fillOval(snake.getBody().get(i).x, snake.getBody().get(i).y, 
                                    CONSTANTS.UNIT_SIZE, CONSTANTS.UNIT_SIZE);
                }
            }
        }else {
            var gameOver = new GameOver(this);
        }
    }

    /**
     * @return the snake
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Creates a new food.
     */
    private void newFood() {
        Point newPoint = new Point(
                        random.nextInt((int)(CONSTANTS.SCREAN_WIDTH/CONSTANTS.UNIT_SIZE)) * CONSTANTS.UNIT_SIZE,
                        random.nextInt((int)(CONSTANTS.SCREAN_HEIGHT/CONSTANTS.UNIT_SIZE))* CONSTANTS.UNIT_SIZE
                        );
        while(!this.rock.takenPosition(newPoint)){
            newPoint = new Point(
                        random.nextInt((int)(CONSTANTS.SCREAN_WIDTH/CONSTANTS.UNIT_SIZE)) * CONSTANTS.UNIT_SIZE,
                        random.nextInt((int)(CONSTANTS.SCREAN_HEIGHT/CONSTANTS.UNIT_SIZE))* CONSTANTS.UNIT_SIZE
                        );
        }
        food = new Food( (this.snake.getBody().size() - 2), newPoint);
    }

    /**
     * Creates a new snake.
     */
    private void newSnake() {
        snake = new Snake();
        int i  = CONSTANTS.SCREAN_HEIGHT/CONSTANTS.UNIT_SIZE / 2;
        switch (direction) {
        case "UP"    -> snake.add(new Point( i * CONSTANTS.UNIT_SIZE, (i - 1) *CONSTANTS.UNIT_SIZE ));
        case "DOWN"  -> snake.add(new Point( i * CONSTANTS.UNIT_SIZE, (i + 1) *CONSTANTS.UNIT_SIZE ));
        case "LEFT"  -> snake.add(new Point( (i - 1) * CONSTANTS.UNIT_SIZE, i *CONSTANTS.UNIT_SIZE ));
        case "RIGHT" -> snake.add(new Point( (i + 1) * CONSTANTS.UNIT_SIZE, i *CONSTANTS.UNIT_SIZE ));
        default -> {}
        }
    }

    /**
     * Moving the snake.
     */
    private void move() {
        for(int i = snake.getBody().size()-1; i>0 ; i--) 
                snake.getBody().get(i).setLocation(snake.getBody().get(i-1));
        snake.movePart(direction, 0); 
    }

    /**
     * @param bimg
     * @param angle
     * Rotating an image.
     */
    private BufferedImage rotateImage(BufferedImage bimg , int angle){
        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
               cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        int neww = (int) Math.floor(w*cos + h*sin),
            newh = (int) Math.floor(h*cos + w*sin);
        BufferedImage rotated = new BufferedImage(neww, newh, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.translate((neww-w)/2, (newh-h)/2);
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawRenderedImage(bimg, null);
        graphic.dispose();
        return rotated;
    }

    /**
     * Checks collision with food.
     */
    private void checkFood() {
        if(food.checkCollistion(snake)) {
            food.getEaten();
            snake.add(new Point(snake.getBody().get(snake.getBody().size()-1).x + CONSTANTS.GAME_UNIT, 
                                        snake.getBody().get(snake.getBody().size()-1).y + CONSTANTS.GAME_UNIT ));
            newFood();
        }
    }
    
    /**
     * Checks collision with walls and rock.
     */
    private void checkCollisions() {
        // Collides with its body 
        for (int i = 1; i < snake.getBody().size(); i++) 
            if(snake.getBody().get(0).equals(snake.getBody().get(i))) 
                    running = false;

        // Collides with left boarder 
        if(snake.getBody().get(0).x < 0) running = false;

        // Collides with right boarder 
        if(snake.getBody().get(0).x > CONSTANTS.SCREAN_WIDTH - CONSTANTS.UNIT_SIZE) running = false;

        // Collides with top boarder 
        if(snake.getBody().get(0).y < 0) running = false;

        // Collides with bottom boarder 
        if(snake.getBody().get(0).y > CONSTANTS.SCREAN_HEIGHT - CONSTANTS.UNIT_SIZE) running = false;

        // Collides with rock
        if(rock.checkCollistion(snake)) running = false;

        if(!running) timer.stop();
    }
    
    /**
     * Sets the game level.
     */

    private void setLevel() {
        for(int i = 1 ; i <= 10 ; i++){
            if(this.level == i){
                this.deley = CONSTANTS.DELAY - (30 * i);
                for(int j = 0 ; j < 2 * i ; j++){
                    this.rock.addNewRock();
                }
            }
        }
    }

    /**
     * Pauses the game.
     */
    public void pause() {
        this.pause = true;
    }
    
    /**
     * Restarts the game.
     */
    public void restart() throws SQLException {
        this.startGame();
    }

 
    private class myKeyAdapterClass extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                if(!"UP".equals(direction)) {
                    direction = "DOWN";
                    snakeHead = rotateImage(snakeHeadImage, 270);
                }
            }
            case KeyEvent.VK_S-> {
                if(!"DOWN".equals(direction)) {
                    direction = "UP";
                    snakeHead = rotateImage(snakeHeadImage, 90);
                }
            }
            case KeyEvent.VK_A-> {
                if(!"LEFT".equals(direction)) {
                    direction = "RIGHT";
                    snakeHead = rotateImage(snakeHeadImage, 180);
                }
            }
            case KeyEvent.VK_D-> {
                if(!"RIGHT".equals(direction)) {
                    direction = "LEFT";
                    snakeHead = rotateImage(snakeHeadImage, 0);
                }
            }
            case KeyEvent.VK_SPACE -> pause = !pause;
            default -> {}
            }
        }
    }

    /**
     *
     * @param e
     * Repainting all components of the game.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!this.pause) {
            if(this.running) {
                move();
                checkFood();
                checkCollisions();
            }
        }
        repaint();
    }
}
