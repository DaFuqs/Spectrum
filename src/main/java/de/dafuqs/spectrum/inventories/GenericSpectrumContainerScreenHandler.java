package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.enums.SpectrumTier;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GenericSpectrumContainerScreenHandler extends GenericContainerScreenHandler {

	private final SpectrumTier tier;

	private GenericSpectrumContainerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, int columns, int rows, SpectrumTier tier) {
		this(type, syncId, playerInventory, new SimpleInventory(columns * rows), rows, tier);
	}

	public GenericSpectrumContainerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows, SpectrumTier tier) {
		super(type, syncId, playerInventory, inventory, rows);
		this.tier = tier;
	}

	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3_Tier1(int syncId, PlayerInventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X3, syncId, playerInventory, 9, 3, SpectrumTier.TIER1);
	}

	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3_Tier2(int syncId, PlayerInventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X3, syncId, playerInventory, 9, 3, SpectrumTier.TIER2);
	}

	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3_Tier3(int syncId, PlayerInventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X3, syncId, playerInventory, 9, 3, SpectrumTier.TIER3);
	}

	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6_Tier1(int syncId, PlayerInventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X6, syncId, playerInventory, 9, 6, SpectrumTier.TIER1);
	}

	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6_Tier2(int syncId, PlayerInventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X6, syncId, playerInventory, 9, 6, SpectrumTier.TIER2);
	}

	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6_Tier3(int syncId, PlayerInventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X6, syncId, playerInventory, 9, 6, SpectrumTier.TIER3);
	}

	@Contract("_, _, _, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3(int syncId, PlayerInventory playerInventory, Inventory inventory, @NotNull SpectrumTier tier) {
		switch (tier) {
			case TIER1 -> { return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X3, syncId, playerInventory, inventory, 3, tier); }
			case TIER2 -> { return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X3, syncId, playerInventory, inventory, 3, tier); }
			default    -> { return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X3, syncId, playerInventory, inventory, 3, tier); }
		}
	}

	@Contract("_, _, _, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6(int syncId, PlayerInventory playerInventory, Inventory inventory, @NotNull SpectrumTier tier) {
		switch (tier) {
			case TIER1 -> { return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X6, syncId, playerInventory, inventory, 6, tier); }
			case TIER2 -> { return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X6, syncId, playerInventory, inventory, 6, tier); }
			default    -> { return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X6, syncId, playerInventory, inventory, 6, tier); }
		}
	}

	public SpectrumTier getTier() {
		return this.tier;
	}

}
