package me.theentropyshard.isoblock.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImageUtils {
    public static BufferedImage readImage(Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return ImageIO.read(inputStream);
        }
    }

    public static void writeImage(Path path, BufferedImage image) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            ImageIO.write(image, "PNG", outputStream);
        }
    }

    public static BufferedImage loadImage(Path path) throws IOException {
        BufferedImage rawImage = ImageUtils.readImage(path);
        BufferedImage image = new BufferedImage(rawImage.getWidth(), rawImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(rawImage, 0, 0, null);
        g.dispose();

        return image;
    }

    private ImageUtils() {
        throw new UnsupportedOperationException();
    }
}
