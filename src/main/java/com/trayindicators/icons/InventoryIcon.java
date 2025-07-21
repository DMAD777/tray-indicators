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
import java.util.Arrays;
import net.runelite.api.Client;
import net.runelite.api.ItemContainer;
import net.runelite.api.InventoryID;

public class InventoryIcon extends Icon
{
	public InventoryIcon(Client client, TrayIndicatorsConfig config)
	{
		super(IconType.Inventory, client, config);
	}

	@Override
	public IconData getIconData()
	{
		int filledSlots = getFilledInventorySlots();
		Color txtColor = config.inventoryTxtColor();

		if (filledSlots <= 0)
		{
			txtColor = config.inventoryTxtEmptyColor();
		}
		else if (filledSlots >= 28)
		{
			txtColor = config.inventoryTxtFullColor();
		}
		else if (filledSlots >= config.inventoryThreshold())
		{
			txtColor = config.inventoryTxtThresholdColor();
		}

		return new IconData(
			filledSlots,
			config.inventoryColor(),
			txtColor
		);
	}

	@Override
	public boolean isActive()
	{
		return config.inventory();
	}

	private int getFilledInventorySlots()
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);

		if (inventory == null)
		{
			return 0;
		}

		return (int)Arrays.stream(inventory.getItems())
			.filter(item -> item.getQuantity() > 0)
			.count();
	}
}
