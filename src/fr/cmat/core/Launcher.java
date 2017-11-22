package fr.cmat.core;

import org.apache.log4j.Logger;

import fr.cmat.data.AppItem;
import fr.cmat.data.Configuration;

public class Launcher {
	private static final Logger LOGGER = Logger.getLogger(Launcher.class);

	public boolean process() throws InterruptedException {
		boolean result = false;
		Configuration configuration = Configuration.getInstance();
		Runtime runtime = Runtime.getRuntime();

		LOGGER.debug("Start execution of launcher.");
		LOGGER.debug("Launcher go to sleep...");
		Thread.sleep(configuration.getStartTimeInMillis());
		LOGGER.debug("Launcher wake up...");

		for (AppItem appItem : configuration.getAppItems()) {
			LOGGER.info("Launch [" + appItem.getName() + "].");
			try {
				runtime.exec(appItem.getPath() + " " + appItem.getArgs());
			} catch (Exception e) {
				try {
					runtime.exec("cmd /c " + appItem.getPath() + " " + appItem.getArgs());
				} catch (Exception e2) {
					LOGGER.error("Failed to execute app [" + appItem.getName() + "] [" + appItem.getPath() + " " + appItem.getArgs() + "]", e);
				}
			}

			LOGGER.debug("Launcher go to sleep...");
			Thread.sleep(configuration.getIntervalTimeInMillis());
			LOGGER.debug("Launcher wake up...");
		}

		return result;
	}
}
