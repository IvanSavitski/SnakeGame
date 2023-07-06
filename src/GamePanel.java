import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 900;
    static final int SCREEN_HEIGHT = 900;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 90;


    static final double LEMON_APPEAR_PROBABILITY = 0.3;
    static final double APPLE_APPEAR_PROBABILITY = 0.7;
    double randomValue;

    final int[] xCoordinate = new int[GAME_UNITS];
    final int[] yCoordinate = new int[GAME_UNITS];


    int bodyPartsLength = 8;
    int applesEatenCount;

    int appleLocationCoordinatesX;
    int appleLocationCoordinatesY;



    static final char beginGameDirection = 'R';
    char direction = beginGameDirection;
    boolean isRunning = false;
    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        isRunning = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }


    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (isRunning) {
            //squares of dashboard
            for(int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; i++) {
                graphics.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }

            //apple
            graphics.setColor(Color.RED);
            graphics.fillOval(appleLocationCoordinatesX, appleLocationCoordinatesY, UNIT_SIZE, UNIT_SIZE);


            //snake
            int redRGBbodySnake = random.nextInt(0, 180);
            int greenRGBbodySnake = random.nextInt(0, 180);
            int blueRGBbodySnake = random.nextInt(0,180);

            for (int i = 0; i < bodyPartsLength; i++) {
                if (i == 0){
                    int redRGBheadSnake = random.nextInt(50,255);
                    int greenRGBheadSnake = random.nextInt(50,255);
                    int blueRGBheadSnake = random.nextInt(50, 255);
                    graphics.setColor(new Color(redRGBheadSnake, greenRGBheadSnake, blueRGBheadSnake));
                    //graphics.setColor(Color.GREEN);
                    graphics.fillRect(xCoordinate[i], yCoordinate[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {

                    if (i == 1) {
                        graphics.setColor(new Color(redRGBbodySnake, greenRGBbodySnake, blueRGBbodySnake));
                        graphics.fillRect(xCoordinate[i], yCoordinate[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    else {
                        graphics.setColor(new Color(redRGBbodySnake, greenRGBbodySnake, blueRGBbodySnake));
                        graphics.fillRect(xCoordinate[i], yCoordinate[i], UNIT_SIZE, UNIT_SIZE);
                    }

                }
            }

            graphics.setColor(Color.MAGENTA);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("SCORE: " + applesEatenCount, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + applesEatenCount))/2, graphics.getFont().getSize());
        }
        else gameOver(graphics);
    }


    public void newApple() {
        appleLocationCoordinatesX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleLocationCoordinatesY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }


    public void move() {
        for (int i = bodyPartsLength; i > 0; i--) {
            xCoordinate[i] = xCoordinate[i - 1];
            yCoordinate[i] = yCoordinate[i - 1];
        }

        switch (direction) {
            case 'L' -> xCoordinate[0] = xCoordinate[0] - UNIT_SIZE;
            case 'R' -> xCoordinate[0] = xCoordinate[0] + UNIT_SIZE;
            case 'U' -> yCoordinate[0] = yCoordinate[0] - UNIT_SIZE;
            case 'D' -> yCoordinate[0] = yCoordinate[0] + UNIT_SIZE;

        }
    }

    public void checkApple() {
        if((xCoordinate[0] == appleLocationCoordinatesX) && (yCoordinate[0] == appleLocationCoordinatesY)) {
            bodyPartsLength++;
            applesEatenCount++;

            newApple();
        }
    }

    public void checkCollisions() {
        //проверяем не врезалась ли голова в тело
        for (int i = bodyPartsLength; i > 0; i--) {
            if ((xCoordinate[0] == xCoordinate[i]) && (yCoordinate[0] == yCoordinate[i])) {
                isRunning = false;
                break;
            }
        }

        //проверяем не вышла ли змея за границу поля
        if ((xCoordinate[0] < 0) || (xCoordinate[0] > SCREEN_WIDTH) || (yCoordinate[0] < 0) || (yCoordinate[0] > SCREEN_HEIGHT))
            isRunning = false;

        if (!isRunning)
            timer.stop();
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.MAGENTA);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 60));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
        graphics.drawString("SCORE: " + applesEatenCount, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + applesEatenCount))/3, /*graphics.getFont().getSize(),*/ SCREEN_HEIGHT/3);
    }


    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {if (direction != 'R') direction = 'L';}
                case KeyEvent.VK_RIGHT -> {if (direction != 'L') direction = 'R';}
                case KeyEvent.VK_UP -> {if (direction != 'D') direction = 'U';}
                case KeyEvent.VK_DOWN -> {if (direction != 'U') direction = 'D';}
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
}
