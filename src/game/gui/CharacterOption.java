package game.gui;

import javax.swing.*;

public class CharacterOption {
    private final String name;
    private final ImageIcon icon;

    public CharacterOption(String name, ImageIcon icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
