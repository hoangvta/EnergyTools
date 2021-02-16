package dev.hoangvta.energytools.machine;

import dev.hoangvta.energytools.Items;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.inventory.ItemStack;

public class EnergyTransmitter extends Capacitor {

    public EnergyTransmitter() {
        super (Items.ENERGYTOOLS, 4_000_000, Items.ENERGY_TRANSMITTER,
                RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                        dev.j3fftw.litexpansion.Items.CARBON_PLATE, SlimefunItems.GPS_TRANSMITTER_4, dev.j3fftw.litexpansion.Items.CARBON_PLATE,
                        SlimefunItems.REINFORCED_PLATE, dev.j3fftw.litexpansion.Items.MULTI_FUNCTIONAL_ELECTRIC_STORAGE_UNIT, SlimefunItems.REINFORCED_PLATE,
                        dev.j3fftw.litexpansion.Items.THORIUM_PLATE, dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE, dev.j3fftw.litexpansion.Items.THORIUM_PLATE
                }
                );
    }
}
