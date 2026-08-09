package de.dafuqs.spectrum.registries;

import com.google.common.base.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.config.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;
import java.util.function.Supplier;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumArmorMaterials {
	
	private static final DeferredRegister<ArmorMaterial> REGISTRAR = DeferredRegister.create(Registries.ARMOR_MATERIAL, SpectrumCommon.MOD_ID);
	
	public static Holder<ArmorMaterial> GEMSTONE = register("gemstone",
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, SpectrumConfig.CONFIG.GemstoneArmorBootsProtection.get());
				map.put(ArmorItem.Type.LEGGINGS, SpectrumConfig.CONFIG.GemstoneArmorLeggingsProtection.get());
				map.put(ArmorItem.Type.CHESTPLATE, SpectrumConfig.CONFIG.GemstoneArmorChestplateProtection.get());
				map.put(ArmorItem.Type.HELMET, SpectrumConfig.CONFIG.GemstoneArmorHelmetProtection.get());
			}),
			15,
			BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.AMETHYST_BLOCK_CHIME),
			SpectrumConfig.CONFIG.GemstoneArmorToughness.get().floatValue(),
			SpectrumConfig.CONFIG.GemstoneArmorKnockbackResistance.get().floatValue(),
			() -> Ingredient.of(SpectrumItemTags.GEMSTONE_SHARDS));
	
	public static Holder<ArmorMaterial> BEDROCK = register("bedrock",
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, SpectrumConfig.CONFIG.BedrockArmorBootsProtection.get());
				map.put(ArmorItem.Type.LEGGINGS, SpectrumConfig.CONFIG.BedrockArmorLeggingsProtection.get());
				map.put(ArmorItem.Type.CHESTPLATE, SpectrumConfig.CONFIG.BedrockArmorChestplateProtection.get());
				map.put(ArmorItem.Type.HELMET, SpectrumConfig.CONFIG.BedrockArmorHelmetProtection.get());
			}),
			5,
			SoundEvents.ARMOR_EQUIP_NETHERITE,
			SpectrumConfig.CONFIG.BedrockArmorToughness.get().floatValue(),
			SpectrumConfig.CONFIG.BedrockArmorKnockbackResistance.get().floatValue(),
			() -> Ingredient.of(SpectrumItems.BEDROCK_DUST));
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(eventBus);
	}
	
	public static Holder<ArmorMaterial> register(
			String id,
			EnumMap<ArmorItem.Type, Integer> defense,
			int enchantability,
			Holder<SoundEvent> equipSound,
			float toughness,
			float knockbackResistance,
			Supplier<Ingredient> repairIngredient) {
		
		List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(locate(id)));
		EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);
		
		for (ArmorItem.Type type : ArmorItem.Type.values()) {
			enumMap.put(type, defense.get(type));
		}
		
		return REGISTRAR.register(
				id,
				() -> new ArmorMaterial(
						enumMap, enchantability, equipSound, Suppliers.memoize(repairIngredient::get), layers, toughness,
						knockbackResistance
				)
		);
	}
	
}
