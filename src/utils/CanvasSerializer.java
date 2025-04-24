package utils;

import com.google.gson.*;
import models.Canvas;
import models.drawable.Point;
import models.drawable.shape.Shape;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class CanvasSerializer {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeHierarchyAdapter(Shape.class, new ShapeSerializer()).create();

    public static void saveCanvas(Canvas canvas, File file) throws IOException {
        JsonObject canvasData = new JsonObject();

        JsonArray shapesArray = new JsonArray();
        for (Shape shape : canvas.getShapes()) {
            shapesArray.add(gson.toJsonTree(shape));
        }
        canvasData.add("shapes", shapesArray);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(canvas.getFillLayer(), "png", baos);
        String fillLayerBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        canvasData.addProperty("fillLayer", fillLayerBase64);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(canvasData, writer);
        }
    }

    public static void loadCanvas(Canvas canvas, File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            JsonObject canvasData = JsonParser.parseReader(reader).getAsJsonObject();

            canvas.clear();

            JsonArray shapesArray = canvasData.getAsJsonArray("shapes");
            for (JsonElement shapeElement : shapesArray) {
                Shape shape = gson.fromJson(shapeElement, Shape.class);
                canvas.addShape(shape);
            }

            String fillLayerBase64 = canvasData.get("fillLayer").getAsString();
            byte[] imageBytes = Base64.getDecoder().decode(fillLayerBase64);
            BufferedImage fillLayer = ImageIO.read(new ByteArrayInputStream(imageBytes));
            canvas.setFillLayer(fillLayer);

            canvas.repaint();
        }
    }

    private static class ShapeSerializer implements JsonSerializer<Shape>, JsonDeserializer<Shape> {
        @Override
        public JsonElement serialize(Shape shape, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", shape.getClass().getName());

            jsonObject.addProperty("lineType", shape.lineType.toString());
            jsonObject.addProperty("thickness", shape.thickness);
            jsonObject.addProperty("isFinished", shape.isFinished);
            jsonObject.addProperty("color", shape.color.getRGB());

            JsonArray pointsArray = new JsonArray();
            for (Point point : shape.points) {
                JsonObject pointObj = new JsonObject();
                pointObj.addProperty("x", point.getX());
                pointObj.addProperty("y", point.getY());
                pointsArray.add(pointObj);
            }
            jsonObject.add("points", pointsArray);

            return jsonObject;
        }

        @Override
        public Shape deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                JsonObject jsonObject = json.getAsJsonObject();
                String type = jsonObject.get("type").getAsString();

                Class<?> clazz = Class.forName(type);
                Shape shape = (Shape) clazz.getDeclaredConstructor().newInstance();

                shape.lineType = Enum.valueOf(enums.LineType.class, jsonObject.get("lineType").getAsString());
                shape.thickness = jsonObject.get("thickness").getAsInt();
                shape.isFinished = jsonObject.get("isFinished").getAsBoolean();
                shape.color = new java.awt.Color(jsonObject.get("color").getAsInt(), true);

                shape.points.clear();
                JsonArray pointsArray = jsonObject.getAsJsonArray("points");
                for (JsonElement pointElement : pointsArray) {
                    JsonObject pointObj = pointElement.getAsJsonObject();
                    int x = pointObj.get("x").getAsInt();
                    int y = pointObj.get("y").getAsInt();
                    shape.points.add(new Point(x, y));
                }

                return shape;
            } catch (Exception e) {
                throw new JsonParseException("Error deserializing shape: " + e.getMessage(), e);
            }
        }
    }
}