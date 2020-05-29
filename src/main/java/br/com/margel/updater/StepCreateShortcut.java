package br.com.margel.updater;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.com.margel.updater.model.UpdateStep;
import mslinks.ShellLink;

class StepCreateShortcut extends UpdaterStep{
	private static final Logger LOGGER = Logger.getLogger(StepCreateShortcut.class);
	private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;

	StepCreateShortcut(JarUpdater updater) {
		super(updater);
	}

	@Override
	void run() {
		updateIcons();
		updateShortcut();
	}

	private void updateIcons() {
		updater.dispatchStep(UpdateStep.UPDATING_DESKTOP_SHORTCUT);
		copyResourceTo(updater.getConfig().getResourceAppIconFile(), new File(updater.getConfig().getLocalAppIconFile()));
	}
	
	private void copyResourceTo(String source, File dest) {
		try {
			URL inputUrl = getClass().getClassLoader().getResource(source);
			FileUtils.copyURLToFile(inputUrl, dest);
		} catch (IOException e) {
			LOGGER.error("Error copying resource "+source+" to "+dest+"! "+e.getMessage(), e);
		}
	}
	
	private void updateShortcut() {
		try {
			updater.dispatchStep(UpdateStep.CHECKING_DESKTOP_SHORTCUT);
			ShellLink.createLink(updater.getConfig().getRunnableJarFile())
			.setIconLocation(updater.getConfig().getLocalAppIconFile()).saveTo(DESKTOP_PATH+updater.getConfig().getShortcutLinkName());
		} catch (IOException e) {
			LOGGER.error("Error updating shortcut! "+e.getMessage(), e);
		}
	}
}
