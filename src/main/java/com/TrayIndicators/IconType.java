package com.TrayIndicators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;

import java.awt.Color;

@Getter
@AllArgsConstructor
public enum IconType
{
    Health(
            TrayIndicatorsConfig::healthColor,
            TrayIndicatorsConfig::healthTxtColor,
            client -> Integer.toString(client.getBoostedSkillLevel(Skill.HITPOINTS)),
            TrayIndicatorsConfig::health
    ),
    Prayer(
            TrayIndicatorsConfig::prayerColor,
            TrayIndicatorsConfig::prayerTxtColor,
            client -> Integer.toString(client.getBoostedSkillLevel(Skill.PRAYER)),
            TrayIndicatorsConfig::prayer
    ),
    Absorption(
            TrayIndicatorsConfig::absorptionColor,
            TrayIndicatorsConfig::absorptionTxtColor,
            client -> Integer.toString(client.getVarbitValue(Varbits.NMZ_ABSORPTION)),
            TrayIndicatorsConfig::absorption
    );

    private final ColorSupplier bgColorSupplier;
    private final ColorSupplier txtColorSupplier;
    private final TextSupplier textSupplier;
    private final VisibilitySupplier visibilitySupplier;

    public Color getBgColor(TrayIndicatorsConfig config)
    {
        return bgColorSupplier.getColor(config);
    }

    public Color getTxtColor(TrayIndicatorsConfig config)
    {
        return txtColorSupplier.getColor(config);
    }

    public String getTxt(Client client)
    {
        return textSupplier.getText(client);
    }

    public Boolean isActive(TrayIndicatorsConfig config)
    {
        return visibilitySupplier.isActive(config);
    }

    private interface ColorSupplier
    {
        Color getColor(TrayIndicatorsConfig config);
    }

    private interface TextSupplier
    {
        String getText(Client client);
    }

    private interface VisibilitySupplier
    {
        Boolean isActive(TrayIndicatorsConfig config);
    }
}