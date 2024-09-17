package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.armor.BedrockArmorModel;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BedrockArmorItem extends ArmorItem implements Preenchanted {
    @Environment(EnvType.CLIENT)
    private BipedEntityModel<LivingEntity> model;

    public BedrockArmorItem(ArmorMaterial material, ArmorItem.Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public Map<Enchantment, Integer> getDefaultEnchantments() {
        return Map.of();
    }

    @Override
    public ItemStack getDefaultStack() {
        return getDefaultEnchantedStack(this);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    protected BipedEntityModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
        var models = MinecraftClient.getInstance().getEntityModelLoader();
        var root = models.getModelPart(SpectrumModelLayers.MAIN_BEDROCK_LAYER);
        return new BedrockArmorModel(root, slot);
    }

    @Environment(EnvType.CLIENT)
    public BipedEntityModel<LivingEntity> getArmorModel() {
        if (model == null) {
            model = provideArmorModelForSlot(getSlotType());
        }
        return model;
    }

    // this takes the "unused" stack, so addons can mixin into it
    public RenderLayer getRenderLayer(ItemStack stack) {
        return RenderLayer.getEntitySolid(SpectrumModelLayers.BEDROCK_ARMOR_MAIN_ID);
    }

    @NotNull
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        return SpectrumCommon.locate("textures/armor/bedrock_armor_main.png");
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
