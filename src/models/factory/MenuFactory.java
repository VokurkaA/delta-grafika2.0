package models.factory;

import enums.MenuType;
import models.menus.ColorMenu;
import models.menus.GenericMenu;
import models.menus.Menu;
import models.menus.SliderMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class MenuFactory {

    public static JButton createToolSelectionButton(String title, String tooltip, Icon icon, Enum<?>[] items, MenuType menuType, boolean isVisible, ActionListener listener, Map<String, JButton> buttonMap) {

        return getMenuByType(menuType).createButton(title, tooltip, icon, items, isVisible, e -> {
            listener.actionPerformed(e);
            updateButtonVisibility(buttonMap, e.getActionCommand());
        });
    }

    private static void updateButtonVisibility(Map<String, JButton> buttonMap, String toolName) {
        boolean isShapeTool = "shape".equals(toolName);
        boolean isEraserTool = "eraser".equals(toolName);

        if (buttonMap.containsKey("Shapes")) {
            buttonMap.get("Shapes").setVisible(isShapeTool);
        }

        if (buttonMap.containsKey("Line")) {
            buttonMap.get("Line").setVisible(isShapeTool);
        }

        if (buttonMap.containsKey("Color")) {
            buttonMap.get("Color").setVisible(!isEraserTool);
        }

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