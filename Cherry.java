import java.util.Random;
public class Cherry extends GameObject {
    private Random rgen = new Random();
    
    public Cherry() {
        super(0, 0);
    }
    
    public void update() {
        setX(1 + rgen.nextInt(SnakeGame.GRID_SIZE - 2));
        setY(1 + rgen.nextInt(SnakeGame.GRID_SIZE - 2));
    }
}
