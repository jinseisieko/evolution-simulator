// src/main/java/org/jinseisieko/evolution/view/Viewport.java
package org.jinseisieko.evolution.view;

import java.awt.*;
import java.util.List;

/**
 * Класс, представляющий окно просмотра для отрисовки объектов, реализующих интерфейс {@link Drawable}.
 * <p>
 * Viewport отвечает за:
 * <ul>
 *   <li>Преобразование логических координат (в диапазоне [0.0, 1.0)) в пиксельные координаты на экране.</li>
 *   <li>Применение стиля рисования ({@link DrawStyle}) к объектам перед их отрисовкой.</li>
 *   <li>Отрисовку кругов и квадратов с учетом их типа и размера.</li>
 * </ul>
 * <p>
 * Этот класс используется компонентом {@link SimulationView} для визуализации состояния симуляции.
 *
 * @author jinseisieko
 */
public class Viewport {
    private final int width;
    private final int height;
    private final int posX;
    private final int posY;
    private Graphics2D graphics2D;

    /**
     * Конструктор Viewport.
     *
     * @param graphics2D Объект Graphics2D, в котором будет производиться отрисовка.
     * @param width      Ширина области просмотра в пикселях.
     * @param height     Высота области просмотра в пикселях.
     * @param posX       X-координата верхнего левого угла области просмотра на экране.
     * @param posY       Y-координата верхнего левого угла области просмотра на экране.
     */
    public Viewport(int width, int height, int posX, int posY) {
        this.graphics2D = null;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Обновляет graphics2D для последующей отрисовки
     * Метод должен быть обязательно вызван перед методом draw()
     */
    public void updateGraphics2D(Graphics2D graphics2D) {
        if (graphics2D == null) {
            throw new IllegalArgumentException("graphics2D cannot be null");
        }
        this.graphics2D = graphics2D;
    }

    /**
     * Отрисовывает один объект, реализующий интерфейс {@link Drawable}.
     * <p>
     * Метод преобразует логические координаты и размер объекта в пиксельные,
     * применяет его стиль рисования и вызывает соответствующий метод отрисовки
     * в зависимости от типа формы объекта.
     *
     * @param drawable Объект для отрисовки.
     */
    public void draw(Drawable drawable) {
        if (this.graphics2D == null) {
            throw new IllegalStateException("graphics2D must be updated before drawing");
        }
        // Применяем стиль объекта
        applyStyle(drawable.getStyle());
        // Преобразуем логические координаты и размер в пиксельные
        int pixelX = toPixelX(drawable.getX());
        int pixelY = toPixelY(drawable.getY());
        int pixelSize = toPixelSize(drawable.getSize());

        // Отображаем объект в зависимости от его типа формы
        drawable.draw(graphics2D, pixelX, pixelY, pixelSize);

        this.graphics2D = null;
    }

    /**
     * Отрисовывает список объектов, реализующих интерфейс {@link Drawable}.
     *
     * @param drawables Список объектов для отрисовки.
     */
    public void drawAll(List<Drawable> drawables) {
        for (Drawable drawable : drawables) {
            draw(drawable);
        }
    }

    /**
     * Применяет стиль рисования к текущему контексту Graphics2D.
     *
     * @param style Стиль рисования для применения.
     */
    private void applyStyle(DrawStyle style) {
        style.apply(graphics2D);
    }

    /**
     * Преобразует логическую X-координату (в диапазоне [0.0, 1.0)) в пиксельную координату.
     *
     * @param x Логическая X-координата.
     * @return Пиксельная X-координата.
     */
    private int toPixelX(double x) {
        return posX + (int) Math.round(x * width);
    }

    /**
     * Преобразует логическую Y-координату (в диапазоне [0.0, 1.0)) в пиксельную координату.
     *
     * @param y Логическая Y-координата.
     * @return Пиксельная Y-координата.
     */
    private int toPixelY(double y) {
        return posY + (int) Math.round(y * height);
    }

    /**
     * Преобразует логический размер (например, диаметр или длина стороны) в пиксельный размер.
     *
     * @param size Логический размер.
     * @return Пиксельный размер.
     */
    private int toPixelSize(double size) {
        // Используем среднее арифметическое ширины и высоты для масштабирования размера
        return (int) Math.round(size * (width + height) / 2.0);
    }
}