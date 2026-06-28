package net.steveson.createtrimmable;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTrimmableMod implements ModInitializer {
	public static final String MOD_ID = "trimmable_create_armor";

//	public static final Boolean isStackedTrimsEnabled3 = FabricLoader.getInstance().isModLoaded("stacked_trims");

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

//		if (isStackedTrimsEnabled3) {
//			LOGGER.info("trimmable_create_armor says, stacked_trims is here 3");
//		}
	}
}