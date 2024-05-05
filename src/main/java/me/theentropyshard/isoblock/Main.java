package me.theentropyshard.isoblock;

import me.theentropyshard.isoblock.render.IsometricBlockRenderer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        Path topImage = Paths.get("top texture png");
        Path leftImage = Paths.get("left texture png");
        Path rightImage = Paths.get("right texture png");

        Path outputFile = Paths.get(".", "output.png");

        IsometricBlockRenderer.render(topImage, leftImage, rightImage, 1, false, outputFile, "PNG");
    }
}
