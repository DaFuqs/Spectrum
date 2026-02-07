package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.pastel.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public class SpectrumPastelUpgrades {
	
	private static final DeferredRegister<PastelUpgradeSignature> REGISTRAR = DeferredRegister.create(SpectrumRegistryKeys.PASTEL_UPGRADE, SpectrumCommon.MOD_ID);
	
	private static final String NAMESPACE = SpectrumCommon.MOD_ID;
	
	public static final PastelUpgradeSignature.Category NON_COMPOUNDING = PastelUpgradeSignature.Category.nonCompounding();
	public static final PastelUpgradeSignature.Category STACK = PastelUpgradeSignature.Category.simple();
	public static final PastelUpgradeSignature.Category SPEED = PastelUpgradeSignature.Category.simple();
	public static final PastelUpgradeSignature.Category FILTER = PastelUpgradeSignature.Category.simple();
	public static final PastelUpgradeSignature.Category REDSTONE = PastelUpgradeSignature.Category.redstone();
	
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> WEAK_STACK = register(
			"weak_stack", () -> PastelUpgradeSignature.builder(SpectrumItems.RAW_BLOODSTONE.get(), STACK, NAMESPACE)
					.named("weak_stack").stackMod(3).stackMult(2).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> STRONG_STACK = register(
			"strong_stack", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_BLOODSTONE.get(), STACK, NAMESPACE)
					.named("strong_stack").stackMod(15).stackMult(4).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> WEAK_SPEED = register(
			"weak_speed", () -> PastelUpgradeSignature.builder(SpectrumItems.RAW_MALACHITE.get(), SPEED, NAMESPACE)
					.named("weak_speed").speedMod(-5).speedMult(0.8F).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> STRONG_SPEED = register(
			"strong_speed", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_MALACHITE.get(), SPEED, NAMESPACE)
					.named("strong_speed").speedMod(-10).speedMult(0.5F).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> WEAK_FILTER = register(
			"weak_filter", () -> PastelUpgradeSignature.builder(SpectrumItems.RAW_AZURITE.get(), FILTER, NAMESPACE)
					.named("weak_filter").slotRowMod(1).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> STRONG_FILTER = register(
			"strong_filter", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_AZURITE.get(), FILTER, NAMESPACE)
					.named("strong_filter").slotRowMod(2).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> RATE = register(
			"rate", () -> PastelUpgradeSignature.builder(SpectrumItems.RESONANCE_SHARD.get(), NON_COMPOUNDING, NAMESPACE)
					.named("rate").priority(true).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> LIGHT = register(
			"light", () -> PastelUpgradeSignature.builder(SpectrumItems.SHIMMERSTONE_GEM.get(), NON_COMPOUNDING, NAMESPACE)
					.named("light").light(true).build()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> ALWAYS_ACTIVE = register(
			"always_active", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_REDSTONE.get(), REDSTONE, NAMESPACE)
					.redstone("always_active").redstonePreProcess(context -> InteractionResult.SUCCESS).buildRedstone()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> ALWAYS_INACTIVE = register(
			"always_inactive", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_LAPIS.get(), REDSTONE, NAMESPACE)
					.redstone("always_inactive").redstonePreProcess(context -> InteractionResult.FAIL).buildRedstone()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> INVERTED = register(
			"inverted", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_COAL.get(), REDSTONE, NAMESPACE)
					.redstone("inverted").redstonePostProcess(context -> context.active() ? InteractionResult.FAIL : InteractionResult.SUCCESS).buildRedstone()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> SENSOR = register(
			"sensor", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_ECHO.get(), REDSTONE, NAMESPACE)
					.redstone("sensor").sensor(true).buildRedstone()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> TRIGGER = register(
			"trigger", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_QUARTZ.get(), REDSTONE, NAMESPACE)
					.redstone("trigger").triggerTransfer(true).buildRedstone()
	);
	public static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> LAMP = register(
			"lamp", () -> PastelUpgradeSignature.builder(SpectrumItems.PURE_GLOWSTONE.get(), REDSTONE, NAMESPACE)
					.redstone("lamp").lamp(true).buildRedstone()
	);
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(eventBus);
	}
	
	private static DeferredHolder<PastelUpgradeSignature, PastelUpgradeSignature> register(String name, Supplier<PastelUpgradeSignature> upgrade) {
		return REGISTRAR.register(name, upgrade);
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

