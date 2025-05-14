package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.data.*;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;

import java.util.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

@SuppressWarnings("unused")
public class SpectrumEnchantments {
	
	private static final DeferredRegistrar.Contextual<DatagenProxy.ProvidedTagBuilderBuilder<Item>> ITEM_TAG_REGISTRAR = new DeferredRegistrar.Contextual<>(DatagenProxy.IS_DATAGEN);
	private static final DeferredRegistrar.Contextual<DatagenProxy.BootstrapContext<Enchantment>> BOOTSTRAP_REGISTAR = new DeferredRegistrar.Contextual<>(DatagenProxy.IS_DATAGEN);
	private static final DeferredRegistrar.KeyedContextual<ResourceKey<Enchantment>, DatagenProxy.ProvidedTagBuilderBuilder<Enchantment>> ENCHANTMENT_TAG_REGISTRAR = new DeferredRegistrar.KeyedContextual<>(DatagenProxy.IS_DATAGEN);
	
	// Increase the chance to reel in entities instead of fishing loot
	public static final ResourceKey<Enchantment> BIG_CATCH = new Builder(
			"big_catch",
			2,
			3,
			new Enchantment.Cost(20, 0),
			new Enchantment.Cost(50, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.FISHING_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.FISHING_ENCHANTABLE)
	).register();
	
