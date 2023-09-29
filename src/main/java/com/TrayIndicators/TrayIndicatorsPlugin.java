package com.TrayIndicators;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.*;

@Slf4j
@PluginDescriptor(
		name = "Tray Indicators",
		description = "Displays your hitpoints, prayer or absorption in the system tray.",
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

	@Provides
	TrayIndicatorsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TrayIndicatorsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		if (trayIcons.isEmpty())
			for(IconType type : IconType.values())
				trayIcons.put(type, new Icon());
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

	public void updateAllTrayIcons()
	{
		trayIcons.forEach((iconType, icon) -> {
			if (shouldRemoveIcon(iconType)) {
				icon.removeIcon();
				return;
			}

			icon.updateIcon(iconType.getTxt(client), iconType.getBgColor(config), iconType.getTxtColor(config));
		});
	}

	private boolean shouldRemoveIcon(IconType iconType)
	{
		return client.getGameState() != GameState.LOGGED_IN ||
				!iconType.isActive(config) ||
				(iconType == IconType.Absorption && !isInNightmareZone());
	}

	public boolean isInNightmareZone()
	{
		return Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}
}