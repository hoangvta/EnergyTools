package dev.hoangvta.energytools.object;

import com.sun.istack.internal.NotNull;
import dev.j3fftw.litexpansion.extrautils.interfaces.InventoryBlock;
import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoPresser extends SlimefunItem implements InventoryBlock, EnergyNetComponent {

    public static final int ENERGY_CONSUMPTION = 128;
    public static final int CAPACITY = ENERGY_CONSUMPTION * 3;
    private static final int plateSlot = 16;
    private static final int statusSlot = 23;
    private final int[] border = {0, 1, 3, 5, 13, 14, 50, 51, 52, 53};
    private final int[] inputBorder = {9, 10, 11, 12, 13, 18, 22, 27, 31, 36, 40, 45, 46, 47, 48, 49};
    private final int[] outputBorder = {32, 33, 34, 35, 41, 44, 50, 51, 52, 53};
    private final int[] plateBorder = {6, 7, 8, 15, 17, 24, 25, 26};
    private final String machineName;
    private final RecipeType machineRecipes;
    private final MultiBlockMachine mblock;
    private final Material plateMaterial;

    public AutoPresser(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String displayName, String machineName, RecipeType machineRecipes, Material plateMaterial) {
        super(category, item, recipeType, recipe);

        this.machineName = machineName;
        this.machineRecipes = machineRecipes;
        this.mblock = (MultiBlockMachine) machineRecipes.getMachine();
        this.plateMaterial = plateMaterial;

        new BlockMenuPreset(getId(), displayName) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Block b) {
                if (!BlockStorage.hasBlockInfo(b)
                        || BlockStorage.getLocationInfo(b.getLocation(), "enabled") == null
                        || BlockStorage.getLocationInfo(b.getLocation(), "enabled").equals(String.valueOf(false))) {
                    menu.replaceExistingItem(4, new CustomItem(Material.GUNPOWDER, "&7Enabled: &4\u2718",
                            "", "&e> Click to enable this Machine")
                    );
                    menu.replaceExistingItem(statusSlot,
                            new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE),
                                    "&7&lDisabled"));
                    menu.addMenuClickHandler(4, (p, slot, item, action) -> {
                        BlockStorage.addBlockInfo(b, "enabled", String.valueOf(true));
                        newInstance(menu, b);
                        return false;
                    });
                } else {
                    menu.replaceExistingItem(4, new CustomItem(Material.REDSTONE, "&7Enabled: &2\u2714",
                            "", "&e> Click to disable this Machine"));
                    menu.addMenuClickHandler(4, (p, slot, item, action) -> {
                        BlockStorage.addBlockInfo(b, "enabled", String.valueOf(false));
                        newInstance(menu, b);
                        return false;
                    });
                }
            }

            @Override
            public boolean canOpen(@NotNull Block b, @NotNull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(),
                        ProtectableAction.INTERACT_BLOCK
                );
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return getOutputSlots();
                }

                List<Integer> slots = new ArrayList<>();
                for (int slot : getInputSlots()) {
                    if (menu.getItemInSlot(slot) != null) {
                        slots.add(slot);
                    }
                }
                slots.sort(compareSlots(menu));
                slots.add(plateSlot);

                int[] array = new int[slots.size()];

                for (int i = 0; i < slots.size(); i++) {
                    array[i] = slots.get(i);
                }

                return array;
            }
        };

        addItemHandler(onPlace());
        registerBlockHandler(getId(), (p, b, stack, reason) -> {
            BlockMenu inv = BlockStorage.getInventory(b);
            Location location = b.getLocation();

            if (inv != null) {
                inv.dropItems(location, getInputSlots());
                inv.dropItems(location, getOutputSlots());
                inv.dropItems(location, plateSlot);
            }

            return true;
        });
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(true) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "enabled", String.valueOf(false));
            }

            @Override
            public void onBlockPlacerPlace(BlockPlacerPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "enabled", String.valueOf(false));
            }
        };
    }

    private Comparator<Integer> compareSlots(DirtyChestMenu menu) {
        return Comparator.comparingInt(slot -> menu.getItemInSlot(slot).getAmount());
    }

    protected void constructMenu(BlockMenuPreset preset) {
        borders(preset, border, inputBorder, outputBorder);

        for (int i : plateBorder) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "&e&lPlate Slot"),
                    (p, slot, item, action) -> false);
        }

        preset.addItem(statusSlot, new CustomItem(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "&e&lIdle"),
                (p, slot, item, action) -> false);

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor,
                                       ClickAction action) {
                    if (cursor == null) return true;
                    return cursor.getType() == Material.AIR;
                }
            });
        }

        preset.addItem(2, new CustomItem(new ItemStack(Material.CRAFTING_TABLE), "&eRecipe", "",
                        "&bPut in the Recipe you want to craft", "&ePut in the item you want crafted",
                        machineName + " Recipes ONLY"
                ),
                (p, slot, item, action) -> false);
    }

    @Override
    public int[] getInputSlots() {
        return new int[] {19, 20, 21, 28, 29, 30, 37, 38, 39};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] {42, 43};
    }

    @NotNull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                AutoPresser.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });
    }

    protected void tick(Block block) {

        if (BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals(String.valueOf(false))) {
            return;
        }

        if (getCharge(block.getLocation()) < getEnergyConsumption()) {
            return;
        }

        craftIfValid(block);
    }

    private void craftIfValid(Block block) {
        BlockMenu menu = BlockStorage.getInventory(block);

        // Make sure at least 1 slot is free
        for (int outSlot : getOutputSlots()) {
            ItemStack outItem = menu.getItemInSlot(outSlot);
            if (outItem == null || outItem.getAmount() < outItem.getMaxStackSize()) {
                break;
            } else if (outSlot == getOutputSlots()[1]) {
                return;
            }
        }

        // Find matching recipe
        for (ItemStack[] input : RecipeType.getRecipeInputList(mblock)) {
            if (isCraftable(menu, input)) {
                ItemStack output = RecipeType.getRecipeOutputList(mblock, input).clone();
                craft(output, menu);
                removeCharge(block.getLocation(), getEnergyConsumption());
                return;
            }
        }
        // we're only executing the last possible shaped recipe
        // we don't want to allow this to be pressed instead of the default timer-based
        // execution to prevent abuse and auto clickers
    }

    private boolean isCraftable(BlockMenu inv, ItemStack[] recipe) {
        ItemStack plate = inv.getItemInSlot(plateSlot);

        if (plate == null) return false;
        if (plate.getType() != plateMaterial || plate.getAmount() == 1) return false;

        for (int j = 0; j < 9; j++) {
            ItemStack item = inv.getItemInSlot(getInputSlots()[j]);
            if ((item != null && item.getAmount() == 1)
                    || !SlimefunUtils.isItemSimilar(inv.getItemInSlot(getInputSlots()[j]), recipe[j], true)) {
                return false;
            }
        }

        return true;
    }

    private void craft(ItemStack output, BlockMenu inv) {
        for (int j = 0; j < 9; j++) {
            ItemStack item = inv.getItemInSlot(getInputSlots()[j]);

            if (item != null && item.getType() != Material.AIR) {
                inv.consumeItem(getInputSlots()[j]);
            }
        }
        inv.consumeItem(plateSlot);
        inv.pushItem(output, getOutputSlots());
    }

    static void borders(BlockMenuPreset preset, int[] border, int[] inputBorder, int[] outputBorder) {
        for (int i : border) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                    (p, slot, item, action) -> false);
        }

        for (int i : inputBorder) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                    (p, slot, item, action) -> false);
        }

        for (int i : outputBorder) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
                    (p, slot, item, action) -> false);
        }
    }
}
