package models.menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorMenu extends Menu {

    @Override
    public JPopupMenu createPopupMenu(String title, Object[] items, ActionListener listener) {
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

    @Override
    public JButton createButton(String title, String tooltip, Icon icon, Object[] items, boolean isVisible, ActionListener listener) {
        JButton button = new JButton(title, icon);
        setButtonDefaults(button, isVisible, tooltip);

        button.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(button, "Choose a Color", Color.BLACK);
            if (selectedColor != null) {
                ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, String.valueOf(selectedColor.getRGB()));
                listener.actionPerformed(event);
            }
        });

        return button;
    }
}