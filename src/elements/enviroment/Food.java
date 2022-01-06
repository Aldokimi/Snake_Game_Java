package elements.enviroment;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;

import elements.entity.Snake;
import helpers.CONSTANTS;

/**
 *
 * @author aldokimi
 */
public class Food {
	
    private int foodEaten ;

    private final Point position;

    /**
     *
     * @param foodEaten
     * @param position
     * initilize the food object
     */
    public Food(int foodEaten, Point position) {
        this.foodEaten = foodEaten;
        this.position  = position;
    }
	
    /**
     * @return the foodEaten.
     */
    public int getFoodEaten() {
        return foodEaten;
    }


    /**
     * @return the position.
     */
    public Point getPosition() {
        return position;
    }
    public void getEaten() {
        this.foodEaten++;
    }
	
    /**
     *
     * @param s
     * @return collides 
     * checks if the snake collides with the food or not!
     */
    public boolean checkCollistion(Snake s) {
        Rectangle  snake = new Rectangle(s.getBody().get(0).x, s.getBody().get(0).y, CONSTANTS.UNIT_SIZE, CONSTANTS.UNIT_SIZE);
        Rectangle  food = new Rectangle(this.position.x, this.position.y, CONSTANTS.UNIT_SIZE, CONSTANTS.UNIT_SIZE);
        Area areaA = new Area(snake);
        areaA.intersect(new Area(food));
        boolean collides = !areaA.isEmpty();
        return collides;
    }
	
}
