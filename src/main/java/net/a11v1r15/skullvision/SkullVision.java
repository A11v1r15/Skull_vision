package net.a11v1r15.skullvision;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkullVision implements ModInitializer {
    public static final String MOD_ID = "skull-vision";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("The eyes are the window to your soul");
	}
}