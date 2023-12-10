package tui;

/**
 * Элемент, который можно выделить
 */
public interface ISelectable {
    void onSelect();
    void onDeselect();
}
