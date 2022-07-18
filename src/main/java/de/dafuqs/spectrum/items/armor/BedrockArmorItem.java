package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.Preenchanted;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BedrockArmorItem extends ArmorItem implements Preenchanted {
	@Environment(EnvType.CLIENT)
	private BipedEntityModel<LivingEntity> model;

	public BedrockArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
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
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}

	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
		var models = MinecraftClient.getInstance().getEntityModelLoader();
		var root = models.getModelPart(SpectrumClient.BEDROCK_LAYER);
		return new FullArmorModel(root, slot);
	}

	@Environment(EnvType.CLIENT)
	public BipedEntityModel<LivingEntity> getArmorModel() {
		if (model == null) {
			model = provideArmorModelForSlot(slot);
		}
		return model;
	}

	@NotNull
	public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
		return SpectrumCommon.locate("textures/armor/bedrock_armor.png");
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return false;
	}
}
