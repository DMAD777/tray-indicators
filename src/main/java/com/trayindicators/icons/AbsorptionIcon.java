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
import java.util.Arrays;
import net.runelite.api.Client;
import net.runelite.api.Varbits;

public class AbsorptionIcon extends Icon
{
	private static final int[] NMZ_MAP_REGION = {9033};

	public AbsorptionIcon(Client client, TrayIndicatorsConfig config)
	{
		super(IconType.Absorption, client, config);
	}

	@Override
	public IconData getIconData()
	{
		return new IconData(
			client.getVarbitValue(Varbits.NMZ_ABSORPTION),
			config.absorptionColor(),
			config.absorptionTxtColor()
		);
	}

	@Override
	public boolean isActive()
	{
		return config.absorption() && isInNightmareZone();
	}

	private boolean isInNightmareZone()
	{
		return Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}
}
