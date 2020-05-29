package br.com.margel.updater.listener;

@FunctionalInterface
public interface DownloadListener {
	public void progress(double downloadedFileSize, double fileSize, int percent);
}
