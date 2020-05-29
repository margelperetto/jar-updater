package br.com.margel.updater.utils;

import java.io.File;
import java.net.URI;

import org.apache.log4j.Logger;

public final class JarUtils {
	private static final Logger LOGGER = Logger.getLogger(JarUtils.class);
	
	private JarUtils() {}

	public static boolean isDevEnv() {
		LOGGER.debug("Checking environment type...");
		File jarFile = getJarFile();
		return jarFile==null || jarFile.isDirectory() || 
				jarFile.getAbsolutePath().contains(File.separator+"jar-updater"+File.separator);
	}

	public static File getJarFile() {
		try {
			URI uri = JarUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			File file = new File(uri);
			LOGGER.debug(".jar that is running the application: "+file);
			return file;
		} catch (Exception e) {
			LOGGER.error("Error loading application .jar! "+e.getMessage(), e);
			return null;
		}
	}

	public static void runJarFile(File jarFile) {
		try {
			String cmd = "javaw -jar "+jarFile.getName();
			LOGGER.debug("Running .jar: "+cmd);
			Runtime.getRuntime().exec(cmd, null, jarFile.getParentFile());
		} catch (Exception e) {
			LOGGER.error("Error running .jar! "+jarFile+" "+e.getMessage(), e);
		}
	}
	
	public static boolean isAppJarFile(File f) {
		boolean isAppJarFile = f!=null && f.exists() && !f.isDirectory() && f.getName().endsWith(".jar");
		LOGGER.debug("File "+f+" is .jar -> "+isAppJarFile);
		return isAppJarFile;
	}
}
