package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.pastel.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SpectrumPastelUpgrades {

    private static final Map<Item, PastelUpgradeSignature> UPGRADES = new HashMap<>();
    private static final String NAMESPACE = SpectrumCommon.MOD_ID;

    public static final PastelUpgradeSignature.Category NON_COMPOUNDING = PastelUpgradeSignature.Category.nonCompounding();
    public static final PastelUpgradeSignature.Category STACK = PastelUpgradeSignature.Category.simple();
    public static final PastelUpgradeSignature.Category SPEED = PastelUpgradeSignature.Category.simple();
    public static final PastelUpgradeSignature.Category FILTER = PastelUpgradeSignature.Category.simple();
    public static final PastelUpgradeSignature.Category REDSTONE = PastelUpgradeSignature.Category.redstone();

    public static void register() {
        register(PastelUpgradeSignature.builder(SpectrumItems.RAW_BLOODSTONE, STACK, NAMESPACE).named("weak_stack").stackMod(3).stackMult(2).build());
		register(PastelUpgradeSignature.builder(SpectrumItems.PURE_BLOODSTONE, STACK, NAMESPACE).named("strong_stack").stackMod(15).stackMult(4).build());

        register(PastelUpgradeSignature.builder(SpectrumItems.RAW_MALACHITE, SPEED, NAMESPACE).named("weak_speed").speedMod(-5).speedMult(0.8F).build());
		register(PastelUpgradeSignature.builder(SpectrumItems.PURE_MALACHITE, SPEED, NAMESPACE).named("strong_speed").speedMod(-10).speedMult(0.5F).build());

        register(PastelUpgradeSignature.builder(SpectrumItems.RAW_AZURITE, FILTER, NAMESPACE).named("weak_filter").slotRowMod(1).build());
		register(PastelUpgradeSignature.builder(SpectrumItems.PURE_AZURITE, FILTER, NAMESPACE).named("strong_filter").slotRowMod(2).build());

        register(PastelUpgradeSignature.builder(SpectrumItems.RESONANCE_SHARD, NON_COMPOUNDING, NAMESPACE).named("rate").priority().build());
        register(PastelUpgradeSignature.builder(SpectrumItems.SHIMMERSTONE_GEM, NON_COMPOUNDING, NAMESPACE).named("light").light().build());
		
		register(PastelUpgradeSignature.builder(SpectrumItems.PURE_REDSTONE, REDSTONE, NAMESPACE).redstone("always_active").redstonePreProcess(context -> InteractionResult.SUCCESS).buildRedstone());
		register(PastelUpgradeSignature.builder(SpectrumItems.PURE_LAPIS, REDSTONE, NAMESPACE).redstone("always_inactive").redstonePreProcess(context -> InteractionResult.FAIL).buildRedstone());

        register(PastelUpgradeSignature.builder(SpectrumItems.PURE_COAL, REDSTONE, NAMESPACE).redstone("inverted").redstonePostProcess(context -> context.active() ? InteractionResult.FAIL : InteractionResult.SUCCESS).buildRedstone());

        register(PastelUpgradeSignature.builder(SpectrumItems.PURE_GLOWSTONE, REDSTONE, NAMESPACE).redstone("lamp").lamp().buildRedstone());
        register(PastelUpgradeSignature.builder(SpectrumItems.PURE_QUARTZ, REDSTONE, NAMESPACE).redstone("trigger").triggerTransfer().buildRedstone());
        register(PastelUpgradeSignature.builder(SpectrumItems.PURE_ECHO, REDSTONE, NAMESPACE).redstone("sensor").sensor().buildRedstone());
    }

    private static void register(PastelUpgradeSignature upgrade) {
        UPGRADES.put(upgrade.upgradeItem, upgrade);
        Registry.register(SpectrumRegistries.PASTEL_UPGRADE, SpectrumCommon.locate(upgrade.name), upgrade);
    }

	public static @Nullable PastelUpgradeSignature of(Item item) {
        if (!UPGRADES.containsKey(item)) {
            throw new IllegalArgumentException("Attempted to fetch an upgrade that does not exist");
        }
        return UPGRADES.get(item);
    }

    public static String toString(PastelUpgradeSignature upgrade) {
		return SpectrumRegistries.PASTEL_UPGRADE.getKey(upgrade).toString();
    }

	public static @Nullable PastelUpgradeSignature of(ItemStack stack) {
        return of(stack.getItem());
    }
}

