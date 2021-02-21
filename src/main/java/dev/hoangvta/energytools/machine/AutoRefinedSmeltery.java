package dev.hoangvta.energytools.machine;

import dev.hoangvta.energytools.Items;
import dev.hoangvta.energytools.object.AutoCrafter;
import dev.j3fftw.litexpansion.machine.RefinedSmeltery;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AutoRefinedSmeltery extends AutoCrafter{

    public AutoRefinedSmeltery() {
        super(Items.ENERGYTOOLS, Items.AUTO_REFINED_SMELTERY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                dev.j3fftw.litexpansion.Items.POWER_UNIT, dev.j3fftw.litexpansion.Items.ADVANCED_CIRCUIT, SlimefunItems.ENRICHED_NETHER_ICE,
                dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE, dev.j3fftw.litexpansion.Items.ADVANCED_MACHINE_BLOCK, dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE,
                SlimefunItems.ELECTRIC_SMELTERY_2, SlimefunItems.AUTOMATED_CRAFTING_CHAMBER, SlimefunItems.ELECTRIC_SMELTERY_2
        }, "&6Auto Refined Smeltery", Material.BLAST_FURNACE, "&6Auto Refined Smeltery", RefinedSmeltery.RECIPE_TYPE);
    }

}
