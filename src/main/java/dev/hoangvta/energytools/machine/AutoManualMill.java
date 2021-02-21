package dev.hoangvta.energytools.machine;

import dev.hoangvta.energytools.Items;
import dev.hoangvta.energytools.object.AutoPresser;
import dev.j3fftw.litexpansion.machine.ManualMill;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AutoManualMill extends AutoPresser {
    public AutoManualMill() {
        super(Items.ENERGYTOOLS, Items.AUTO_MANUAL_MILL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                dev.j3fftw.litexpansion.Items.IRON_PLATE, dev.j3fftw.litexpansion.Items.ADVANCED_CIRCUIT, dev.j3fftw.litexpansion.Items.IRON_PLATE,
                dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE, dev.j3fftw.litexpansion.Items.ADVANCED_MACHINE_BLOCK, dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE,
                SlimefunItems.REINFORCED_PLATE, SlimefunItems.AUTOMATED_CRAFTING_CHAMBER, SlimefunItems.REINFORCED_PLATE
        }, "&6Auto Manual Mill","&6Manual Mill", ManualMill.RECIPE_TYPE, Material.IRON_BLOCK);
    }
}
