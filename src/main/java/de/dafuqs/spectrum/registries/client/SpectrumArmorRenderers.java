package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.armor.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumArmorRenderers {
	
	public static final DeferredItem<?>[] BEDROCK_ARMOR = {
			SpectrumItems.BEDROCK_HELMET,
			SpectrumItems.BEDROCK_CHESTPLATE,
			SpectrumItems.BEDROCK_LEGGINGS,
			SpectrumItems.BEDROCK_BOOTS
	};
	
	public static void register(RegisterClientExtensionsEvent event) {
		// TODO: maybe the Circlet of arrogance can be moved here and use IClientItemExtensions.setupModelAnimations()?
		event.registerItem(
				new IClientItemExtensions() {
					private final Map<EquipmentSlot, HumanoidModel<LivingEntity>> MODELS = new Object2ObjectArrayMap<>();
					
					@Override
					public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull EquipmentSlot equipmentSlot, @NotNull HumanoidModel<?> original) {
						return MODELS.computeIfAbsent(equipmentSlot, this::provideArmorModelForSlot);
					}
					
					private HumanoidModel<LivingEntity> provideArmorModelForSlot(EquipmentSlot slot) {
						EntityModelSet models = Minecraft.getInstance().getEntityModels();
						ModelPart root = models.bakeLayer(SpectrumModelLayers.BEDROCK_LAYER);
						return new BedrockArmorModel(root, slot);
					}
				}, BEDROCK_ARMOR);
	}
	
}
