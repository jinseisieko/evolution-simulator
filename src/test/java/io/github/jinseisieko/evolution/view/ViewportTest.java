// src/test/java/org/jinseisieko/evolution/view/ViewportTest.java
package io.github.jinseisieko.evolution.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.github.jinseisieko.evolution.view.stubs.DrawableStub;

/**
 * @author jinseisieko
 */
class ViewportTest {
    @Test
    void viewprot_worksCorrectly() {
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        Viewport viewport = new Viewport(width, height, 0, 0);
        viewport.updateGraphics2D(graphics2D);
        

        DrawStyle redStyle = DrawStyle.filled(Color.RED);
        DrawableStub f = new DrawableStub(0.5, 0.5, 0.05, redStyle);
        viewport.draw(f);
        
        graphics2D.dispose();

        Color color = new Color(image.getRGB(50, 50), true);
        assertTrue(color.getRed() > 200);
    }

    @Test
    void toPixelX_toPixelY_shouldConvertCoordinatesCorrectly() {
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        Viewport viewport = new Viewport(width, height, 0, 0);
        viewport.updateGraphics2D(graphics2D);

        DrawStyle blackStyle = DrawStyle.filled(Color.BLACK);
        DrawableStub cornerCircle = new DrawableStub(0.0, 0.0, 0.05, blackStyle);

        viewport.draw(cornerCircle);

        graphics2D.dispose();

        Color pixelAtCorner = new Color(image.getRGB(0, 0), true);
        assertTrue(pixelAtCorner.getRed() < 50);
        assertTrue(pixelAtCorner.getGreen() < 50);
        assertTrue(pixelAtCorner.getBlue() < 50);
    }

    @Test
    void drawCircle_shouldRenderCorrectly() {
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        Viewport viewport = new Viewport(width, height, 0, 0);
        viewport.updateGraphics2D(graphics2D);

        DrawStyle redStyle = DrawStyle.filled(Color.RED);

        DrawableStub circleDrawable = new DrawableStub(0.5, 0.5, 0.2, redStyle);

        viewport.draw(circleDrawable);

        graphics2D.dispose();

        Color centerColor = new Color(image.getRGB(50, 50), true);
        assertTrue(centerColor.getRed() > 200);
        assertTrue(centerColor.getGreen() < 50);
        assertTrue(centerColor.getBlue() < 50);
    }
}