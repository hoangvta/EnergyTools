package dev.hoangvta.energytools;

import dev.hoangvta.energytools.machine.*;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import dev.hoangvta.energytools.items.TransmittingCard;

public class ItemSetup {
    static final ItemSetup INSTANCE = new ItemSetup();
    private final SlimefunAddon plugin = EnergyTools.getInstance();
    private boolean initialised;

    private ItemSetup() {}

    public void init() {
        if (initialised) {
            return;
        }

        initialised = true;

        registerTools();
        registerMachines();
    }

    private void registerTools() {
        new TransmittingCard().register(plugin);

    }

    private void registerMachines() {
        new EnergyTransmitter().register(plugin);
        new EnergyReceiver().register(plugin);
        new AutoRefinedSmeltery().register(plugin);
        new AutoMetalForge().register(plugin);
        new AutoManualMill().register(plugin);
    }
}
