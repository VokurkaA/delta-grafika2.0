package models.menus;

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
        popupMenu.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE * enumItems.length / 2));

        for (Enum<?> item : enumItems) {
            JMenuItem menuItem = new JMenuItem(item.name());
            menuItem.setActionCommand(item.name());
            menuItem.addActionListener(listener);
            popupMenu.add(menuItem);
        }

        return popupMenu;
    }
}