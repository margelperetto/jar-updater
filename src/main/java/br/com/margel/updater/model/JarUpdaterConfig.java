package br.com.margel.updater.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import br.com.margel.updater.exception.UpdaterRequiredFieldsException;
import br.com.margel.updater.listener.DownloadListener;

public class JarUpdaterConfig {
	
	private Double localVersion;
	private String urlLatestVersionInfoFile;
	private String urlLatestVersionJar;
	private String runnableJarFile;
	private String resourceAppIconFile;
	private String localAppIconFile;
	private String shortcutLinkName;
	private String tempDownloadFolderPath;
	private Consumer<UpdateStep> stepConsumer;
	private DownloadListener downloadListener;
	
	private JarUpdaterConfig() {}
	
	public Double getLocalVersion() {
		return localVersion;
	}
	public String getUrlLatestVersionInfoFile() {
		return urlLatestVersionInfoFile;
	}
	public String getUrlLatestVersionJar() {
		return urlLatestVersionJar;
	}
	public String getRunnableJarFile() {
		return runnableJarFile;
	}
	public String getResourceAppIconFile() {
		return resourceAppIconFile;
	}
	public String getLocalAppIconFile() {
		return localAppIconFile;
	}
	public String getShortcutLinkName() {
		return shortcutLinkName;
	}
	public String getTempDownloadFolderPath() {
		return tempDownloadFolderPath;
	}
	public Consumer<UpdateStep> getStepConsumer() {
		return stepConsumer;
	}
	public DownloadListener getDownloadListener() {
		return downloadListener;
	}
	
	@Override
	public String toString() {
		return "JarUpdaterConfig [localVersion=" + localVersion + ", urlLatestVersionInfoFile="
				+ urlLatestVersionInfoFile + ", urlLatestVersionJar=" + urlLatestVersionJar + ", runnableJarFile="
				+ runnableJarFile + ", resourceAppIconFile=" + resourceAppIconFile + ", localAppIconFile="
				+ localAppIconFile + ", shortcutLinkName=" + shortcutLinkName + ", tempDownloadFolderPath="
				+ tempDownloadFolderPath + "]";
	}

	public static class Builder {
		
		private Double localVersion;
		private String urlLatestVersionInfoFile;
		private String urlLatestVersionJar;
		private String runnableJarFile;
		private String resourceAppIconFile;
		private String localAppIconFile;
		private String shortcutLinkName;
		private String tempDownloadFolderPath;
		private Consumer<UpdateStep> stepConsumer;
		private DownloadListener downloadListener;
		
		public JarUpdaterConfig build() throws UpdaterRequiredFieldsException {
			JarUpdaterConfig config = new JarUpdaterConfig();
			config.localVersion = localVersion;
			config.urlLatestVersionInfoFile = urlLatestVersionInfoFile;
			config.urlLatestVersionJar = urlLatestVersionJar;
			config.runnableJarFile = runnableJarFile;
			config.resourceAppIconFile = resourceAppIconFile;
			config.localAppIconFile = localAppIconFile;
			config.shortcutLinkName = shortcutLinkName;
			config.tempDownloadFolderPath = tempDownloadFolderPath;
			config.stepConsumer = stepConsumer;
			config.downloadListener = downloadListener;
			checkRequiredFields(config);
			return config;
		}
		
		private void checkRequiredFields(JarUpdaterConfig config) throws UpdaterRequiredFieldsException {
			List<String> nullFields = new ArrayList<>();
			try {
				for (Field field : JarUpdaterConfig.class.getFields()) {
					if(field.get(config) == null) {
						nullFields.add(field.getName());
					}
				}
			} catch (Exception e) {
				Logger.getLogger(getClass()).error("Error read config fields!", e);
			}
			if(!nullFields.isEmpty()) {
				throw new UpdaterRequiredFieldsException(nullFields);
			}
		}
		
		public Builder localVersion(Double localVersion) {
			this.localVersion = localVersion;
			return this;
		}
		public Builder urlLatestVersionInfoFile(String urlLatestVersionInfo) {
			this.urlLatestVersionInfoFile = urlLatestVersionInfo;
			return this;
		}
		public Builder urlLatestVersionJar(String urlLatestVersionJar) {
			this.urlLatestVersionJar = urlLatestVersionJar;
			return this;
		}
		public Builder runnableJarFile(String runnableJarFile) {
			this.runnableJarFile = runnableJarFile;
			return this;
		}
		public Builder resourceAppIconFile(String resourceAppIconFile) {
			this.resourceAppIconFile = resourceAppIconFile;
			return this;
		}
		public Builder localAppIconFile(String localAppIconFile) {
			this.localAppIconFile = localAppIconFile;
			return this;
		}
		public Builder shortcutLinkName(String shortcutLinkName) {
			this.shortcutLinkName = shortcutLinkName;
			return this;
		}
		public Builder tempDownloadFolderPath(String tempDownloadFolderPath) {
			this.tempDownloadFolderPath = tempDownloadFolderPath;
			return this;
		}
		public Builder stepConsumer(Consumer<UpdateStep> stepConsumer) {
			this.stepConsumer = stepConsumer;
			return this;
		}
		public Builder downloadListener(DownloadListener downloadListener) {
			this.downloadListener = downloadListener;
			return this;
		}
	}
}
