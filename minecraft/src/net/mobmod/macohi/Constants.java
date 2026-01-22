package net.mobmod.macohi;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiOptions;

/**
 * Holds in variables that don't change.
 */
public class Constants {

    public static String DISPLAY_TITLE = "MOBMOD: Macohi Being Extra";

    public static String SAVE_APPDATAPATH = "crew3/mmmbe";

    public static List MENU_TITLE_BUTTONS()
    {
        List btns = new ArrayList<MenuTitleButton>();

        btns.add(new MenuTitleButton().setText("1"));
        btns.add(new MenuTitleButton().setText("2").setNextScene(new GuiOptions()));

        return btns;
    }

    public static List MENU_TITLE_BUTTON_FUNCTIONS()
    {
        List btns = new ArrayList();

        return btns;
    }
}