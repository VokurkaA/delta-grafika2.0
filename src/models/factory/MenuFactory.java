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
    private static final int BUTTON_SIZE = 50;
    private static final Color BUTTON_COLOR = new Color(220, 220, 220);
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 14);

    static {
        menuMap.put(MenuType.generic, (items, listener) -> createGenericMenu((Enum<?>[]) items, listener));
        menuMap.put(MenuType.slider, (items, listener) -> createSliderMenu("Slider Menu", 1, 10, 1, listener));
        menuMap.put(MenuType.color, (items, listener) -> createColorMenu(listener));
    }

    public static JButton createMenu(String title, Enum<?>[] items, MenuType menuType, boolean isVisible, ActionListener listener) {
        JButton button = new JButton(title);
        setButtonDefaults(button, isVisible);

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

    public static JButton createMenuWithIcon(String title, Icon icon, Enum<?>[] items, MenuType menuType, boolean isVisible, ActionListener listener) {
        JButton button = new JButton(title, icon);
        setButtonDefaults(button, isVisible);

        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

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

    public static JButton createMenuWithIcon(String title, Icon icon, MenuType menuType, int min, int max, int initialValue, boolean isVisible, ActionListener listener) {
        if (menuType != MenuType.slider) {
            return createMenuWithIcon(title, icon, null, menuType, isVisible, listener);
        }

        final int[] currentValue = {initialValue};

        JButton button = new JButton(title, icon);
        setButtonDefaults(button, isVisible);

        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        button.addActionListener(e -> {
            JPopupMenu popupMenu = createSliderMenu(title, min, max, currentValue[0], evt -> {
                int newValue = Integer.parseInt(evt.getActionCommand());
                currentValue[0] = newValue;
                listener.actionPerformed(evt);
            });
            popupMenu.show(button, button.getWidth(), 0);
        });

        return button;
    }

    public static JButton createMenuWithIcon(String title, Icon icon, MenuType menuType, boolean isVisible, ActionListener listener) {
        JButton button = new JButton(title, icon);
        setButtonDefaults(button, isVisible);

        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

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
                JPopupMenu popupMenu = createPopupMenu(menuType, null, listener);
                popupMenu.show(button, button.getWidth(), 0);
            });
        }

        return button;
    }

    private static void setButtonDefaults(JButton button, boolean isVisible) {
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setMaximumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setBackground(BUTTON_COLOR);
        button.setFont(BUTTON_FONT);
        button.setVisible(isVisible);
        button.setFocusable(false);
    }

    public static JButton createMenu(String title, MenuType menuType, boolean isVisible, ActionListener listener) {
        return createMenu(title, null, menuType, isVisible, listener);
    }

    public static JButton createMenu(String title, MenuType menuType, int min, int max, int initialValue, boolean isVisible, ActionListener listener) {
        if (menuType != MenuType.slider) {
            return createMenu(title, null, menuType, isVisible, listener);
        }

        final int[] currentValue = {initialValue};

        JButton button = new JButton(title);
        setButtonDefaults(button, isVisible);

        button.addActionListener(e -> {
            JPopupMenu popupMenu = createSliderMenu(title, min, max, currentValue[0], evt -> {
                int newValue = Integer.parseInt(evt.getActionCommand());
                currentValue[0] = newValue;
                listener.actionPerformed(evt);
            });
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
        popupMenu.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE * items.length / 2));

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
        popupMenu.setPreferredSize(new Dimension(BUTTON_SIZE * 2, BUTTON_SIZE));

        JSlider slider = new JSlider(min, max, current);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing((max - min) / 5);

        slider.setPreferredSize(new Dimension(BUTTON_SIZE * 2 - 20, 50));
        slider.setMinimumSize(new Dimension(BUTTON_SIZE * 2 - 20, 50));
        slider.setMaximumSize(new Dimension(BUTTON_SIZE * 2 - 20, 50));

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                ActionEvent event = new ActionEvent(slider, ActionEvent.ACTION_PERFORMED, String.valueOf(slider.getValue()));
                listener.actionPerformed(event);
            }
        });

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setPreferredSize(new Dimension(BUTTON_SIZE * 2 - 10, 60));
        sliderPanel.add(slider, BorderLayout.CENTER);

        popupMenu.add(sliderPanel);
        return popupMenu;
    }

    private static JPopupMenu createColorMenu(ActionListener listener) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        JButton colorButton = new JButton("Choose Color");
        colorButton.setPreferredSize(new Dimension(BUTTON_SIZE - 20, BUTTON_SIZE / 2));

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