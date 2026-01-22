package net.mobmod.macohi;

import net.minecraft.client.GuiMainTitle;

public class MenuTitleButton {

    public String text = "";

    public Gui nextScene = GuiMainTitle;

    protected MenuTitleButton() {}

    public MenuTitleButton setText(String text) {
        this.text = text;
        return this;
    }

    public MenuTitleButton setNextScene(String nextScene) {
        this.nextScene = nextScene;
        return this;
    }

}
