package dev.hoangvta.energytools;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Event implements Listener {
    @EventHandler
    public void onReceiverBreakEvent(BlockBreakEvent e) {
        Block b = e.getBlock();
        Location locate = b.getLocation();

        if (!BlockStorage.check(b, "ENERGY_RECEIVER")) {
            return;
        }
        ItemStack item = BlockStorage.getInventory(locate).getItemInSlot(13);
        if (item == null) {
            return;
        }
        locate.getWorld().dropItem(locate, item);
    }
}
