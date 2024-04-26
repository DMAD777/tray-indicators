package com.TrayIndicators;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;
import java.util.*;

@Slf4j
@PluginDescriptor(
		name = "Tray Indicators",
		description = "Displays your hitpoints, prayer, absorption or cannonballs in the system tray.",
		tags = {"notifications"}
)
public class TrayIndicatorsPlugin extends Plugin
{
	private static final int[] NMZ_MAP_REGION = {9033};

	private final Map<IconType, Icon> trayIcons = new HashMap<>();

	@Inject
	private Client client;

	@Inject
	private TrayIndicatorsConfig config;

	private boolean cannonPlaced;

	@Provides
	TrayIndicatorsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TrayIndicatorsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		if (!SystemTray.isSupported())
		{
			log.error("System tray is not supported.");
			return;
		}

		if (trayIcons.isEmpty())
			for(IconType type : IconType.values())
				trayIcons.put(type, new Icon(type, client, config));
	}

	@Override
	protected void shutDown() throws Exception
	{
		trayIcons.forEach((iconType, icon) -> icon.removeIcon());
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		updateAllTrayIcons();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		updateAllTrayIcons();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("Tray Indicators"))
			return;

		updateAllTrayIcons();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.GAMEMESSAGE)
			return;

		if (event.getMessage().equals("You add the furnace."))
		{
			cannonPlaced = true;
		}
		else if (event.getMessage().contains("You pick up the cannon")
				|| event.getMessage().contains("Your cannon has decayed.")
				|| event.getMessage().contains("Your cannon has been destroyed!"))
		{
			cannonPlaced = false;
		}
	}

	public void updateAllTrayIcons()
	{
		trayIcons.forEach((iconType, icon) -> {
			if (shouldRemoveIcon(icon)) {
				icon.removeIcon();
				return;
			}

			icon.updateIcon();
		});
	}

	private boolean shouldRemoveIcon(Icon icon)
	{
		return client.getGameState() != GameState.LOGGED_IN ||
				!icon.isActive() ||
				(icon.type == IconType.Absorption && !isInNightmareZone()) ||
				(icon.type == IconType.Cannon && !cannonPlaced);
	}

	private boolean isInNightmareZone()
	{
		return Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}
}