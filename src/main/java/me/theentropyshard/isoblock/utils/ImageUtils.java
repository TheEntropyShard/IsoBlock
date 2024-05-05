package me.theentropyshard.isoblock.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImageUtils {
    public static BufferedImage loadImage(Path path) throws IOException {
        BufferedImage rawImage = ImageIO.read(Files.newInputStream(path));
        BufferedImage image = new BufferedImage(rawImage.getWidth(), rawImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(rawImage, 0, 0, null);
        g.dispose();

        return image;
    }

    public static void writeImage(Path path, BufferedImage image) throws IOException {
        ImageIO.write(image, "PNG", Files.newOutputStream(path));
    }

    private ImageUtils() {
        throw new UnsupportedOperationException();
    }
}
