import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Block extends JPanel {
   Color normalColor, EventColor;

   Block(Graphics g, int eventBlockCnt, int eventBlockNum[]) {
      int cnt = 1;
      int i, j, k;

      for (i = 0; i < 3; i++) {
         for (j = 0; j < 3; j++) {
            for (k = 0; k < eventBlockCnt; k++) {
               if (cnt == eventBlockNum[k]) {
                  break;
               }
            }

            int x = (int) (j * (320 + 10));
            int y = (int) (i * (160 + 10));
            int w = 320;
            int h = 160;

            if (k == eventBlockCnt) {
               NormalBlock nomalBlock = new NormalBlock();
               normalColor = nomalBlock.setColor();

               Graphics2D g2 = (Graphics2D) g;

               GradientPaint gp = new GradientPaint((int) ((j * 320 + 5) / 2), (int) (i * 160 + 5), Color.WHITE,
                     (int) ((j * 320 + 5) / 2), (int) ((i + 1) * 160 + 5), Color.BLACK);

               g.setColor(normalColor);
               g.fillRoundRect(x, y + 5, w, h, 30, 30);

               g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 0));
               g2.setPaint(gp);
               g2.drawRoundRect(x, y + 5, w, h, 30, 30);
            } else {
               EventBlock eventBlock = new EventBlock();
               EventColor = eventBlock.setColor();

               Graphics2D g2 = (Graphics2D) g;

               GradientPaint gp = new GradientPaint((int) ((j * 320 + 5) / 2), (int) (i * 160 + 5), Color.WHITE,
                     (int) ((j * 320 + 5) / 2), (int) ((i + 1) * 160 + 5), Color.BLACK);

               g.setColor(EventColor);
               g.fillRoundRect(x, y + 5, w, h, 30, 30);

               g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 0));
               g2.setPaint(gp);
               g2.drawRoundRect(x, y + 5, w, h, 30, 30);
            }
            cnt++;
         }
      }
   }

   abstract class Block1 extends JPanel { // 추상클래스
      public abstract Color setColor(); // 벽돌 색 지정 메소드
   }

   class NormalBlock extends Block1 { // 자식클래스 1
      @Override
      public Color setColor() { // 일반 벽돌 색 지정
         // TODO Auto-generated method stub
         return new Color(68, 31, 8);
      }
   }

   class EventBlock extends Block1 { // 자식클래스 2
      @Override
      public Color setColor() { // 이벤트 벽돌 색 지정
         // TODO Auto-generated method stub
         return new Color(249, 164, 39);
      }
   }
}
public class BrickBreakGame extends JFrame {
   public TitleUI titleUI = new TitleUI();
   public MainUI mainUI = new MainUI();
   public EndUI endUI = new EndUI();
   Clip startClip, endClip;
   Container c;
   CardLayout card;
   Thread mainThread;
   
   BrickBreakGame() {
      setTitle("BrickBreakGame");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      c = getContentPane();
      card = new CardLayout();

      c.setLayout(card);
      
      add(titleUI, "title");
      add(mainUI, "main");
      add(endUI, "end");
      
      setSize(1000, 1000);
      setVisible(true);
   }

   ////////////////////////////시작 화면//////////////////////////////////
   
   class TitleUI extends JPanel implements KeyListener{
      public BufferedImage backImage;
      JLabel titleLabel1, titleLabel2, titleLabel3, flickeringLabel;
      
      Thread startThread;

