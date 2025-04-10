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
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.VarPlayer;
import java.awt.Color;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;

public class CannonIcon extends Icon
{
	private boolean cannonPlaced;

	public CannonIcon(Client client, TrayIndicatorsConfig config)
	{
		super(IconType.Cannon, client, config);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

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

	@Override
	public IconData getIconData()
	{
		int value = client.getVarpValue(VarPlayer.CANNON_AMMO);
		Color txtColor = config.cannonTxtDynamic() ? getDynamicTextColor(value) : config.cannonTxtColor();

		return new IconData(
			value,
			config.cannonColor(),
			txtColor
		);
	}

	private Color getDynamicTextColor(int cannonballs)
	{
		if (cannonballs > 15)
		{
			return Color.GREEN;
		}
		else if (cannonballs > 5)
		{
			return Color.ORANGE;
		}

		return Color.RED;
	}

	@Override
	public boolean isActive()
	{
		return config.cannon() && cannonPlaced;
	}
}
