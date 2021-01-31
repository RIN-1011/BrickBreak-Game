import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class UI extends JFrame {
	public TitleUI titleUI = new TitleUI();
	public MainUI mainUI = new MainUI();
	public EndUI EndUI = new EndUI();
	
	public Container c;
	public CardLayout card;
	
	UI(){
		setTitle("º®µ¹±ú±â °ÔÀÓ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c = this.getContentPane();
		card = new CardLayout();
		
		c.setLayout(card);
		
		add(titleUI, "title");
		add(mainUI, "main");
		add(EndUI, "End");
		
		setVisible(true);
		setSize(1000,1000);
	}
	class TitleUI extends JPanel implements KeyListener{
		public BufferedImage backImage;
		JLabel titleLabel1, titleLabel2, titleLabel3, flickeringLabel;
		Container titleC;
		
		TitleUI() {
			try {
				backImage = ImageIO.read(new File("TitleBackImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setLayout(null);
			
			this.setFocusable(true);
			this.requestFocus();
			this.addKeyListener(this);
			
			titleLabel1 = new JLabel("Java Programming");
			titleLabel1.setHorizontalAlignment(JLabel.CENTER);
			titleLabel1.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 70));
			titleLabel1.setBounds(0, 130, 1000, 100);
			
			titleLabel2 = new JLabel("Homework #5");
			titleLabel2.setHorizontalAlignment(JLabel.CENTER);
			titleLabel2.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 70));
			titleLabel2.setBounds(0, 230, 1000, 100);
			
			titleLabel3 = new JLabel("BLOCK BREAKER");
			titleLabel3.setHorizontalAlignment(JLabel.CENTER);
			titleLabel3.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 100));
			titleLabel3.setForeground(new Color(57, 3, 101));
			titleLabel3.setBounds(0, 400, 1000, 100);
			
			FlickeringLabel flickeringLabel = new FlickeringLabel("PRESS SPACEBAR TO PLAY!");
			flickeringLabel.setHorizontalAlignment(JLabel.CENTER);
			flickeringLabel.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 40));
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
		
		class FlickeringLabel extends JLabel implements Runnable{
			FlickeringLabel(String str){
				super(str);
				
				Thread thread = new Thread(this);
				thread.start();
			}
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int n = 0;
				while(true) {
					if(n==0) {
						setVisible(true);
					}
					else {
						setVisible(false);
					}
					if(n==0) {
						n = 1;
					}
					else {
						n = 0;
					}
					try {
						Thread.sleep(120);
					} catch(InterruptedException e) {
						return;
					}
				}
			}
		}
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getKeyCode() == KeyEvent.VK_SPACE) {
				card.show(c, "main");
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
	class MainUI extends JPanel {
		public BufferedImage backImage;
		
		MainUI(){
			try {
				backImage = ImageIO.read(new File("MainBackImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
		}
	}
	
	public class EndUI extends JPanel {
		public BufferedImage backImage;
		
		EndUI(){
			try {
				backImage = ImageIO.read(new File("TitleBackImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
		}
	}
}

public class BrickBreakGame {
	BrickBreakGame(){
		UI ui = new UI();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BrickBreakGame();
	}

}
