package dev.hoangvta.energytools;

import com.sun.istack.internal.NotNull;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnergyTools extends JavaPlugin implements SlimefunAddon {

    private static EnergyTools instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

//        final Metrics metrics = new Metrics(this, 7111);
//        setupCustomMetrics(metrics);

        ItemSetup.INSTANCE.init();

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void setupResearches() {

    }

//    private void setupCustomMetrics(@Nonnull Metrics metrics) {
//        metrics.addCustomChart(new Metrics.AdvancedPie("blocks_placed", () -> {
//            final Map<String, Integer> data = new HashMap<>();
//            try {
//                Class<?> blockStorage = Class.forName("me.mrCookieSlime.Slimefun.api.BlockStorage");
//
//                for (World world : Bukkit.getWorlds()) {
//                    final BlockStorage storage = BlockStorage.getStorage(world);
//                    if (storage == null) {
//                        continue;
//                    }
//
//                    final Field f = blockStorage.getDeclaredField("storage");
//                    f.setAccessible(true);
//                    @SuppressWarnings("unchecked") final Map<Location, Config> blocks =
//                            (Map<Location, Config>) f.get(storage);
//
//                    for (Map.Entry<Location, Config> entry : blocks.entrySet()) {
//                        final SlimefunItem item = SlimefunItem.getByID(entry.getValue().getString("id"));
//                        if (item == null || !(item.getAddon() instanceof EnergyTools)) {
//                            continue;
//                        }
//
//                        data.merge(item.getId(), 1, Integer::sum);
//                    }
//                }
//            } catch (ReflectiveOperationException e) {
//                getLogger().log(Level.WARNING, "Failed to load placed blocks", e);
//            }
//            return data;
//        }));
//    }

    @NotNull
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    public static EnergyTools getInstance() {
        return instance;
    }

    public String getBugTrackerURL() {
        return "https://facebook.com/hoangvtadesu";
    }

    public static FileConfiguration getCfg() {
        return instance.getConfig();
    }
}
