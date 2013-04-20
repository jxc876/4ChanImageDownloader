/*
* Copyright (C) 2013 Mohatu.net
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package net.mohatu.chan;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;

public class MainView {

	private JFrame frmchanImageDownloader;
	private JTextField threadLink;
	private static JButton btnSettings;
	private static JButton btnClear;
	private static JButton btnDownload;
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
	private String version = "v1.4";
	private String re1 = "(boards\\.4chan\\.org)";
	private String re2 = "(\\/)";
	private String re3 = "((?:[a-z][a-z0-9_]*))";
	private String re4 = "(\\/)";
	private String re5 = "(res)";
	private String re6 = "(\\/)";
	private String re7 = "(\\d+)";

	Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5 + re6 + re7,
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Matcher matcher;

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
		try {
			UIManager.setLookAndFeel(UIManager
					.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		frmchanImageDownloader = new JFrame();
		frmchanImageDownloader.setResizable(false);
		frmchanImageDownloader.setTitle("4Chan Image Downloader " + version);
		frmchanImageDownloader.setBounds(100, 100, 587, 430);
		frmchanImageDownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frmchanImageDownloader.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		threadLink = new JTextField();
		threadLink.setBounds(93, 8, 375, 20);
		panel.add(threadLink);
		threadLink.setColumns(10);

		JLabel lblThreadLink = new JLabel("Thread Link:");
		lblThreadLink.setBounds(10, 11, 102, 14);
		panel.add(lblThreadLink);

		btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnSettings.setEnabled(false);
				btnClear.setEnabled(false);
				btnDownload.setEnabled(false);
				// Button starts download
				matcher = p.matcher(threadLink.getText().toString());
				if (matcher.find()) {
					progressBar.setIndeterminate(true);
					statusLabel.setText("Generating Links...");
					GetImageLinks gil = new GetImageLinks();
					gil.setThreadLink(threadLink.getText());
					Thread getImages = new Thread(gil);
					getImages.start();
				} else {
					JOptionPane.showMessageDialog(MainView.scrollPane,
							"Please enter a valid thread URL");
				}
			}
		});
		btnDownload.setBounds(473, 7, 98, 23);
		panel.add(btnDownload);

		table = new JTable(1, 1);
		table.setModel(images);

		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 70, 561, 324);
		panel.add(scrollPane);

		statusLabel = new JLabel("Idle...");
		statusLabel.setBounds(10, 39, 244, 20);
		panel.add(statusLabel);

		progressBar = new JProgressBar();
		progressBar.setBounds(215, 36, 146, 23);
		panel.add(progressBar);

		btnSettings = new JButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsView settings = new SettingsView();
				Thread set = new Thread(settings);
				set.start();
			}
		});
		btnSettings.setBounds(473, 36, 98, 23);
		panel.add(btnSettings);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				images.setRowCount(0);
			}
		});
		btnClear.setBounds(370, 36, 98, 23);
		panel.add(btnClear);
	}

	public static void setImageSize(boolean bool, int w, int h) {
		MainView.imgHeight = h;
		MainView.imgWidth = w;
		MainView.customSize = bool;
	}

	public static void enableButtons() {
		btnSettings.setEnabled(true);
		btnClear.setEnabled(true);
		btnDownload.setEnabled(true);
	}
}
