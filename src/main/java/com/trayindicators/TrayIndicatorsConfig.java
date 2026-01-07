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

import java.awt.Color;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("Tray Indicators")
public interface TrayIndicatorsConfig extends Config
{

	//region Health Options
	@ConfigSection(
		name = "Hitpoints",
		description = "",
		position = 0
	)
	String healthSection = "Hitpoints";

	@ConfigItem(
		keyName = "health",
		name = "Enable Hitpoints",
		description = "Shows the amount of hitpoints left.",
		section = healthSection,
		position = 0
	)
	default boolean health()
	{
		return true;
	}

	@ConfigItem(
		keyName = "healthColor",
		name = "Background Color",
		description = "",
		section = healthSection,
		position = 1
	)
	default Color healthColor()
	{
		return Color.decode("#ff0000");
	}

	@ConfigItem(
		keyName = "healthTxtColor",
		name = "Text Color",
		description = "",
		section = healthSection,
		position = 2
	)
	default Color healthTxtColor()
	{
		return Color.decode("#ffffff");
	}

  @ConfigItem(
    keyName = "healthTxtSize",
    name = "Text Size",
    description = "",
    section = healthSection,
    position = 3
  )
  default int healthTxtSize()
  {
    return 12;
  }

  @ConfigItem(
    keyName = "healthXOffset",
    name = "Text X Offset",
    description = "",
    section = healthSection,
    position = 4
  )
  default int healthXOffset()
  {
    return 0;
  }

  @ConfigItem(
    keyName = "healthYOffset",
    name = "Text Y Offset",
    description = "",
    section = healthSection,
    position = 5
  )
  default int healthYOffset()
  {
    return 0;
  }
	//endregion

	//region Prayer Options
	@ConfigSection(
		name = "Prayer",
		description = "",
		position = 1
	)
	String prayerSection = "Prayer";

	@ConfigItem(
		keyName = "prayer",
		name = "Enable Prayer",
		description = "Shows the amount of prayer points left.",
		section = prayerSection,
		position = 0
	)
	default boolean prayer()
	{
		return true;
	}

	@ConfigItem(
		keyName = "prayerColor",
		name = "Background Color",
		description = "",
		section = prayerSection,
		position = 1
	)
	default Color prayerColor()
	{
		return Color.decode("#00f3ff");
	}

	@ConfigItem(
		keyName = "prayerTxtColor",
		name = "Text Color",
		description = "",
		section = prayerSection,
		position = 2
	)
	default Color prayerTxtColor()
	{
		return Color.decode("#000000");
	}

	@ConfigItem(
		keyName = "prayerTxtSize",
		name = "Text Size",
		description = "",
		section = prayerSection,
		position = 3
	)
	default int prayerTxtSize()
	{
		return 12;
	}

  @ConfigItem(
    keyName = "prayerXOffset",
    name = "Text X Offset",
    description = "",
    section = prayerSection,
    position = 4
  )
  default int prayerXOffset()
  {
    return 0;
  }

  @ConfigItem(
    keyName = "prayerYOffset",
    name = "Text Y Offset",
    description = "",
    section = prayerSection,
    position = 5
  )
  default int prayerYOffset()
  {
    return 0;
  }


	//endregion

	//region Absorption Options
	@ConfigSection(
		name = "Absorption",
		description = "",
		position = 2
	)
	String absorptionSection = "Absorption";

	@ConfigItem(
		keyName = "absorption",
		name = "Enable Absorption",
		description = "Shows the amount of absorption points left while in Nightmare Zone.",
		section = absorptionSection,
		position = 0
	)
	default boolean absorption()
	{
		return true;
	}

	@ConfigItem(
		keyName = "absorptionColor",
		name = "Background Color",
		description = "",
		section = absorptionSection,
		position = 1
	)

	default Color absorptionColor()
	{
		return Color.decode("#ffffff");
	}

	@ConfigItem(
		keyName = "absorptionTxtColor",
		name = "Text Color",
		description = "",
		section = absorptionSection,
		position = 2
	)

	default Color absorptionTxtColor()
	{
		return Color.decode("#000000");
	}

	@ConfigItem(
		keyName = "absorptionTxtSize",
		name = "Text Size",
		description = "",
		section = absorptionSection,
		position = 3
	)

	default int absorptionTxtSize()
	{
		return 12;
	}

  @ConfigItem(
    keyName = "absorptionXOffset",
    name = "Text X Offset",
    description = "",
    section = absorptionSection,
    position = 4
  )
  default int absorptionXOffset()
  {
    return 0;
  }

  @ConfigItem(
    keyName = "absorptionYOffset",
    name = "Text Y Offset",
    description = "",
    section = absorptionSection,
    position = 5
  )
  default int absorptionYOffset()
  {
    return 0;
  }
	//endregion

	//region Cannonballs Options
	@ConfigSection(
		name = "Cannon",
		description = "",
		position = 3
	)
	String cannonSection = "Cannon";

