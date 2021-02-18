package dev.hoangvta.energytools.machine;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import dev.hoangvta.energytools.Items;
import dev.j3fftw.litexpansion.extrautils.interfaces.InventoryBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.core.services.localization.SlimefunLocalization;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EnergyReceiver extends SlimefunItem implements InventoryBlock, EnergyNetProvider {

    private static final int[] CARD_SLOT = {13};
    private static final int CAPACITY = 1000000;
    private static final int LINKED_SLOT = 4;

    private static final CustomItem linkedItem = new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, "&7Linked unit: null");

    public EnergyReceiver() {
        super(Items.ENERGYTOOLS, Items.ENERGY_RECEIVER,
            RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                    dev.j3fftw.litexpansion.Items.CARBON_PLATE, SlimefunItems.GPS_TRANSMITTER_4, dev.j3fftw.litexpansion.Items.CARBON_PLATE,
                    dev.j3fftw.litexpansion.Items.THORIUM_PLATE, dev.j3fftw.litexpansion.Items.MULTI_FUNCTIONAL_ELECTRIC_STORAGE_UNIT, dev.j3fftw.litexpansion.Items.THORIUM_PLATE,
                    SlimefunItems.REINFORCED_PLATE, dev.j3fftw.litexpansion.Items.IRIDIUM_PLATE, SlimefunItems.REINFORCED_PLATE
                });
        setupInv();
    }

    private void setupInv() {
        createPreset(this, "&5Energy Receiver", blockMenuPreset -> {
            for (int i = 0; i < 27; i++)
                blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());

            for (int slot : CARD_SLOT)
                blockMenuPreset.addItem(slot, null, ((player, i, itemStack, clickAction) -> true));
            blockMenuPreset.addItem(LINKED_SLOT, linkedItem);

        });
    }

    @Override
    public void preRegister() {
        this.addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                EnergyReceiver.this.tick(b);
            }
        });
    }

    private void tick(@NotNull Block b) {
        @Nullable final BlockMenu inv = BlockStorage.getInventory(b);
        if (inv == null) {
            return;
        }
        @Nullable ItemStack input = inv.getItemInSlot(CARD_SLOT[0]);

        if (!Items.TRANSMITTING_CARD.getItem().isItem(input))
            input = null;
        if (input == null) {
            inv.replaceExistingItem(LINKED_SLOT, linkedItem);
            return;
        }

        ItemMeta im = input.getItemMeta();
        List<String> lores = im.getLore();
        String loc = lores.get(2);
        if (loc.equals("")) {
            inv.replaceExistingItem(LINKED_SLOT, linkedItem);
            return;
        }

        loc = loc.replace(ChatColors.color("&8\u21E8 &7Linked to: &8"), "");
        World world = Bukkit.getWorld(loc.split(" X: ")[0]);

        if (world == null) {
            inv.replaceExistingItem(LINKED_SLOT, linkedItem);
            return;
        }
        int x = Integer.parseInt(loc.split(" X: ")[1].split(" Y: ")[0]);
        int y = Integer.parseInt(loc.split(" Y: ")[1].split(" Z: ")[0]);
        int z = Integer.parseInt(loc.split(" Z: ")[1]);

        CustomItem panel = new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "&7Linked unit: " + loc);

        inv.replaceExistingItem(LINKED_SLOT, panel);
    }

    @Override
    public int[] getInputSlots() {
        return CARD_SLOT;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public int getGeneratedOutput(@NotNull Location l, @NotNull Config config) {
        @Nullable final BlockMenu inv = BlockStorage.getInventory(l);
        if (inv == null) return 0;

        @Nullable ItemStack input = inv.getItemInSlot(CARD_SLOT[0]);

        if (!Items.TRANSMITTING_CARD.getItem().isItem(input))
            input = null;
        if (input == null) return 0;

        ItemMeta im = input.getItemMeta();
        List<String> lore = im.getLore();
        String loc = lore.get(2);

        final int stored = getCharge(l);
        final boolean canGenerate = stored < getCapacity();

        loc = loc.replace(ChatColors.color("&8\u21E8 &7Linked to: &8"), "");
        World world = Bukkit.getWorld(loc.split(" X: ")[0]);

        int x = Integer.parseInt(loc.split(" X: ")[1].split(" Y: ")[0]);
        int y = Integer.parseInt(loc.split(" Y: ")[1].split(" Z: ")[0]);
        int z = Integer.parseInt(loc.split(" Z: ")[1]);

        if (world == null) {
            return 0;
        }

        Block send = world.getBlockAt(x, y, z);

        final int rate = canGenerate ? getGeneratingAmount(send.getLocation(), l) : 0;

        return rate;
    }

    private int getGeneratingAmount(Location send, Location receive) {

        int senderCanSendAmount = getCharge(send);
        final boolean canCharge = senderCanSendAmount > 0;

        int receiverHasAmount = getCharge(receive);
        int receiverCanChargingAmount =  senderCanSendAmount;
        if (receiverCanChargingAmount > CAPACITY) receiverCanChargingAmount = CAPACITY - receiverHasAmount;
        if (canCharge) removeCharge(send, receiverCanChargingAmount);

        return receiverCanChargingAmount;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public boolean willExplode(@NotNull Location l, @NotNull Config data) {
        return false;
    }
}
