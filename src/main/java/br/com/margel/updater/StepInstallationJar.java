package br.com.margel.updater;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;

import br.com.margel.updater.model.UpdateStep;
import br.com.margel.updater.utils.JarUtils;

class StepInstallationJar extends UpdaterStep{
	private static final Logger LOGGER = Logger.getLogger(StepInstallationJar.class);

	StepInstallationJar(JarUpdater updater) {
		super(updater);
	}

	@Override
	void run() {
		LOGGER.debug("Checking main jar");
		File jarFile = JarUtils.getJarFile();
		if(JarUtils.isAppJarFile(jarFile) && !isMainJarFile(jarFile)) {
			LOGGER.debug("Copying jar");
			File mainJarFile = new File(updater.getConfig().getRunnableJarFile());
			copyFile(jarFile, mainJarFile);
			updater.dispatchStep(UpdateStep.INSTALLATION_FINISHED);
			sleep(3_000);
			JarUtils.runJarFile(mainJarFile);
			System.exit(0);
		}
	}
	
	private boolean isMainJarFile(File jarFile) {
		if(jarFile==null) {
			return false;
		}
		String mainJarFile = updater.getConfig().getRunnableJarFile();
		boolean isMainJarFile = jarFile.getAbsolutePath().equals(mainJarFile );
		LOGGER.debug("Jar "+jarFile+" running from installation folder "+mainJarFile+" -> "+isMainJarFile);
		return isMainJarFile;
	}
	
	private void copyFile(File source, File target) {
		try {
			Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
			LOGGER.debug("Copied "+source+" to "+target);
		} catch (Exception e) {
			LOGGER.error("Error copy "+source+" to "+target+"! "+e.getMessage(), e);
		}
	}
	
}
