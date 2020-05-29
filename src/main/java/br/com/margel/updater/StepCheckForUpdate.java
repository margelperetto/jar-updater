package br.com.margel.updater;

import java.io.File;

import org.apache.log4j.Logger;

import br.com.margel.updater.model.UpdateStep;
import br.com.margel.updater.utils.JarUtils;

class StepCheckForUpdate extends UpdaterStep{
	private static final Logger LOGGER = Logger.getLogger(StepCheckForUpdate.class);
	
	private UpdaterRequestController requestCtrl = new UpdaterRequestController();

	public StepCheckForUpdate(JarUpdater updater) {
		super(updater);
	}

	@Override
	void run() {
		updater.dispatchStep(UpdateStep.LOOKING_FOR_UPDATES_ONLINE);
		Double onlineVersion = requestCtrl.loadOnlineVersion(updater.getConfig().getUrlLatestVersionInfoFile());
		Double localVersion = updater.getConfig().getLocalVersion();
		LOGGER.debug("Local version: "+localVersion);
		if(onlineVersion>localVersion) {
			LOGGER.debug("There is a newer online version");
			File downloadedJarFile = downloadMostRecentVersion(onlineVersion);
			if(downloadedJarFile!=null) {
				updater.dispatchStep(UpdateStep.UPDATE_FINISHED);
				sleep(3_000);
				JarUtils.runJarFile(downloadedJarFile);
				System.exit(0);
			}
		}
	}

	private File downloadMostRecentVersion(Double onlineVersion) {
		updater.dispatchStep(UpdateStep.DOWNLOADING_UPDATE);
		try {
			File dest = new File(updater.getConfig().getTempDownloadFolderPath()+File.separator+"app"+onlineVersion+".jar");
			requestCtrl.downloadFile(updater.getConfig().getUrlLatestVersionJar(), dest, updater.getConfig().getDownloadListener());
			updater.dispatchStep(UpdateStep.DOWNLOAD_SUCCESS);
			return dest;
		} catch (Exception e) {
			updater.dispatchStep(UpdateStep.DOWNLOAD_ERROR);
			LOGGER.error("Error download file from "+updater.getConfig().getUrlLatestVersionJar()+"! "+e.getMessage(), e);
			return null;
		}
	}
	
}
