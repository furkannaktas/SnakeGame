import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH  = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];     //coordinates of x including the head of the snake
    final int y[] = new int[GAME_UNITS];     //coordinates of y including the head of the snake
    int bodyPart = 6;
    int applesEaten = 0;
    int appleX;                              //coordinates of Apples on x coordinate
    int appleY;                              //coordinates of Apples on y coordinate
    char direction = 'D';                    // Right - Left - Up - Down
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);    // size of the apple

            for (int i = 0; i < bodyPart; i++) {

                if (i == 0) {
                    g.setColor(Color.GREEN);                 // color of the head of the snake
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                  //  g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));      // We will change the color of the snake each time
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //Draw the current score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score :"+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score :"+applesEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);

        }
    }

    public void newApple(){
        appleX =random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY =random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    public void move(){

        for (int i = bodyPart; i > 0 ; i--) {                            // shifting the body parts
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && y[0] == appleY){
            bodyPart++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){

        // This checks if head collides with body
        for (int i = bodyPart; i >0 ; i--) {

            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        // This checks if the head touches left border
        if(x[0] < 0 ){
            running = false;
        }

        // This checks if the head touches right border
        if(x[0] > SCREEN_WIDTH - UNIT_SIZE ){
            running = false;
        }

        // This checks if the head touches top border
        if(y[0] < 0 ){
            running = false;
        }

        // This checks if the head touches bottom border
        if(y[0] > SCREEN_HEIGHT - UNIT_SIZE ){
            running = false;
        }

        //stops the times
        if(!running){
            timer.stop();
        }

    }

    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score :"+applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("Score :"+applesEaten))/2, g.getFont().getSize());
        // Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER",(SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
