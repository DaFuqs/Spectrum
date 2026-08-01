package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.pastel_network.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public class SpectrumPastelUpgradeSignatures {
	
	private static final DeferredRegister<PastelUpgradeSignature> REGISTRAR = DeferredRegister.create(SpectrumRegistryKeys.PASTEL_UPGRADE, SpectrumCommon.MOD_ID);
	
	public static final PastelUpgradeSignature.Category NON_COMPOUNDING = PastelUpgradeSignature.Category.nonCompounding();
	public static final PastelUpgradeSignature.Category STACK = PastelUpgradeSignature.Category.simple();
	public static final PastelUpgradeSignature.Category SPEED = PastelUpgradeSignature.Category.simple();
	public static final PastelUpgradeSignature.Category FILTER = PastelUpgradeSignature.Category.simple();
	public static final PastelUpgradeSignature.Category REDSTONE = PastelUpgradeSignature.Category.redstone();
	
	public static void register(IEventBus eventBus) {
		register("weak_stack", () -> PastelUpgradeSignature.builder(SpectrumItems.RAW_BLOODSTONE.get(), STACK, SpectrumCommon.MOD_ID)
						.named("weak_stack").stackMod(3).stackMult(2).build());
		register("strong_stack", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_BLOODSTONE.get(), STACK, SpectrumCommon.MOD_ID)
						.named("strong_stack").stackMod(15).stackMult(4).build());
		register("weak_speed", () -> PastelUpgradeSignature.builder(SpectrumItems.RAW_MALACHITE.get(), SPEED, SpectrumCommon.MOD_ID)
						.named("weak_speed").speedMod(-5).speedMult(0.8F).build());
		register("strong_speed", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_MALACHITE.get(), SPEED, SpectrumCommon.MOD_ID)
						.named("strong_speed").speedMod(-10).speedMult(0.5F).build());
		register("weak_filter", () -> PastelUpgradeSignature.builder(SpectrumItems.RAW_AZURITE.get(), FILTER, SpectrumCommon.MOD_ID)
						.named("weak_filter").slotRowMod(1).build());
		register("strong_filter", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_AZURITE.get(), FILTER, SpectrumCommon.MOD_ID)
						.named("strong_filter").slotRowMod(2).build());
		register("rate", () -> PastelUpgradeSignature.builder(SpectrumItems.RESONANCE_SHARD.get(), NON_COMPOUNDING, SpectrumCommon.MOD_ID)
						.named("rate").transferRateMultiplier(0.5F).build());
		register("light", () -> PastelUpgradeSignature.builder(SpectrumItems.SHIMMERSTONE_GEM.get(), NON_COMPOUNDING, SpectrumCommon.MOD_ID)
						.named("light").light().build());
		register("always_active", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_REDSTONE.get(), REDSTONE, SpectrumCommon.MOD_ID)
						.redstone("always_active").redstonePreProcess(context -> InteractionResult.SUCCESS).buildRedstone());
		register("always_inactive", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_LAPIS.get(), REDSTONE, SpectrumCommon.MOD_ID)
						.redstone("always_inactive").redstonePreProcess(context -> InteractionResult.FAIL).buildRedstone());
		register("inverted", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_COAL.get(), REDSTONE, SpectrumCommon.MOD_ID)
						.redstone("inverted").redstonePostProcess(context -> context.active() ? InteractionResult.FAIL : InteractionResult.SUCCESS).buildRedstone());
		register("sensor", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_ECHO.get(), REDSTONE, SpectrumCommon.MOD_ID)
						.redstone("sensor").sensor().buildRedstone());
		register("trigger", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_QUARTZ.get(), REDSTONE, SpectrumCommon.MOD_ID)
						.redstone("trigger").triggerTransfer().buildRedstone());
		register("lamp", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_GLOWSTONE.get(), REDSTONE, SpectrumCommon.MOD_ID)
						.redstone("lamp").lamp().buildRedstone());
		
		REGISTRAR.register(eventBus);
	}
	
	private static void register(String name, Supplier<PastelUpgradeSignature> upgrade) {
		REGISTRAR.register(name, upgrade);
	}
	
	public static PastelUpgradeSignature of(ItemStack stack) {
		return of(stack.getItem());
	}
	
	public static PastelUpgradeSignature of(Item item) {
		return SpectrumRegistries.PASTEL_UPGRADE
				.stream()
				.filter(upgrade -> upgrade.upgradeItem == item)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Attempted to fetch an upgrade that does not exist"));
	}
	
	public static String toString(PastelUpgradeSignature upgrade) {
		return SpectrumRegistries.PASTEL_UPGRADE.getKey(upgrade).toString();
	}
}

