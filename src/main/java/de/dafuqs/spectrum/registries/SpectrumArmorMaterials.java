package de.dafuqs.spectrum.registries;

import com.google.common.base.*;
import de.dafuqs.spectrum.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;
import java.util.function.*;
import java.util.function.Supplier;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumArmorMaterials {
	
	public static Holder<ArmorMaterial> GEMSTONE;
	public static Holder<ArmorMaterial> BEDROCK;

	public static void register() {
		GEMSTONE = register("gemstone",
				Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
					map.put(ArmorItem.Type.BOOTS, SpectrumCommon.CONFIG.GemstoneArmorBootsProtection);
					map.put(ArmorItem.Type.LEGGINGS, SpectrumCommon.CONFIG.GemstoneArmorLeggingsProtection);
					map.put(ArmorItem.Type.CHESTPLATE, SpectrumCommon.CONFIG.GemstoneArmorChestplateProtection);
					map.put(ArmorItem.Type.HELMET, SpectrumCommon.CONFIG.GemstoneArmorHelmetProtection);
				}),
				15,
				BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.AMETHYST_BLOCK_CHIME),
				SpectrumCommon.CONFIG.GemstoneArmorToughness,
				SpectrumCommon.CONFIG.GemstoneArmorKnockbackResistance,
				() -> Ingredient.of(SpectrumItemTags.GEMSTONE_SHARDS));

		BEDROCK = register("bedrock",
				Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
					map.put(ArmorItem.Type.BOOTS, SpectrumCommon.CONFIG.BedrockArmorBootsProtection);
					map.put(ArmorItem.Type.LEGGINGS, SpectrumCommon.CONFIG.BedrockArmorLeggingsProtection);
					map.put(ArmorItem.Type.CHESTPLATE, SpectrumCommon.CONFIG.BedrockArmorChestplateProtection);
					map.put(ArmorItem.Type.HELMET, SpectrumCommon.CONFIG.BedrockArmorHelmetProtection);
				}),
				5,
				SoundEvents.ARMOR_EQUIP_NETHERITE,
				SpectrumCommon.CONFIG.BedrockArmorToughness,
				SpectrumCommon.CONFIG.BedrockArmorKnockbackResistance,
				() -> Ingredient.of(SpectrumItems.BEDROCK_DUST));
	}
	
	public static Holder<ArmorMaterial> register(
			String id,
			EnumMap<ArmorItem.Type, Integer> defense,
			int enchantability,
			Holder<SoundEvent> equipSound,
			float toughness,
			float knockbackResistance,
			Supplier<Ingredient> repairIngredient
	) {
		List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(locate(id)));

		EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

		for (ArmorItem.Type type : ArmorItem.Type.values()) {
			enumMap.put(type, defense.get(type));
		}
		
		return Registry.registerForHolder(
				BuiltInRegistries.ARMOR_MATERIAL,
				locate(id),
				new ArmorMaterial(enumMap, enchantability, equipSound, Suppliers.memoize(repairIngredient::get), layers, toughness, knockbackResistance));
	}
	
}
