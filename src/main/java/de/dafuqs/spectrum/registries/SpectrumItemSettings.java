package de.dafuqs.spectrum.registries;

import io.wispforest.owo.itemgroup.ItemGroupReference;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Rarity;

import java.util.function.BiConsumer;

/**
 * Extension of OwoItemSettings
 * Holds a DyeColor, which is required for the map in {@link de.dafuqs.spectrum.registries.color.ItemColors}
 * <br>
 * Using this and an {@link io.wispforest.owo.registration.reflect.ItemRegistryContainer}, you could remove essentially
 * all the lines of "Registry.register(...)" boilerplate.
 * <br><br>
 *
 */
public class SpectrumItemSettings extends OwoItemSettings {
    private DyeColor color = null;

    public SpectrumItemSettings color(DyeColor color) {
        this.color = color;
        return this;
    }

    public DyeColor color() {
        return color;
    }

    @Override
    public SpectrumItemSettings group(ItemGroupReference ref) {
        return (SpectrumItemSettings) super.group(ref);
    }

    @Override
    public SpectrumItemSettings group(OwoItemGroup group) {
        return (SpectrumItemSettings) super.group(group);
    }

    @Override
    public OwoItemGroup group() {
        return super.group();
    }

    @Override
    public SpectrumItemSettings tab(int tab) {
        return (SpectrumItemSettings) super.tab(tab);
    }

    @Override
    public int tab() {
        return super.tab();
    }

    @Override
    public SpectrumItemSettings stackGenerator(BiConsumer<Item, ItemGroup.Entries> generator) {
        return (SpectrumItemSettings) super.stackGenerator(generator);
    }

    @Override
    public BiConsumer<Item, ItemGroup.Entries> stackGenerator() {
        return super.stackGenerator();
    }

    @Override
    public SpectrumItemSettings equipmentSlot(EquipmentSlotProvider equipmentSlotProvider) {
        return (SpectrumItemSettings) super.equipmentSlot(equipmentSlotProvider);
    }

    @Override
    public SpectrumItemSettings customDamage(CustomDamageHandler handler) {
        return (SpectrumItemSettings) super.customDamage(handler);
    }

    @Override
    public SpectrumItemSettings food(FoodComponent foodComponent) {
        return (SpectrumItemSettings) super.food(foodComponent);
    }

    @Override
    public SpectrumItemSettings maxCount(int maxCount) {
        return (SpectrumItemSettings) super.maxCount(maxCount);
    }

    @Override
    public SpectrumItemSettings maxDamageIfAbsent(int maxDamage) {
        return (SpectrumItemSettings) super.maxDamageIfAbsent(maxDamage);
    }

    @Override
    public SpectrumItemSettings maxDamage(int maxDamage) {
        return (SpectrumItemSettings) super.maxDamage(maxDamage);
    }

    @Override
    public SpectrumItemSettings recipeRemainder(Item recipeRemainder) {
        return (SpectrumItemSettings) super.recipeRemainder(recipeRemainder);
    }

    @Override
    public SpectrumItemSettings rarity(Rarity rarity) {
        return (SpectrumItemSettings) super.rarity(rarity);
    }

    @Override
    public SpectrumItemSettings fireproof() {
        return (SpectrumItemSettings) super.fireproof();
    }
}
