package com.TrayIndicators;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class Icon
{
    private TrayIcon trayIcon;

    public Icon()
    {
        if (!SystemTray.isSupported())
            log.error("System tray is not supported");
    }

    private void createIcon(String text, Color bgColor, Color txtColor)
    {
        if (trayIcon != null)
            removeIcon();

        trayIcon = new TrayIcon(createImage(text, bgColor, txtColor));
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

    public void updateIcon(String text, Color bgColor, Color txtColor)
    {
        if (trayIcon == null)
        {
            createIcon(text, bgColor, txtColor);
            return;
        }

        trayIcon.getImage().flush();
        trayIcon.setImage(createImage(text, bgColor, txtColor));
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

    private BufferedImage createImage(String text, Color bgColor, Color txtColor)
    {
        int size = 16;

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
}
