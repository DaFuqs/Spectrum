package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
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
		var feet = models.getModelPart(SpectrumModelLayers.FEET_BEDROCK_LAYER);
		var root = models.getModelPart(SpectrumModelLayers.MAIN_BEDROCK_LAYER);
		if (slot == EquipmentSlot.FEET)
			return new FullArmorModel(feet, slot);
		else
			return new FullArmorModel(root, slot);
	}
	
	@Environment(EnvType.CLIENT)
	public BipedEntityModel<LivingEntity> getArmorModel() {
		if (model == null) {
			model = provideArmorModelForSlot(getSlotType());
		}
		return model;
	}
	
	@NotNull
	public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
		if (slot == EquipmentSlot.FEET) {
			return SpectrumCommon.locate("textures/armor/bedrock_armor_feet.png");
		} else {
			return SpectrumCommon.locate("textures/armor/bedrock_armor_main.png");
		}
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return false;
	}
}
