import javax.swing.*;

class MainApp{
    public static void main(String args[]){
        int boardWidth=288;
        int boardHeight=512;

        JFrame frame =new JFrame("Flappy Bird");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        FlappyBird flappybird=new FlappyBird();
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);

    }
}