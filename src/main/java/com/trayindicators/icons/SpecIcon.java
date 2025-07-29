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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.gameval.ItemID;

public class SpecIcon extends Icon
{
	private static final int SPEC_REGEN_TICKS = 50;
	private static final int LIGHTBEARER_REGEN_TICKS = 25;
	private static final int MAX_SPECIAL_ENERGY = 1000;

	private int ticksSinceSpecRegen;
	private boolean wearingLightbearer;

	public SpecIcon(Client client, TrayIndicatorsConfig config)
	{
		super(IconType.Spec, client, config);
	}

	@Override
	public IconData getIconData()
	{
		return new IconData(
			client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10,
			config.specColor(),
			config.specTxtColor()
		);
	}

	@Override
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.EQUIPMENT.getId())
		{
			return;
		}

		ItemContainer equipment = event.getItemContainer();

		if (equipment == null)
		{
			return;
		}

		final boolean hasLightbearer = equipment.contains(ItemID.LIGHTBEARER);

		// Reset some regen progress when equipping/removing Lightbearer
		if (hasLightbearer != wearingLightbearer)
		{
			ticksSinceSpecRegen = Math.max(0, ticksSinceSpecRegen - LIGHTBEARER_REGEN_TICKS);
		}

		wearingLightbearer = hasLightbearer;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		int specialAttackEnergy = client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT);
		int ticksPerSpecRegen = wearingLightbearer ? SPEC_REGEN_TICKS / 2 : SPEC_REGEN_TICKS;

		// Reset ticksSinceSpecRegen if special attack energy is full
		if (specialAttackEnergy >= MAX_SPECIAL_ENERGY)
		{
			ticksSinceSpecRegen = 0;
		}
		else
		{
			ticksSinceSpecRegen = (ticksSinceSpecRegen + 1) % ticksPerSpecRegen;
		}

		// Don't cache the icon if progress is enabled, as the progress bar will change every tick thus requiring a new image.
		// Kinda dirty, but it works.
		cacheImage = !config.specProgress();

		super.onGameTick(event);
	}

	@Override
	protected BufferedImage createImage(int value, Color bgColor, Color txtColor) {
		// Use the default image creation if we don't have to draw the progress bar.
		if (!config.specProgress())
		{
			return super.createImage(value, bgColor, txtColor);
		}

		final int ticksPerSpecRegen = wearingLightbearer ? LIGHTBEARER_REGEN_TICKS : SPEC_REGEN_TICKS;
		int size = 16;
		String text = Integer.toString(value);

		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();

		float regenPercent = ticksSinceSpecRegen / (float) ticksPerSpecRegen;

		int filledHeight = (int) (size * regenPercent);
		int unfilledHeight = size - filledHeight;

		// Draw unfilled area
		graphics.setColor(bgColor);
		graphics.fillRect(0, 0, size, unfilledHeight);

		// Draw filled regen area
		graphics.setColor(config.specProgressColor());
		graphics.fillRect(0, unfilledHeight, size, filledHeight);

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

	@Override
	public boolean isActive()
	{
		return config.spec();
	}
}
