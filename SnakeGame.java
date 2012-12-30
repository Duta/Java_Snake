import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;
import javax.swing.*;
public class SnakeGame extends Applet implements KeyListener, ActionListener {
    private Font newFont = new Font("sansserif", Font.BOLD, 20);
    private Graphics bufferGraphics;
    private Image offscreen;
    public static final int
        WIDTH = 800,
        HEIGHT = 800,
        BORDER = 10,
        GRID_SIZE = 50,
        CELL_SIZE = 10;
    
    private boolean gameOver = false;
    private boolean keyPressedThisTick = false;
    private boolean wrapAround = true;
    private Snake snake = new Snake();
    private int frameCounter = 0;
    private int score = 0;
    private Cherry cherry = new Cherry();
    private Cherry blueCherry = new Cherry();
    private int blueCherryCountdown = 0;
    private int blueCherryCooldown = 500;
    private boolean blueCherryOn = false;
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
    private Timer time = new Timer(15, this);
    private Random rgen = new Random();
    
    public void init() {
        setSize(WIDTH, HEIGHT);
        
        gameOver = false;
        grid[snake.getY()][snake.getX()] = snake.getLength();
        cherry.update();
        
        addKeyListener(this);
        setBackground(Color.black);
        
        offscreen = createImage(WIDTH, HEIGHT);
        bufferGraphics = offscreen.getGraphics();
        
        time.start();
    }
   
    public void reset() {
        snake.reset();
        frameCounter = 0;
        score = 0;
        gameOver = false;
        wrapAround = true;
        keyPressedThisTick = false;
        blueCherryOn = false;
        blueCherryCooldown = 500;
        blueCherryCountdown = 0;
        cherry.update();
        
        for(int i = 0; i < GRID_SIZE; i++)
        for(int j = 0; j < GRID_SIZE; j++) {
            grid[i][j] = 0;
        }
        
        grid[snake.getY()][snake.getX()] = snake.getLength();
        
        time.restart();
    }
   
    public void actionPerformed(ActionEvent event) {
        if(gameOver) {
            time.stop();
            repaint();
        } else {
            frameCounter += 10;
       
            if (frameCounter >= snake.getSpeed()) {
                checkCollision();
                
                snake.update();
               
                if (snake.getX() == 0) {
                    if(!wrapAround) {
                        gameOver = true;
                    }
                    snake.setX(GRID_SIZE - 2);
                } else if (snake.getX() == (GRID_SIZE - 1)) {
                    if(!wrapAround) {
                        gameOver = true;
                    }
                    snake.setX(1);
                }
                   
                if (snake.getY() == 0) {
                    if(!wrapAround) {
                        gameOver = true;
                    }
                    snake.setY(GRID_SIZE - 2);
                } else if (snake.getY() == (GRID_SIZE - 1)) {
                    if(!wrapAround) {
                        gameOver = true;
                    }
                    snake.setY(1);
                }
           
                for(int i = 0; i < GRID_SIZE; i++)
                for(int j = 0; j < GRID_SIZE; j++) {
                    if (grid[i][j] > 0) {
                        grid[i][j]--;
                    }
                }
                
                grid[snake.getX()][snake.getY()] = snake.getLength();
           
                frameCounter = 0;
                if(blueCherryCountdown > 0) {
                    blueCherryCountdown--;
                } else {
                    blueCherryOn = false;
                }
               
                if(blueCherryCooldown > 0) {
                    blueCherryCooldown--;
                }
               
                if(!blueCherryOn && blueCherryCooldown == 0) {
                    newBlueCherry();
                }
                keyPressedThisTick = false;
            }
            repaint();
        }
    }
   
