package tui;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс, позволяющий работать с определенной вырезкой из двухмерного буфера
 * На все окно создается буфер char[][], который и используется для отрисовки окна
 * На каждый TuiElement в окне затем выдается FramebufferSlice, который позволяет элементам работать со своим кусочком буфера
 * 
 * В примере ниже внешний прямоугольник - это оригинальный буфер char[][]
 * Внутренний прямоугольник - это кусочек буфера FramebufferSlice, который может быть выдан какому-нибудь элементу
 * +--------+
 * |        |
 * | +--+   |
 * | |  |   |
 * | +--+   |
 * +--------+
 */
public class FramebufferSlice {
    private final char[][] framebuffer; // Ссылка на оригинальный буфер
    private final AtomicBoolean dirty; // Был ли буфер изменен?

    // Параметры кусочка
    private final int x;
    private final int y;
    public final int w;
    public final int h;

    public FramebufferSlice(char[][] framebuffer, AtomicBoolean dirty, int x, int y, int w, int h) throws IllegalArgumentException {
        if(x < 0 || y < 0 || y + h > framebuffer.length || x + w > framebuffer[0].length) {
            throw new IllegalArgumentException("Я запрещаю вам выходить за пределы буфера");
        }

        this.framebuffer = framebuffer;
        this.dirty = dirty;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public FramebufferSlice(FramebufferSlice framebufferSlice, int x, int y, int w, int h) throws IllegalArgumentException {
        this(framebufferSlice.framebuffer, framebufferSlice.dirty, framebufferSlice.x + x, framebufferSlice.y + y, w, h);

        if(x < 0 || y < 0 || y + h > framebufferSlice.h || x + w > framebufferSlice.w) {
            throw new IllegalArgumentException("Я запрещаю вам выходить за пределы вырезки буфера");
        }
    }

    public char get(int x, int y) {
        if(x >= 0 && x < w && y >= 0 && y <= h) {
            return framebuffer[this.y + y][this.x + x];
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void set(int x, int y, char ch) {
        if(x >= 0 && x < w && y >= 0 && y <= h) {
            framebuffer[this.y + y][this.x + x] = ch;
            dirty.set(true);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

}
