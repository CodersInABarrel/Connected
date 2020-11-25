package net.fabricmc.friend;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.friend.Commands.FriendCommand;
import net.fabricmc.friend.Commands.ProfileCommand;

public class Friend implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		this.registerCallbacks();

	}
	private void registerCallbacks() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			FriendCommand.register(dispatcher);
			ProfileCommand.register(dispatcher);
		});
	}
}