    public void checkCollision() {
        int snakeNextX = snake.getNextX();
        int snakeNextY = snake.getNextY();
        
        // If the snake will collide with
        // itself, end the game.
        if(grid[snakeNextX][snakeNextY] > 0) {
            gameOver = true;
        }
        
        // If the snake will collide with
        // the cherry, lengthen the snake,
        // increment the score and move
        // the cherry to a new position.
        if(snakeNextX == cherry.getX() && snakeNextY == cherry.getY()) {
            snake.lengthen();
            score++;
            cherry.update();
        }
        
        // If the snake will collide with
        // the blue cherry, increase the
        // score by 10 and reset the blue
        // cherry countdown.
        if(snakeNextX == blueCherry.getX() && snakeNextY == blueCherry.getY()) {
            score += 10;
            blueCherryOn = false;
            blueCherryCountdown = 0;
        }
    }
   
         
    public void paint(Graphics g) {
        // Clear the image.
        bufferGraphics.clearRect(0,0,WIDTH,HEIGHT);
        
        // Draw the controls and score information.
        bufferGraphics.setColor(Color.white);
        String[] lines = {
            "Controls:",
            "ArrowKeys - Change Direction",
            "F - Faster",
            "S - Slower",
            "W - Toggle Wrap-Around",
            "L - Longer",
            "K - Shorter"};
        int x = BORDER + CELL_SIZE*GRID_SIZE + 20;
        int y = BORDER + CELL_SIZE + 10;
        bufferGraphics.drawString("Score: " + score, x, y);
        y += 24;
        for(int i = 0; i < lines.length; i++, y += 12) {
            bufferGraphics.drawString(lines[i], x, y);
        }
        
        // If wrap-around is enabled, draw the
        // border white, otherwise draw it red.
        if(wrapAround) {
            bufferGraphics.setColor(Color.white);
        } else {
            bufferGraphics.setColor(Color.red);
        }
        
        // Honestly, I don't know if offset and length
        // are the correct names to be using, but I'm
        // too tired to work out what they actually are.
        int offset = BORDER - 1 + CELL_SIZE;
        int length = GRID_SIZE*CELL_SIZE + BORDER + 1 - CELL_SIZE;
        bufferGraphics.drawLine(offset, offset, length, offset);
        bufferGraphics.drawLine(offset, offset, offset, length);
        bufferGraphics.drawLine(length, offset, length, length);
        bufferGraphics.drawLine(offset, length, length, length);
        
        // Draw the cherry.
        bufferGraphics.setColor(Color.white);
        bufferGraphics.fillRect(
            cherry.getX()*CELL_SIZE + BORDER,
            cherry.getY()*CELL_SIZE + BORDER,
            CELL_SIZE,
            CELL_SIZE);
        
        // If the blue cherry is available, draw it.
        if(blueCherryOn) {
            bufferGraphics.setColor(Color.blue);
            bufferGraphics.fillRect(
                blueCherry.getX()*CELL_SIZE + BORDER,
                blueCherry.getY()*CELL_SIZE + BORDER,
                CELL_SIZE,
                CELL_SIZE);
        }
        
        // If the game is over, tell the player.
        if(gameOver) {
            bufferGraphics.setColor(Color.red);
            bufferGraphics.drawString("GAME OVER.   Press R to reset", BORDER + CELL_SIZE*GRID_SIZE + 20, BORDER + CELL_SIZE + 22);
        } else {
            bufferGraphics.setColor(Color.white);
        }
        
        // Draw the snake, red if the game is
        // over, white otherwise.
        for(int i = 0; i < GRID_SIZE; i++)
        for(int j = 0; j < GRID_SIZE; j++) {
            if(grid[i][j] > 0) {
                bufferGraphics.fillRect(
                    i*CELL_SIZE + BORDER,
                    j*CELL_SIZE + BORDER,
                    CELL_SIZE,
                    CELL_SIZE);
            }
        }
        
        g.drawImage(offscreen, 0, 0, this);
        
        Toolkit.getDefaultToolkit().sync();
    }
   
    public void update(Graphics g) {
        paint(g);
    }
       
    public void keyPressed(KeyEvent e) {
        if(keyPressedThisTick) return;
        int keyCode = e.getKeyCode();
        if(gameOver) {
            if(keyCode == KeyEvent.VK_R) {
                reset();
            }
            return;
        }
        switch(keyCode) {
            case KeyEvent.VK_RIGHT:
                if(snake.getVSpeed() != 0) {
                    snake.setVSpeed(0);
                    snake.setHSpeed(1);
                    keyPressedThisTick = true;
                }
                break;
            case KeyEvent.VK_LEFT:
                if(snake.getVSpeed() != 0) {
                    snake.setVSpeed(0);
                    snake.setHSpeed(-1);
                    keyPressedThisTick = true;
                }
                break;
            case KeyEvent.VK_UP:
                if(snake.getHSpeed() != 0) {
                    snake.setHSpeed(0);
                    snake.setVSpeed(-1);
                    keyPressedThisTick = true;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(snake.getHSpeed() != 0) {
                    snake.setHSpeed(0);
                    snake.setVSpeed(1);
                    keyPressedThisTick = true;
                }
                break;
            case KeyEvent.VK_W:
                wrapAround = !wrapAround;
                break;
            case KeyEvent.VK_F:
                snake.setSpeed(snake.getSpeed() - 10);
                if(snake.getSpeed() < 10) {
                    snake.setSpeed(10);
                }
                break;
            case KeyEvent.VK_S:
                snake.setSpeed(snake.getSpeed() + 10);
                break;
            case KeyEvent.VK_L:
                snake.lengthen();
                break;
            case KeyEvent.VK_K:
                snake.shorten();
                break;
        }
    }
    
    public void keyTyped(KeyEvent e) {}
    
    public void keyReleased(KeyEvent e) {}
    
    public void newBlueCherry() {
        if(rgen.nextInt(100) == 0){
            blueCherryOn = true;
            blueCherry.update();
            blueCherryCountdown = 100;
            blueCherryCooldown = 500;
        }
    }
}