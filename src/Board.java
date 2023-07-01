import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class Board extends JPanel implements ActionListener {
    int B_Width = 400;
    int B_Height = 400;
    int Max_Dots = 1600;
    int Dot_Size = 10;
    int Dots;
    int[] x = new int[Max_Dots];
    int[] y = new int[Max_Dots];
    int apple_X;
    int apple_Y;
    // images
    Image apple, head, body;
    Timer timer;
    int DELAY = 300;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;
    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_Width, B_Height));
        setBackground(Color.BLACK);
        initGame();
        loadImages();
    }
    // initialize the game
    public void initGame(){
        Dots = 3;
        // initialize snakes position
        x[0]=200;
        y[0]=200;
        for(int i=1; i<Max_Dots; i++){
            x[i] = x[0]+Dot_Size*i;
            y[i] = y[0];
        }
//        // initialize apples position
//        apple_X = 150;
//        apple_Y = 150;
        locateApples();
        timer = new Timer(DELAY, this);
        timer.start();

    }
    // load images from resources folder to Image object
    public void loadImages(){
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    // draw images
    public void doDrawing(Graphics g){
        if(inGame) {
            g.drawImage(apple, apple_X, apple_Y, this);
            for (int i = 0; i < Dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[0], y[0], this);
                } else {
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        }
        else {
            gameOver(g);
            timer.stop();
        }
    }
    // Randomize the apples position
    public void locateApples(){
        apple_X=((int)(Math.random()*39))*Dot_Size;
        apple_Y=((int)(Math.random()*39))*Dot_Size;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    public void move(){
        for(int i=Dots-1; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] -= Dot_Size;
        }
        if(rightDirection){
            x[0] += Dot_Size;
        }
        if(upDirection){
            y[0] -= Dot_Size;
        }
        if(downDirection){
            y[0] += Dot_Size;
        }
    }
    // make snake eat food
    public void checkApple(){
        if(apple_X==x[0] && apple_Y==y[0]){
            Dots++;
            locateApples();
        }
    }
    // implements controls

    private class TAdapter extends KeyAdapter{
        @Override

        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
    // check collisions with border and body
    public void checkCollision(){
        // collision with body
        for(int i=1; i<Dots; i++){
            if(i>=4 && x[0]==x[i] && y[0]==y[i]){
                inGame = false;
            }
        }
        // collision with border
        if(x[0] < 0){
            inGame = false;
        }
        if(x[0] >= B_Width){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
        if(y[0] >= B_Height){
            inGame = false;
        }
    }
    // display Game Over
    public void gameOver(Graphics g){
        String msg = "Game Over";
        int score = (Dots-3)*100;
        String scoremsg = "Score:"+Integer.toString(score);
        Font small = new Font("Halvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg,(B_Width-fontMetrics.stringWidth(msg))/2, B_Height/4);
        g.drawString(scoremsg,(B_Width-fontMetrics.stringWidth(scoremsg))/2, 3*(B_Width/4));
    }
}
