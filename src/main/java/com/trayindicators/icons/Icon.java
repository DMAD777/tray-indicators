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
package com.trayindicators.icons;

import com.trayindicators.TrayIndicatorsConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.events.ConfigChanged;

@Slf4j
public abstract class Icon
{
	public final IconType type;

	protected final Client client;
	protected final TrayIndicatorsConfig config;

	private TrayIcon trayIcon;
	private IconData lastIconData;

	public Icon(IconType type, Client client, TrayIndicatorsConfig config)
	{
		this.type = type;
		this.client = client;
		this.config = config;
	}

	private void createIcon(int value, Color bgColor, Color txtColor)
	{
		if (trayIcon != null)
		{
			removeIcon();
		}

		trayIcon = new TrayIcon(createImage(value, bgColor, txtColor));
		trayIcon.setImageAutoSize(true);

		try
		{
			SystemTray.getSystemTray().add(trayIcon);
		}
		catch (AWTException ex)
		{
			log.error("Unable to add system tray icon.", ex);
		}
	}

	public void onGameTick(GameTick event)
	{
		updateIcon();
	}

	public void onGameStateChanged(GameStateChanged event)
	{
		updateIcon();
	}

	public void onConfigChanged(ConfigChanged event)
	{
		updateIcon();
	}

	public void onItemContainerChanged(ItemContainerChanged event)
	{
		// Default implementation does nothing
		// Subclasses can override this method if needed
	}

	public void updateIcon()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !isActive())
		{
			removeIcon();
			return;
		}

		IconData data = getIconData();

		if (trayIcon != null && data.equals(lastIconData))
		{
			return;
		}

		if (trayIcon == null)
		{
			createIcon(data.value, data.bgColor, data.txtColor);
		}
		else
		{
			trayIcon.getImage().flush();
			trayIcon.setImage(createImage(data.value, data.bgColor, data.txtColor));
		}

		lastIconData = data;
	}

	public void removeIcon()
	{
		if (trayIcon == null)
		{
			return;
		}

		SystemTray.getSystemTray().remove(trayIcon);
		trayIcon = null;
	}

	protected BufferedImage createImage(int value, Color bgColor, Color txtColor)
	{
		int size = 16;
		String text = Integer.toString(value);

		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();

		// Draw background
		graphics.setColor(bgColor);
		graphics.fillRect(0, 0, size, size);

		// Draw text
		graphics.setColor(txtColor);

		int fontSize = (text.length() >= 4) ? 8 : (text.length() == 3) ? 9 : 12;
		graphics.setFont(new Font(graphics.getFont().getName(), Font.PLAIN, fontSize));

		FontMetrics metrics = graphics.getFontMetrics();
		int x = (size - metrics.stringWidth(text)) / 2;
		int y = ((size - metrics.getHeight()) / 2) + metrics.getAscent();
		graphics.drawString(text, x, y);

		graphics.dispose();

		return image;
	}

	public abstract IconData getIconData();

	public abstract boolean isActive();
}
