package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import javax.annotation.*;

public abstract class BedrockArmorItem extends ArmorItem implements Preenchanted {

	public static final ArmorMaterial.Layer ARMOR_MATERIAL_LAYER = new ArmorMaterial.Layer(SpectrumCommon.locate("bedrock"));
	
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
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return SpectrumModelLayerLocations.BEDROCK_ARMOR_ID;
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}
	
}
