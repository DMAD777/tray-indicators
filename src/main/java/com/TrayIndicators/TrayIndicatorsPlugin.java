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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@PluginDescriptor(
		name = "Tray Indicators",
		description = "Displays your hitpoints, prayer or absorption in the system tray.",
		tags = {"notifications"}
)
public class TrayIndicatorsPlugin extends Plugin
{
	private static final int[] NMZ_MAP_REGION = {9033};

	private boolean startUp;

	private java.util.List<TrayIcon> trayIcons = new ArrayList<TrayIcon>(); // Should never be bigger then 3

	@Inject
	private Client client;

	@Inject
	private TrayIndicatorsConfig config;

	enum IconType { Health, Prayer, Absorption }

	@Provides
	TrayIndicatorsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TrayIndicatorsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		startUp = true;
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("Tray Indicators"))
		{
			switch (event.getKey())
			{
				case "health":
					if (event.getNewValue().equals("true")) {
						trayIcons.add(IconType.Health.ordinal(), setupTrayIcon(IconType.Health));
					} else {
						removeTrayIcon(IconType.Health);
					}
					break;
				case "prayer":
					if (event.getNewValue().equals("true")) {
						trayIcons.add(IconType.Prayer.ordinal(), setupTrayIcon(IconType.Prayer));
					} else {
						removeTrayIcon(IconType.Prayer);
					}
					break;
				case "absorption":
					if (event.getNewValue().equals("true")) {
						trayIcons.add(IconType.Absorption.ordinal(), setupTrayIcon(IconType.Absorption));
					} else {
						removeTrayIcon(IconType.Absorption);
					}
					break;
				default:
					break;
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGIN_SCREEN)
		{
			if(startUp == false) {
				removeAllTrayIcons();
				startUp = true;
			}
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		startUp = false;
		removeAllTrayIcons();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if(startUp)
		{
			if (config.health())
				trayIcons.add(IconType.Health.ordinal(), setupTrayIcon(IconType.Health));

			if (config.prayer())
				trayIcons.add(IconType.Prayer.ordinal(), setupTrayIcon(IconType.Prayer));

			if(isInNightmareZone() && config.absorption())
				trayIcons.add(IconType.Absorption.ordinal(), setupTrayIcon(IconType.Absorption));

			startUp = false;
		}

		// Causes issue #2
		if (config.absorption()) {
			if (isInNightmareZone() && !indexExists(trayIcons, IconType.Absorption.ordinal())) {
				trayIcons.add(IconType.Absorption.ordinal(), setupTrayIcon(IconType.Absorption));
			} else if (!isInNightmareZone() && indexExists(trayIcons, IconType.Absorption.ordinal())) {
				removeTrayIcon(IconType.Absorption);
			}
		}

		for (int i=0; i < trayIcons.size(); i++)
		{
			TrayIcon trayIcon = trayIcons.get(i);

			switch (i)
			{
				case 0:
					if(config.health()) {
						trayIcon.setImage(createImage(IconType.Health));
					}
					break;
				case 1:
					if(config.prayer()) {
						trayIcon.setImage(createImage(IconType.Prayer));
					}
					break;
				case 2:
					if(config.absorption()) {
						trayIcon.setImage(createImage(IconType.Absorption));
					}
					break;
			}
		}
	}

	private TrayIcon setupTrayIcon(IconType i)
	{
		if (!SystemTray.isSupported())
		{
			return null;
		}

		SystemTray systemTray = SystemTray.getSystemTray();

		TrayIcon trayIcon = new TrayIcon(createImage(i));
		trayIcon.setImageAutoSize(true);

		try
		{
			systemTray.add(trayIcon);
		}
		catch (AWTException ex)
		{
			log.debug("Unable to add system tray icon", ex);
			return trayIcon;
		}

		return trayIcon;
	}

	public BufferedImage createImage(IconType type){
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();

		// Set the background color
		switch(type) {
			case Health:
				graphics.setColor(config.healthColor());
				break;
			case Prayer:
				graphics.setColor(config.prayerColor());
				break;
			case Absorption:
				graphics.setColor(config.absorptionColor());
				break;
		}

		graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight());

		if(client.getLocalPlayer() != null) {
			String text = "";
			switch(type) {
				case Health:
					text = Integer.toString(client.getBoostedSkillLevel(Skill.HITPOINTS));
					graphics.setColor(config.healthTxtColor());
					break;
				case Prayer:
					text = Integer.toString(client.getBoostedSkillLevel(Skill.PRAYER));
					graphics.setColor(config.prayerTxtColor());
					break;
				case Absorption:
					if(client.getVarbitValue(Varbits.NMZ_ABSORPTION) == 1000)
						graphics.setFont(new Font(graphics.getFont().getName(), Font.PLAIN, 8));
					else if(client.getVarbitValue(Varbits.NMZ_ABSORPTION) >= 100)
						graphics.setFont(new Font(graphics.getFont().getName(), Font.PLAIN, 9));


					text = Integer.toString(client.getVarbitValue(Varbits.NMZ_ABSORPTION));
					graphics.setColor(config.absorptionTxtColor());
					break;
			}

			FontMetrics metrics = graphics.getFontMetrics();
			int x = (image.getWidth() - metrics.stringWidth(text)) / 2;
			int y = ((image.getWidth() - metrics.getHeight()) / 2) + metrics.getAscent();

			graphics.drawString(text, x, y);
		}

		return image;
	}

	public boolean isInNightmareZone()
	{
		return Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}

	public boolean indexExists(final List list, final int index)
	{
		return index >= 0 && index < list.size();
	}

	public void removeTrayIcon(IconType type){
		if(!indexExists(trayIcons, type.ordinal()))
		{
			log.info("Index: '" + type.ordinal() + "' (" + type + ") does not exist for trayIcons ;(");
			return;
		}

		SystemTray systemTray = SystemTray.getSystemTray();
		TrayIcon trayIcon = trayIcons.get(type.ordinal());
		systemTray.remove(trayIcon);
		trayIcons.remove(type.ordinal());
	}

	public void removeAllTrayIcons(){
		SystemTray systemTray = SystemTray.getSystemTray();

		for (int i=0; i < trayIcons.size(); i++)
		{
			TrayIcon trayIcon = trayIcons.get(i);
			systemTray.remove(trayIcon);
		}

		trayIcons.removeAll(trayIcons);
	}
}
