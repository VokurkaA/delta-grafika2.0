# DELTA grafika

## Overview

DELTA grafika is a feature-rich Java drawing application that provides a digital canvas for creating and manipulating
vector and raster graphics. It offers various drawing tools, shapes, and customization options in an intuitive user
interface.

## Features

### Drawing Tools

- **Pen Tool**: Freehand drawing with adjustable line width and color
- **Shape Tool**: Create geometric shapes with customizable properties
- **Bucket Tool**: Fill enclosed areas with a selected color
- **Text Tool**: Add text to the canvas with customizable font size
- **Eraser Tool**: Remove parts of the drawing with adjustable size
- **Move Tool**: Select and reposition shapes on the canvas
- **Rasterizer Tool**: Convert vector shapes to raster graphics

### Shapes

- Lines with optional alignment constraints (hold Shift)
- Polygons with multiple control points
- Circles
- Rectangles
- Squares

### Line Styles

- Solid lines
- Dashed lines with customizable dash length
- Dotted lines with customizable spacing

### Canvas Operations

- Clear canvas
- Save drawings (.delta format)
- Open saved drawings
- Export as PNG image

## How to Build and Run

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- IntelliJ IDEA (recommended) or another Java IDE

### Building the Project

1. Clone the repository or download the source code
2. Open the project in IntelliJ IDEA
3. Build the project using the Build menu or Ctrl+F9

### Running the Application

- Run the `Main` class to launch the application
- Alternatively, build the JAR artifact and run it using:
  ```
  java -jar delta-grafika2-0.jar
  ```

## Usage Instructions

### Tool Selection

1. Click the "Tools" button in the toolbar
2. Select a drawing tool from the popup menu

### Drawing with Shapes

1. Select the shape tool
2. Choose a shape type from the Shapes menu
3. Click on the canvas to place the starting point
4. Move the mouse to adjust the shape
5. Click again to finalize the shape

### Customizing Appearance

- Use the Line menu to select line style (solid, dashed, dotted)
- Use the Width slider to adjust line thickness
- Use the Color picker to change the drawing color

### Canvas Management

- Use the Canvas menu to access options for saving, opening, exporting, or clearing

### Keyboard Shortcuts

- **Shift**: Hold while drawing to align lines/shapes to 45-degree angles
- **Escape**: Cancel current shape or deselect the moving shape

## Project Structure

### Core Packages

- `models`: Core data structures and UI components
- `rasterizers`: Algorithms for rendering different line types and shapes
- `utils`: Helper classes for settings, serialization, and icon management
- `enums`: Type definitions for tools, shapes, and line styles

### Key Classes

- `Canvas`: The main drawing area and application window
- `DrawingParams`: Manages the current drawing settings
- `ToolBar`: UI component for tool selection
- `SettingsManager`: Loads and applies application settings
- `CanvasSerializer`: Handles saving and loading of drawings

## Configuration

The application can be customized through the `settings.json` file, which allows you to modify:

- Canvas dimensions and background color
- Default drawing parameters (line width, color, shapes)
- UI properties (icon sizes, button dimensions, fonts)
- Tool-specific settings

## Dependencies

- [Google Gson](https://github.com/google/gson): For JSON parsing and serialization
- Java Swing and AWT: For UI components and graphics

## License

This project was created for educational purposes. See the zadani.md file for the original assignment requirements.

## Contributing

Contributions to improve DELTA grafika are welcome. Please feel free to submit pull requests or open issues for bugs and
feature requests.