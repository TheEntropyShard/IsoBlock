package me.theentropyshard.isoblock.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IsometricBlockRenderer {
    public static final int TEXTURE_SIZE = 16;

    public static void render(
            Path topImagePath, Path leftImagePath, Path rightImagePath,
            int scale, boolean noShadow, Path outputPath, String format
    ) throws Exception {

        BufferedImage topImage = IsometricBlockRenderer.scale(IsometricBlockRenderer.loadImage(topImagePath), scale);
        BufferedImage leftImage = noShadow ? IsometricBlockRenderer.scale(IsometricBlockRenderer.loadImage(leftImagePath), scale) : IsometricBlockRenderer.applyShadow(IsometricBlockRenderer.scale(IsometricBlockRenderer.loadImage(leftImagePath), scale), 1);
        BufferedImage rightImage = noShadow ? IsometricBlockRenderer.scale(IsometricBlockRenderer.loadImage(rightImagePath), scale) : IsometricBlockRenderer.applyShadow(IsometricBlockRenderer.scale(IsometricBlockRenderer.loadImage(rightImagePath), scale), 2);

        float isoWidth = 0.5f;
        float skew = isoWidth * 2;
        float z = (float) (scale * IsometricBlockRenderer.TEXTURE_SIZE) / 2;
        float sideHeight = topImage.getHeight() * 1.2f;

        BufferedImage image = new BufferedImage(topImage.getWidth() * 2, (int) (topImage.getHeight() + rightImage.getHeight() * 1.2f), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g.setTransform(new AffineTransform(1, -isoWidth, 1, isoWidth, 0, 0));
        g.drawImage(topImage, (int) (-z - 1), (int) z, topImage.getWidth(), (int) (topImage.getHeight() + 1.5), null);

        float x = IsometricBlockRenderer.TEXTURE_SIZE * scale;
        g.setTransform(new AffineTransform(1, -isoWidth, 0, skew, 0, isoWidth));
        g.drawImage(rightImage, (int) x, (int) (x + z), rightImage.getWidth(), (int) sideHeight, null);

        g.setTransform(new AffineTransform(1, isoWidth, 0, skew, 0, 0));
        g.drawImage(leftImage, 0, (int) z, leftImage.getWidth(), (int) sideHeight, null);

        ImageIO.write(image, format, Files.newOutputStream(outputPath));
    }

    private static int[] getPixels(BufferedImage image) {
        return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    private static BufferedImage applyShadow(BufferedImage image, int multiplier) {
        int[] pixels = IsometricBlockRenderer.getPixels(image);

        float val = 1.25f * multiplier;

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];

            int a = pixel >> 24 & 0xFF;
            int r = pixel >> 16 & 0xFF;
            int g = pixel >> 8 & 0xFF;
            int b = pixel & 0xFF;

            pixels[i] = a << 24 | (int) (r / val) << 16 | (int) (g / val) << 8 | (int) (b / val);
        }

        return image;
    }

    private static BufferedImage scale(BufferedImage image, int scale) {
        if (scale == 1) {
            return image;
        }

        int scaledWidth = image.getWidth() * scale;
        int scaledHeight = image.getHeight() * scale;

        Image scaledInstance = image.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_FAST);

        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(scaledInstance, 0, 0, null);
        g.dispose();

        return scaledImage;
    }

    private static BufferedImage loadImage(Path path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(Files.newInputStream(path));

        if (bufferedImage.getWidth() != IsometricBlockRenderer.TEXTURE_SIZE) {
            throw new RuntimeException("Texture width must be " + IsometricBlockRenderer.TEXTURE_SIZE + " pixels");
        }

        if (bufferedImage.getHeight() != IsometricBlockRenderer.TEXTURE_SIZE) {
            throw new RuntimeException("Texture height must be " + IsometricBlockRenderer.TEXTURE_SIZE + " pixels");
        }

        BufferedImage image = new BufferedImage(
                IsometricBlockRenderer.TEXTURE_SIZE,
                IsometricBlockRenderer.TEXTURE_SIZE,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(bufferedImage, 0, 0, null);
        g.dispose();

        return image;
    }
}
