package tui;

import rawconsole.RawKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuiContainer extends TuiElement implements IContainer {
    protected Map<String, TuiElement> children = new HashMap<>();
    public String selected = "";

    public TuiContainer(int x, int y, int width, int height) throws IllegalArgumentException {
        super(x, y, width, height);
    }

    public void addChild(String id, TuiElement child) throws IllegalArgumentException {
        if(children.containsKey(id))
            throw new IllegalArgumentException("Элемент с таким id уже есть");

        boolean overlaps = false;
        for (Map.Entry<String, TuiElement> element : children.entrySet()) {
            int xmin = element.getValue().x;
            int xmax = xmin + element.getValue().width;
            int ymin = element.getValue().y;
            int ymax = ymin + element.getValue().height;

            overlaps |= (xmax > child.x) && (child.x + child.width > xmin) && (ymax > child.y) && (child.y + child.height > ymin);
        }

        if(overlaps)
            throw new IllegalArgumentException("Я запрещаю вам пересекать элементы");

        child.framebuffer = new FramebufferSlice(framebuffer, child.x, child.y, child.width, child.height);
        child.update();

        children.put(id, child);
    }

    public TuiElement getChild(String id) {
        return children.get(id);
    }

    public void removeChild(String id) {
        FramebufferSlice framebuffer = children.get(id).framebuffer;
        for (int i = 0; i < framebuffer.w; i++) {
            for (int j = 0; j < framebuffer.h; j++) {
                framebuffer.set(i, j, ' ');
            }
        }

        children.remove(id);
        if(selected.equals(id))
            selected = "";
    }

    protected void updateChildren() {
        for (Map.Entry<String, TuiElement> element : children.entrySet()) {
            element.getValue().update();
        }
    }

    @Override
    public void update() {
        updateChildren();
    }

    public void deselect() {
        if(children.get(selected) instanceof ISelectable)
            ((ISelectable) children.get(selected)).onDeselect();
        else
            ((IContainer) children.get(selected)).deselect();

        selected = "";
    }

    public List<String> getSelectable() {
        List<String> selectableChildren = new ArrayList<>();
        for (Map.Entry<String, TuiElement> element : children.entrySet()) {
            if(element.getValue() instanceof ISelectable || (element.getValue() instanceof IContainer && ((IContainer) element.getValue()).isSelectable()))
                selectableChildren.add(element.getKey());
        }
        return selectableChildren;
    }

    public boolean isSelectable() {
        List<String> selectableChildren = getSelectable();
        return !selectableChildren.isEmpty();
    }

    public boolean moveSelection(RawKeys key) {
        List<String> selectableChildren = getSelectable();
        if(selectableChildren.isEmpty())
            return false;

        String mostLeft = selectableChildren.get(0);
        String mostUp = selectableChildren.get(0);
        String mostRight = selectableChildren.get(0);
        String mostDown = selectableChildren.get(0);

        for (String element : selectableChildren) {
            if(children.get(element).x < children.get(mostLeft).x)
                mostLeft = element;

            if(children.get(element).y < children.get(mostUp).y)
                mostUp = element;

            if(children.get(element).x > children.get(mostRight).x)
                mostRight = element;

            if(children.get(element).y > children.get(mostDown).y)
                mostDown = element;
        }

        if(selected.isEmpty()) {
            String bestChoice = "";
            switch(key) {
                case KEY_LEFT:  bestChoice = mostRight; break;
                case KEY_UP:    bestChoice = mostDown;  break;
                case KEY_RIGHT: bestChoice = mostLeft;  break;
                case KEY_DOWN:  bestChoice = mostUp;    break;
            }

            if(children.get(bestChoice) instanceof ISelectable)
                ((ISelectable) children.get(bestChoice)).onSelect();
            else {
                ((IContainer) children.get(bestChoice)).moveSelection(key);
            }
            selected = bestChoice;

            return true;
        }
        else {
            TuiElement selectedElement = children.get(selected);
            if(selectedElement instanceof IContainer && ((IContainer) selectedElement).moveSelection(key))
                return true;

            String bestChoice = "";
            switch(key) {
                case KEY_LEFT:
                    bestChoice = mostLeft;
                    if(children.get(bestChoice).x == children.get(selected).x)
                        bestChoice = "";
                    break;
                case KEY_UP:
                    bestChoice = mostUp;
                    if(children.get(bestChoice).y == children.get(selected).y)
                        bestChoice = "";
                    break;
                case KEY_RIGHT:
                    bestChoice = mostRight;
                    if(children.get(bestChoice).x == children.get(selected).x)
                        bestChoice = "";
                    break;
                case KEY_DOWN:
                    bestChoice = mostDown;
                    if(children.get(bestChoice).y == children.get(selected).y)
                        bestChoice = "";
                    break;
            }

            if(bestChoice.isEmpty())
                return false;

            for (String element : selectableChildren) {
                switch(key) {
                    case KEY_LEFT:
                        if(children.get(element).x >= children.get(bestChoice).x && children.get(element).x < children.get(selected).x)
                            bestChoice = element;
                        break;
                    case KEY_UP:
                        if(children.get(element).y >= children.get(bestChoice).y && children.get(element).y < children.get(selected).y)
                            bestChoice = element;
                        break;
                    case KEY_RIGHT:
                        if(children.get(element).x <= children.get(bestChoice).x && children.get(element).x > children.get(selected).x)
                            bestChoice = element;
                        break;
                    case KEY_DOWN:
                        if(children.get(element).y <= children.get(bestChoice).y && children.get(element).y > children.get(selected).y)
                            bestChoice = element;
                        break;
                }
            }

            for (String element : selectableChildren) {
                switch(key) {
                    case KEY_LEFT:
                    case KEY_RIGHT:
                        if(children.get(element).x == children.get(bestChoice).x && Math.abs(children.get(selected).y - children.get(element).y) < Math.abs(children.get(selected).y - children.get(bestChoice).y))
                            bestChoice = element;
                        break;
                    case KEY_UP:
                    case KEY_DOWN:
                        if(children.get(element).y == children.get(bestChoice).y && Math.abs(children.get(selected).x - children.get(element).x) < Math.abs(children.get(selected).x - children.get(bestChoice).x))
                            bestChoice = element;
                        break;
                }
            }

            if(children.get(selected) instanceof ISelectable)
                ((ISelectable) children.get(selected)).onDeselect();
            else {
                ((IContainer) children.get(selected)).deselect();
            }

            if(children.get(bestChoice) instanceof ISelectable)
                ((ISelectable) children.get(bestChoice)).onSelect();
            else {
                ((IContainer) children.get(bestChoice)).moveSelection(key);
            }

            selected = bestChoice;
            return true;
        }
    }

    public void click() {
        TuiElement selectedElement = children.get(selected);
        if(selectedElement instanceof IClickable)
            ((IClickable) selectedElement).onClick();
        else if(selectedElement instanceof IContainer)
            ((IContainer) selectedElement).click();
    }
}
