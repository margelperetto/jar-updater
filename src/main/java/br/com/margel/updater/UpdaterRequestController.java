package br.com.margel.updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;

import br.com.margel.updater.listener.DownloadListener;

class UpdaterRequestController {
	private static final Logger LOGGER = Logger.getLogger(UpdaterRequestController.class);
	
	private final DecimalFormat df = new DecimalFormat("###,##0.00");
	
	void downloadFile(String url, File dest, DownloadListener listener) throws IOException {
		dest.getParentFile().mkdirs();
		File temp = new File(dest.getAbsolutePath()+".temp");
		HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url).openConnection());
		double contentLength = httpConnection.getContentLength();
		double downloadedFileSize = 0;
		try(
				BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
				FileOutputStream fos = new FileOutputStream(temp);
				BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
				){
			byte[] data = new byte[1024];
			int x = 0;
			while ((x = in.read(data, 0, 1024)) >= 0) {
				downloadedFileSize += x;
				showDownloadProgress(downloadedFileSize, contentLength, listener);
				bout.write(data, 0, x);
			}
		}
		LOGGER.debug("Finish download: "+temp+" -> "+dest);
		Files.move(temp.toPath(), dest.toPath());
	}
	
	private void showDownloadProgress(double downloadedFileSize, double fileSize, DownloadListener listener) {
		int percent = (int)((downloadedFileSize*100d)/fileSize);
		LOGGER.debug("Download progress -> "+df.format(downloadedFileSize/1024)+" / "+df.format(fileSize/1024)+" ("+percent+"%)");
		if(listener!=null) {
			listener.progress(downloadedFileSize, fileSize, percent);
		}
	}
	
	Double loadOnlineVersion(String urlVersionInfoFile) {
		LOGGER.debug("Loading online version");
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlVersionInfoFile);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String version = getResponse(conn.getInputStream());
			Double onlineVersion = version == null ? null : Double.parseDouble(version.trim());
			LOGGER.debug("Online version: "+onlineVersion);
			return onlineVersion;
		} catch (Exception e) {
			LOGGER.warn("Unable to load online version! "+e.getMessage());
			return -1d;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	private String getResponse(InputStream inputStream) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			return content.toString();
		}
	}
}
