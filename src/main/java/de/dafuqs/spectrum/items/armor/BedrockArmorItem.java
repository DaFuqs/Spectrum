package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.armor.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BedrockArmorItem extends ArmorItem implements Preenchanted {
    @Environment(EnvType.CLIENT)
	private HumanoidModel<LivingEntity> model;
	
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
	protected HumanoidModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
		var models = Minecraft.getInstance().getEntityModels();
		var root = models.bakeLayer(SpectrumModelLayers.MAIN_BEDROCK_LAYER);
        return new BedrockArmorModel(root, slot);
    }

    @Environment(EnvType.CLIENT)
	public HumanoidModel<LivingEntity> getArmorModel() {
        if (model == null) {
			model = provideArmorModelForSlot(getEquipmentSlot());
        }
        return model;
    }

    // this takes the "unused" stack, so addons can mixin into it
    @SuppressWarnings("unused")
	public RenderType getRenderLayer(ItemStack stack) {
		return RenderType.entitySolid(SpectrumModelLayers.BEDROCK_ARMOR_MAIN_ID);
    }

    @NotNull
    @SuppressWarnings("unused")
	public ResourceLocation getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        return SpectrumCommon.locate("textures/armor/bedrock_armor_main.png");
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
