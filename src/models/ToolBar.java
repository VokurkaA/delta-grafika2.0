package models;

import enums.*;
import models.factory.MenuFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ToolBar extends JToolBar {
    private final Map<String, JButton> toolButtons = new HashMap<>();

    public ToolBar(DrawingParams drawingParams, Consumer<DrawingShape> changeShape, Runnable clearCanvas) {
        super(JToolBar.VERTICAL);

        add(MenuFactory.createMenu("Canvas", CanvasMenuOptions.values(), MenuType.generic, true, e -> {
            switch (CanvasMenuOptions.valueOf(e.getActionCommand())) {
                case clear -> clearCanvas.run();
                case export -> System.out.println("Export triggered!");
            }
        }));

        add(MenuFactory.createMenu("Tools", DrawingTool.values(), MenuType.generic, true, e -> {
            DrawingTool selectedTool = DrawingTool.valueOf(e.getActionCommand());
            drawingParams.drawingTool = selectedTool;
            drawingParams.drawingShape = null;
            drawingParams.movingShape = null;
            System.out.println("Tool selected: " + e.getActionCommand());

            updateButtonVisibility(selectedTool);
        }));

        JButton shapesButton = MenuFactory.createMenu("Shapes", DrawingShape.values(), MenuType.generic, drawingParams.drawingTool == DrawingTool.shape, e -> {
            changeShape.accept(DrawingShape.valueOf(e.getActionCommand()));
        });
        toolButtons.put("Shapes", shapesButton);
        add(shapesButton);

        JButton lineButton = MenuFactory.createMenu("Line", LineType.values(), MenuType.generic, drawingParams.drawingTool == DrawingTool.shape, e -> {
            drawingParams.lineType = LineType.valueOf(e.getActionCommand());
        });
        toolButtons.put("Line", lineButton);
        add(lineButton);

        JButton thicknessButton = MenuFactory.createMenu("Thickness", MenuType.slider, 1, 10, drawingParams.lineWidth, drawingParams.drawingTool == DrawingTool.shape, e -> {
            drawingParams.lineWidth = Integer.parseInt(e.getActionCommand());
            System.out.println("Thickness selected: " + drawingParams.lineWidth);
        });
        toolButtons.put("Thickness", thicknessButton);
        add(thicknessButton);

        JButton colorButton = MenuFactory.createMenu("Color", MenuType.color, drawingParams.drawingTool == DrawingTool.shape, e -> {
            Color selectedColor = new Color(Integer.parseInt(e.getActionCommand()));
            drawingParams.drawingColor = selectedColor;
            System.out.println("Color chosen: " + selectedColor);
        });
        toolButtons.put("Color", colorButton);
        add(colorButton);

        updateButtonVisibility(drawingParams.drawingTool);
    }

    private void updateButtonVisibility(DrawingTool selectedTool) {
        boolean showShapeButtons = (selectedTool == DrawingTool.shape);

        toolButtons.get("Shapes").setVisible(showShapeButtons);
        toolButtons.get("Line").setVisible(showShapeButtons);

        revalidate();
        repaint();
    }
}