import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Gameplay extends JPanel implements KeyListener, ActionListener {
    /**=============================== Images ===============================*/
    ImageIcon title = new ImageIcon("/Users/xuyuge/IdeaProjects/SnakeProject/src/Images/newTitle.png");
    ImageIcon body = new ImageIcon("/Users/xuyuge/IdeaProjects/SnakeProject/src/Images/newBody.png");
    ImageIcon up = new ImageIcon("/Users/xuyuge/IdeaProjects/SnakeProject/src/Images/newUp.png");
    ImageIcon down = new ImageIcon("/Users/xuyuge/IdeaProjects/SnakeProject/src/Images/newDown.png");
    ImageIcon right = new ImageIcon("/Users/xuyuge/IdeaProjects/SnakeProject/src/Images/newRight.png");
    ImageIcon left = new ImageIcon("/Users/xuyuge/IdeaProjects/SnakeProject/src/Images/newLeft.png");
    //ImageIcon food = new ImageIcon("newFood.png");
    /**=============================== Images ===============================*/

    private boolean isStarted = false;
    private Timer timer = new Timer(100, this);
    private boolean isFailed = false;

    private java.util.List<Food> food_RList;
    private java.util.List<Food> food_GList;
    private java.util.List<Food> food_BList;
    private Food[] foodIcon;
    private Food[] currentFood;
    private Snake[] snakes;//蛇的总数
    private Snake[] currentSnakes;//当前场景中的蛇

    /**=====================================银行家算法参数=====================================*/
    private BankersAlgorithm ba;
    private int[][] max,allocated,need;
    private int[] available;

    private int food_R,food_G,food_B;

    /**=====================================银行家算法参数=====================================*/

    private GameState state;
    enum GameState {
        START,PLAYING, PAUSED, COMPLETE, FAILED, WIN
    }

    // constructor
    public Gameplay() {
        GameStart();
        this.setFocusable(true);
        this.addKeyListener(this);
        timer.start();
    }

    //method
    public void paintComponent(Graphics g) {// main painting method
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 180, 11);

        //绘制剩余资源数面板
        for (int i = 0; i <foodIcon.length ; i++) {
            foodIcon[i].getImage().paintIcon(this,g,32+i*256,48);
        }
        g.drawString("x"+food_RList.size(),60,70);
        g.drawString("x"+food_GList.size(),60+256,70);
        g.drawString("x"+food_BList.size(),60+512,70);

        g.fillRect(32, 96, 1088, 800);// fill window with color

        if (state.equals(GameState.PLAYING)) {
            for (int i = 0; i < currentSnakes.length; i++) {
                //决定蛇头的位置朝向
                switch(currentSnakes[i].direction) {// determine the picture of head
                    case RIGHT: currentSnakes[i].body[0].setImageState(Vertex.ImageState.HEAD_R);
                        break;
                    case LEFT: currentSnakes[i].body[0].setImageState(Vertex.ImageState.HEAD_L);
                        break;
                    case UP: currentSnakes[i].body[0].setImageState(Vertex.ImageState.HEAD_U);
                        break;
                    case DOWN: currentSnakes[i].body[0].setImageState(Vertex.ImageState.HEAD_D);
                        break;
                }
            }
            for (int j = 0; j < currentSnakes.length; j++) {
                for(int i = 0; i < currentSnakes[j].body.length; i++) {

                    currentSnakes[j].body[i].getImage().paintIcon(this, g, currentSnakes[j].body[i].getX(), currentSnakes[j].body[i].getY());
                }
            }
            /**==================================管理Food绘制==================================*/
            for (int i = 0; i < currentFood.length; i++) {
                currentFood[i].getImage().paintIcon(this, g, currentFood[i].position.getX(),currentFood[i].position.getY());
            }
        }

        // print words on the middle of page
        if (state.equals(GameState.PAUSED)) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 30));
            g.drawString("Press Space to get started", 295, 350);
        }

        if(state.equals(GameState.FAILED)) {
            g.setColor(Color.RED);
            g.setFont(new Font("arial", Font.BOLD, 30));
            g.drawString("Failed! Press Space to Restart", 235, 350);
        }

        if(state.equals(GameState.COMPLETE)) {
            g.setFont(new Font("arial", Font.BOLD, 30));
            g.setColor(Color.GRAY);
            g.fillRect(128, 160, 896, 640);// fill window with color；
            g.setColor(Color.BLACK);
            g.drawString("Success One Snake! \n Press num and chose another snake", 192, 224);
            for (int j = 0; j < snakes.length; j++) {
                for(int i = 0; i < snakes[j].body.length; i++) {
                    snakes[j].body[i].getImage().paintIcon(this, g, snakes[j].body[i].getX(), snakes[j].body[i].getY());
                }
            }
        }

        if(state.equals(GameState.START)) {
            g.setFont(new Font("arial", Font.BOLD, 30));
            g.setColor(Color.GRAY);
            g.fillRect(128, 160, 896, 640);// fill window with color；
            g.setColor(Color.BLACK);
            g.drawString("Success Two Snake! \n Press num and chose two snake", 192, 224);
            for (int j = 0; j < snakes.length; j++) {
                for(int i = 0; i < snakes[j].body.length; i++) {
                    snakes[j].body[i].getImage().paintIcon(this, g, snakes[j].body[i].getX(), snakes[j].body[i].getY());
                }
            }
        }

        if (state.equals(GameState.WIN)){
            g.setColor(Color.BLACK);
            g.fillRect(32, 96, 1088, 800);
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 80));
            g.drawString("You Win!", 416, 512);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        /**============================ 开始/暂停游戏 ============================*/
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE) {
            if(state.equals(GameState.PAUSED)) {
                state = GameState.PLAYING;
            }
            else if (state.equals(GameState.PLAYING)){
                state = GameState.PAUSED;
            }else if(state.equals(GameState.FAILED)){
                GameStart();
            }
            repaint();
        }
        /**============================ 开始/暂停游戏 ============================*/


        /**============================ Playing 状态下玩家操作逻辑 ============================*/
        if(state.equals(GameState.PLAYING))
        {
            // Player2 controls using arrow keys
            if (keyCode == KeyEvent.VK_UP) {
                currentSnakes[0].setDirection(Snake.Direciton.UP);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                currentSnakes[0].setDirection(Snake.Direciton.DOWN);
            } else if (keyCode == KeyEvent.VK_LEFT) {
                currentSnakes[0].setDirection(Snake.Direciton.LEFT);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                currentSnakes[0].setDirection(Snake.Direciton.RIGHT);
            }

            // Player1 controls using arrow keys
            if (keyCode == KeyEvent.VK_W) {
                currentSnakes[1].setDirection(Snake.Direciton.UP);
            } else if (keyCode == KeyEvent.VK_S) {
                currentSnakes[1].setDirection(Snake.Direciton.DOWN);
            } else if (keyCode == KeyEvent.VK_A) {
                currentSnakes[1].setDirection(Snake.Direciton.LEFT);
            } else if (keyCode == KeyEvent.VK_D) {
                currentSnakes[1].setDirection(Snake.Direciton.RIGHT);
            }
        }
        /**============================ Playing 状态下玩家操作逻辑 ============================*/

        if (state.equals(GameState.COMPLETE)) {
            if (keyCode == KeyEvent.VK_1 && !snakes[0].getIsCompleted()
                    && !snakes[0].equals(currentSnakes[0])
                    && !snakes[0].equals(currentSnakes[1])) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if (currentSnakes[i].getIsCompleted()){
                        currentSnakes[i] = snakes[0];
                        state = GameState.PLAYING;
                    }
                }
                selectDown();
            }else if (keyCode == KeyEvent.VK_2 && !snakes[1].getIsCompleted()
                    && !snakes[1].equals(currentSnakes[0])
                    && !snakes[1].equals(currentSnakes[1])){
                for (int i = 0; i < currentSnakes.length; i++) {
                    if (currentSnakes[i].getIsCompleted()){
                        currentSnakes[i] = snakes[1];
                        state = GameState.PLAYING;
                    }
                }
                selectDown();
            }else if (keyCode == KeyEvent.VK_3 && !snakes[2].getIsCompleted()
                    && !snakes[2].equals(currentSnakes[0])
                    && !snakes[2].equals(currentSnakes[1])) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if (currentSnakes[i].getIsCompleted()) {
                        currentSnakes[i] = snakes[2];
                        state = GameState.PLAYING;
                    }
                }
                selectDown();
            }else if (keyCode == KeyEvent.VK_4 && !snakes[3].getIsCompleted()
                    && !snakes[3].equals(currentSnakes[0])
                    && !snakes[3].equals(currentSnakes[1])) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if (currentSnakes[i].getIsCompleted()) {
                        currentSnakes[i] = snakes[3];
                        state = GameState.PLAYING;
                    }
                }
                selectDown();
            }else if (keyCode == KeyEvent.VK_5 && !snakes[4].getIsCompleted()
                    && !snakes[4].equals(currentSnakes[0])
                    && !snakes[4].equals(currentSnakes[1])) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if (currentSnakes[i].getIsCompleted()) {
                        currentSnakes[i] = snakes[4];
                        state = GameState.PLAYING;
                    }
                }
                selectDown();
            }
        }

        if (state.equals(GameState.START)) {
            if (keyCode == KeyEvent.VK_1 ) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if(currentSnakes[i] == null){
                        currentSnakes[i] = snakes[0];
                        break;
                    }
                }
                if(currentSnakes[0]!= null && currentSnakes[1]!=null){
                    outStart();
                }

            }else if (keyCode == KeyEvent.VK_2){
                for (int i = 0; i < currentSnakes.length; i++) {
                    if(currentSnakes[i] == null){
                        currentSnakes[i] = snakes[1];
                        break;
                    }
                }
                if(currentSnakes[0]!= null && currentSnakes[1]!=null){
                    outStart();
                }

            }else if (keyCode == KeyEvent.VK_3 ) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if(currentSnakes[i] == null){
                        currentSnakes[i] = snakes[2];
                        break;
                    }
                }
                if(currentSnakes[0]!= null && currentSnakes[1]!=null){
                    outStart();
                }
            }else if (keyCode == KeyEvent.VK_4 ) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if(currentSnakes[i] == null){
                        currentSnakes[i] = snakes[3];
                        break;
                    }
                }
                if(currentSnakes[0]!= null && currentSnakes[1]!=null){
                    outStart();
                }
            }else if (keyCode == KeyEvent.VK_5 ) {
                for (int i = 0; i < currentSnakes.length; i++) {
                    if(currentSnakes[i] == null){
                        currentSnakes[i] = snakes[4];
                        break;
                    }
                }
                if(currentSnakes[0]!= null && currentSnakes[1]!=null){
                    outStart();
                }
            }
        }
        /**============================测试用代码============================*/
        if(keyCode == KeyEvent.VK_9)
        {
            state = GameState.START;
        }
    }

    private void rankSnakes(){
        timer.setDelay(200);
        for (int i = 0; i < snakes.length; i++) {
            snakes[i].setBound(1088,32,768,256);
            snakes[i].setLocation(288+i*128,512);
        }
    }
    
    private void refreshFood(){
        for (int i = 0; i < currentFood.length; i++) {
            currentFood[i].randomLocation();
        }
    }

    private void selectDown(){
        timer.setDelay(100);
        for (int i = 0; i < snakes.length; i++) {
            snakes[i].setBound(1088,32,800,96);
        }
        for (int i = 0; i < currentSnakes.length; i++) {
            currentSnakes[i].setLocation(256+i*512,512);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public Food getRandomFood(){

        //获取最佳need向量
        int[] need = new int[3];
        for (int i = 0; i < need.length; i++) {
            need[i] = currentSnakes[0].returnNeed()[i]+currentSnakes[1].returnNeed()[i];
        }

        Random random = new Random();
        double totalWeight = need[0] + need[1] + need[2];
        double randomValue = random.nextDouble() * totalWeight;

        if (randomValue < need[0]) {
            if(!food_RList.isEmpty())
                return food_RList.remove(0);
        } else if (randomValue < need[0]+need[1]) {
            if(!food_GList.isEmpty())
                return food_GList.remove(0);
        } else {
            if(!food_BList.isEmpty())
                return food_BList.remove(0);
        }

        if(!food_RList.isEmpty())
            return food_RList.remove(0);
        else if (!food_GList.isEmpty())
            return food_GList.remove(0);
        else
            return food_BList.remove(0);
    }

    //处理游戏事件
    @Override
    public void actionPerformed(ActionEvent e) {
        //当前状态机为播放模式
        if (state.equals(GameState.PLAYING)) {
            // 移动和检测碰撞的逻辑...
            for (int i = 0; i < currentSnakes.length; i++) {
                currentSnakes[i].move(); // Move each snake

                // Check for collisions with the other snake's body
                for (int j = 0; j < currentSnakes.length; j++) {
                    if (i != j && currentSnakes[i].headCollidesWithBody(currentSnakes[j])) {
                        // Collision detected, end the game
                        state = GameState.FAILED;
                    }
                }
                // Check for collisions with food for each snake
                for (int j = 0; j < currentFood.length; j++) {
                    if (currentSnakes[i].body[0].equals(currentFood[j].position)) {
                        if (currentSnakes[i].checkEat(currentFood[j])) {

                            /**==============================食物被吃掉后的逻辑==============================*/
                            currentSnakes[i].updateAllocation(currentFood[j]);
                            currentFood[j] = getRandomFood();
                            switch (currentFood[j].imageState){
                                case RED:
                                    food_R--;
                                break;
                                case GREEN:
                                    food_G--;
                                break;
                                case BLUE:
                                    food_B--;
                                break;
                            }
                            refreshFood();
                            repaint();
                        }else
                            state = GameState.FAILED;

                        /**有蛇完成了*/
                        if (currentSnakes[i].checkComplete()){
                            if(checkWin()){
                                state = GameState.WIN;
                            }else{
                                int[] max = currentSnakes[i].returnMAX();
//                              food_R += max[0];
//                              food_G += max[1];
//                              food_B += max[2];
                                for (int k = 0; k < max[0]; k++) {
                                    food_RList.add(new Food());
                                }
                                for (int k = 0; k < max[1]; k++) {
                                    food_GList.add(new Food(Food.foodImageState.GREEN));
                                }
                                for (int k = 0; k < max[2]; k++) {
                                    food_BList.add(new Food(Food.foodImageState.BLUE));
                                }

                                state = GameState.COMPLETE;
                                rankSnakes();
                                break;
                            }
                        }
                    }
                }
            }

            // 更新食物位置
            for (int i = 0; i < currentFood.length; i++)
                currentFood[i].getImage().paintIcon(this, getGraphics(), currentFood[i].position.getX(), currentFood[i].position.getY());

            /**进入选择蛇状态下每帧更新所有蛇的位置*/
        }else if (state.equals(GameState.COMPLETE) || state.equals(GameState.START)){
            for (int i = 0; i < snakes.length; i++) {
                if (!snakes[i].getIsCompleted()
                        && !snakes[i].equals(currentSnakes[0])
                        && !snakes[i].equals(currentSnakes[1]))
                {
                    snakes[i].move();
                }
            }
        }

        repaint();
        timer.start();
    }

    public boolean checkWin(){
        int count = 0;
        for (int i = 0; i < snakes.length; i++) {
            if (snakes[i].getIsCompleted() == true)
                count++;
        }

        if (count == ba.getProcesses()-1)
            return true;
        else
            return false;
    }

    public void outStart(){
        currentFood[0] = getRandomFood();
        currentFood[1] = getRandomFood();
        currentFood[2] = getRandomFood();
        selectDown();
        state = GameState.PLAYING;
    }
    public void GameStart(){
        //现在只有三个资源的图片，千万不要改resource的数量
        ba = new BankersAlgorithm(5,3);
        ba.runBankersAlgorithm();

        max = ba.getMax();
        allocated = ba.getAllocated();
        need = ba.getNeed();
        available = ba.getAvailable();

        snakes = new Snake[ba.getProcesses()];

        for (int i = 0; i < snakes.length; i++) {
            snakes[i] = new Snake(max[i][0],allocated[i][0],
                    max[i][1],allocated[i][1],
                    max[i][2],allocated[i][2]);
        }

        currentFood = new Food[ba.getResources()];
        food_R = available[0];
        food_G = available[1];
        food_B = available[2];
        food_RList = new ArrayList<>();
        food_GList = new ArrayList<>();
        food_BList = new ArrayList<>();
        foodIcon = new Food[3];
        foodIcon[0] = new Food();
        foodIcon[1] = new Food(Food.foodImageState.GREEN);
        foodIcon[2] = new Food(Food.foodImageState.BLUE);

        for (int i = 0; i < food_R; i++) {
            food_RList.add(new Food());
        }
        for (int i = 0; i < food_G; i++) {
            food_GList.add(new Food(Food.foodImageState.GREEN));
        }
        for (int i = 0; i < food_B; i++) {
            food_BList.add(new Food(Food.foodImageState.BLUE));
        }

//        snakes[0] = new Snake(3,2,2,1,2,2,256,256);
//        snakes[1] = new Snake(4,1,3,1,5,1,256,512);
//        snakes[2] = new Snake(5,3,4,1,2,2,256,256);
//        snakes[3] = new Snake(6,2,4,1,2,2,256,256);
//        snakes[4] = new Snake(8,3,5,1,2,2,256,256);

        currentSnakes = new Snake[2];//currentSnakes表示存储snake的数组，一条snake，两条snake
        //currentSnakes[0] = snakes[0];
        //currentSnakes[0].setLocation(256,512);
        //表示0号snake最多吃3个R资源， 已经有了2个R资源; 最多吃2个G资源，已经有了1个G资源； 最多吃两个B资源，已经有了2个比资源
        //currentSnakes[1] = snakes[1];
        //currentSnakes[1].setLocation(512,512);
        //state = GameState.PAUSED;
        rankSnakes();
        state = GameState.START;


//        currentFood[0] = new Food();
//        currentFood[1] = new Food(Food.foodImageState.BLUE);
//        currentFood[2] = new Food(Food.foodImageState.GREEN);

//        currentFood[0] = getRandomFood();
//        currentFood[1] = getRandomFood();
//        currentFood[2] = getRandomFood();
    }
}
