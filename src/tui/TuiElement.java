package tui;

public abstract class TuiElement {
    public FramebufferSlice framebuffer;
    public final int x, y;
    public final int width, height;

    public abstract void update();

    public TuiElement(int x, int y, int width, int height) throws IllegalArgumentException {
        if(width < 1 || height < 1)
            throw new IllegalArgumentException("Слишком маленький элемент");

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
