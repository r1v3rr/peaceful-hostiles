package net.river.peacefulhostiles;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeacefulHostiles implements ModInitializer {
	public static final String MOD_ID = "peaceful-hostiles";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Log a message when the mod initializes
		LOGGER.info("Peaceful Hostiles mod initialized! Hostile mobs should now spawn and be passive in Peaceful difficulty.");
	}
}