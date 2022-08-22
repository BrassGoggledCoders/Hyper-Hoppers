package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.util.entry.MenuEntry;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.menu.FilterMenu;
import xyz.brassgoggledcoders.hyperhoppers.screen.FilterScreen;

public class HyppersMenus {
    public static final MenuEntry<FilterMenu> FILTER_MENU = HyperHoppers.getRegistrate()
            .menu(FilterMenu::create, () -> FilterScreen::new)
            .register();

    public static void setup() {

    }
}
