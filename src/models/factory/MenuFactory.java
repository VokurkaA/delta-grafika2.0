package models.factory;

import enums.MenuType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.function.BiConsumer;

public class MenuFactory {

    private static final EnumMap<MenuType, BiConsumer<Object[], ActionListener>> menuMap = new EnumMap<>(MenuType.class);

    static {
        menuMap.put(MenuType.generic, (items, listener) -> createGenericMenu((Enum<?>[]) items, listener));
        menuMap.put(MenuType.slider, (items, listener) -> createSliderMenu("Slider Menu", 1, 10, 1, listener));
        menuMap.put(MenuType.color, (items, listener) -> createColorMenu(listener));
    }

    public static JButton createMenu(String title, Enum<?>[] items, MenuType menuType, boolean isVisible, ActionListener listener) {
        JButton button = new JButton(title);
        int size = 50;
        button.setPreferredSize(new Dimension(size, size));
        button.setBackground(new Color(220, 220, 220));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setVisible(isVisible);

        if (menuType == MenuType.color) {
            button.addActionListener(e -> {
                Color selectedColor = JColorChooser.showDialog(button, "Choose a Color", Color.BLACK);
                if (selectedColor != null) {
                    ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, String.valueOf(selectedColor.getRGB()));
                    listener.actionPerformed(event);
                }
            });
        } else {
            button.addActionListener(e -> {
                JPopupMenu popupMenu = createPopupMenu(menuType, items, listener);
                popupMenu.show(button, button.getWidth(), 0);
            });
        }

        return button;
    }

    public static JButton createMenu(String title, MenuType menuType, boolean isVisible, ActionListener listener) {
        return createMenu(title, null, menuType, isVisible, listener);
    }

    public static JButton createMenu(String title, MenuType menuType, int min, int max, int current, boolean isVisible, ActionListener listener) {
        if (menuType != MenuType.slider) {
            return createMenu(title, null, menuType, isVisible, listener);
        }

        JButton button = new JButton(title);
        int size = 50;
        button.setPreferredSize(new Dimension(size, size));
        button.setBackground(new Color(220, 220, 220));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setVisible(isVisible);

        button.addActionListener(e -> {
            JPopupMenu popupMenu = createSliderMenu(title, min, max, current, listener);
            popupMenu.show(button, button.getWidth(), 0);
        });

        return button;
    }

    private static JPopupMenu createPopupMenu(MenuType menuType, Enum<?>[] items, ActionListener listener) {
        return switch (menuType) {
            case generic -> createGenericMenu(items, listener);
            case slider -> createSliderMenu("Slider Menu", 1, 10, 1, listener);
            case color -> createColorMenu(listener);
            default -> throw new IllegalArgumentException("Unknown MenuType");
        };
    }

    private static JPopupMenu createGenericMenu(Enum<?>[] items, ActionListener listener) {
        JPopupMenu popupMenu = new JPopupMenu();
        for (Enum<?> item : items) {
            JMenuItem menuItem = new JMenuItem(item.name());
            menuItem.setActionCommand(item.name());
            menuItem.addActionListener(listener);
            popupMenu.add(menuItem);
        }
        return popupMenu;
    }

    private static JPopupMenu createSliderMenu(String title, int min, int max, int current, ActionListener listener) {
        JPopupMenu popupMenu = new JPopupMenu();

        JSlider slider = new JSlider(min, max, current);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing((max - min) / 5);

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                ActionEvent event = new ActionEvent(slider, ActionEvent.ACTION_PERFORMED, String.valueOf(slider.getValue()));
                listener.actionPerformed(event);
            }
        });

        popupMenu.add(slider);
        return popupMenu;
    }

    private static JPopupMenu createColorMenu(ActionListener listener) {
        JPopupMenu popupMenu = new JPopupMenu();

        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
            if (selectedColor != null) {
                ActionEvent event = new ActionEvent(colorButton, ActionEvent.ACTION_PERFORMED, String.valueOf(selectedColor.getRGB()));
                listener.actionPerformed(event);
            }
        });

        popupMenu.add(colorButton);
        return popupMenu;
    }
}