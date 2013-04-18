package net.mohatu.chan;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JProgressBar;


public class MainView {

	private JFrame frmchanImageDownloader;
	private JTextField threadLink;
	public static JTable table;
	public static JProgressBar progressBar;
	public static DefaultTableModel images = new DefaultTableModel(
			new Object[] { "Image Link", "Progress" }, 0);
	public ArrayList<String> imageLinks = new ArrayList<String>();
	public static JLabel statusLabel;
	public static JScrollPane scrollPane;
	public static int imgWidth;
	public static int imgHeight;
	public static boolean customSize = false;
	private String version = "v1.3";


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frmchanImageDownloader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmchanImageDownloader = new JFrame();
		frmchanImageDownloader.setResizable(false);
		frmchanImageDownloader.setTitle("4Chan Image Downloader "+version);
		frmchanImageDownloader.setBounds(100, 100, 598, 465);
		frmchanImageDownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmchanImageDownloader.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		threadLink = new JTextField();
		threadLink.setBounds(93, 8, 370, 20);
		panel.add(threadLink);
		threadLink.setColumns(10);
		
		JLabel lblThreadLink = new JLabel("Thread Link:");
		lblThreadLink.setBounds(21, 11, 91, 14);
		panel.add(lblThreadLink);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				progressBar.setIndeterminate(true);
				//Button that starts the fun
				statusLabel.setText("Generating Links...");
				GetImageLinks gil = new GetImageLinks();
				gil.setThreadLink(threadLink.getText());
				Thread getImages = new Thread(gil);
				getImages.start();
				
			}
		});
		btnDownload.setBounds(473, 7, 98, 23);
		panel.add(btnDownload);
		
		table = new JTable(1, 1);
		table.setModel(images);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(21, 70, 550, 324);
		panel.add(scrollPane);
		
		statusLabel = new JLabel("Idle...");
		statusLabel.setBounds(20, 405, 233, 14);
		panel.add(statusLabel);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(263, 405, 308, 21);
		panel.add(progressBar);
		
		JButton btnSettings = new JButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsView settings = new SettingsView();
				Thread set = new Thread(settings);
				set.start();
			}
		});
		btnSettings.setBounds(473, 36, 98, 23);
		panel.add(btnSettings);
	}
	
	public static void setImageSize(boolean bool, int w, int h) {
		MainView.imgHeight = h;
		MainView.imgWidth = w;
		MainView.customSize = bool;
	}
	
}
