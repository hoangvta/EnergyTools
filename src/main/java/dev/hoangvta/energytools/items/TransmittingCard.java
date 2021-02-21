package dev.hoangvta.energytools.items;

import com.sun.istack.internal.NotNull;
import dev.hoangvta.energytools.Items;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import dev.hoangvta.energytools.machine.EnergyTransmitter;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TransmittingCard extends SimpleSlimefunItem<ItemUseHandler> {

    public TransmittingCard() {
        super (Items.ENERGYTOOLS, Items.TRANSMITTING_CARD,
                RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                        dev.j3fftw.litexpansion.Items.IRIDIUM, SlimefunItems.GPS_TRANSMITTER, dev.j3fftw.litexpansion.Items.IRIDIUM,
                        dev.j3fftw.litexpansion.Items.GOLD_PLATE, SlimefunItems.GPS_MARKER_TOOL, dev.j3fftw.litexpansion.Items.GOLD_PLATE,
                        null, dev.j3fftw.litexpansion.Items.GOLD_PLATE, null
                }
                );
    }

    @Override
    @NotNull
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.getInteractEvent().setCancelled(true);
            ItemStack stack = e.getItem();
            ItemMeta im = stack.getItemMeta();

            List<String> lore = im.getLore();
            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            if (lore.isEmpty()) {
                return;
            }

            if (e.getClickedBlock().isPresent() && e.getSlimefunBlock().isPresent()) {

                if (!p.getInventory().getItemInMainHand().isSimilar(stack))
                    return;

                if (e.getSlimefunBlock().get() instanceof EnergyTransmitter) {
                    if (!SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK)
                            || p.hasPermission("slimefun.inventory.bypass")) {
                        p.sendMessage(ChatColors.color("&4You are not permitted to access this block"));
                        return;
                    }
                    lore.set(2, ChatColors.color("&8\u21E8 &7Linked to: &8") + b.getWorld().getName() + " X: " + b.getX() + " Y: " + b.getY() + " Z: " + b.getZ());
                    p.sendMessage(ChatColors.color("&bLink established!"));
                    im.setLore(lore);
                    stack.setItemMeta(im);
                    p.getInventory().setItemInMainHand(stack);
                } else {
                    lore.set(2, "");
                    p.sendMessage(ChatColors.color("&bLink unestablished"));
                    im.setLore(lore);
                    stack.setItemMeta(im);
                    p.getInventory().setItemInMainHand(stack);
                }

                e.cancel();
            } else {
                lore.set(2, "");
                p.sendMessage(ChatColors.color("&bLink unestablished"));
                im.setLore(lore);
                stack.setItemMeta(im);
                p.getInventory().setItemInMainHand(stack);
            }
        };
    }

}
