package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.armor.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;

public class SpectrumArmorRenderers {
	
	public static final DeferredItem<?>[] BEDROCK_ARMOR = {
			SpectrumItems.BEDROCK_HELMET,
			SpectrumItems.BEDROCK_CHESTPLATE,
			SpectrumItems.BEDROCK_LEGGINGS,
			SpectrumItems.BEDROCK_BOOTS
	};
	
	public static void register(RegisterClientExtensionsEvent event) {
		event.registerItem(
				new IClientItemExtensions() {
					private Map<EquipmentSlot, HumanoidModel<LivingEntity>> models = new Object2ObjectArrayMap<>();
					
					@Override
					public HumanoidModel<?> getHumanoidArmorModel(
							LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot,
							HumanoidModel<?> original
					) {
						return models.computeIfAbsent(equipmentSlot, this::provideArmorModelForSlot);
					}
					
					private HumanoidModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
						var models = Minecraft.getInstance().getEntityModels();
						var root = models.bakeLayer(SpectrumModelLayers.BEDROCK_LAYER);
						return new BedrockArmorModel(root, slot);
					}
					
				}, BEDROCK_ARMOR
		);
	}
	
}
