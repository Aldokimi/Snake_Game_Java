package elements.entity;

import java.awt.Point;
import java.util.ArrayList;

import helpers.CONSTANTS;

/**
 *
 * @author aldokimi
 */
public class Snake {

	
    private final ArrayList<Point> body = new ArrayList<>();
    
    /**
     * @return the body.
     */
    public ArrayList<Point> getBody() {
            return body;
    }
    /**
     * Creates a new Snake object with the head position of the snake!
     */
    public Snake() {
        this.body.add(new Point((CONSTANTS.SCREAN_WIDTH/CONSTANTS.UNIT_SIZE)/2 * CONSTANTS.UNIT_SIZE,
                        (CONSTANTS.SCREAN_HEIGHT/CONSTANTS.UNIT_SIZE)/2 * CONSTANTS.UNIT_SIZE));
    }
	
    /**
     * @param newPart 
     * 
     * add a new unit to the snake.
     */
    public void add(Point newPart) {
        this.body.add(newPart);
    }

    /**
     * @param direction
     * @param  part
     * 
     * moves a specific unit of the snake to a specific direction.
     */
    public void movePart(String direction, int part) {
        Point bodyPart = this.body.get(part);
        if(direction.equals("UP")) {
                this.body.get(part).setLocation(bodyPart.x , bodyPart.y + CONSTANTS.SPEED);
        }
        if(direction.equals("DOWN")) {
                this.body.get(part).setLocation(bodyPart.x , bodyPart.y - CONSTANTS.SPEED);
        }
        if(direction.equals("LEFT")) {
                this.body.get(part).setLocation(bodyPart.x + CONSTANTS.SPEED , bodyPart.y );
        }
        if(direction.equals("RIGHT")) {
                this.body.get(part).setLocation(bodyPart.x - CONSTANTS.SPEED , bodyPart.y );
        }
    }

    /**
     *
     * @return a string format for the snake body.
     */
    @Override
    public String toString() {
        return "Snake [body=" + body + "]";
    }
}

