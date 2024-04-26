package com.TrayIndicators;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.Color;

@ConfigGroup("Tray Indicators")
public interface TrayIndicatorsConfig extends Config {

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
			description = "",
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
	default Color healthColor() { return Color.decode("#ff0000"); }

	@ConfigItem(
			keyName = "healthTxtColor",
			name = "Text Color",
			description = "",
			section = healthSection,
			position = 2
	)
	default Color healthTxtColor() { return Color.decode("#ffffff"); }
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
			description = "",
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
			description = "",
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
			description = "",
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
			name = "Text Color",
			description = "",
			section = cannonSection,
			position = 2
	)

	default boolean cannonTxtDynamic()
	{
		return false;
	}
	//endregion
}