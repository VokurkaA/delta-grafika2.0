package models;

import enums.DrawingShape;
import enums.DrawingTool;
import enums.CanvasMenuOptions;
import enums.MenuType;
import models.factory.MenuFactory;

import javax.swing.*;

public class ToolBar extends JToolBar {

    public ToolBar() {
        super(JToolBar.VERTICAL);

        add(MenuFactory.createMenu("Canvas", CanvasMenuOptions.values(), MenuType.generic, e -> {
            System.out.println("Option selected: " + e.getActionCommand());
        }));
        add(MenuFactory.createMenu("Tools", DrawingTool.values(), MenuType.generic, e -> {
            System.out.println("Tool selected: " + e.getActionCommand());
        }));
        add(MenuFactory.createMenu("Shapes", DrawingShape.values(), MenuType.generic, e -> {
            System.out.println("Shape selected: " + e.getActionCommand());
        }));
        add(MenuFactory.createMenu("Thickness", null, MenuType.slider, e -> {
            System.out.println("Slider value: " + e.getActionCommand());
        }));
        add(MenuFactory.createMenu("Color", null, MenuType.color, e -> {
            System.out.println("Color chosen: " + e.getActionCommand());
        }));


    }
}
