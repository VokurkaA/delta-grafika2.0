package models.factory;

import enums.MenuType;
import models.menus.ColorMenu;
import models.menus.GenericMenu;
import models.menus.Menu;
import models.menus.SliderMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class MenuFactory {

    public static JButton createToolSelectionButton(String title, String tooltip, Icon icon, Enum<?>[] items, MenuType menuType, boolean isVisible, ActionListener listener, Map<String, JButton> buttonMap) {

        return getMenuByType(menuType).createButton(title, tooltip, icon, items, isVisible, e -> {
            listener.actionPerformed(e);
            updateButtonVisibility(buttonMap, e.getActionCommand());
        });
    }

    private static void updateButtonVisibility(Map<String, JButton> buttonMap, String toolName) {
        String[] alwaysVisibleButtons = {"Canvas", "Tools"};

        HashMap<String, String[]> toolButtonWhitelist = new HashMap<>() {{
            put("pen", new String[]{"Thickness", "Color"});
            put("bucket", new String[]{"Color"});
            put("shape", new String[]{"Shapes", "Line", "Thickness", "Color"});
            put("text", new String[]{"Thickness", "Color"});
            put("eraser", new String[]{"Thickness"});
            put("rasterizer", new String[]{});
            put("moveTool", new String[]{});
        }};

        Set<String> visibleButtons = new HashSet<>(Arrays.asList(alwaysVisibleButtons));

        if (toolButtonWhitelist.containsKey(toolName)) {
            visibleButtons.addAll(Arrays.asList(toolButtonWhitelist.get(toolName)));
        }

        buttonMap.forEach((key, button) -> button.setVisible(visibleButtons.contains(key)));

        SwingUtilities.invokeLater(() -> {
            if (!buttonMap.isEmpty()) {
                JButton anyButton = buttonMap.values().iterator().next();
                Container topLevelAncestor = anyButton.getTopLevelAncestor();
                if (topLevelAncestor instanceof JFrame) {
                    for (Component comp : ((JFrame) topLevelAncestor).getContentPane().getComponents()) {
                        if (comp instanceof JPanel) {
                            comp.requestFocusInWindow();
                            break;
                        }
                    }
                }
            }
        });
    }

    public static JButton createMenu(String title, String tooltip, Icon icon, Enum<?>[] items, MenuType menuType, boolean isVisible, ActionListener listener) {
        return getMenuByType(menuType).createButton(title, tooltip, icon, items, isVisible, listener);
    }

    public static JButton createMenu(String title, String tooltip, Icon icon, MenuType menuType, int min, int max, int initialValue, boolean isVisible, ActionListener listener) {
        return new SliderMenu(min, max, initialValue).createButton(title, tooltip, icon, null, isVisible, listener);
    }

    public static JButton createMenu(String title, String tooltip, Icon icon, MenuType menuType, boolean isVisible, ActionListener listener) {
        return getMenuByType(menuType).createButton(title, tooltip, icon, null, isVisible, listener);
    }

    private static Menu getMenuByType(MenuType menuType) {
        return switch (menuType) {
            case generic -> new GenericMenu();
            case slider -> new SliderMenu();
            case color -> new ColorMenu();
        };
    }
}