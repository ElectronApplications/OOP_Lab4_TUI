package tui;

import rawconsole.RawKeys;

public interface IContainer {
    boolean moveSelection(RawKeys key);
    boolean isSelectable();
    void deselect();
    void click();
}
