package models.menus;

import utils.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GenericMenu extends Menu {

    @Override
    public JPopupMenu createPopupMenu(String title, Object[] items, ActionListener listener) {
        if (!(items instanceof Enum<?>[] enumItems)) {
            throw new IllegalArgumentException("Generic menu requires enum items");
        }

        JPopupMenu popupMenu = new JPopupMenu();

        int columns = Math.min(3, enumItems.length);
        int rows = (int) Math.ceil((double) enumItems.length / columns);

        popupMenu.setLayout(new GridLayout(rows, columns, 2, 2));

        int iconSize = BUTTON_SIZE - 10;

        for (Enum<?> item : enumItems) {
            String itemName = item.name();
            JMenuItem menuItem = new JMenuItem();

            ImageIcon icon = IconLoader.getIcon(itemName.toLowerCase(), iconSize);
            menuItem.setIcon(icon);

            menuItem.setToolTipText(capitalizeFirstLetter(itemName));

            menuItem.setText("");

            menuItem.setActionCommand(itemName);
            menuItem.addActionListener(listener);

            menuItem.setPreferredSize(new Dimension(iconSize + 4, iconSize + 4));
            popupMenu.add(menuItem);
        }

        return popupMenu;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}