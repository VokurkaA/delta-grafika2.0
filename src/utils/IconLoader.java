package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IconLoader {
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();

    public static ImageIcon getIcon(String name, int size) {
        String key = name + "_" + size;

        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }

        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(IconLoader.class.getResource("/icons/" + name + ".png")));

            if (icon.getIconWidth() > 0) {
                Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                iconCache.put(key, scaledIcon);
                return scaledIcon;
            }
        } catch (Exception e) {
            System.err.println("Could not load icon: " + name + ", creating placeholder");
        }

        return createSimpleIcon(name, size);
    }

    private static ImageIcon createSimpleIcon(String name, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        int hash = name.hashCode();
        Color color = new Color(Math.abs(hash) % 256, Math.abs(hash * 31) % 256, Math.abs(hash * 63) % 256);

        g2d.setColor(color);
        g2d.fillRect(0, 0, size, size);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, size - 1, size - 1);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, size / 2));
        FontMetrics fm = g2d.getFontMetrics();
        String letter = name.substring(0, 1).toUpperCase();
        int textWidth = fm.stringWidth(letter);
        int textHeight = fm.getHeight();

        g2d.drawString(letter, (size - textWidth) / 2, size / 2 + textHeight / 4);

        g2d.dispose();

        ImageIcon icon = new ImageIcon(image);
        iconCache.put(name + "_" + size, icon);
        return icon;
    }
}