	@ConfigItem(
		keyName = "cannon",
		name = "Enable Cannon",
		description = "Shows the amount of cannonballs left in your cannon.",
		section = cannonSection,
		position = 0
	)
	default boolean cannon()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cannonColor",
		name = "Background Color",
		description = "",
		section = cannonSection,
		position = 1
	)

	default Color cannonColor()
	{
		return Color.decode("#797979");
	}

	@ConfigItem(
		keyName = "cannonTxtColor",
		name = "Text Color",
		description = "",
		section = cannonSection,
		position = 2
	)

	default Color cannonTxtColor()
	{
		return Color.decode("#ffffff");
	}

	@ConfigItem(
		keyName = "cannonTxtDynamic",
		name = "Dynamic Text Color",
		description = "Changes the text color based on the amount of cannonballs left.",
		section = cannonSection,
		position = 3
	)

	default boolean cannonTxtDynamic()
	{
		return false;
	}

	@ConfigItem(
		keyName = "cannonTxtSize",
		name = "Text Size",
		description = "",
		section = cannonSection,
		position = 4
	)

	default int cannonTxtSize()
	{
		return 12;
	}

  @ConfigItem(
      keyName = "cannonXOffset",
      name = "Text X Offset",
      description = "",
      section = cannonSection,
      position = 5
    )
    default int cannonXOffset()
    {
      return 0;
    }

  @ConfigItem(
      keyName = "cannonYOffset",
      name = "Text Y Offset",
      description = "",
      section = cannonSection,
      position = 6
    )
    default int cannonYOffset()
    {
      return 0;
    }

	//endregion

	//region Inventory Options
	@ConfigSection(
		name = "Inventory",
		description = "",
		position = 4
	)

	String inventorySection = "Inventory";

	@ConfigItem(
		keyName = "inventory",
		name = "Enable Inventory Count",
		description = "Shows the amount of filled inventory slots.",
		section = inventorySection,
		position = 0
	)

	default boolean inventory()
	{
		return true;
	}

	@ConfigItem(
		keyName = "inventoryColor",
		name = "Background Color",
		description = "",
		section = inventorySection,
		position = 1
	)

	default Color inventoryColor()
	{
		return Color.decode("#845020");
	}

	@ConfigItem(
		keyName = "inventoryTxtColor",
		name = "Text Color",
		description = "",
		section = inventorySection,
		position = 2
	)

	default Color inventoryTxtColor()
	{
		return Color.decode("#ffffff");
	}

	@ConfigItem(
		keyName = "inventoryThreshold",
		name = "Count Threshold",
		description = "The amount of filled inventory slots at which the text color changes.",
		section = inventorySection,
		position = 3
	)

	default int inventoryThreshold()
	{
		return 20;
	}

	@ConfigItem(
		keyName = "inventoryThresholdTxtColor",
		name = "Threshold Text Color",
		description = "The amount of filled inventory slots at which the text color changes.",
		section = inventorySection,
		position = 4
	)

	default Color inventoryTxtThresholdColor()
	{
		return Color.decode("#ff6a00");
	}

	@ConfigItem(
		keyName = "inventoryFullTxtColor",
		name = "Inventory Full Text Color",
		description = "The color of the text when the inventory is full.",
		section = inventorySection,
		position = 5
	)

	default Color inventoryTxtFullColor()
	{
		return Color.decode("#00ff26");
	}

	@ConfigItem(
		keyName = "inventoryEmptyTxtColor",
		name = "Inventory Empty Text Color",
		description = "The color of the text when the inventory is empty.",
		section = inventorySection,
		position = 6
	)

	default Color inventoryTxtEmptyColor()
	{
		return Color.decode("#ff0000");
	}

	@ConfigItem(
		keyName = "inventoryTxtSize",
		name = "Inventory Text Size",
		description = "",
		section = inventorySection,
		position = 7
	)

	default int inventoryTxtSize()
	{
		return 12;
	} 

  @ConfigItem(
      keyName = "inventoryXOffset",
      name = "Text X Offset",
      description = "",
      section = cannonSection,
      position = 8
  )
  default int inventoryXOffset()
  {
    return 0;
  }

  @ConfigItem(
      keyName = "inventoryYOffset",
      name = "Text Y Offset",
      description = "",
      section = cannonSection,
      position = 9
  )
  default int inventoryYOffset()
  {
    return 0;
  }
	//endregion

	//region Special Attack Options
	@ConfigSection(
		name = "Special Attack",
		description = "",
		position = 5
	)

	String specSection = "Special Attack";

	@ConfigItem(
		keyName = "spec",
		name = "Enable Special Attack",
		description = "Shows the amount of special attack energy left.",
		section = specSection,
		position = 0
	)

	default boolean spec()
	{
		return true;
	}

	@ConfigItem(
		keyName = "specColor",
		name = "Background Color",
		description = "",
		section = specSection,
		position = 1
	)

	default Color specColor()
	{
		return Color.decode("#1b444f");
	}

	@ConfigItem(
		keyName = "specTxtColor",
		name = "Text Color",
		description = "",
		section = specSection,
		position = 2
	)

	default Color specTxtColor()
	{
		return Color.decode("#ffffff");
	}

	@ConfigItem(
		keyName = "specProgress",
		name = "Enable Special Attack Progress",
		description = "Changes the icon background to a progress bar that shows how long it will take to charge the special attack bar.",
		section = specSection,
		position = 3
	)

	default boolean specProgress()
	{
		return true;
	}

	@ConfigItem(
		keyName = "specProgressColor",
		name = "Special Attack Progress Color",
		description = "The color of the special attack progress bar.",
		section = specSection,
		position = 4
	)

	default Color specProgressColor()
	{
		return Color.decode("#4398ae");
	}

	@ConfigItem(
		keyName = "specTxtSize",
		name = "Special Attack Text Size",
		description = "",
		section = specSection,
		position = 5
	)

	default int specTxtSize()
	{
		return 12;
	}

  @ConfigItem(
      keyName = "specXOffset",
      name = "Text X Offset",
      description = "",
      section = specSection,
      position = 6
  )
  default int specXOffset()
  {
    return 0;
  }

  @ConfigItem(
      keyName = "specYOffset",
      name = "Text Y Offset",
      description = "",
      section = specSection,
      position = 7
  )
  default int specYOffset()
  {
    return 0;
  }
	//endregion
}
