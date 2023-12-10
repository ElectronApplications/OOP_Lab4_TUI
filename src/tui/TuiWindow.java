package tui;

import rawconsole.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class TuiWindow {
    public final TuiContainer container;
    public final String name;
    public final char[][] framebuffer;
    public final AtomicBoolean dirty;

    public boolean closed = false;

    public TuiWindow(String name, int width, int height) throws IllegalArgumentException {
        if(width < 1 || height < 1)
            throw new IllegalArgumentException("Размеры окна минимум 1x1");

        if(name.contains("\n"))
            throw new IllegalArgumentException("Название должно быть в одну строчку");

        this.name = name;

        framebuffer = new char[height + 2][width + 2];
        dirty = new AtomicBoolean(true);

        for (char[] line : framebuffer) {
            Arrays.fill(line, ' ');
        }
        TuiUtils.renderBorder(new FramebufferSlice(framebuffer, dirty, 0, 0, width + 2, height + 2));

        container = new TuiContainer(1, 1, width, height);
        container.framebuffer = new FramebufferSlice(framebuffer, dirty, 1, 1, width, height);

        name.getChars(0, Math.min(width - 2, name.length()), framebuffer[0], 2);
    }

    public void eventLoop() {
        container.update();
        render();
        RawKeys c;
        do {
            c = RawConsole.getRawKey();

            switch(c) {
                case KEY_ENTER:
                    container.click();
                    break;
                case KEY_LEFT:
                case KEY_UP:
                case KEY_RIGHT:
                case KEY_DOWN:
                    container.moveSelection(c);
                    break;
                case KEY_ESC:
                    if(!container.selected.isEmpty()) {
                        container.deselect();
                        c = RawKeys.KEY_UNKNOWN;
                    }
                    break;
            }

            if(dirty.get()) {
                render();
                dirty.set(false);
            }
        } while(c != RawKeys.KEY_ESC && !closed);
    }

    private void render() {
        RawConsole.clearConsole();
        for (char[] y : framebuffer) {
            for (char x : y) {
                System.out.print(x);
            }
            System.out.println();
        }
    }
}
