package br.com.margel.updater;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.margel.updater.model.JarUpdaterConfig;
import br.com.margel.updater.model.UpdateStep;
import br.com.margel.updater.utils.JarUtils;

public class JarUpdater {
	private static final Logger LOGGER = Logger.getLogger(JarUpdater.class);
	
	private final List<UpdaterStep> steps = Arrays.asList(
			new StepCheckForUpdate(this),
			new StepInstallationJar(this),
			new StepCreateShortcut(this),
			new StepClearTempFiles(this)
			);
	
	private final JarUpdaterConfig config;
	
	public JarUpdater(JarUpdaterConfig config) {
		this.config = config;
	}
	
	public void start() {
		LOGGER.debug("Starting updater...");
		if(JarUtils.isDevEnv()) {
			LOGGER.debug("Development environment! Updater not run...");
			return;
		}
		for (UpdaterStep step : steps) {
			step.run();
		}
	}
	
	JarUpdaterConfig getConfig() {
		return config;
	}
	
	void dispatchStep(UpdateStep step){
		LOGGER.debug(step);
		if(config.getStepConsumer()!=null) {
			config.getStepConsumer().accept(step);
		}
	}
	
}