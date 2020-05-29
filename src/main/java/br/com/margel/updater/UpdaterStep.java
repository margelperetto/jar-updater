package br.com.margel.updater;

abstract class UpdaterStep {

	protected JarUpdater updater;
	
	UpdaterStep(JarUpdater updater) {
		this.updater = updater;
	}
	
	void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {}
	}
	
	abstract void run();
}
