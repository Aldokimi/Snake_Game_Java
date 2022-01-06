package elements.database;

/**
 *
 * @author aldokimi
 */
public class HighScore{
    private final String name;
    private final int score;
    private final int level;

    /**
     *
     * @param name
     * @param score
     * @param level
     * Initialize a HighScore object.
     */
    public HighScore(String name, int score, int level) {
        this.name = name;
        this.score = score;
        this.level = level;
    }

    /**
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return score.
     */
    public int getScore() {
        return score;
    }
    
    /**
     *
     * @return score.
     */
    public int getLevel() {
        return level;
    }
    
    /**
     *
     * @return string format for the HighScore.
     */
    @Override
    public String toString() {
        return "HighScore{" + "name=" + name + ", score=" + score + '}';
    }
}
