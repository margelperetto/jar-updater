package br.com.margel.updater;

import java.io.File;
import java.nio.file.Files;
import org.apache.log4j.Logger;

class StepClearTempFiles extends UpdaterStep{
	private static final Logger LOGGER = Logger.getLogger(StepClearTempFiles.class);

	StepClearTempFiles(JarUpdater updater) {
		super(updater);
	}

	@Override
	void run() {
		String tempFolder = null;
		try {
			tempFolder = updater.getConfig().getTempDownloadFolderPath();
			LOGGER.debug("Clear temp files -> "+tempFolder);
			File[] tempFiles = new File(tempFolder).listFiles();
			if(tempFiles==null) {
				return;
			}
			for(File f : tempFiles) {
				Files.delete(f.toPath());
				LOGGER.debug("Delete -> "+f);
			}
		} catch (Exception e) {
			LOGGER.error("Error clear temp files -> "+tempFolder, e);
		}
	}

}
