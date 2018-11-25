package packageRunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class PathRunnerFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PathRunnerFrame frame = new PathRunnerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PathRunnerFrame() {
		setTitle("Path Runneeeerr");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 550, 393);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnAction = new JMenu("Action");
		menuBar.add(mnAction);

		JMenuItem mntmDrawPath = new JMenuItem("Draw Path");

		JMenuItem mntmInitialize = new JMenuItem("Initialize");

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		PathBoard panel = new PathBoard();

		mntmInitialize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.setInitialLocationFlag();
			}
		});
		mnAction.add(mntmDrawPath);
		mnAction.add(mntmInitialize);

		JCheckBoxMenuItem chckbxmntmErase = new JCheckBoxMenuItem("Erase");
		chckbxmntmErase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.toggleDrwingColor();
			}
		});
		mnAction.add(chckbxmntmErase);

		JCheckBoxMenuItem chckbxmntmAi = new JCheckBoxMenuItem("AI");
		chckbxmntmAi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.toggleUserAndAI();
			}
		});
		mnAction.add(chckbxmntmAi);
		contentPane.add(panel, BorderLayout.CENTER);
		pack();
	}
}

class PathBoard extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage boardImage;
	private boolean useKeyInsteadAI;
	TrainingSetElement tse = null;
	private Graphics gBoardImage;
	private int i, j, k;
	private boolean initialized;
	PathRunner pr;
	int LR = 0;
	private int pathRunnerInitialLocationX;
	private int pathRunnerInitialLocationY;
	private Color drawingColor, backgroundColor, currentColor;
	private int headingToX;
	private int headingToY;
	private int widthHalf;
	Thread tt = null;
	private double m, t, doubleNumber;
	public ArrayList<TrainingSetElement> trainingSet = null;
	private int bitBuilder = 0;
	private int xx, yy, zz;
	public int lapsDone;
	public NeuralNetwork PRNN;

	PathBoard() {
		PRNN = NeuralNetwork.createFromFile(
				"C:\\Users\\basly\\OneDrive\\Documents\\NetBeansProjects\\NeurophProject1\\Neural Networks\\PathRunnerNN15.nnet");
		System.out.println(PRNN.getLayersCount() + " is number of layers");

		m = t = doubleNumber = 0.0d;
		tt = new Thread(new TimerThread(this));
		useKeyInsteadAI = true;
		headingToX = 0;
		headingToY = 0;
		drawingColor = Color.DARK_GRAY;
		currentColor = drawingColor;
		backgroundColor = Color.LIGHT_GRAY;
		pr = new PathRunner(-10, -10, 5);
		widthHalf = 5 / 2;
		pathRunnerInitialLocationX = -10;
		pathRunnerInitialLocationY = -10;
		initialized = false;
		setFocusable(true);
		addMouseMotionListener(new MouseMotionAdapter() {
			private int xDivided;
			private int yDivided;
			private int xCandidate1;
			private int xCandidate2;
			private int yCandidate1;
			private int yCandidate2;
			private int xPersist;
			private int yPersist;

			@Override
			public void mouseDragged(MouseEvent e) {
				if (initialized == false) {
					xPersist = e.getX();
					yPersist = e.getY();
					xDivided = xPersist / 20;
					yDivided = yPersist / 30;
					xCandidate1 = xDivided * 20;
					xCandidate2 = (xDivided + 1) * 20;
					if (xDivided % 2 == yDivided % 2) {
						yCandidate1 = yDivided * 30;
						yCandidate2 = (yDivided + 1) * 30;
					} else {
						yCandidate1 = (yDivided + 1) * 30;
						yCandidate2 = yDivided * 30;
					}
					if ((Math.pow(xPersist - xCandidate1, 2)
							+ Math.pow(yPersist - yCandidate1, 2)) < (Math.pow(xPersist - xCandidate2, 2)
									+ Math.pow(yPersist - yCandidate2, 2))) {
						drawHexagon(xCandidate1, yCandidate1);
					} else {
						drawHexagon(xCandidate2, yCandidate2);
					}
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (initialized == true) {
					initialized = false;
					initializeRunner(e.getX(), e.getY());
					// maybe sleep the runner for a bit here
				}
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 37) {
					LR = -3;
					// System.out.println("key pressed");
				} else if (e.getKeyCode() == 39)
					LR = 3;
				else if (e.getKeyCode() == 38)
					LR = 0;
			}
		});
		setPreferredSize(new Dimension(1700, 850));
		boardImage = new BufferedImage(this.getPreferredSize().width, this.getPreferredSize().height,
				BufferedImage.TYPE_INT_ARGB);
		gBoardImage = boardImage.getGraphics();
		gBoardImage.setColor(backgroundColor);
		gBoardImage.fillRect(0, 0, this.getPreferredSize().width + 1, this.getPreferredSize().height + 1);
		trainingSet = new ArrayList<>(5000);
		lapsDone = 0;
	}

	public void toggleUserAndAI() {
		if (useKeyInsteadAI == false) {
			useKeyInsteadAI = true;
		} else
			useKeyInsteadAI = false;
	}

	public void initializeRunner(int x, int y) {

		pathRunnerInitialLocationX = x;
		pathRunnerInitialLocationY = y;
		headingToX = x;
		headingToY = y - 2500;
		pr.locationX = x;
		pr.locationY = y;
		// initialize AI or keyboard access
		tt.start();

		//
		// Thread ait = new Thread(new AIThread(this));
		// ait.start();
	}

	public void toggleDrwingColor() {
		if (currentColor.getRGB() == drawingColor.getRGB()) {
			currentColor = backgroundColor;
		} else {
			currentColor = drawingColor;
		}
	}

	public void drawHexagon(int x, int y) {
		gBoardImage.setColor(currentColor);
		gBoardImage.fillRect(x - 20, y - 10, 41, 21);
		for (i = 0; i < 10; ++i) {
			gBoardImage.drawLine(x - (i * 2), y - 20 + i, x + (i * 2), y - 20 + i);
			gBoardImage.drawLine(x - (i * 2), y + 20 - i, x + (i * 2), y + 20 - i);
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);
		g.drawImage(boardImage, 0, 0, null);
		g.drawImage(pr.pathRunnerImage, pr.locationX - widthHalf, pr.locationY - widthHalf, null);
		g.setColor(Color.YELLOW);
		g.drawLine(pr.locationX, pr.locationY, headingToX, headingToY);
		// g.fillRect(headingToX - 2, headingToY - 2, 5, 5);
		// gBoardImage.setColor(Color.green);
		// gBoardImage.fillRect(headingToX - 2, headingToY - 2, 5, 5);
	}

	public void setInitialLocationFlag() {
		initialized = true;
	}

	public TrainingSetElement createLearningData() {
		TrainingSetElement tempElement = new TrainingSetElement();
		tempElement.output[0] = LR / 3;
		zz = -1;
		if (pr.angle >= 45 && pr.angle < 135) { // going down
			tempElement.input[++zz] = (pr.angle - 44) * 91;
			for (xx = 120; xx > -121; xx -= 10) {
				bitBuilder = 0;
				for (yy = 120; yy > -1; yy -= 10) {
					bitBuilder = bitBuilder << 1;
					if ((pr.locationX + xx < 1700) && (pr.locationX + xx > -1) && (pr.locationY + yy < 850)
							&& boardImage.getRGB(pr.locationX + xx, pr.locationY + yy) == drawingColor.getRGB()) {
						bitBuilder = bitBuilder | 1;
					}
				}
				tempElement.input[++zz] = bitBuilder + 1;
			}
			// trainingSet.add(tempElement);
		} else if (pr.angle >= 135 && pr.angle < 225) { // going left
			tempElement.input[++zz] = (pr.angle - 134) * 91;
			for (yy = 120; yy > -121; yy -= 10) {
				bitBuilder = 0;
				for (xx = 120; xx > -1; xx -= 10) {
					bitBuilder = bitBuilder << 1;
					if ((pr.locationX - xx > -1) && (pr.locationY + yy < 850) && (pr.locationY + yy > -1)
							&& boardImage.getRGB(pr.locationX - xx, pr.locationY + yy) == drawingColor.getRGB()) {
						bitBuilder = bitBuilder | 1;
					}
				}
				tempElement.input[++zz] = bitBuilder + 1;
			}
			// trainingSet.add(tempElement);
		} else if (pr.angle >= 225 && pr.angle < 315) { // going up
			tempElement.input[++zz] = (pr.angle - 224) * 91;
			for (xx = 120; xx > -121; xx -= 10) {
				bitBuilder = 0;
				for (yy = 120; yy > -1; yy -= 10) {
					bitBuilder = bitBuilder << 1;
					if ((pr.locationX - xx > -1) && (pr.locationX - xx < 1700) && (pr.locationY - yy > -1)
							&& boardImage.getRGB(pr.locationX - xx, pr.locationY - yy) == drawingColor.getRGB()) {
						bitBuilder = bitBuilder | 1;
					}
				}
				tempElement.input[++zz] = bitBuilder + 1;
			}
			// trainingSet.add(tempElement);
		} else { // this is when going to the right
			if (pr.angle >= 315) {
				tempElement.input[++zz] = (pr.angle - 314) * 91;
			} else {
				tempElement.input[++zz] = (pr.angle + 46) * 91;
			}
			for (yy = 120; yy > -121; yy -= 10) {
				bitBuilder = 0;
				for (xx = 120; xx > -1; xx -= 10) {
					bitBuilder = bitBuilder << 1;
					if ((pr.locationX + xx < 1700) && (pr.locationY - yy > -1) && (pr.locationY - yy < 850)
							&& boardImage.getRGB(pr.locationX + xx, pr.locationY - yy) == drawingColor.getRGB()) {
						bitBuilder = bitBuilder | 1;
					}
				}
				tempElement.input[++zz] = bitBuilder + 1;
			}
			// trainingSet.add(tempElement);
		}
		return tempElement;
	}

	public boolean calculateNextMove() {
		// this is where we introduce the training data creator
		tse = createLearningData();
		// for (int tni = 0; tni < 26; ++tni) {
		// System.out.print(tse.input[tni] + ", ");
		// }
		// System.out.println();
		PRNN.setInput(tse.input[0], tse.input[1], tse.input[2], tse.input[3], tse.input[4], tse.input[5], tse.input[6],
				tse.input[7], tse.input[8], tse.input[9], tse.input[10], tse.input[11], tse.input[12], tse.input[13],
				tse.input[14], tse.input[15], tse.input[16], tse.input[17], tse.input[18], tse.input[19], tse.input[20],
				tse.input[21], tse.input[22], tse.input[23], tse.input[24], tse.input[25]);
		PRNN.calculate();
		System.out.println(PRNN.getOutput()[0] + "  " + tse.input[0] + " " + tse.input[1] + " " + tse.input[2] + " "
				+ tse.input[3] + " " + tse.input[4] + " " + tse.input[5] + " " + tse.input[6] + " " + tse.input[7] + " "
				+ tse.input[8] + " " + tse.input[9] + " " + tse.input[10] + " " + tse.input[11] + " " + tse.input[12]
				+ " " + tse.input[13] + " " + tse.input[14] + " " + tse.input[15] + " " + tse.input[16] + " "
				+ tse.input[17] + " " + tse.input[18] + " " + tse.input[19] + " " + tse.input[20] + " " + tse.input[21]
				+ " " + tse.input[22] + " " + tse.input[23] + " " + tse.input[24] + " " + tse.input[25]);
		int tempLR = (int) Math.round(PRNN.getOutput()[0]);
		LR = tempLR * 3;
		System.out.println(LR);
		if (LR != 0) {
			pr.angle = pr.angle + LR;
			LR = 0;
			if (pr.angle > 359)
				pr.angle = pr.angle - 360;
			else if (pr.angle < 0)
				pr.angle = 360 + pr.angle;
			headingToX = (int) Math.round(2500d * Math.cos(Math.toRadians(pr.angle))) + pr.locationX;
			headingToY = (int) Math.round(2500d * Math.sin(Math.toRadians(pr.angle))) + pr.locationY;
			if (headingToX - pr.locationX != 0)
				m = (double) (headingToY - pr.locationY) / (headingToX - pr.locationX);
			if (m != 0)
				t = 1 / m;
			if (m >= -1 && m <= 1)
				doubleNumber = pr.locationY;
			else
				doubleNumber = pr.locationX;
		}
		// System.out.println(pr.angle);
		//
		if (pr.angle == 90 || pr.angle == 270) {
			// System.out.println("turned");
			if (pr.angle == 90)
				pr.locationY += 1;
			else
				pr.locationY -= 1;
		} else if (m >= -1 && m <= 1) {
			// System.out.println("turned");
			if (pr.locationX < headingToX) {
				pr.locationX += 1;
				doubleNumber += m;
				// pr.locationY = (int) (doubleNumber + 1.0d);
			} else {
				pr.locationX -= 1;
				doubleNumber -= m;
				// pr.locationY = (int) (doubleNumber);
			}

			pr.locationY = (int) Math.round(doubleNumber);
		} else {
			// System.out.println("turned");
			if (pr.locationY < headingToY) {
				pr.locationY += 1;
				doubleNumber += t;
				// pr.locationX = (int) (doubleNumber + 1.0d);
			} else {
				pr.locationY -= 1;
				doubleNumber -= t;
				// pr.locationX = (int) (doubleNumber);
			}
			pr.locationX = (int) Math.round(doubleNumber);
		}
		return true;
	}

	public boolean isOnTrack() {
		for (j = pr.locationX - widthHalf; j <= pr.locationX + widthHalf; ++j) {
			for (k = pr.locationY - widthHalf; k <= pr.locationY + widthHalf; ++k) {
				if (boardImage.getRGB(j, k) == backgroundColor.getRGB()) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean reachedDestination() {
		if (pr.locationY == pathRunnerInitialLocationY && Math.abs(pr.locationX - pathRunnerInitialLocationX) < 21) {
			System.out.println("a lap");
			return true;
		}
		return false;
	}

	public void saveTraining() {
		System.out.println("Saving trainingSet");
		DataSet ds = new DataSet(26, 1);
		for (TrainingSetElement dData : trainingSet) {
			ds.addRow(new DataSetRow(dData.input, dData.output));
		}
		ds.saveAsTxt(".\\pzRnrTestDataSet2", ",");
		System.out.println("saved " + ds.size() + " done");
	}
}

class PathRunner {
	int locationX;
	int locationY;
	BufferedImage pathRunnerImage;
	private Graphics gPathRunnerImage;
	int angle;
	int sideLength;

	PathRunner(int initialLocationX, int initialLocationY, int sideLength) {
		locationX = initialLocationX;
		locationY = initialLocationY;
		angle = 270;
		this.sideLength = sideLength;
		pathRunnerImage = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_ARGB);
		gPathRunnerImage = pathRunnerImage.getGraphics();
		gPathRunnerImage.setColor(Color.RED);
		gPathRunnerImage.fillRect(0, 0, sideLength, sideLength);
	}
}

class TimerThread implements Runnable {
	private int sleepLength;
	PathBoard pb = null;
	private int counter = 0;

	TimerThread(PathBoard pb) {
		sleepLength = 50;
		this.pb = pb;
	}

	@Override
	public void run() {
		boolean x;
		while (pb.isOnTrack()) {

			x = pb.calculateNextMove();

			pb.repaint();

			try {
				Thread.sleep(sleepLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (pb.reachedDestination()) {
				pb.lapsDone += 1;
			}
			// if (pb.lapsDone == 3) {
			// pb.saveTraining();
			// break;
			// }
		}
		System.out.println("Thread finished");
	}
}

class AISolver {
	byte[][] paths;
	int iterations;
	int[][] whereIsIt;
	PathBoard pb;

	AISolver(PathBoard board) {
		paths = new byte[100][1_445_000];
		iterations = 0;
		whereIsIt = new int[100][3];
		pb = board;
		for (int i = 0; i < whereIsIt.length; ++i) {
			whereIsIt[i][0] = pb.pr.locationX;
			whereIsIt[i][1] = pb.pr.locationY;
			whereIsIt[i][2] = pb.pr.angle;
		}
		// calculateNext60(paths[0], pb.pr.locationX, pb.pr.locationY, pb.pr.angle);
	}

	public boolean calculateNext60(byte[] path, int locationX, int locationY, int degree) {
		// Thread thr = new Thread(new AIThread(this));

		int x;
		int[][] the60 = new int[100][60];
		for (int i = 0; i < the60.length; ++i) {
			for (int j = 0; j < the60[i].length; ++j) {
				x = (int) (Math.random() * 3);
				the60[i][j] = -3 + (x * 3);
				pb.LR = the60[i][j];
				// thr.start();
			}
		}

		return true;
	}

}

class AIThread implements Runnable {
	PathBoard pb = null;
	AISolver ais = null;

	AIThread(PathBoard pb) {
		this.pb = pb;
		ais = new AISolver(this.pb);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int x;
		int[][] the60 = new int[100][60];
		for (int i = 0; i < the60.length; ++i) {
			for (int j = 0; j < the60[i].length; ++j) {
				x = (int) (Math.random() * 3);
				the60[i][j] = -3 + (x * 3);
				pb.LR = the60[i][j];
				System.out.println(x);

				pb.calculateNextMove();
				pb.repaint();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}

class TrainingSetElement {
	public double[] input = new double[26];
	public double[] output = new double[1];
}