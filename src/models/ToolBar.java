package models;

import enums.*;
import models.factory.MenuFactory;
import models.factory.ToolFactory;
import models.tools.ShapeTool;
import models.tools.Tool;

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
                case export -> {
                    Container topLevelAncestor = getTopLevelAncestor();
                    if (topLevelAncestor instanceof Canvas) {
                        ((Canvas) topLevelAncestor).exportCanvas();
                    }
                }
            }
        }));

        add(MenuFactory.createMenu("Tools", DrawingTool.values(), MenuType.generic, true, e -> {
            drawingParams.drawingTool = ToolFactory.getToolByEnum(DrawingTool.valueOf(e.getActionCommand()));
            drawingParams.movingShape = null;

            updateButtonVisibility(drawingParams.drawingTool);
        }));

        JButton shapesButton = MenuFactory.createMenu("Shapes", DrawingShape.values(), MenuType.generic, (drawingParams.drawingTool instanceof ShapeTool), e -> changeShape.accept(DrawingShape.valueOf(e.getActionCommand())));
        toolButtons.put("Shapes", shapesButton);
        add(shapesButton);

        JButton lineButton = MenuFactory.createMenu("Line", LineType.values(), MenuType.generic, (drawingParams.drawingTool instanceof ShapeTool), e -> drawingParams.lineType = LineType.valueOf(e.getActionCommand()));
        toolButtons.put("Line", lineButton);
        add(lineButton);

        JButton thicknessButton = MenuFactory.createMenu("Thickness", MenuType.slider, 1, 25, drawingParams.lineWidth, true, e -> drawingParams.lineWidth = Integer.parseInt(e.getActionCommand()));
        toolButtons.put("Thickness", thicknessButton);
        add(thicknessButton);

        JButton colorButton = MenuFactory.createMenu("Color", MenuType.color, true, e -> drawingParams.drawingColor = new Color(Integer.parseInt(e.getActionCommand())));
        toolButtons.put("Color", colorButton);
        add(colorButton);

        updateButtonVisibility(drawingParams.drawingTool);
    }

    private void updateButtonVisibility(Tool selectedTool) {
        boolean showShapeButtons = (selectedTool instanceof ShapeTool);

        toolButtons.get("Shapes").setVisible(showShapeButtons);
        toolButtons.get("Line").setVisible(showShapeButtons);

        SwingUtilities.invokeLater(() -> {
            Container topLevelAncestor = getTopLevelAncestor();
            if (topLevelAncestor instanceof Canvas) {
                ((Canvas) topLevelAncestor).getDrawingPanel().requestFocusInWindow();
            }
        });

        revalidate();
        repaint();
    }
}