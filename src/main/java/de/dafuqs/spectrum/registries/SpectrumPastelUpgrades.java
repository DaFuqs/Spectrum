package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.pastel.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

import java.util.*;

public class SpectrumPastelUpgrades {

    private static final Map<Item, PastelUpgradeSignature> UPGRADES = new HashMap<>();
    private static final String NAMESPACE = SpectrumCommon.MOD_ID;

    public static PastelUpgradeSignature WEAK_STACK;
    public static PastelUpgradeSignature STRONG_STACK;
    public static PastelUpgradeSignature WEAK_SPEED;
    public static PastelUpgradeSignature STRONG_SPEED;
    public static PastelUpgradeSignature WEAK_FILTER;
    public static PastelUpgradeSignature STRONG_FILTER;
    public static PastelUpgradeSignature RATE;
    public static PastelUpgradeSignature LIGHT;

    public static PastelUpgradeSignature ALWAYS_ON;
    public static PastelUpgradeSignature ALWAYS_OFF;
    public static PastelUpgradeSignature INVERTED;
    public static PastelUpgradeSignature SENSOR;
    public static PastelUpgradeSignature TRIGGER;
    public static PastelUpgradeSignature LAMP;


    public static final PastelUpgradeSignature.Category NON_COMPOUNDING = PastelUpgradeSignature.Category.nonCompounding();
    public static final PastelUpgradeSignature.Category STACK = PastelUpgradeSignature.Category.simple();
    public static final PastelUpgradeSignature.Category SPEED = PastelUpgradeSignature.Category.simple();
    public static final PastelUpgradeSignature.Category FILTER = PastelUpgradeSignature.Category.simple();
    public static final PastelUpgradeSignature.Category REDSTONE = PastelUpgradeSignature.Category.redstone();

    public static void register() {
        WEAK_STACK = register(PastelUpgradeSignature.builder(SpectrumItems.RAW_BLOODSTONE, STACK, NAMESPACE).named("weak_stack").stackMod(3).stackMult(2).build());
        STRONG_STACK = register(PastelUpgradeSignature.builder(SpectrumItems.REFINED_BLOODSTONE, STACK, NAMESPACE).named("strong_stack").stackMod(15).stackMult(4).build());

        WEAK_SPEED = register(PastelUpgradeSignature.builder(SpectrumItems.RAW_MALACHITE, SPEED, NAMESPACE).named("weak_speed").speedMod(-5).speedMult(0.8F).build());
        STRONG_SPEED = register(PastelUpgradeSignature.builder(SpectrumItems.REFINED_MALACHITE, SPEED, NAMESPACE).named("strong_speed").speedMod(-10).stackMult(0.5F).build());

        WEAK_FILTER = register(PastelUpgradeSignature.builder(SpectrumItems.RAW_AZURITE, FILTER, NAMESPACE).named("weak_filter").slotRowMod(1).build());
        STRONG_FILTER = register(PastelUpgradeSignature.builder(SpectrumItems.REFINED_AZURITE, FILTER, NAMESPACE).named("strong_filter").slotRowMod(2).build());

        RATE = register(PastelUpgradeSignature.builder(SpectrumItems.RESONANCE_SHARD, NON_COMPOUNDING, NAMESPACE).named("rate").priority(true).build());
        LIGHT = register(PastelUpgradeSignature.builder(SpectrumItems.SHIMMERSTONE_GEM, NON_COMPOUNDING, NAMESPACE).named("light").light(true).build());

        ALWAYS_ON = register(PastelUpgradeSignature.builder(SpectrumItems.PURE_REDSTONE, REDSTONE, NAMESPACE).redstone("always_active").redstonePreProcess(context -> ActionResult.SUCCESS).buildRedstone());
        ALWAYS_OFF = register(PastelUpgradeSignature.builder(SpectrumItems.PURE_LAPIS, REDSTONE, NAMESPACE).redstone("always_inactive").redstonePreProcess(context -> ActionResult.FAIL).buildRedstone());

        INVERTED = register(PastelUpgradeSignature.builder(SpectrumItems.PURE_COAL, REDSTONE, NAMESPACE).redstone("inverted").redstonePostProcess(context -> {
            if (context.active())
                return ActionResult.FAIL;
            return ActionResult.SUCCESS;
        }).buildRedstone());

        LAMP = register(PastelUpgradeSignature.builder(SpectrumItems.PURE_GLOWSTONE, REDSTONE, NAMESPACE).redstone("lamp").lamp(true).buildRedstone());
        TRIGGER = register(PastelUpgradeSignature.builder(SpectrumItems.PURE_QUARTZ, REDSTONE, NAMESPACE).redstone("trigger").triggerTransfer(true).buildRedstone());
        SENSOR = register(PastelUpgradeSignature.builder(SpectrumItems.PURE_ECHO, REDSTONE, NAMESPACE).redstone("sensor").sensor(true).buildRedstone());
    }

    private static PastelUpgradeSignature register(PastelUpgradeSignature upgrade) {
        UPGRADES.put(upgrade.upgradeItem, upgrade);
        return Registry.register(SpectrumRegistries.PASTEL_UPGRADE, SpectrumCommon.locate(upgrade.name), upgrade);
    }

    public static PastelUpgradeSignature of(Item item) {
        if (!UPGRADES.containsKey(item)) {
            throw new IllegalArgumentException("Attempted to fetch an upgrade that does not exist");
        }
        return UPGRADES.get(item);
    }

    public static String toString(PastelUpgradeSignature upgrade) {
        return SpectrumRegistries.PASTEL_UPGRADE.getId(upgrade).toString();
    }

    public static PastelUpgradeSignature of(ItemStack stack) {
        return of(stack.getItem());
    }
}

