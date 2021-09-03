import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    private static final int DELAY = 75;
    private static final int[] x = new int[GamePanel.GAME_UNITS];
    private static final int[] y = new int[GamePanel.GAME_UNITS];

    private int bodyParts, applesEaten, appleX, appleY;
    private char direction;
    private boolean running;
    private Timer timer;
    private Random random;

    public GamePanel(){
        this.bodyParts = 6;
        this.random = new Random();
        this.direction = 'D';
        this.setPreferredSize(new Dimension(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        x[0] = 0;
        startGame();
    }

    public void startGame(){
        newApple();
        this.running = true;
        this.timer = new Timer(GamePanel.DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.draw(g);
    }

    public void draw(Graphics g){
        if(this.running) {
            for (int i = 0; i < GamePanel.SCREEN_HEIGHT / GamePanel.UNIT_SIZE; ++i) {
                g.drawLine(i * GamePanel.UNIT_SIZE, 0, i * GamePanel.UNIT_SIZE, GamePanel.SCREEN_HEIGHT);
                g.drawLine(0, i * GamePanel.UNIT_SIZE, GamePanel.SCREEN_WIDTH, i * GamePanel.UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, GamePanel.UNIT_SIZE, GamePanel.UNIT_SIZE);
            for (int i = 0; i < this.bodyParts; ++i) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], GamePanel.UNIT_SIZE, GamePanel.UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], GamePanel.UNIT_SIZE, GamePanel.UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + this.applesEaten, (GamePanel.SCREEN_WIDTH - metrics.stringWidth("Score: " + this.applesEaten)) / 2, g.getFont().getSize());
        } else
            gameOver(g);
    }

    public void newApple(){
        this.appleX = random.nextInt((int)(GamePanel.SCREEN_WIDTH / GamePanel.UNIT_SIZE)) * GamePanel.UNIT_SIZE;
        this.appleY = random.nextInt((int)(GamePanel.SCREEN_HEIGHT / GamePanel.UNIT_SIZE)) * GamePanel.UNIT_SIZE;
    }

    public void move(){
        for(int i = this.bodyParts ; i > 0 ; --i){
            GamePanel.x[i] = x[i - 1];
            GamePanel.y[i] =  y[i - 1];
        }

        switch (this.direction){
            case 'U': y[0] = y[0] - GamePanel.UNIT_SIZE; break;
            case 'D': y[0] = y[0] + GamePanel.UNIT_SIZE; break;
            case 'L': x[0] = x[0] - GamePanel.UNIT_SIZE; break;
            case 'R': x[0] = x[0] + GamePanel.UNIT_SIZE; break;
            default: break;
        }
    }

    public void checkApple(){
        if((x[0] == this.appleX) && (y[0] == this.appleY)){
            this.bodyParts += 1;
            this.applesEaten += 1;
            newApple();
        }
    }

    public void checkCollisions(){
        for(int i = this.bodyParts ; i > 0 ; --i){
            if((x[0] == x[i]) && (y[0] == y[i]))
                this.running = false;
        }

        if(x[0] < 0 || x[0] > GamePanel.SCREEN_WIDTH || y[0] < 0 || y[0] > GamePanel.SCREEN_HEIGHT)
            this.running = false;



        if(!this.running)
            timer.stop();
    }

    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metricsOne = getFontMetrics(g.getFont());
        g.drawString("Score: " + this.applesEaten, (GamePanel.SCREEN_WIDTH - metricsOne.stringWidth("Score: " + this.applesEaten)) / 2, g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metricsTwo = getFontMetrics(g.getFont());
        g.drawString("Game Over", (GamePanel.SCREEN_WIDTH - metricsTwo.stringWidth("Game Over")) / 2, GamePanel.SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT: if(direction != 'R') direction = 'L'; break;
                case KeyEvent.VK_RIGHT: if(direction != 'L') direction = 'R'; break;
                case KeyEvent.VK_UP: if(direction != 'D') direction = 'U'; break;
                case KeyEvent.VK_DOWN: if(direction != 'U') direction = 'D'; break;
            }
        }
    }
}
