package dev.hoangvta.energytools.machine;

import dev.hoangvta.energytools.Items;
import dev.hoangvta.energytools.object.AutoPresser;
import dev.j3fftw.litexpansion.machine.MetalForge;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AutoMetalForge extends AutoPresser {

    public static final int ENERGY_CONSUMPTION = 512;
    public static final int CAPACITY = ENERGY_CONSUMPTION * 3;

    public AutoMetalForge() {
        super(Items.ENERGYTOOLS, Items.AUTO_METAL_FORGE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                dev.j3fftw.litexpansion.Items.DIAMOND_PLATE, dev.j3fftw.litexpansion.Items.ADVANCED_CIRCUIT, dev.j3fftw.litexpansion.Items.DIAMOND_PLATE,
                dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE, dev.j3fftw.litexpansion.Items.ADVANCED_MACHINE_BLOCK, dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE,
                SlimefunItems.CARBON_PRESS, SlimefunItems.AUTOMATED_CRAFTING_CHAMBER, SlimefunItems.CARBON_PRESS
        }, "&6Auto Metal Forge","&6Metal Forge", MetalForge.RECIPE_TYPE, Material.DIAMOND_BLOCK);
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public int getCapacity() {
        return  CAPACITY;
    }
}
