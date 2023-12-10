package tui;

import rawconsole.RawKeys;

/**
 * Элемент-контейнер
 */
public interface IContainer {
    boolean moveSelection(RawKeys key);
    boolean isSelectable();
    void deselect();
    void click();
}
