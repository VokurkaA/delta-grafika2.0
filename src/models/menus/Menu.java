package models.menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Menu {
    protected static final int BUTTON_SIZE = 50;
    protected static final Color BUTTON_COLOR = new Color(220, 220, 220);
    protected static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 14);

    public abstract JPopupMenu createPopupMenu(String title, Object[] items, ActionListener listener);

    public JButton createButton(String title, String tooltip, Icon icon, Object[] items, boolean isVisible, ActionListener listener) {
        JButton button = new JButton(title, icon);
        setButtonDefaults(button, isVisible, tooltip);

        button.addActionListener(e -> {
            JPopupMenu popupMenu = createPopupMenu(title, items, listener);
            popupMenu.show(button, button.getWidth(), 0);
        });

        return button;
    }

    protected void setButtonDefaults(JButton button, boolean isVisible, String tooltip) {
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setMaximumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setBackground(BUTTON_COLOR);
        button.setFont(BUTTON_FONT);
        button.setVisible(isVisible);
        button.setFocusable(false);
        button.setToolTipText(tooltip);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
    }
}