	// Increases drop chance of <1 loot drops
	public static final ResourceKey<Enchantment> CLOVERS_FAVOR = new Builder(
			"clovers_favor",
			2,
			3,
			new Enchantment.Cost(20, 0),
			new Enchantment.Cost(50, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.SWORD_ENCHANTABLE)
			.addOptionalTag(ResourceLocation.parse("malum:scythe"))
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.LOOTING)
			.addOptional(ResourceLocation.parse("malum:spirit_plunder"))
	).register();
	
	// Drops mob equipment on hit (and players, but way less often)
	public static final ResourceKey<Enchantment> DISARMING = new Builder(
			"disarming",
			1,
			2,
			new Enchantment.Cost(10, 0),
			new Enchantment.Cost(40, 0),
			8,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.SWORD_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
	).register();
	
	// Drops more XP on kill
	public static final ResourceKey<Enchantment> EXUBERANCE = new Builder(
			"exuberance",
			5,
			5,
			new Enchantment.Cost(10, 0),
			new Enchantment.Cost(40, 0),
			2,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.SWORD_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
			.forceAddTag(ItemTags.MINING_LOOT_ENCHANTABLE)
			.forceAddTag(SpectrumItemTags.FISHING_RODS)
			.addOptionalTag(ResourceLocation.parse("malum:scythe"))
	).register();
	
	// Increased damage if the enemy has full health
	public static final ResourceKey<Enchantment> FIRST_STRIKE = new Builder(
			"first_strike",
			2,
			2,
			new Enchantment.Cost(10, 0),
			new Enchantment.Cost(40, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.SWORD_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
			.addOptionalTag(ResourceLocation.parse("malum:scythe"))
	).register();
	
	// applies smelting recipe before dropping items after mining
	public static final ResourceKey<Enchantment> FOUNDRY = new Builder(
			"foundry",
			2,
			1,
			new Enchantment.Cost(15, 0),
			new Enchantment.Cost(65, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.MINING_LOOT_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.MINING_LOOT_ENCHANTABLE)
			.forceAddTag(SpectrumItemTags.FISHING_RODS)
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.SILK_TOUCH)
			.addOptional(ResourceLocation.parse("gofish:deepfry"))
	).register();
	
	// Increased damage when landing a critical hit
	public static final ResourceKey<Enchantment> IMPROVED_CRITICAL = new Builder(
			"improved_critical",
			2,
			2,
			new Enchantment.Cost(10, 0),
			new Enchantment.Cost(40, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.SWORD_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
			.addOptionalTag(ResourceLocation.parse("malum:scythe"))
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.SHARPNESS)
			.addOptional(ResourceLocation.parse("malum:haunted"))
	).register();
	
	// Make tools not use up durability
	public static final ResourceKey<Enchantment> INDESTRUCTIBLE = new Builder(
			"indestructible",
			2,
			1,
			new Enchantment.Cost(30, 0),
			new Enchantment.Cost(60, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
					.forceAddTag(ItemTags.DURABILITY_ENCHANTABLE)
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.INFINITY)
			.add(Enchantments.UNBREAKING)
			.add(Enchantments.EFFICIENCY)
			.add(Enchantments.MENDING)
			.add(Enchantments.PROTECTION)
			.add(Enchantments.BINDING_CURSE)
	).register();
	
	// Decreases mining speed, but increases with each mined block of the same type
	public static final ResourceKey<Enchantment> INERTIA = new Builder(
			"inertia",
			1,
			3,
			new Enchantment.Cost(10, 0),
			new Enchantment.Cost(40, 0),
			8,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.MINING_LOOT_ENCHANTABLE)
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.EFFICIENCY)
	).register();
	
	// prevents mining & movement slowdowns
	public static final ResourceKey<Enchantment> INEXORABLE = new Builder(
			"inexorable",
			1,
			1,
			new Enchantment.Cost(50, 0),
			new Enchantment.Cost(100, 0),
			8,
			List.of(EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND, EquipmentSlotGroup.CHEST),
			Optional.of(ItemTags.CHEST_ARMOR_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.CHEST_ARMOR_ENCHANTABLE)
			.forceAddTag(ItemTags.MINING_LOOT_ENCHANTABLE)
			.forceAddTag(ItemTags.TRIDENT_ENCHANTABLE)
	).register();
	
	// don't drop items into the world, add to inv instead
	public static final ResourceKey<Enchantment> INVENTORY_INSERTION = new Builder(
			"inventory_insertion",
			2,
			1,
			new Enchantment.Cost(15, 0),
			new Enchantment.Cost(45, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.MINING_LOOT_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.MINING_ENCHANTABLE)
			.forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
			.forceAddTag(ItemTags.BOW_ENCHANTABLE)
			.forceAddTag(ItemTags.CROSSBOW_ENCHANTABLE)
			.forceAddTag(SpectrumItemTags.FISHING_RODS)
			.addOptionalTag(ResourceLocation.parse("malum:scythe"))
	).register();
	
	// Kills silverfish when mining infested blocks
	public static final ResourceKey<Enchantment> PEST_CONTROL = new Builder(
			"pest_control",
			1,
			1,
			new Enchantment.Cost(10, 0),
			new Enchantment.Cost(30, 0),
			8,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.MINING_LOOT_ENCHANTABLE)
	).withExclusiveSet((key, provider) -> provider
			.add(of("resonance"))
	).register();
	
	// increased mining speed for very hard blocks
	public static final ResourceKey<Enchantment> RAZING = new Builder(
			"razing",
			5,
			3,
			new Enchantment.Cost(20, 0),
			new Enchantment.Cost(30, 0),
			2,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.MINING_LOOT_ENCHANTABLE)
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.FORTUNE)
	).register();
	
	// Silk Touch, just for different blocks
	public static final ResourceKey<Enchantment> RESONANCE = new Builder(
			"resonance",
			1,
			1,
			new Enchantment.Cost(25, 0),
			new Enchantment.Cost(100, 0),
			8,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.MINING_LOOT_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.MINING_ENCHANTABLE)
			.add(SpectrumItems.ENDER_SPLICE)
			.add(SpectrumItems.EXCHANGING_STAFF)
	).withExclusiveSet((key, provider) -> provider
			.forceAddTag(EnchantmentTags.MINING_EXCLUSIVE)
			.add(PEST_CONTROL)
	).register();
	
	// Increase luck when fishing
	public static final ResourceKey<Enchantment> SERENDIPITY_REEL = new Builder(
			"serendipity_reel",
			2,
			2,
			new Enchantment.Cost(40, 0),
			new Enchantment.Cost(120, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.FISHING_ENCHANTABLE)
	).register();
	
	// Increases projectile speed => increased damage + range
	public static final ResourceKey<Enchantment> SNIPING = new Builder(
			"sniping",
			1,
			2,
			new Enchantment.Cost(20, 0),
			new Enchantment.Cost(50, 0),
			8,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.CROSSBOW_ENCHANTABLE)
			.add(SpectrumItems.GLEAMING_PIN)
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.MULTISHOT)
	).register();
	
	// ItemStacks with this enchantment are not destroyed by cactus, fire, lava, ...
	public static final ResourceKey<Enchantment> STEADFAST = new Builder(
			"steadfast",
			10,
			1,
			new Enchantment.Cost(30, 0),
			new Enchantment.Cost(60, 0),
			1,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.DURABILITY_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.DURABILITY_ENCHANTABLE)
			.forceAddTag(ItemTags.MINING_ENCHANTABLE)
			.forceAddTag(ItemTags.VANISHING_ENCHANTABLE)
			.addOptionalTag(ResourceLocation.parse("trinkets:enchantable/enchantable"))
	).register();
	
	// Increases attack speed
	public static final ResourceKey<Enchantment> TIGHT_GRIP = new Builder(
			"tight_grip",
			2,
			2,
			new Enchantment.Cost(5, 0),
			new Enchantment.Cost(35, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.SWORD_ENCHANTABLE)
	).withEffects((key, ctx, builder) -> builder
			.withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(locate("enchantment.tight_grip"), Attributes.ATTACK_SPEED, LevelBasedValue.perLevel(0.0625f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.SWORD_ENCHANTABLE)
			.addOptionalTag(ResourceLocation.parse("malum:scythe"))
	).withExclusiveSet((key, provider) -> provider
			.addOptional(ResourceLocation.parse("malum:rebound"))
	).register();
	
	// Drops mob heads
	public static final ResourceKey<Enchantment> TREASURE_HUNTER = new Builder(
			"treasure_hunter",
			2,
			3,
			new Enchantment.Cost(15, 0),
			new Enchantment.Cost(45, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.of(ItemTags.SWORD_ENCHANTABLE)
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
			.addOptionalTag(ResourceLocation.parse("malum:scythe"))
	).withExclusiveSet((key, provider) -> provider
			.add(Enchantments.LOOTING)
			.addOptional(ResourceLocation.parse("malum:spirit_plunder"))
	).register();
	
	// Voids all items mined
	public static final ResourceKey<Enchantment> VOIDING = new Builder(
			"voiding",
			2,
			1,
			new Enchantment.Cost(25, 0),
			new Enchantment.Cost(50, 0),
			4,
			List.of(EquipmentSlotGroup.MAINHAND),
			Optional.empty()
	).withEnchantable(provider -> provider
			.forceAddTag(ItemTags.MINING_LOOT_ENCHANTABLE)
			.add(SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem())
	).register();
	
	private static ResourceKey<Enchantment> of(String id) {
		return ResourceKey.create(Registries.ENCHANTMENT, locate(id));
	}
	
	public static TagKey<Item> getEnchantableTag(ResourceKey<Enchantment> key) {
		return TagKey.create(Registries.ITEM, locate("enchantable/" + key.location().getPath()));
	}
	
	public static TagKey<Enchantment> getExclusiveSetTag(ResourceKey<Enchantment> key) {
		return TagKey.create(Registries.ENCHANTMENT, locate("exclusive_set/" + key.location().getPath()));
	}
	
	public static ResourceKey<Enchantment> getCloakKey(ResourceKey<Enchantment> key) {
		return ResourceKey.create(Registries.ENCHANTMENT, locate("cloaked/" + key.location().getPath()));
	}
	
	private static class Builder {
		private final ResourceKey<Enchantment> key;
		private final int weight;
		private final int maxLevel;
		private final Enchantment.Cost minCost;
		private final Enchantment.Cost maxCost;
		private final int anvilCost;
		private final List<EquipmentSlotGroup> slots;
		private final Optional<TagKey<Item>> primaryItems;
		
		private EnchantmentBuilderCallback effectsBuilder = (key, ctx, builder) -> builder;
		private DatagenProxy.TagBuilderCallback<Item> enchantableBuilder = provider -> provider;
		private DatagenProxy.KeyedTagBuilderCallback<Enchantment> exclusiveSetBuilder = (key, provider) -> provider;
		
		public Builder(String name, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, List<EquipmentSlotGroup> slots, Optional<TagKey<Item>> primaryItems) {
			this.key = of(name);
			this.weight = weight;
			this.maxLevel = maxLevel;
			this.minCost = minCost;
			this.maxCost = maxCost;
			this.anvilCost = anvilCost;
			this.slots = slots;
			this.primaryItems = primaryItems;
		}
		
		public Builder withEffects(EnchantmentBuilderCallback enchantmentBuilder) {
			this.effectsBuilder = enchantmentBuilder;
			return this;
		}
		
		public Builder withEnchantable(DatagenProxy.TagBuilderCallback<Item> enchantableBuilder) {
			this.enchantableBuilder = enchantableBuilder;
			return this;
		}
		
		public Builder withExclusiveSet(DatagenProxy.KeyedTagBuilderCallback<Enchantment> exclusiveSetBuilder) {
			this.exclusiveSetBuilder = exclusiveSetBuilder;
			return this;
		}
		
		public ResourceKey<Enchantment> register() {
			BOOTSTRAP_REGISTAR.defer(ctx -> {
				// Build the enchantment
				HolderSet<Item> supportedItemsList = ctx.items().getOrThrow(getEnchantableTag(key));
				Optional<HolderSet<Item>> primaryItemsList = primaryItems.map(tag -> ctx.items().getOrThrow(tag));
				Enchantment.EnchantmentDefinition definition = new Enchantment.EnchantmentDefinition(supportedItemsList, primaryItemsList, weight, maxLevel, minCost, maxCost, anvilCost, slots);
				Enchantment.Builder enchantment = new Enchantment.Builder(definition)
						.exclusiveWith(ctx.enchantments().getOrThrow(getExclusiveSetTag(key)));
				ctx.registerable().register(key, effectsBuilder.build(key, ctx, enchantment).build(key.location()));
			});
			
			ENCHANTMENT_TAG_REGISTRAR.defer(key, (key, ctx) -> {
				// Build the exclusive set enchantment tag
				exclusiveSetBuilder.build(key, ctx.build(getExclusiveSetTag(key)));
			});
			
			ITEM_TAG_REGISTRAR.defer(ctx -> {
				// Build the enchantable items tag
				enchantableBuilder.build(ctx.build(getEnchantableTag(key)));
			});
			
			return key;
		}
		
	}
	
	public static void provideItemTags(DatagenProxy.ProvidedTagBuilderBuilder<Item> builder) {
		ITEM_TAG_REGISTRAR.flush(builder);
	}
	
	public static void provideEnchantmentTags(DatagenProxy.ProvidedTagBuilderBuilder<Enchantment> builder) {
		FabricTagProvider<Enchantment>.FabricTagBuilder enchantments = builder.build(SpectrumEnchantmentTags.SPECTRUM_ENCHANTMENT);
		ENCHANTMENT_TAG_REGISTRAR.streamKeys().sorted(Comparator.comparing(ResourceKey::location)).forEach(enchantments::addOptional);
		ENCHANTMENT_TAG_REGISTRAR.flush(builder);
	}
	
	public static void provideEnchantments(DatagenProxy.BootstrapContext<Enchantment> ctx) {
		BOOTSTRAP_REGISTAR.flush(ctx);
	}
	
	private interface EnchantmentBuilderCallback {
		Enchantment.Builder build(ResourceKey<Enchantment> key, DatagenProxy.BootstrapContext<Enchantment> ctx, Enchantment.Builder builder);
	}
	
}
