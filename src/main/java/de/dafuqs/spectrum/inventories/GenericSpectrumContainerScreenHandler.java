package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.enums.SpectrumTier;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class GenericSpectrumContainerScreenHandler extends GenericContainerScreenHandler {

    private final SpectrumTier tier;

    private GenericSpectrumContainerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, int rows, SpectrumTier tier) {
        this(type, syncId, playerInventory, new SimpleInventory(9 * rows), rows, tier);
    }

    public GenericSpectrumContainerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows, SpectrumTier tier) {
        super(type, syncId, playerInventory, inventory, rows);
        this.tier = tier;
    }

    public static GenericSpectrumContainerScreenHandler createGeneric9x3_Tier1(int syncId, PlayerInventory playerInventory) {
        return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_9X3, syncId, playerInventory, 3, SpectrumTier.TIER1);
    }

    public static GenericSpectrumContainerScreenHandler createGeneric9x6_Tier1(int syncId, PlayerInventory playerInventory) {
        return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_9X6, syncId, playerInventory, 6, SpectrumTier.TIER1);
    }

    public static GenericSpectrumContainerScreenHandler createGeneric9x3(int syncId, PlayerInventory playerInventory, Inventory inventory, SpectrumTier tier) {
        return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_9X3, syncId, playerInventory, inventory, 3, tier);
    }

    public static GenericSpectrumContainerScreenHandler createGeneric9x6(int syncId, PlayerInventory playerInventory, Inventory inventory, SpectrumTier tier) {
        return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_9X6, syncId, playerInventory, inventory, 6, tier);
    }

    public SpectrumTier getTier() {
        return this.tier;
    }
}
