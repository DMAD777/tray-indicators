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
package com.TrayIndicators;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.Varbits;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class Icon
{
	public final IconType type;
	private TrayIcon trayIcon;

	private final Client client;
	private final TrayIndicatorsConfig config;

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

	public void updateIcon()
	{
		// Default values
		Color bgColor = Color.WHITE;
		Color txtColor = Color.BLACK;
		int value = 0;

		switch (type)
		{
			case Health:
				value = client.getBoostedSkillLevel(Skill.HITPOINTS);
				bgColor = config.healthColor();
				txtColor = config.healthTxtColor();
				break;
			case Prayer:
				value = client.getBoostedSkillLevel(Skill.PRAYER);
				bgColor = config.prayerColor();
				txtColor = config.prayerTxtColor();
				break;
			case Absorption:
				value = client.getVarbitValue(Varbits.NMZ_ABSORPTION);
				bgColor = config.absorptionColor();
				txtColor = config.absorptionTxtColor();
				break;
			case Cannon:
				value = client.getVarpValue(VarPlayer.CANNON_AMMO);
				bgColor = config.cannonColor();

				if (config.cannonTxtDynamic())
				{
					if (value > 15)
					{
						txtColor = Color.green;
					}
					else if (value > 5)
					{
						txtColor = Color.orange;
					}
					else
					{
						txtColor = Color.red;
					}
				}
				else
				{
					txtColor = config.cannonTxtColor();
				}

				break;
		}

		if (trayIcon == null)
		{
			createIcon(value, bgColor, txtColor);
			return;
		}

		trayIcon.getImage().flush();
		trayIcon.setImage(createImage(value, bgColor, txtColor));
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

	private BufferedImage createImage(int value, Color bgColor, Color txtColor)
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

	public boolean isActive()
	{
		switch (type)
		{
			case Health:
				return config.health();
			case Prayer:
				return config.prayer();
			case Absorption:
				return config.absorption();
			case Cannon:
				return config.cannon();
		}

		return false;
	}
}
