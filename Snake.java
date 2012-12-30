public class Snake extends GameObject {
    private static final int INITIAL_LENGTH = 3;
    private static final int INITIAL_X = 25;
    private static final int INITIAL_Y = 25;
    private static final int INITIAL_SPEED = 50;
    private int length;
    private int vSpeed;
    private int hSpeed;
    private int speed;
    
    public Snake() {
        super(INITIAL_X, INITIAL_Y);
        this.length = INITIAL_LENGTH;
        this.vSpeed = 0;
        this.hSpeed = -1;
        this.speed = INITIAL_SPEED;
    }
    
    public void reset() {
        setX(INITIAL_X);
        setY(INITIAL_Y);
        setLength(INITIAL_LENGTH);
        setVSpeed(0);
        setHSpeed(-1);
        setSpeed(INITIAL_SPEED);
    }
    
    public void update() {
        setX(getNextX());
        setY(getNextY());
    }
    
    public void lengthen() {
        length++;
    }
    
    public void shorten() {
        length--;
    }
    
    public int getNextX() {
        return getX() + getHSpeed();
    }
    
    public int getNextY() {
        return getY() + getVSpeed();
    }
    
    public void setLength(int length) {
        this.length = length;
    }
    
    public void setVSpeed(int vSpeed) {
        this.vSpeed = vSpeed;
    }
    
    public void setHSpeed(int hSpeed) {
        this.hSpeed = hSpeed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public int getLength() {
        return length;
    }
    
    public int getVSpeed() {
        return vSpeed;
    }
    
    public int getHSpeed() {
        return hSpeed;
    }
    
    public int getSpeed() {
        return speed;
    }
}
