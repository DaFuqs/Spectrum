package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.armor.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BedrockArmorItem extends ArmorItem implements Preenchanted {
	private BedrockArmorModel model;
	
	public BedrockArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties settings) {
		super(material, type, settings);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}
	
	@Override
	public @Nullable ResourceLocation getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EquipmentSlot slot, ArmorMaterial.@NotNull Layer layer, boolean innerModel) {
		return SpectrumModelLayers.BEDROCK_ARMOR_ID;
	}
	
	@NotNull
	@SuppressWarnings("unused")
	public ResourceLocation getArmorTexture(ItemStack stack, EquipmentSlot slot) {
		return SpectrumCommon.locate("textures/armor/bedrock_armor.png");
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of();
	}
	
	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}
	
}
