package models;

import enums.*;
import models.factory.MenuFactory;
import models.factory.ToolFactory;
import models.tools.EraserTool;
import models.tools.ShapeTool;
import utils.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ToolBar extends JToolBar {
    private static final int ICON_SIZE = 24;

    public ToolBar(DrawingParams drawingParams, Consumer<DrawingShape> changeShape, Runnable clearCanvas) {
        super(JToolBar.VERTICAL);
        setFloatable(false);

        add(MenuFactory.createMenu("Canvas", "Canvas", IconLoader.getIcon("canvas", ICON_SIZE), CanvasMenuOptions.values(), MenuType.generic, true, e -> {
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

        Map<String, JButton> toolButtons = new HashMap<>();
        JButton toolsButton = MenuFactory.createToolSelectionButton("Tools", "Select a drawing tool", IconLoader.getIcon("tools", ICON_SIZE), DrawingTool.values(), MenuType.generic, true, e -> {
            drawingParams.drawingTool = ToolFactory.getToolByEnum(DrawingTool.valueOf(e.getActionCommand()));
            drawingParams.movingShape = null;
        }, toolButtons);
        add(toolsButton);

        JButton shapesButton = MenuFactory.createMenu("Shapes", "Choose a shape", IconLoader.getIcon("shapes", ICON_SIZE), DrawingShape.values(), MenuType.generic, (drawingParams.drawingTool instanceof ShapeTool), e -> changeShape.accept(DrawingShape.valueOf(e.getActionCommand())));
        toolButtons.put("Shapes", shapesButton);
        add(shapesButton);

        JButton lineButton = MenuFactory.createMenu("Line", "Select line style", IconLoader.getIcon("line_style", ICON_SIZE), LineType.values(), MenuType.generic, (drawingParams.drawingTool instanceof ShapeTool), e -> drawingParams.lineType = LineType.valueOf(e.getActionCommand()));
        toolButtons.put("Line", lineButton);
        add(lineButton);

        JButton thicknessButton = MenuFactory.createMenu("Width", "Adjust line thickness", IconLoader.getIcon("thickness", ICON_SIZE), MenuType.slider, 1, 25, drawingParams.lineWidth, true, e -> drawingParams.lineWidth = Integer.parseInt(e.getActionCommand()));
        toolButtons.put("Thickness", thicknessButton);
        add(thicknessButton);

        JButton colorButton = MenuFactory.createMenu("Color", "Choose drawing color", IconLoader.getIcon("color", ICON_SIZE), MenuType.color, !(drawingParams.drawingTool instanceof EraserTool), e -> drawingParams.drawingColor = new Color(Integer.parseInt(e.getActionCommand())));
        toolButtons.put("Color", colorButton);
        add(colorButton);
    }
}