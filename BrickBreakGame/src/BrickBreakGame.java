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

class UI extends JFrame {
	public TitleUI titleUI = new TitleUI();
	public MainUI mainUI = new MainUI();
	public EndUI EndUI = new EndUI();

	public Container c;
	public CardLayout card;

	UI() {
		setTitle("∫Æµπ±˙±‚ ∞‘¿”");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		c = this.getContentPane();
		card = new CardLayout();

		c.setLayout(card);

		add(titleUI, "title");
		add(mainUI, "main");
		add(EndUI, "End");

		setVisible(true);
		setSize(1000, 1000);
	}

	/////////////////////////////// ≈∏¿Ã∆≤ »≠∏È////////////////////////////////

	class TitleUI extends JPanel {
		public BufferedImage backImage;
		JLabel titleLabel1, titleLabel2, titleLabel3, flickeringLabel;
		Clip clip;

		TitleUI() {
			try {
				backImage = ImageIO.read(new File("TitleBackImg.jpg"));
				clip = AudioSystem.getClip();
				File audioFile = new File("Start.wav");
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				clip.open(audioStream);
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
			this.addKeyListener(new MyKeyAdapter());

			clip.start();

			titleLabel1 = new JLabel("Java Programming");
			titleLabel1.setHorizontalAlignment(JLabel.CENTER);
			titleLabel1.setFont(new Font("∏º¿∫∞ÌµÒ", Font.BOLD, 70));
			titleLabel1.setBounds(0, 130, 1000, 100);

			titleLabel2 = new JLabel("Homework #5");
			titleLabel2.setHorizontalAlignment(JLabel.CENTER);
			titleLabel2.setFont(new Font("∏º¿∫∞ÌµÒ", Font.BOLD, 70));
			titleLabel2.setBounds(0, 230, 1000, 100);

			titleLabel3 = new JLabel("BLOCK BREAKER");
			titleLabel3.setHorizontalAlignment(JLabel.CENTER);
			titleLabel3.setFont(new Font("∏º¿∫∞ÌµÒ", Font.BOLD, 100));
			titleLabel3.setForeground(new Color(57, 3, 101));
			titleLabel3.setBounds(0, 400, 1000, 100);

			FlickeringLabel flickeringLabel = new FlickeringLabel("PRESS SPACEBAR TO PLAY!");
			flickeringLabel.setHorizontalAlignment(JLabel.CENTER);
			flickeringLabel.setFont(new Font("∏º¿∫∞ÌµÒ", Font.BOLD, 40));
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

		class MyKeyAdapter extends KeyAdapter {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
					card.show(c, "main");
				}
			}
		}

		class FlickeringLabel extends JLabel implements Runnable {
			FlickeringLabel(String str) {
				super(str);

				Thread thread = new Thread(this);
				thread.start();
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

	/////////////////////////////// ∏ﬁ¿Œ »≠∏È////////////////////////////////

	class MainUI extends JPanel {
		public BufferedImage backImage;
		int eventBlockCnt = 0;
		int eventBlockNum[];

		MainUI() {
			try {
				backImage = ImageIO.read(new File("MainBackImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			eventBlockCnt = (int)(Math.random()*3)+1;
			eventBlockNum = new int[eventBlockCnt];
			
			int i, j, k;
			for(i=0; i<eventBlockCnt; i++) {
				int randomNum = (int)(Math.random()*9)+1;
				for(j=0; j<i; j++) {
					if(eventBlockNum[j] == randomNum) {
						i--;
						break;
					}
				}
				if(j==i) {
					eventBlockNum[i] = randomNum;
				}
			}
			
			int cnt=1;
			
			for(i=0; i<3; i++) {
				for(j=0; j<3; j++) {
					for(k=0; k<eventBlockCnt; k++) {
						if(cnt == eventBlockNum[k]) {
							break;
						}
					}
					if(k==eventBlockCnt) {
						NormalBlock nomalBlock = new NormalBlock(j*300+5, i*160+5, 300, 160);
					}
					else {
						EventBlock eventBlock = new EventBlock(j*300+5, i*160+5, 300, 160);
					}
					cnt++;
				}
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
			
			
		}

		abstract class Block extends JPanel {
			public abstract void paintComponent(Graphics g);
		}

		class NormalBlock extends Block {
			int x, y, w, h;

			NormalBlock() {}

			NormalBlock(int x, int y, int w, int h) {
				System.out.println("¿œπ›");
				this.x = x;
				this.y = y;
				this.w = w;
				this.h = h;
			}

			@Override
			public void paintComponent(Graphics g) {
				// TODO Auto-generated method stub\
				Graphics2D g2 = (Graphics2D) g;

				GradientPaint gp = new GradientPaint((int) ((x + w) / 2), y, Color.WHITE, (int) ((x + w) / 2),
						(int) ((y + h) / 2), Color.BLACK);

				g.setColor(new Color(68, 31, 8));
				g.fillRoundRect(x, y, w, h, 30, 30);

				g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 0));
				g2.setPaint(gp);
				g2.drawRoundRect(x, y, w, h, 30, 30);
			}
		}

		class EventBlock extends Block {
			int x, y, w, h;

			EventBlock(){}

			EventBlock(int x, int y, int w, int h){
				this.x = x;
				this.y = y;
				this.w = w;
				this.h = h;
			}

			@Override
			public void paintComponent(Graphics g) {
				// TODO Auto-generated method stub\
				Graphics2D g2 = (Graphics2D) g;

				GradientPaint gp = new GradientPaint((int) ((x + w) / 2), y, Color.WHITE, (int) ((x + w) / 2),
						(int) ((y + h) / 2), Color.BLACK);

				g.setColor(new Color(249, 164, 39));
				g.fillRoundRect(x, y, w, h, 30, 30);

				g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 0));
				g2.setPaint(gp);
				g2.drawRoundRect(x, y, w, h, 30, 30);
			}
		}

	}

	public class EndUI extends JPanel {
		public BufferedImage backImage;

		EndUI() {
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
	BrickBreakGame() {
		UI ui = new UI();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BrickBreakGame();
	}

}
