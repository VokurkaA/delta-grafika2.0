package models.menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SliderMenu extends Menu {
    private int min;
    private int max;
    private int currentValue;

    public SliderMenu() {
        this(1, 10, 1);
    }

    public SliderMenu(int min, int max, int initialValue) {
        this.min = min;
        this.max = max;
        this.currentValue = initialValue;
    }

    @Override
    public JPopupMenu createPopupMenu(String title, Object[] items, ActionListener listener) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setPreferredSize(new Dimension(BUTTON_SIZE * 2, BUTTON_SIZE));

        JSlider slider = new JSlider(min, max, currentValue);
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
                currentValue = slider.getValue();
            }
        });

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setPreferredSize(new Dimension(BUTTON_SIZE * 2 - 10, 60));
        sliderPanel.add(slider, BorderLayout.CENTER);

        popupMenu.add(sliderPanel);
        return popupMenu;
    }

    @Override
    public JButton createButton(String title, String tooltip, Icon icon, Object[] items, boolean isVisible, ActionListener listener) {
        if (items != null && items.length >= 3) {
            try {
                min = (int) items[0];
                max = (int) items[1];
                currentValue = (int) items[2];
            } catch (ClassCastException e) {
            }
        }

        return super.createButton(title, tooltip, icon, null, isVisible, listener);
    }
}