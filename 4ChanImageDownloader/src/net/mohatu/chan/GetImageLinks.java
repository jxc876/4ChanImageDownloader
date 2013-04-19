package net.mohatu.chan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jsoup.*;

public class GetImageLinks implements Runnable {
	private String threadLink = "http://www.google.com";
	private List<String> allMatches = new ArrayList<String>();
	private int threadNumber = 0;
	private int imageSkip = 0;

	@Override
	public void run() { // Main runnable class
		// TODO Auto-generated method stub
		String txt = "";
		getThreadNumber();
		try { // Connect to thread URL using Mozilla 5 userAgent. 4Chan.org
				// blocks automated requests, this is a bypass.
			txt = Jsoup
					.connect(threadLink)
					.userAgent(
							"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
					.referrer("http://www.google.com").get().toString();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (IllegalArgumentException iae){
			JOptionPane.showMessageDialog(MainView.scrollPane,
					"Please enter a valid thread URL");
		}

		String re3 = "(images\\.4chan\\.org)"; // Fully Qualified Domain Name 1
		String re4 = "(\\/)"; // Any Single Character 3
		String re5 = "((?:[a-z][a-z0-9_]*))"; // Variable Name 1
		String re6 = "(\\/)"; // Any Single Character 4
		String re7 = "((?:[a-z][a-z0-9_]*))"; // Variable Name 2
		String re8 = "(\\/)"; // Any Single Character 5
		String re9 = "(\\d+)"; // Integer Number 1
		String re10 = "(\\.)"; // Any Single Character 6
		String re11 = "((?:[a-z][a-z0-9_]*))"; // Variable Name 3

		Pattern p = Pattern.compile(re3 + re4 + re5 + re6 + re7 + re8 + re9
				+ re10 + re11, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(txt);

		while (m.find()) {
			allMatches.add(m.group());
		}

		for (String strings : allMatches) {
			if (imageSkip == 0) {
				MainView.images
						.addRow(new Object[] { strings, "Not Downloaded" });
				imageSkip = 1;
			} else {
				imageSkip = 0;
			}
		}
		MainView.statusLabel.setText("Initiating Download...");
		downloadAndSave();
	}

	public void setThreadLink(String link) { // thread
		this.threadLink = link;
	}

	public void getThreadNumber() {
		List<String> allMatches = new ArrayList<String>();
		String re1 = "(\\d+)"; // Integer Number 1

		Pattern p = Pattern.compile(re1, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		Matcher m = p.matcher(threadLink);
		while (m.find()) {
			allMatches.add(m.group());
		}
		for (String strings : allMatches) {
			if (strings.length() > 1) {
				threadNumber = Integer.parseInt(strings);
				System.out.println(threadNumber);
			}

		}
	}

	public void downloadAndSave() {
		int rowCount = MainView.images.getRowCount();
		try {
			// make folder
			File folder = new File(new java.io.File(
					System.getProperty("user.dir"))
					+ "/" + Integer.toString(threadNumber));
			folder.mkdir();

			// save image

			ExecutorService executor = Executors.newFixedThreadPool(Runtime
					.getRuntime().availableProcessors());
			System.out.println("Starting ThreadPool with "
					+ Runtime.getRuntime().availableProcessors() + " threads.");
			for (int i = 0; i < rowCount; i++) {
				Runnable worker = new DownloadImages("http://"
						+ MainView.images.getValueAt(i, 0),
						System.getProperty("user.dir")
								+ "/"
								+ threadNumber
								+ "/"
								+ MainView.images.getValueAt(i, 0).toString()
										.split("/src/")[1], i);
				executor.execute(worker);
			}

			executor.shutdown();
			while (!executor.isTerminated()) {
			}
			System.out.println("Finished all threads");
		} catch (ArrayIndexOutOfBoundsException aobe) {

		}
		MainView.statusLabel.setText("Done!");
		MainView.progressBar.setIndeterminate(false);
		MainView.enableButtons();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainView.statusLabel.setText("Idle...");
	}

}
