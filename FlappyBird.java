import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth=288;
    int boardHeight=512;

    //IMAGES
    Image backgroundimg;
    Image birdimg;
    Image toppipe;
    Image downpipe;

    //bird
    int birdX=boardWidth/8;
    int birdY=boardHeight/2;
    int birdWidth= 34;
    int birdHeight= 24;

    class Bird{
        int x=birdX;
        int y=birdY;
        int height=birdHeight;
        int width=birdWidth;

        Image img;

        Bird(Image img){
            this.img=img;
        }
    }

    //pipes
    int pipeX=boardWidth;
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=480;

    class Pipe{
        int x=pipeX;
        int y=pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;

        Pipe(Image img){
            this.img=img;
        }
    }


    //game logic
    Bird bird;
    int velocityX=-4;
    int velocityY=0;
    int gravity=1;

    ArrayList<Pipe> pipes;
    Random random=new Random();
    
    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver=false;
    double score =0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        setFocusable(true);
        addKeyListener(this);
        backgroundimg=new ImageIcon(getClass().getResource("./background-day.png")).getImage();
        birdimg=new ImageIcon(getClass().getResource("./redbird-midflap.png")).getImage();
        toppipe=new ImageIcon(getClass().getResource("./up-pipe.png")).getImage();
        downpipe=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdimg);
        pipes =new ArrayList<Pipe>();

        //place pipe timer
        placePipesTimer=new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();
        //timer

        gameLoop=new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes(){
        int randomPipeY=(int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace= boardHeight/4;

        Pipe topPipe=new Pipe(toppipe);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe =new Pipe(downpipe);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //background
        g.drawImage(backgroundimg, 0, 0, boardWidth, boardHeight, null);

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for(int i=0;i<pipes.size(); i++){
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Ariel", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        //bird
        velocityY +=gravity;
        bird.y+=velocityY;
        bird.y=Math.max(bird.y,0);

        //pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe= pipes.get(i);
            pipe.x+=velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed =true;
                score+=0.5;
            }
            
            if(collison(bird,pipe))
            gameOver=true;
        }

        if(bird.y > boardHeight)
        gameOver=true;
    }

    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    public boolean collison(Bird a, Pipe b){
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void keyPressed(KeyEvent e){
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            velocityY=-9;
            if(gameOver){
                bird.y=birdY;
                velocityY=0;
                pipes.clear();
                score=0;
                gameOver=false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }

    }

    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
}