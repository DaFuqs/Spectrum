package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.armor.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.*;

import java.util.*;

public class BedrockArmorItem extends ArmorItem implements Preenchanted {
    @Environment(EnvType.CLIENT)
	private @Nullable BedrockArmorModel model;
	
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

    @Environment(EnvType.CLIENT)
	protected BedrockArmorModel provideArmorModelForSlot(EquipmentSlot slot) {
		var models = Minecraft.getInstance().getEntityModels();
		var root = models.bakeLayer(SpectrumModelLayers.BEDROCK_LAYER);
		return new BedrockArmorModel(root, slot);
    }

    @Environment(EnvType.CLIENT)
	public BedrockArmorModel getArmorModel() {
        if (model == null) model = provideArmorModelForSlot(getEquipmentSlot());
        
        return model;
    }
	
	// this passes the "unused" stack, so addons can mixin into it
    @SuppressWarnings("unused")
	public RenderType getRenderLayer(ItemStack stack) {
		return RenderType.entitySolid(SpectrumModelLayers.BEDROCK_ARMOR_ID);
    }

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
}
