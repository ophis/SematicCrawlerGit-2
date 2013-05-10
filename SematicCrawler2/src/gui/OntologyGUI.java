package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import crawler.Crawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class OntologyGUI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ActionListener startBtnListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String storage = storageField.getText();
			String type = typeField.getText();
			String url = urlField.getText();
			String crawlStorageFolder = storage;
			
			final int numberOfCrawlers = 1;
			Crawler.OntologyType = type;
			final String seed = url;
			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(crawlStorageFolder);
			config.setPolitenessDelay(8);
			config.setMaxDepthOfCrawling(1);
			config.setMaxPagesToFetch(1);
			config.setResumableCrawling(false);
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
			
			try {
				final CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
				new Thread() {
					public void run() {
						controller.addSeed(seed);
						controller.start(Crawler.class, numberOfCrawlers);
					}
				}.start();	
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		}
	};
	
	JTextField storageField = new JTextField();
	JTextField typeField = new JTextField();
	JTextField urlField = new JTextField();
	JButton btn = new JButton("Start");
	ConsoleTextArea consoleArea;
	JScrollPane panelOutput = new JScrollPane(consoleArea);
    
	public OntologyGUI() throws IOException {
		super("OntologyGUI");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		consoleArea = new ConsoleTextArea();
		storageField.setBounds(0, 2, 300, 25);
		storageField.setText("/Volumes/Mac Storage/crawlerTest");
		typeField.setBounds(0, 30, 70, 25);
		typeField.setText("电子商务");
		urlField.setBounds(75, 30, 200, 25);
		btn.setBounds(280, 30, 70, 25);
		btn.addActionListener(startBtnListener);
		consoleArea.setLineWrap(true);        //激活自动换行功能 
		consoleArea.setWrapStyleWord(true);            // 激活断行不断字功能
		JScrollPane panelOutput;
	    panelOutput = new JScrollPane(consoleArea);
	    panelOutput.setBounds(5, 60, 380, 260);
	    
		//add controllers to main panel
		mainPanel.add(panelOutput);
		mainPanel.add(typeField);
		mainPanel.add(storageField);
		mainPanel.add(urlField);
		mainPanel.add(btn);
		add(mainPanel);
		setSize(400,350);
		setVisible(true);	
	}
	
	static OntologyGUI ontologyGUI;
	public static void main(String args[]) throws IOException{
		ontologyGUI = new OntologyGUI();
	}
} 