      TitleUI() {
         try {
            backImage = ImageIO.read(new File("BackImg.jpg"));
            startClip = AudioSystem.getClip();
            File audioFile = new File("Start.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            startClip.open(audioStream);
         } catch (LineUnavailableException e) {
            e.printStackTrace();
         } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
         this.setLayout(null);

         this.setFocusable(true);
         this.requestFocus();
         this.addKeyListener(this);

         startClip.start();

         titleLabel1 = new JLabel("Java Programming");
         titleLabel1.setHorizontalAlignment(JLabel.CENTER);
         titleLabel1.setFont(new Font("맑은고딕", Font.BOLD, 70));
         titleLabel1.setBounds(0, 130, 1000, 100);

         titleLabel2 = new JLabel("Homework #5");
         titleLabel2.setHorizontalAlignment(JLabel.CENTER);
         titleLabel2.setFont(new Font("맑은고딕", Font.BOLD, 70));
         titleLabel2.setBounds(0, 230, 1000, 100);

         titleLabel3 = new JLabel("BLOCK BREAKER");
         titleLabel3.setHorizontalAlignment(JLabel.CENTER);
         titleLabel3.setFont(new Font("맑은고딕", Font.BOLD, 100));
         titleLabel3.setForeground(new Color(57, 3, 101));
         titleLabel3.setBounds(0, 400, 1000, 100);

         FlickeringLabel flickeringLabel = new FlickeringLabel("PRESS SPACEBAR TO PLAY!");
         flickeringLabel.setHorizontalAlignment(JLabel.CENTER);
         flickeringLabel.setFont(new Font("맑은고딕", Font.BOLD, 40));
         flickeringLabel.setForeground(Color.RED);
         flickeringLabel.setBounds(0, 700, 1000, 100);

         add(titleLabel1);
         add(titleLabel2);
         add(titleLabel3);
         add(flickeringLabel);
      }

      protected void paintComponent(Graphics g) {
         super.paintComponents(g);
         g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
      }

      class FlickeringLabel extends JLabel implements Runnable {
         FlickeringLabel(String str) {
            super(str);

            startThread = new Thread(this);
            startThread.start();
         }

         @Override
         public void run() {
            // TODO Auto-generated method stub
            int n = 0;
            while (true) {
               if (n == 0) {
                  setVisible(true);
               } else {
                  setVisible(false);
               }
               if (n == 0) {
                  n = 1;
               } else {
                  n = 0;
               }
               try {
                  Thread.sleep(120);
               } catch (InterruptedException e) {
                  System.out.println("타이틀 스레드 종료!");
                  return;
               }
            }
         }
      }

      @Override
      public void keyPressed(KeyEvent arg0) {
         // TODO Auto-generated method stub
         if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
            startThread.interrupt();
            
            card.show(c, "main");
            mainThread = new Thread(mainUI);
            mainThread.start();
         }
      }

      @Override
      public void keyReleased(KeyEvent arg0) {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void keyTyped(KeyEvent arg0) {
         // TODO Auto-generated method stub
         
      }
   }
   
////////////////////////////메인 화면//////////////////////////////////
   
   class MainUI extends JPanel implements Runnable, KeyListener {
      public BufferedImage backImage;
      float ballX = 495, ballY = 855;
      float ballvx = 0, ballvy = 0;
      float r = 12;
      float racketX = 370, racketY = 870;
      float racketvx = 0;
      int speed;
      int eventBlockCnt = 0;
      int eventBlockNum[];

      MainUI() {
         try {
            backImage = ImageIO.read(new File("MainBackImg.jpg"));
            endClip = AudioSystem.getClip();
            File audioFile = new File("Fail.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            endClip.open(audioStream);
         } catch (LineUnavailableException e) {
            e.printStackTrace();
         } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }

         speed = 20;

         this.setLayout(null);
         this.setFocusable(true);
         this.requestFocus();
         this.addKeyListener(this);
         
         blockColorNum();

         ballvx = 10;
         ballvy = -speed;
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponents(g);
         g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);

         Graphics2D g2 = (Graphics2D) g;

         g2.setColor(new Color(205, 244, 136));
         g2.fillRoundRect((int) racketX, (int) racketY, 250, 30, 20, 20);
         Block block = new Block(g, eventBlockCnt, eventBlockNum);
         g2.setColor(Color.RED);
         g2.fillOval((int) (ballX - r), (int) (ballY - r), (int) (2 * r), (int) (2 * r));
      }

      public void blockColorNum() {
         eventBlockCnt = (int) (Math.random() * 3) + 1;
         eventBlockNum = new int[eventBlockCnt];

         int i, j, k;

         for (i = 0; i < eventBlockCnt; i++) {
            int randomNum = (int) (Math.random() * 9) + 1;
            for (j = 0; j < i; j++) {
               if (eventBlockNum[j] == randomNum) {
                  i--;
                  break;
               }
            }
            if (j == i) {
               eventBlockNum[i] = randomNum;
            }
         }
      }

      @Override
      public void run() {
         while (true) {
            try {
               Thread.sleep(30);
            } catch (InterruptedException e) {
               return;
            }
            updatePositionRacket();
            updatePositionBall();
         }
      }

