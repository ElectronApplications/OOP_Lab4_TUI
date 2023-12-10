package tui;

public class Label extends TuiElement {
    private String text;
    private boolean centered;

    public Label(int x, int y, int width, int height, String text, boolean centered) throws IllegalArgumentException {
        super(x, y, width, height);
        this.text = text;
        this.centered = centered;
    }

    @Override
    public void update() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                framebuffer.set(i, j, ' ');
            }
        }
        TuiUtils.renderText(framebuffer, text, centered);
    }

    public void setText(String text) {
        this.text = text;
        update();
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
        update();
    }
}
