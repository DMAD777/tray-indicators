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

        if (!SystemTray.isSupported())
            log.error("System tray is not supported");
    }

    private void createIcon(int value, Color bgColor, Color txtColor)
    {
        if (trayIcon != null)
            removeIcon();

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

                if (config.cannonTxtDynamic()) {
                    if (value > 15) {
                        txtColor = Color.green;
                    } else if (value > 5) {
                        txtColor = Color.orange;
                    } else {
                        txtColor = Color.red;
                    }
                }
                else {
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
        graphics.fillRect (0, 0, size, size);

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
