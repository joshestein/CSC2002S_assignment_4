package treeGrow;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class TreeGrow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static ForestPanel fp;
	static final ForkJoinPool commonPool = new ForkJoinPool();
	static volatile boolean run_simulation;
	static volatile int year;
	static JLabel yearLabel = new JLabel("Year: ");

	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	public static void setupGUI(int frameX,int frameY,Tree [] trees) {
		Dimension fsize = new Dimension(800, 800);
		// Frame init and dimensions
    	JFrame frame = new JFrame("Photosynthesis"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setPreferredSize(fsize);
    	frame.setSize(800, 800);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setPreferredSize(fsize);
 
		fp = new ForestPanel(trees);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		JScrollPane scrollFrame = new JScrollPane(fp);
		fp.setAutoscrolls(true);
		scrollFrame.setPreferredSize(fsize);
		g.add(scrollFrame);
		
		JButton resetBtn = new JButton("Reset");
		JButton pauseBtn = new JButton("Pause");
		JButton playBtn = new JButton("Play");
		playBtn.setEnabled(false);
		JButton endBtn = new JButton("End");

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttons.add(yearLabel);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(resetBtn);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(pauseBtn);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(playBtn);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(endBtn);

		g.add(buttons);

		resetBtn.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e){
				commonPool.invoke(new Reset(0, trees.length, trees));
				year = 0;
			}
		});
		pauseBtn.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e){
				//commonPool().shutdownNow();
				playBtn.setEnabled(true);
				pauseBtn.setEnabled(false);
				try{
					Thread.sleep(500); //Wait for thread to shutdown
				} catch(InterruptedException f){
					f.printStackTrace();
				}
				run_simulation = false;
			}
		});
		playBtn.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e){
				run_simulation = true;
				pauseBtn.setEnabled(true);
				playBtn.setEnabled(false);
			}
		});
		endBtn.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
        frame.setVisible(true);
		Thread fpt = new Thread(fp);
		fpt.start();
	}
	
	public static void main(String[] args) {
		SunData sundata = new SunData();
		run_simulation = true;
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java treeGrow.java intputfilename");
			System.exit(0);
		}
				
		// read in forest and landscape information from file supplied as argument
		sundata.readData(args[0]);
		System.out.println("Data loaded");
		
		frameX = sundata.sunmap.getDimX();
		frameY = sundata.sunmap.getDimY();

		/*SwingWorker reset_worker = new SwingWorker<Boolean, Void>(){
			@Override
			public Boolean doInBackground(){
				commonPool.invoke(new Reset(0, sundata.trees.length, sundata.trees)); //reset tree extents
				return true;
			}
			
			@Override
			public void done(){
				//Thread fpt = new Thread(fp);
				//fpt.start();
			}
		};
		reset_worker.execute();*/

/* 		Runnable runner = new Runnable(){
			@Override
			public void run() {
				setupGUI(frameX, frameY, sundata.trees);
				SwingWorker worker = new SwingWorker () {

					protected String doInBackground() throws InterruptedException {
						commonPool.invoke(new Reset(0, sundata.trees.length, sundata.trees)); //reset tree extents
						return null;
					}

					protected void done(){
						Thread fpt = new Thread(fp);
						fpt.start();
					}
				};
				worker.execute();
			}
		}; 
		EventQueue.invokeLater(runner);
		*/
/* 		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run(){
					setupGUI(frameX, frameY, sundata.trees);
				}
			});
	 	} catch (Exception e){
			 System.out.println("Error setting up GUI");
		} */
		
		setupGUI(frameX, frameY, sundata.trees);
		commonPool.invoke(new Reset(0, sundata.trees.length, sundata.trees)); //reset tree extents
		// create and start simulation loop here as separate thread
		float time;
		while (true) {
			if (run_simulation == true){
				tick();
				System.out.println("Year: "+year);
				for (int i = 20; i > 0; i = i-2){
					//TODO: implement splitting of trees into layers in parallel
					ArrayList<Tree> temp_layer = new ArrayList<Tree>();
					for (int j = 0; j < sundata.trees.length; j++){
						if (sundata.trees[j].inrange(i-2, i)) {
							temp_layer.add(sundata.trees[j]);
						} 
					}
					//Start new thread processing with arraylist of trees
					commonPool.invoke(new SimulateLayer(0, temp_layer.size(), temp_layer, sundata.sunmap));
				}
				time = tock();
				System.out.println("Time: "+time);
				sundata.sunmap.resetShade();
				year++;
				yearLabel.setText("Year: "+year);
			} else {
				continue;
			}
		}

	}
}