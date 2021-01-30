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

class View extends JFrame {
	public TitleView titleView = new TitleView();
	
	public Container c;
	public CardLayout card;
	
	View(){
		setTitle("º®µ¹±ú±â °ÔÀÓ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c = this.getContentPane();
		card = new CardLayout();
		
		this.setLayout(card);
		
		add(titleView, "title");
		
		setVisible(true);
		setSize(1000,1000);
	}
	public class TitleView extends JPanel{
		public BufferedImage backImage;
		JLabel titleLabel1, titleLabel2, titleLabel3, flickeringLabel;
		
		TitleView() {
			try {
				backImage = ImageIO.read(new File("TitleBackImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
			
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
			
			flickeringLabel = new JLabel("PRESS SPACEBAR TO PLAY!");
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
	}
}

public class BrickBreakGame {
	BrickBreakGame(){
		View ui = new View();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BrickBreakGame();
	}

}
