package tui;

public class Button extends TuiElement implements ISelectable, IClickable {
    private boolean selected = false;

    private String text;
    public IClickAction clickAction = null;

    public Button(int x, int y, int width, int height, String text) throws IllegalArgumentException {
        super(x, y, width, height);

        if(width < 3 || height < 3)
            throw new IllegalArgumentException("Слишком маленький элемент");

        this.text = text;
    }

    @Override
    public void update() {
        TuiUtils.renderBorder(framebuffer);
        
        // Добавляем крестики по углам, если кнопка выделена
        if(selected) {
            framebuffer.set(0, 0, 'X');
            framebuffer.set(framebuffer.w - 1, 0, 'X');
            framebuffer.set(0, framebuffer.h - 1, 'X');
            framebuffer.set(framebuffer.w - 1, framebuffer.h - 1, 'X');
        }

        // Закрашиваем кнопку по разному, в зависимости от того, выделена она или нет
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                framebuffer.set(i, j, selected ? '#' : ' ');
            }
        }

        TuiUtils.renderText(new FramebufferSlice(framebuffer, 1, 1, framebuffer.w - 2, framebuffer.h - 2), text, true);
    }

    @Override
    public void onClick() {
        if (clickAction != null)
            clickAction.onClick(this);
    }

    @Override
    public void onSelect() {
        selected = true;
        update();
    }

    @Override
    public void onDeselect() {
        selected = false;
        update();
    }

    public void setText(String text) {
        this.text = text;
        update();
    }
}
