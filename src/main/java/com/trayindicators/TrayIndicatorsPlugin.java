/*
 * Copyright (c) 2025, DMAD777 <https://github.com/DMAD777>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.trayindicators;

import com.google.inject.Provides;
import com.trayindicators.icons.*;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
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
	description = "Displays your hitpoints, prayer, absorption, special attack, cannon ammo or inventory count in the system tray.",
	tags = {"notifications"}
)
public class TrayIndicatorsPlugin extends Plugin
{
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
		if (!SystemTray.isSupported())
		{
			log.error("System tray is not supported.");
			return;
		}

		if (trayIcons.isEmpty())
		{
			trayIcons.put(IconType.Health, new HealthIcon(client, config));
			trayIcons.put(IconType.Prayer, new PrayerIcon(client, config));
			trayIcons.put(IconType.Absorption, new AbsorptionIcon(client, config));
			trayIcons.put(IconType.Cannon, new CannonIcon(client, config));
			trayIcons.put(IconType.Inventory, new InventoryIcon(client, config));
			trayIcons.put(IconType.Spec, new SpecIcon(client, config));
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		trayIcons.forEach((iconType, icon) -> icon.removeIcon());
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		trayIcons.forEach((iconType, icon) -> icon.onGameTick(event));
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		trayIcons.forEach((iconType, icon) -> icon.onGameStateChanged(event));
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		trayIcons.forEach((iconType, icon) -> icon.onItemContainerChanged(event));
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("Tray Indicators"))
		{
			return;
		}

		trayIcons.forEach((iconType, icon) -> icon.onConfigChanged(event));
	}
}