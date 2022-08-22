package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.util.entry.MenuEntry;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.menu.FilterMenu;
import xyz.brassgoggledcoders.hyperhoppers.menu.HypperMenu;
import xyz.brassgoggledcoders.hyperhoppers.screen.FilterScreen;
import xyz.brassgoggledcoders.hyperhoppers.screen.HypperScreen;

public class HyppersMenus {
    public static final MenuEntry<FilterMenu> FILTER_MENU = HyperHoppers.getRegistrate()
            .menu(FilterMenu::create, () -> FilterScreen::new)
            .register();

    public static final MenuEntry<HypperMenu> HYPPER_MENU = HyperHoppers.getRegistrate()
            .menu(HypperMenu::new, () -> HypperScreen::new)
            .register();

    public static void setup() {

    }
}
