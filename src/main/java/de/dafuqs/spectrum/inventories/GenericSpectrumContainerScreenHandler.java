package de.dafuqs.spectrum.inventories;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import org.jetbrains.annotations.*;

public class GenericSpectrumContainerScreenHandler extends ChestMenu {
	
	private final ScreenBackgroundVariant tier;
	
	private GenericSpectrumContainerScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, int columns, int rows, ScreenBackgroundVariant tier) {
		this(type, syncId, playerInventory, new SimpleContainer(columns * rows), rows, tier);
	}
	
	public GenericSpectrumContainerScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, Container inventory, int rows, ScreenBackgroundVariant tier) {
		super(type, syncId, playerInventory, inventory, rows);
		this.tier = tier;
	}
	
	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3_Tier1(int syncId, Inventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X3, syncId, playerInventory, 9, 3, ScreenBackgroundVariant.EARLYGAME);
	}
	
	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3_Tier2(int syncId, Inventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X3, syncId, playerInventory, 9, 3, ScreenBackgroundVariant.MIDGAME);
	}
	
	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3_Tier3(int syncId, Inventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X3, syncId, playerInventory, 9, 3, ScreenBackgroundVariant.LATEGAME);
	}
	
	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6_Tier1(int syncId, Inventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X6, syncId, playerInventory, 9, 6, ScreenBackgroundVariant.EARLYGAME);
	}
	
	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6_Tier2(int syncId, Inventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X6, syncId, playerInventory, 9, 6, ScreenBackgroundVariant.MIDGAME);
	}
	
	@Contract("_, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6_Tier3(int syncId, Inventory playerInventory) {
		return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X6, syncId, playerInventory, 9, 6, ScreenBackgroundVariant.LATEGAME);
	}
	
	@Contract("_, _, _, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x3(int syncId, Inventory playerInventory, Container inventory, @NotNull ScreenBackgroundVariant tier) {
		switch (tier) {
			case EARLYGAME -> {
				return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X3, syncId, playerInventory, inventory, 3, tier);
			}
			case MIDGAME -> {
				return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X3, syncId, playerInventory, inventory, 3, tier);
			}
			default -> {
				return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X3, syncId, playerInventory, inventory, 3, tier);
			}
		}
	}
	
	@Contract("_, _, _, _ -> new")
	public static @NotNull GenericSpectrumContainerScreenHandler createGeneric9x6(int syncId, Inventory playerInventory, Container inventory, @NotNull ScreenBackgroundVariant tier) {
		switch (tier) {
			case EARLYGAME -> {
				return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X6, syncId, playerInventory, inventory, 6, tier);
			}
			case MIDGAME -> {
				return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X6, syncId, playerInventory, inventory, 6, tier);
			}
			default -> {
				return new GenericSpectrumContainerScreenHandler(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X6, syncId, playerInventory, inventory, 6, tier);
			}
		}
	}
	
	public ScreenBackgroundVariant getTier() {
		return this.tier;
	}
	
}
