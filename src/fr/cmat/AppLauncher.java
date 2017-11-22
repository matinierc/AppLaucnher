package fr.cmat;

import org.apache.log4j.Logger;

import fr.cmat.core.ConfigurationManager;
import fr.cmat.core.Launcher;

public class AppLauncher {
	private static final Logger LOGGER = Logger.getLogger(AppLauncher.class);

	public static void main(String[] args) {
		try {
			LOGGER.info("================== APP LAUNCHER ==================");
			if (args.length == 1) {
				ConfigurationManager.read(args[0]);
				new Launcher().process();
			} else {
				LOGGER.error("Mauvais argument");
			}
		} catch (Exception e) {
			LOGGER.fatal("Erreur d'exécution : ", e);
		}
	}
}
