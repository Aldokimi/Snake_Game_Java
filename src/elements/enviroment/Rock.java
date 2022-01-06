package elements.enviroment;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;

import elements.entity.Snake;
import helpers.CONSTANTS;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author aldokimi
 */
public class Rock {
	
    private final Random random;
    private final ArrayList<Point> rocks = new ArrayList<>();

    /**
     *
     */
    public Rock() {
        this.random = new Random();
    }
	
    /**
     * @param i
     * @return the position.
     * Get the ith position from the rocks array.
     */
    public Point getPosition(int i) {
        return this.rocks.get(i);
    }
    
    public ArrayList<Point> getRocks(){
        return this.rocks;
    }
    
    public void addNewRock(){
        Point newPoint = new Point(
                        random.nextInt((int)(CONSTANTS.SCREAN_WIDTH/CONSTANTS.UNIT_SIZE)) * CONSTANTS.UNIT_SIZE,
                        random.nextInt((int)(CONSTANTS.SCREAN_HEIGHT/CONSTANTS.UNIT_SIZE))* CONSTANTS.UNIT_SIZE
                        );
        while(!takenPosition(newPoint)){
            newPoint = new Point(
                        random.nextInt((int)(CONSTANTS.SCREAN_WIDTH/CONSTANTS.UNIT_SIZE)) * CONSTANTS.UNIT_SIZE,
                        random.nextInt((int)(CONSTANTS.SCREAN_HEIGHT/CONSTANTS.UNIT_SIZE))* CONSTANTS.UNIT_SIZE
                        );
        }
        this.rocks.add(newPoint);
    }
    
    /**
     * @param p
     * @return weather this rock location already exists or not.
     */
    public boolean takenPosition(Point p){
        return this.rocks.stream().noneMatch(pp -> (pp.equals(p)));
    }
	
    /**
     *
     * @param s
     * @return if the snake collides with any of the rocks.
     */
    public boolean checkCollistion(Snake s) {
        for(int i = 0 ; i < this.getRocks().size(); i++){
            Rectangle snake = new Rectangle(s.getBody().get(0).x,
                                            s.getBody().get(0).y, 
                                            CONSTANTS.UNIT_SIZE, 
                                            CONSTANTS.UNIT_SIZE);
            Rectangle rock = new Rectangle(this.getPosition(i).x, this.getPosition(i).y, CONSTANTS.UNIT_SIZE, CONSTANTS.UNIT_SIZE);
            Area areaA = new Area(snake);
            areaA.intersect(new Area(rock));
            if(!areaA.isEmpty()) return true;
        }
        return false;
    }
}
