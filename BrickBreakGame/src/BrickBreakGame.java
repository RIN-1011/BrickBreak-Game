import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

abstract class Block extends JPanel { // 추상클래스
	public abstract void paintBlock(Graphics g, int i, int j, int x, int y, int w, int h);
}

class NormalBlock extends Block {
	Point pointLT, pointRT, pointLB, pointRB;
	
	NormalBlock(Graphics g, int i, int j, int x, int y, int w, int h) {
		paintBlock(g, i, j, x, y, w, h);
		pointBlock(x, y, w, h);
	}

	@Override
	public void paintBlock(Graphics g, int i, int j, int x, int y, int w, int h) {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D) g;

		GradientPaint gp = new GradientPaint((int) ((j * 320 + 5) / 2), (int) (i * 160 + 5), Color.WHITE,
				(int) ((j * 320 + 5) / 2), (int) ((i + 1) * 160 + 5), Color.BLACK);

		g.setColor(new Color(68, 31, 8));
		g.fillRoundRect(x, y + 5, w, h, 30, 30);

		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 0));
		g2.setPaint(gp);
		g2.drawRoundRect(x, y + 5, w, h, 30, 30);
	}
	public void pointBlock(int x, int y, int w, int h) {
		pointLT = new Point(x, y+5);
		pointRT = new Point(x+320, y+5);
		pointLB = new Point(x, y+165);
		pointRB = new Point(x+320, y+165);
	}
}

class EventBlock extends Block {
	
	EventBlock(Graphics g, int i, int j, int x, int y, int w, int h) {
		paintBlock(g, i, j, x, y, w, h);
	}

	@Override
	public void paintBlock(Graphics g, int i, int j, int x, int y, int w, int h) {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D) g;

		GradientPaint gp = new GradientPaint((int) ((j * 320 + 5) / 2), (int) (i * 160 + 5), Color.WHITE,
				(int) ((j * 320 + 5) / 2), (int) ((i + 1) * 160 + 5), Color.BLACK);

		g.setColor(new Color(249, 164, 39));
		g.fillRoundRect(x, y + 5, w, h, 30, 30);

		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 0));
		g2.setPaint(gp);
		g2.drawRoundRect(x, y + 5, w, h, 30, 30);
	}
}

public class BrickBreakGame extends JFrame {
	public TitleUI titleUI = new TitleUI();
	public MainUI mainUI = new MainUI();
	public EndUI endUI = new EndUI();
	Clip startClip, endClip, bounceClip;
	Container c;
	CardLayout card;
	Thread mainThread, startThread, endThread;

	int flag = 2, blockCnt = 0;
	float ballX = 495, ballY = 845;
	float ballvx, ballvy;
	float racketX = 370, racketY = 870;
	float racketvx = 0;
	int speed;

	BrickBreakGame() {
		setTitle("BrickBreakGame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		c = getContentPane();
		card = new CardLayout();

		c.setLayout(card);

		add(titleUI, "title");
		add(mainUI, "main");
		add(endUI, "end");

		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(new MyKeyListener());

		setSize(1000, 1000);
		setVisible(true);
	}

	class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
				if (flag == 1) {
					ballX = 495; ballY = 845;
					ballvx = 10; ballvy = -speed;
					racketX = 370; racketY = 870;
					racketvx = 0;
					
					endClip.stop();
					card.show(c, "title");
					try {
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
					startClip.start();
					
					flag = 2;
				} else if (flag == 2) {
					startClip.stop();
					card.show(c, "main");
					
					mainThread = new Thread(mainUI);
					mainThread.start();
				} else if (flag == 3) {
					flag = 1;

					mainThread = new Thread(mainUI);
					mainThread.start();
				}
			}
			if (flag == 2) {
				switch (arg0.getKeyCode()) {
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
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if (flag == 2) {
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
		}

	}

	//////////////////////////// 시작 화면//////////////////////////////////

	class TitleUI extends JPanel {
		public BufferedImage backImage;
		JLabel titleLabel1, titleLabel2, titleLabel3, flickeringLabel;

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
						return;
					}
				}
			}
		}
	}

////////////////////////////메인 화면//////////////////////////////////

	class MainUI extends JPanel implements Runnable{
		public BufferedImage backImage;

		float r = 12;
		int eventBlockCnt = 0;
		int eventBlockNum[];

		LinkedList<NormalBlock> normalList = new LinkedList<NormalBlock>();
		LinkedList<EventBlock> eventList = new LinkedList<EventBlock>();

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
			
			paintBlock(g);

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

		public void paintBlock(Graphics g) {
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

					if (k == eventBlockCnt && blockCnt < 9) {
						NormalBlock normalBlock = new NormalBlock(g, i, j, x, y, w, h);
						normalList.add(normalBlock);
						blockCnt++;
					} else if(k != eventBlockCnt && blockCnt < 9) {
						EventBlock eventBlock = new EventBlock(g, i, j, x, y, w, h);
						eventList.add(eventBlock);
						blockCnt++;
					}
					cnt++;
				}
			}
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					System.out.println("메인 스레드 종료!");
					return;
				}
				updatePositionRacket();
				updatePositionBall();
				System.out.println("끝");
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

			if (ballX < 17 || ballX > 985 - r * 2) {
				ballvx = -ballvx;
			}
			if (ballY < 17) {
				ballvy = -ballvy;
			}

			if (ballY > 953) {
				mainThread.interrupt();
				try {
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
				endClip.start();
				card.show(c, "end");
				flag = 1;
			}
			if (racketX - r < ballX && ballX < racketX + 250 - r && racketY - r * 2 < ballY
					&& racketY - r * 2 + 30 > ballY) {
				try {
					bounceClip = AudioSystem.getClip();
					File audioFile = new File("Bounce.wav");
					AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
					bounceClip.open(audioStream);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bounceClip.start();
				ballvy = -ballvy;

				if (ballX < racketX - r + 250 / 5) {
					ballvx = -12;
				}
				if (ballX > racketX - r + 250 / 5 && ballX < racketX - r + 250 / 5 * 2) {
					ballvx = -6;
				}
				if (ballX > racketX - r + 250 / 5 * 3 && ballX < racketX - r + 250 / 5 * 4) {
					ballvx = 6;
				}
				if (ballX > racketX - r + 250 / 5 * 4 && ballX < racketX - r + 250) {
					ballvx = 12;
				}
			}
			
			ListIterator<NormalBlock> normalIt = normalList.listIterator();
			for (;normalIt.hasNext();) {
				NormalBlock nb = normalIt.next();
				System.out.println(nb.pointLB.x +" " + nb.pointRB.x);
				/*if((ballX >= nb.pointLB.x)&&(ballX <= nb.pointRB.x)
						&&(ballY == nb.pointLB.y)) {
					ballvy = -ballvy;
					normalIt.remove();
					paint=1;
				}*/
				
			}
		}
	}

	//////////////////////////// 종료 화면//////////////////////////////////

	class EndUI extends JPanel {
		public BufferedImage backImage;
		JLabel endLabel1, flickeringLabel;

		EndUI() {
			try {
				backImage = ImageIO.read(new File("BackImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setLayout(null);

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