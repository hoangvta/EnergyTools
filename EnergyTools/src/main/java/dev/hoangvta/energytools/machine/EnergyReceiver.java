package dev.hoangvta.energytools.machine;

import dev.hoangvta.energytools.Items;
import dev.j3fftw.litexpansion.extrautils.interfaces.InventoryBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
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
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.List;

public class EnergyReceiver extends SlimefunItem implements InventoryBlock, EnergyNetProvider {

    private static final int[] CARD_SLOT = {13};
    private static final int CAPACITY = 1000000;
    private static final int LINKED_SLOT = 4;

    private static final CustomItem linkedItem = new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, "&7Linked unit: null");

    public EnergyReceiver() {
        super(Items.ENERGYTOOLS, Items.ENERGY_RECEIVER,
            RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                    null, null, null,
                    null, Items.ENERGY_RECEIVER, null,
                    null, null, null
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

    private void tick(@Nonnull Block b) {
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
        String linkedLocation = "&4Unknown!";
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

        Block block = world.getBlockAt(x, y, z);

        inv.replaceExistingItem(LINKED_SLOT, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "&7Linked unit: " + loc));
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
    public int getGeneratedOutput(@Nonnull Location l, @Nonnull Config config) {
        @Nullable final BlockMenu inv = BlockStorage.getInventory(l);
        if (inv == null) return 0;

        @Nullable ItemStack input = inv.getItemInSlot(CARD_SLOT[0]);

        if (!Items.TRANSMITTING_CARD.getItem().isItem(input))
            input = null;
        if (input == null) return 0;

        ItemMeta im = input.getItemMeta();
        List<String> lores = im.getLore();
        String loc = lores.get(2);

        final int stored = getCharge(l);
        final boolean canGenerate = stored < getCapacity();

        loc = loc.replace(ChatColors.color("&8\u21E8 &7Linked to: &8"), "");
        World world = Bukkit.getWorld(loc.split(" X: ")[0]);

        int x = Integer.parseInt(loc.split(" X: ")[1].split(" Y: ")[0]);
        int y = Integer.parseInt(loc.split(" Y: ")[1].split(" Z: ")[0]);
        int z = Integer.parseInt(loc.split(" Z: ")[1]);

        Block send = world.getBlockAt(x, y, z);
        final int rate = canGenerate ? getGeneratingAmount(send.getLocation(), l) : 0;
        return rate;
    }

    private int getGeneratingAmount(Location send, Location receive) {
        int chargeableAmount = getCharge(send);
        int currentAmount = getCharge(receive);
        int goingToCharge = chargeableAmount - currentAmount;
        if (currentAmount > chargeableAmount) goingToCharge = chargeableAmount;
        if (goingToCharge > CAPACITY) goingToCharge = CAPACITY - currentAmount;
        removeCharge(send, goingToCharge);

        return goingToCharge;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }


}
