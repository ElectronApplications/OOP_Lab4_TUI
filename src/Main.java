import tui.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    public static void main(String[] args) {
        TuiWindow window = new TuiWindow("Самый сложный выбор в вашей жизни", 50, 20);

        AtomicInteger theHardestThing = new AtomicInteger(-1);
        Button clickerButton = new Button(10, 0, 30, 5, "Кликер");
        clickerButton.clickAction = (TuiElement self) -> {
            window.closed = true;
            theHardestThing.set(0);
        };
        window.container.addChild("clicker-button", clickerButton);

        Button gridButton = new Button(10, 5, 30, 5, "Сетка");
        gridButton.clickAction = (TuiElement self) -> {
            window.closed = true;
            theHardestThing.set(1);
        };
        window.container.addChild("grid-button", gridButton);

        Button joyButton = new Button(10, 10, 30, 5, "Джойстик");
        joyButton.clickAction = (TuiElement self) -> {
            window.closed = true;
            theHardestThing.set(2);
        };
        window.container.addChild("joy-button", joyButton);

        window.eventLoop();

        switch (theHardestThing.get()) {
            case 0:
                clicker();
                break;
            case 1:
                grid();
                break;
            case 2:
                joy();
                break;
        }
    }

    public static void clicker() {
        AtomicLong totalScore = new AtomicLong(0);
        AtomicLong multiplier = new AtomicLong(1);

        TuiWindow window = new TuiWindow("Super Clicker", 60, 20);

        TuiContainer infoContainer = new TuiContainer(0, 0, 10, 20);
        window.container.addChild("info-container", infoContainer);

        infoContainer.addChild("score-text", new Label(0, 0, 5, 1, "Счёт:", false));
        infoContainer.addChild("score", new Label(5, 0, 5, 1, "0", true));
        infoContainer.addChild("multiplier-text", new Label(0, 2, 5, 1, "Множ:", false));
        infoContainer.addChild("multiplier", new Label(5, 2, 5, 1, "1", true));

        TuiContainer buttonsContainer = new TuiContainer(15, 0, 20, 20);
        window.container.addChild("buttons-container", buttonsContainer);

        Button clickButton = new Button(0, 0, 20, 3, "Клик");
        clickButton.clickAction = (TuiElement self) -> {
            totalScore.set(totalScore.get() + multiplier.get());
            ((Label) ((TuiContainer) window.container.getChild("info-container")).getChild("score")).setText("" + totalScore.get());
        };
        buttonsContainer.addChild("click-button", clickButton);

        Button multButton = new Button(0, 4, 20, 3, "Множить");
        multButton.clickAction = (TuiElement self) -> {
            if(totalScore.get() >= Math.pow(2, multiplier.get())) {
                totalScore.set(totalScore.get()  - (long) Math.pow(2, multiplier.get()));
                multiplier.set(multiplier.get() + 1);
            }

            ((Label) ((TuiContainer) window.container.getChild("info-container")).getChild("multiplier")).setText("" + multiplier.get());
            ((Label) ((TuiContainer) window.container.getChild("info-container")).getChild("score")).setText("" + totalScore.get());
        };
        buttonsContainer.addChild("mult-button", multButton);

        window.eventLoop();
    }

    public static void grid() {
        TuiWindow window = new TuiWindow("Grid", 50, 30);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = new Button(i*5, j*3, 5, 3, "");
                button.clickAction = (TuiElement self) -> {
                    ((Button) self).setText("X");
                };
                window.container.addChild("button-" + i + "-" + j, button);
            }
        }

        window.eventLoop();
    }

    public static void joy() {
        TuiWindow window = new TuiWindow("Джойстик", 50, 50);

        Button left = new Button(16, 44, 6, 3, "");
        Button up = new Button(22, 41, 7, 3, "");
        Button right = new Button(29, 44, 6, 3, "");
        Button down = new Button(22, 47, 7, 3, "");
        Label movable = new Label(20, 10, 10, 3, "Lorem ipsum dolor sit amet", true);

        left.clickAction = (TuiElement self) -> {
            Label oldMovable = (Label) window.container.getChild("movable");
            Label newMovable = new Label(oldMovable.x - 1, oldMovable.y, oldMovable.width, oldMovable.height, "Lorem ipsum dolor sit amet", true);
            try {
                window.container.removeChild("movable");
                window.container.addChild("movable", newMovable);
            } catch(IllegalArgumentException e) {
                window.container.addChild("movable", oldMovable);
            }
        };

        up.clickAction = (TuiElement self) -> {
            Label oldMovable = (Label) window.container.getChild("movable");
            Label newMovable = new Label(oldMovable.x, oldMovable.y - 1, oldMovable.width, oldMovable.height, "Lorem ipsum dolor sit amet", true);
            try {
                window.container.removeChild("movable");
                window.container.addChild("movable", newMovable);
            } catch(IllegalArgumentException e) {
                window.container.addChild("movable", oldMovable);
            }
        };

        right.clickAction = (TuiElement self) -> {
            Label oldMovable = (Label) window.container.getChild("movable");
            Label newMovable = new Label(oldMovable.x + 1, oldMovable.y, oldMovable.width, oldMovable.height, "Lorem ipsum dolor sit amet", true);
            try {
                window.container.removeChild("movable");
                window.container.addChild("movable", newMovable);
            } catch(IllegalArgumentException e) {
                window.container.addChild("movable", oldMovable);
            }
        };

        down.clickAction = (TuiElement self) -> {
            Label oldMovable = (Label) window.container.getChild("movable");
            Label newMovable = new Label(oldMovable.x, oldMovable.y + 1, oldMovable.width, oldMovable.height, "Lorem ipsum dolor sit amet", true);
            try {
                window.container.removeChild("movable");
                window.container.addChild("movable", newMovable);
            } catch(IllegalArgumentException e) {
                window.container.addChild("movable", oldMovable);
            }
        };

        window.container.addChild("left", left);
        window.container.addChild("up", up);
        window.container.addChild("right", right);
        window.container.addChild("down", down);
        window.container.addChild("movable", movable);

        window.eventLoop();
    }
}