      void updatePositionRacket() {
         racketX += racketvx;

         if (racketX > 982 - 250) {
            racketX = 982 - 250;
            racketvx = 0;
         }
         if (racketX < 0) {
            racketX = 0;
            racketvx = 0;
         }
         repaint();
      }
      void updatePositionBall() {
         ballX += ballvx;
         ballY += ballvy;

         if(ballX<17 || ballX>985 - r*2) {
            ballvx = -ballvx;
         }
         if(ballY < 17) {
            ballvy = -ballvy;
         }
         
         if(ballY>953) {
            mainThread.interrupt();
            endClip.start();
            card.show(c, "end");
         }
         if (racketX - r < ballX && ballX < racketX + 250 - r
            && racketY - r * 2 < ballY && racketY - r * 2 + 30 > ballY) {
            ballvy = -ballvy;
            
            if (ballX < racketX - r + 250 / 5) {
               ballvx = -6;
            }
            if (ballX > racketX - r + 250 / 5 && ballX < racketX - r + 250 / 5 * 2) {
               ballvx = -4;
            }
            if (ballX > racketX - r + 250 / 5 * 3 && ballX < racketX - r + 250 / 5 * 4) {
               ballvx = 4;
            }
            if (ballX > racketX - r + 250 / 5 * 4 && ballX < racketX - r + 250) {
               ballvx = 6;
            }
         }

         repaint();
      }

      @Override
      public void keyPressed(KeyEvent e) {
         // TODO Auto-generated method stub
         switch (e.getKeyCode()) {
         case KeyEvent.VK_LEFT: {
            racketvx = -speed;
            break;
         }
         case KeyEvent.VK_RIGHT: {
            racketvx = speed;
            break;
         }
         default:
            break;
         }
      }

      @Override
      public void keyReleased(KeyEvent e) {
         // TODO Auto-generated method stub
         switch (e.getKeyCode()) {
         case KeyEvent.VK_LEFT: {
            racketvx = 0;
            break;
         }
         case KeyEvent.VK_RIGHT: {
            racketvx = 0;
            break;
         }
         default:
            break;
         }
      }

      @Override
      public void keyTyped(KeyEvent e) {
         // TODO Auto-generated method stub
         
      }
   }

   ////////////////////////////종료 화면//////////////////////////////////
   
   class EndUI extends JPanel {
      public BufferedImage backImage;
      JLabel endLabel1, flickeringLabel;
      Thread endThread;
      
      EndUI(){
         try {
            backImage = ImageIO.read(new File("BackImg.jpg"));
         } catch (IOException e) {
            e.printStackTrace();
         }
         this.setLayout(null);

         this.setFocusable(true);
         this.requestFocus();
         this.addKeyListener(new MyKeyAdapter());

         endLabel1 = new JLabel("GAME OVER");
         endLabel1.setHorizontalAlignment(JLabel.CENTER);
         endLabel1.setFont(new Font("맑은고딕", Font.BOLD, 100));
         endLabel1.setBounds(0, 200, 1000, 100);

         FlickeringLabel flickeringLabel = new FlickeringLabel("PRESS SPACEBAR!");
         flickeringLabel.setHorizontalAlignment(JLabel.CENTER);
         flickeringLabel.setFont(new Font("맑은고딕", Font.BOLD, 40));
         flickeringLabel.setForeground(Color.RED);
         flickeringLabel.setBounds(0, 700, 1000, 100);

         add(endLabel1);
         add(flickeringLabel);
      }
      protected void paintComponent(Graphics g) {
         super.paintComponents(g);
         g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
      }

      class MyKeyAdapter extends KeyAdapter {
         @Override
         public void keyPressed(KeyEvent arg0) {
            // TODO Auto-generated method stub
            if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
               endThread.interrupt();
               card.show(c, "title");
            }
         }
      }

      class FlickeringLabel extends JLabel implements Runnable {
         FlickeringLabel(String str) {
            super(str);

            endThread = new Thread(this);
            endThread.start();
         }

         @Override
         public void run() {
            // TODO Auto-generated method stub
            int n = 0;
            while (true) {
               if (n == 0) {
                  setVisible(true);
               } else {
                  setVisible(false);
               }
               if (n == 0) {
                  n = 1;
               } else {
                  n = 0;
               }
               try {
                  Thread.sleep(120);
               } catch (InterruptedException e) {
                  return;
               }
            }
         }
      }
   }

   public static void main(String[] args) {
      // TODO Auto-generated method stub
      BrickBreakGame b = new BrickBreakGame();
   }

}