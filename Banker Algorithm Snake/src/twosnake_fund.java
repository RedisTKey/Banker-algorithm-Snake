import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class twosnake_fund extends JPanel implements KeyListener {

    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int UNIT_SIZE = 20;
    private ArrayList<Point> snakeOne;
    private ArrayList<Point> snakeTwo;
    private char directionOne = 'R'; // Initially moving right for snake one
    private char directionTwo = 'L'; // Initially moving left for snake two
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private Point resource1;
    private Point resource2;
    private Point resource3;

    private Color colorResource1 = Color.red;    // 资源1的颜色
    private Color colorResource2 = Color.white;  // 资源2的颜色
    private Color colorResource3 = Color.orange;   // 资源3的颜色
    // 蛇一的资源状态
    private int resource1Snake1 = 0;
    private int resource2Snake1 = 0;
    private int resource3Snake1 = 0;

    // 蛇二的资源状态
    private int resource1Snake2 = 0;
    private int resource2Snake2 = 0;
    private int resource3Snake2 = 0;

    // 资源的阈值
    private final int WINNING_THRESHOLD = 3;

    public twosnake_fund() {
        snakeOne = new ArrayList<>();
        snakeTwo = new ArrayList<>();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    private void startGame() {
        snakeOne.clear();
        snakeTwo.clear();
        gameOver = false;
        snakeOne.add(new Point(5, 5)); // Initial position for snake one
        snakeTwo.add(new Point(15, 15)); // Initial position for snake two
        resource1 = new Point(10, 10); // 设置资源1的初始位置
        resource2 = new Point(15, 15); // 设置资源2的初始位置
        resource3 = new Point(20, 20); // 设置资源3的初始位置
        spawnResource();
        Timer timer = new Timer(300, e -> gameLoop());
        timer.start();
        gameStarted = true;
    }

    private void moveSnake(ArrayList<Point> snake, char direction) {
        Point head = snake.get(0);
        Point newPoint = head.getLocation();
        switch (direction) {
            case 'U':
                newPoint.translate(0, -1);
                break;
            case 'D':
                newPoint.translate(0, 1);
                break;
            case 'L':
                newPoint.translate(-1, 0);
                break;
            case 'R':
                newPoint.translate(1, 0);
                break;
        }

        // Adjust position if hitting the boundary
        if (newPoint.x < 0) {
            newPoint.x = WIDTH / UNIT_SIZE - 1;
        } else if (newPoint.x >= WIDTH / UNIT_SIZE) {
            newPoint.x = 0;
        }
        if (newPoint.y < 0) {
            newPoint.y = HEIGHT / UNIT_SIZE - 1;
        } else if (newPoint.y >= HEIGHT / UNIT_SIZE) {
            newPoint.y = 0;
        }

        snake.add(0, newPoint);
        if (newPoint.equals(resource1)) {
            spawnResource();
        }else if(newPoint.equals(resource2)){
            spawnResource();
        }else if(newPoint.equals(resource3)){
            spawnResource();
        }
        else {
            snake.remove(snake.size() - 1);
        }
    }


    private void gameLoop() {
        if (!gameOver) {
            moveSnake(snakeOne, directionOne);
            moveSnake(snakeTwo, directionTwo);
            checkCollisions();
            checkResource();
            repaint();
        }
    }



    private void checkCollisions() {
        if (resource1Snake1 >= WINNING_THRESHOLD || resource2Snake1 >= WINNING_THRESHOLD || resource3Snake1 >= WINNING_THRESHOLD||
                resource1Snake2 >= WINNING_THRESHOLD || resource2Snake2 >= WINNING_THRESHOLD || resource3Snake2 >= WINNING_THRESHOLD) {
            gameOver = true;
        }
    }

    private void checkResource() {
        if (snakeOne.get(0).equals(resource1)) {
            resource1Snake1++;
            resource1.setLocation(-1, -1);

        } else if (snakeOne.get(0).equals(resource2)) {
            resource2Snake1++;
            resource2.setLocation(-1, -1);

        } else if (snakeOne.get(0).equals(resource3)) {
            resource3Snake1++;
            resource3.setLocation(-1, -1);

        } else if (snakeTwo.get(0).equals(resource1)) {
            resource1Snake2++;
            resource1.setLocation(-1, -1);

        } else if (snakeTwo.get(0).equals(resource2)) {
            resource2Snake2++;
            resource2.setLocation(-1, -1);

        } else if (snakeTwo.get(0).equals(resource3)) {
            resource3Snake2++;
            resource3.setLocation(-1, -1);

        }
    }


    private void spawnResource() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / UNIT_SIZE);
        int y = random.nextInt(HEIGHT / UNIT_SIZE);
        resource1.setLocation(x, y); // 随机生成资源1的位置
        x = random.nextInt(WIDTH / UNIT_SIZE);
        y = random.nextInt(HEIGHT / UNIT_SIZE);
        resource2.setLocation(x, y); // 随机生成资源2的位置
        x = random.nextInt(WIDTH / UNIT_SIZE);
        y = random.nextInt(HEIGHT / UNIT_SIZE);
        resource3.setLocation(x, y); // 随机生成资源3的位置
    }

    private void drawSnake(Graphics g, ArrayList<Point> snake, Color color) {
        g.setColor(color);
        for (Point point : snake) {
            g.fillRect(point.x * UNIT_SIZE, point.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
    }

    private void drawResource(Graphics g) {
        g.setColor(colorResource1);
        g.fillRect(resource1.x * UNIT_SIZE, resource1.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

        g.setColor(colorResource2);
        g.fillRect(resource2.x * UNIT_SIZE, resource2.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

        g.setColor(colorResource3);
        g.fillRect(resource3.x * UNIT_SIZE, resource3.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!gameOver) {
            drawSnake(g, snakeOne, Color.green);
            drawSnake(g, snakeTwo, Color.blue);
            drawResource(g);
        } else {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH / 2 - 120, HEIGHT / 2);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W && directionOne != 'D') {
            directionOne = 'U';
        } else if (key == KeyEvent.VK_S && directionOne != 'U') {
            directionOne = 'D';
        } else if (key == KeyEvent.VK_A && directionOne != 'R') {
            directionOne = 'L';
        } else if (key == KeyEvent.VK_D && directionOne != 'L') {
            directionOne = 'R';
        } else if (key == KeyEvent.VK_UP && directionTwo != 'D') {
            directionTwo = 'U';
        } else if (key == KeyEvent.VK_DOWN && directionTwo != 'U') {
            directionTwo = 'D';
        } else if (key == KeyEvent.VK_LEFT && directionTwo != 'R') {
            directionTwo = 'L';
        } else if (key == KeyEvent.VK_RIGHT && directionTwo != 'L') {
            directionTwo = 'R';
        } else if (key == KeyEvent.VK_SPACE) {
            if (gameOver) {
                startGame(); // Restart the game if it's already over
            } else if (!gameStarted) {
                gameStarted = true;
            }
        }
    }


    public void keyReleased(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Two Player Snake Game");
        twosnake_fund game = new twosnake_fund();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Ensure window gets focus
        game.requestFocus();
    }

}


