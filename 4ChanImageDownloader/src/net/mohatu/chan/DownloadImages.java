package net.mohatu.chan;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

public class DownloadImages implements Runnable {

	private String imageUrl;
	private String destinationFile;
	private int iVal;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public DownloadImages(String url, String dest,int iVal){
		this.imageUrl = url;
		this.destinationFile = dest;
		this.iVal = iVal;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void run() {
		try {
			
			System.out.println(MainView.images.getValueAt(iVal, 0));
			
			MainView.statusLabel.setText("Saving "
					+ MainView.images.getValueAt((int) iVal, 0).toString()
							.split("/src/")[1]);
			URL url = new URL(imageUrl);
			
			if(MainView.customSize){ //Check for custom size setting
				HttpURLConnection con;
				con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				InputStream is = con.getInputStream();
				BufferedImage img = ImageIO.read(is);
				MainView.images.setValueAt("Wrong size", (int) iVal, 1);
				if(img.getWidth() == MainView.imgWidth && img.getHeight() == MainView.imgHeight){
					save(url);
					MainView.images.setValueAt("Saved", (int) iVal, 1);
				}
			}else{
				save(url);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainView.table.getSelectionModel().setSelectionInterval(iVal, iVal);
		MainView.table.scrollRectToVisible(new Rectangle(MainView.table
				.getCellRect(iVal, iVal, false)));
	}
	
	private void save(URL url){
		try{
		HttpURLConnection con;
		con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		InputStream is = con.getInputStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
		// update MainView
		MainView.images.setValueAt("Saved", (int) iVal, 1);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
}