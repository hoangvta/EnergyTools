package dev.hoangvta.energytools;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import dev.hoangvta.energytools.items.TransmittingCard;
import dev.hoangvta.energytools.machine.EnergyReceiver;
import dev.hoangvta.energytools.machine.EnergyTransmitter;

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
    }
}
