package me.theentropyshard.isoblock.render;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class IsometricBlockRenderer {
    public static final int TEXTURE_SIZE = 16;

    public static BufferedImage render(BufferedImage top, BufferedImage left, BufferedImage right, RenderOptions options) {
        top = IsometricBlockRenderer.processTopImage(top, options);
        left = IsometricBlockRenderer.processLeftImage(left, options);
        right = IsometricBlockRenderer.processRightImage(right, options);

        float isoWidth = 0.5f;
        float skew = isoWidth * 2;
        float z = (float) (options.getScale() * IsometricBlockRenderer.TEXTURE_SIZE) / 2;
        float sideHeight = top.getHeight() * 1.2f;

        BufferedImage image = new BufferedImage(top.getWidth() * 2, (int) (top.getHeight() + right.getHeight() * 1.2f), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g.setTransform(new AffineTransform(1, -isoWidth, 1, isoWidth, 0, 0));
        g.drawImage(top, (int) (-z - 1), (int) z, top.getWidth(), (int) (top.getHeight() + 1.5), null);

        float x = IsometricBlockRenderer.TEXTURE_SIZE * options.getScale();
        g.setTransform(new AffineTransform(1, -isoWidth, 0, skew, 0, isoWidth));
        g.drawImage(right, (int) x, (int) (x + z), right.getWidth(), (int) sideHeight, null);

        g.setTransform(new AffineTransform(1, isoWidth, 0, skew, 0, 0));
        g.drawImage(left, 0, (int) z, left.getWidth(), (int) sideHeight, null);

        g.dispose();

        return image;
    }

    private static BufferedImage processTopImage(BufferedImage top, RenderOptions options) {
        return IsometricBlockRenderer.scale(top, options.getScale());
    }

    private static BufferedImage processLeftImage(BufferedImage left, RenderOptions options) {
        BufferedImage scaled = IsometricBlockRenderer.scale(left, options.getScale());

        return options.isNoShadow() ? scaled : IsometricBlockRenderer.shadow(scaled, 1);
    }

    private static BufferedImage processRightImage(BufferedImage right, RenderOptions options) {
        BufferedImage scaled = IsometricBlockRenderer.scale(right, options.getScale());

        return options.isNoShadow() ? scaled : IsometricBlockRenderer.shadow(scaled, 2);
    }

    private static BufferedImage shadow(BufferedImage image, int multiplier) {
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

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
}
