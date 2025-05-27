package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.explosion.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.entity.effect.*;
import net.minecraft.network.codec.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public class SpectrumDataComponentTypes {
	
	private static final DeferredRegistrar REGISTRAR = new DeferredRegistrar();
	
	// It seems like vanilla caches all components with collections (lists, maps, etc.), so we will too
	public static final ComponentType<Unit> ACTIVATED = register("activated", builder -> builder.codec(Codec.unit(Unit.INSTANCE)).packetCodec(PacketCodec.unit(Unit.INSTANCE)));
	public static final ComponentType<Integer> AOE = register("aoe", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.VAR_INT));
	public static final ComponentType<BeverageComponent> BEVERAGE = register("beverage", builder -> builder.codec(BeverageComponent.CODEC).packetCodec(BeverageComponent.PACKET_CODEC));
	public static final ComponentType<BottomlessBundleItem.BottomlessStack> BOTTOMLESS_STACK = register("bottomless_stack", builder -> builder.codec(BottomlessBundleItem.BottomlessStack.CODEC).packetCodec(BottomlessBundleItem.BottomlessStack.PACKET_CODEC));
	public static final ComponentType<Identifier> BOUND_ITEM = register("bound_item", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));
	public static final ComponentType<ItemEnchantmentsComponent> CANVAS_ENCHANTMENTS = register("canvas_enchantments", (builder) -> builder.codec(ItemEnchantmentsComponent.CODEC).packetCodec(ItemEnchantmentsComponent.PACKET_CODEC).cache());
	public static final ComponentType<PairedFoodComponent> PAIRED_FOOD_COMPONENT = register("paired_food_component", builder -> builder.codec(PairedFoodComponent.CODEC).packetCodec(PairedFoodComponent.PACKET_CODEC));
	public static final ComponentType<CustomPotionDataComponent> CUSTOM_POTION_DATA = register("custom_potion_data", builder -> builder.codec(CustomPotionDataComponent.CODEC).packetCodec(CustomPotionDataComponent.PACKET_CODEC));
	public static final ComponentType<EnderSpliceComponent> ENDER_SPLICE = register("ender_splice", builder -> builder.codec(EnderSpliceComponent.CODEC).packetCodec(EnderSpliceComponent.PACKET_CODEC));
	public static final ComponentType<ExtendedBundleComponent> EXTENDED_BUNDLE = register("extended_bundle", builder -> builder.codec(ExtendedBundleComponent.CODEC).packetCodec(ExtendedBundleComponent.PACKET_CODEC));
	public static final ComponentType<Unit> HIDE_USAGE_TOOLTIP = register("hide_usage_tooltip", builder -> builder.codec(Codec.unit(Unit.INSTANCE)).packetCodec(PacketCodec.unit(Unit.INSTANCE)));
	public static final ComponentType<InertiaComponent> INERTIA = register("inertia", builder -> builder.codec(InertiaComponent.CODEC).packetCodec(InertiaComponent.PACKET_CODEC));
	public static final ComponentType<InfusedBeverageComponent> INFUSED_BEVERAGE = register("infused_beverage", builder -> builder.codec(InfusedBeverageComponent.CODEC).packetCodec(InfusedBeverageComponent.PACKET_CODEC));
	public static final ComponentType<InkColor> INK_COLOR = register("ink_color", builder -> builder.codec(InkColor.CODEC).packetCodec(InkColor.PACKET_CODEC));
	public static final ComponentType<InkPoweredComponent> INK_POWERED = register("ink_powered", builder -> builder.codec(InkPoweredComponent.CODEC).packetCodec(InkPoweredComponent.PACKET_CODEC).cache());
	public static final ComponentType<InkStorageComponent> INK_STORAGE = register("ink_storage", builder -> builder.codec(InkStorageComponent.CODEC).packetCodec(InkStorageComponent.PACKET_CODEC).cache());
	public static final ComponentType<Unit> IS_PREVIEW_ITEM = register("is_preview_item", builder -> builder.codec(Codec.unit(Unit.INSTANCE)).packetCodec(PacketCodec.unit(Unit.INSTANCE)));
	public static final ComponentType<JadeWineComponent> JADE_WINE = register("jade_wine", builder -> builder.codec(JadeWineComponent.CODEC).packetCodec(JadeWineComponent.PACKET_CODEC));
	public static final ComponentType<Long> LAST_COOLDOWN_START = register("last_cooldown_start", builder -> builder.codec(Codec.LONG).packetCodec(PacketCodecs.VAR_LONG));
	public static final ComponentType<MemoryComponent> MEMORY = register("memory", builder -> builder.codec(MemoryComponent.CODEC).packetCodec(MemoryComponent.PACKET_CODEC).cache());
	public static final ComponentType<ModularExplosionDefinition> MODULAR_EXPLOSION = register("modular_explosion", builder -> builder.codec(ModularExplosionDefinition.CODEC).packetCodec(ModularExplosionDefinition.PACKET_CODEC));
	public static final ComponentType<StatusEffectInstance> CONCEALED_EFFECT = register("concealed_effect", builder -> builder.codec(StatusEffectInstance.CODEC).packetCodec(StatusEffectInstance.PACKET_CODEC));
	public static final ComponentType<Float> OVERCHARGED = register("overcharged", builder -> builder.codec(Codec.FLOAT).packetCodec(PacketCodecs.FLOAT));
	public static final ComponentType<PairedItemComponent> PAIRED_ITEM = register("paired_item", builder -> builder.codec(PairedItemComponent.CODEC).packetCodec(PairedItemComponent.PACKET_CODEC));
	public static final ComponentType<Long> TIMESTAMP = register("timestamp", builder -> builder.codec(Codec.LONG).packetCodec(PacketCodecs.VAR_LONG));
	public static final ComponentType<ShootingStarComponent> SHOOTING_STAR = register("shooting_star", builder -> builder.codec(ShootingStarComponent.CODEC).packetCodec(ShootingStarComponent.PACKET_CODEC));
	public static final ComponentType<UUID> SLOT_RESERVER = register("slot_eserver", builder -> builder.codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC));
	public static final ComponentType<Unit> SOCKETED = register("socketed", builder -> builder.codec(Codec.unit(Unit.INSTANCE)).packetCodec(PacketCodec.unit(Unit.INSTANCE)));
	public static final ComponentType<Unit> STABLE = register("stable", builder -> builder.codec(Codec.unit(Unit.INSTANCE)).packetCodec(PacketCodec.unit(Unit.INSTANCE)));
	public static final ComponentType<Identifier> STORED_BLOCK = register("stored_block", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));
	public static final ComponentType<Integer> STORED_EXPERIENCE = register("stored_experience", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.VAR_INT));
	public static final ComponentType<Identifier> STORED_RECIPE = register("stored_recipe", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));
	public static final ComponentType<GlobalPos> TARGETED_STRUCTURE = register("targeted_structure", builder -> builder.codec(GlobalPos.CODEC).packetCodec(GlobalPos.PACKET_CODEC));
	public static final ComponentType<WrappedPresentComponent> WRAPPED_PRESENT = register("wrapped_present", builder -> builder.codec(WrappedPresentComponent.CODEC).packetCodec(WrappedPresentComponent.PACKET_CODEC).cache());
	public static final ComponentType<WithMilkComponent> WITH_MILK = register("with_milk", builder -> builder.codec(WithMilkComponent.CODEC).packetCodec(WithMilkComponent.PACKET_CODEC));
	public static final ComponentType<WorkstaffComponent> WORKSTAFF = register("workstaff", builder -> builder.codec(WorkstaffComponent.CODEC).packetCodec(WorkstaffComponent.PACKET_CODEC));
	
	public static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return REGISTRAR.defer(builderOperator.apply(ComponentType.builder()).build(),
				type -> Registry.register(Registries.DATA_COMPONENT_TYPE, SpectrumCommon.locate(id), type));
	}
	
	public static void register() {
		REGISTRAR.flush();
	}
	
}
