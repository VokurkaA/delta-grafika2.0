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
        menuMap.put(MenuType.slider, (items, listener) -> createSliderMenu("Slider Menu", listener));
        menuMap.put(MenuType.color, (items, listener) -> createColorMenu(listener));
    }

    public static JButton createMenu(String title, Enum<?>[] items, MenuType menuType, ActionListener listener) {
        JButton button = new JButton(title);
        int size = 50;
        button.setPreferredSize(new Dimension(size, size));
        button.setBackground(new Color(220, 220, 220));
        button.setFont(new Font("Arial", Font.PLAIN, 14));

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

    private static JPopupMenu createPopupMenu(MenuType menuType, Enum<?>[] items, ActionListener listener) {
        return switch (menuType) {
            case generic -> createGenericMenu(items, listener);
            case slider -> createSliderMenu("Slider Menu", listener);
            case color -> createColorMenu(listener);
            default -> throw new IllegalArgumentException("Unknown MenuType");
        };
    }

    private static JPopupMenu createGenericMenu(Enum<?>[] items, ActionListener listener) {
        JPopupMenu popupMenu = new JPopupMenu();
        for (Enum<?> item : items) {
            String itemName = item.name();
            itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1).toLowerCase();
            JMenuItem menuItem = new JMenuItem(itemName);
            menuItem.setActionCommand(itemName);
            menuItem.addActionListener(listener);
            popupMenu.add(menuItem);
        }
        return popupMenu;
    }


    private static JPopupMenu createSliderMenu(String title, ActionListener listener) {
        JPopupMenu popupMenu = new JPopupMenu();

        JSlider slider = new JSlider(1, 10, 5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(2);

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