package dev.hoangvta.energytools;

import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public final class Items {
    public static final Category ENERGYTOOLS = new Category(
            new NamespacedKey(EnergyTools.getInstance(), "energytools"),
            new CustomItem(new ItemStack(Material.SMOKER, 1), "&7Energy Tools")
    );

    public static final SlimefunItemStack ENERGY_TRANSMITTER = new SlimefunItemStack(
            "ENERGY_TRANSMITTER",
            Material.PURPLE_STAINED_GLASS, Color.PURPLE,
            "&5Energy Transmitter",
            "",
            "&5Transmitting energy all over the world.",
            "",
            "&c&o&8\u21E8 &e\u26A1 &74,000,000 J Buffer"
    );

    public static final SlimefunItemStack ENERGY_RECEIVER = new SlimefunItemStack(
            "ENERGY_RECEIVER",
            Material.PURPLE_CONCRETE, Color.PURPLE,
            "&5Energy Receiver",
            "",
            "&5Receiving energy from a transmitter.",
            "",
            "&c&o&8\u21E8 &e\u26A1 &71,000,000 J Buffer"
    );

    public static final SlimefunItemStack TRANSMITTING_CARD = new SlimefunItemStack(
            "TRANSMITTING_CARD",
            Material.CLOCK,
            "&6Transmitting card",
            "",
            "&5Linking two energy network together",
            ""
    );


    private Items() {}
}
