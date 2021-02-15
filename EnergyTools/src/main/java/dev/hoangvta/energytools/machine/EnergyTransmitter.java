package dev.hoangvta.energytools.machine;

import dev.hoangvta.energytools.Items;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.inventory.ItemStack;

public class EnergyTransmitter extends Capacitor {

    public EnergyTransmitter() {
        super (Items.ENERGYTOOLS, 4_000_000, Items.ENERGY_TRANSMITTER,
                //not complete recipe
                RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                        null, null, null,
                        null, Items.ENERGY_TRANSMITTER, null,
                        null, null, null
                }
                );
    }
}
