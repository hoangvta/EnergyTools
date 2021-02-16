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
        ItemSetup.INSTANCE.init();

    }

    @Override
    public void onDisable() {
        instance = null;
    }

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
