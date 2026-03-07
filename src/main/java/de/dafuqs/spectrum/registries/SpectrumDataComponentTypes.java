package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.components.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public class SpectrumDataComponentTypes {
	
	private static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SpectrumCommon.MOD_ID);
	
	// It seems like vanilla caches all components with collections (lists, maps, etc.), so we will too
	public static final Supplier<DataComponentType<Unit>> ACTIVATED = register("activated", builder -> builder.persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	public static final Supplier<DataComponentType<Integer>> AOE = register("aoe", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final Supplier<DataComponentType<BeverageComponent>> BEVERAGE = register("beverage", builder -> builder.persistent(BeverageComponent.CODEC).networkSynchronized(BeverageComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<BottomlessComponent>> BOTTOMLESS_STACK = register("bottomless_stack", builder -> builder.persistent(BottomlessComponent.CODEC).networkSynchronized(BottomlessComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<ResourceLocation>> BOUND_ITEM = register("bound_item", builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final Supplier<DataComponentType<ItemEnchantments>> CANVAS_ENCHANTMENTS = register("canvas_enchantments", (builder) -> builder.persistent(ItemEnchantments.CODEC).networkSynchronized(ItemEnchantments.STREAM_CODEC).cacheEncoding());
	public static final Supplier<DataComponentType<PairedFoodComponent>> PAIRED_FOOD_COMPONENT = register("paired_food_component", builder -> builder.persistent(PairedFoodComponent.CODEC).networkSynchronized(PairedFoodComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<CustomPotionDataComponent>> CUSTOM_POTION_DATA = register("custom_potion_data", builder -> builder.persistent(CustomPotionDataComponent.CODEC).networkSynchronized(CustomPotionDataComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<EnderSpliceComponent>> ENDER_SPLICE = register("ender_splice", builder -> builder.persistent(EnderSpliceComponent.CODEC).networkSynchronized(EnderSpliceComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<Unit>> HIDE_USAGE_TOOLTIP = register("hide_usage_tooltip", builder -> builder.persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	public static final Supplier<DataComponentType<InertiaComponent>> INERTIA = register("inertia", builder -> builder.persistent(InertiaComponent.CODEC).networkSynchronized(InertiaComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<InfusedBeverageComponent>> INFUSED_BEVERAGE = register("infused_beverage", builder -> builder.persistent(InfusedBeverageComponent.CODEC).networkSynchronized(InfusedBeverageComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<InkColor>> INK_COLOR = register("ink_color", builder -> builder.persistent(InkColor.CODEC).networkSynchronized(InkColor.PACKET_CODEC));
	public static final Supplier<DataComponentType<InkPoweredComponent>> INK_POWERED = register("ink_powered", builder -> builder.persistent(InkPoweredComponent.CODEC).networkSynchronized(InkPoweredComponent.PACKET_CODEC).cacheEncoding());
	public static final Supplier<DataComponentType<InkStorageComponent>> INK_STORAGE = register("ink_storage", builder -> builder.persistent(InkStorageComponent.CODEC).networkSynchronized(InkStorageComponent.PACKET_CODEC).cacheEncoding());
	public static final Supplier<DataComponentType<Unit>> IS_PREVIEW_ITEM = register("is_preview_item", builder -> builder.persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	public static final Supplier<DataComponentType<JadeWineComponent>> JADE_WINE = register("jade_wine", builder -> builder.persistent(JadeWineComponent.CODEC).networkSynchronized(JadeWineComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<Long>> LAST_COOLDOWN_START = register("last_cooldown_start", builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));
	public static final Supplier<DataComponentType<MemoryComponent>> MEMORY = register("memory", builder -> builder.persistent(MemoryComponent.CODEC).networkSynchronized(MemoryComponent.PACKET_CODEC).cacheEncoding());
	public static final Supplier<DataComponentType<MobEffectInstance>> CONCEALED_EFFECT = register("concealed_effect", builder -> builder.persistent(MobEffectInstance.CODEC).networkSynchronized(MobEffectInstance.STREAM_CODEC));
	public static final Supplier<DataComponentType<ResolvableProfile>> CONCEALED_EFFECT_PROFILE = register("concealed_effect_profile", builder -> builder.persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding());
	public static final Supplier<DataComponentType<Float>> OVERCHARGED = register("overcharged", builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
	public static final Supplier<DataComponentType<PairedItemComponent>> PAIRED_ITEM = register("paired_item", builder -> builder.persistent(PairedItemComponent.CODEC).networkSynchronized(PairedItemComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<Long>> TIMESTAMP = register("timestamp", builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));
	public static final Supplier<DataComponentType<ShootingStarComponent>> SHOOTING_STAR = register("shooting_star", builder -> builder.persistent(ShootingStarComponent.CODEC).networkSynchronized(ShootingStarComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<UUID>> SLOT_RESERVER = register("slot_reserver", builder -> builder.persistent(UUIDUtil.AUTHLIB_CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));
	public static final Supplier<DataComponentType<Unit>> SOCKETED = register("socketed", builder -> builder.persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	public static final Supplier<DataComponentType<Unit>> STABLE = register("stable", builder -> builder.persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	public static final Supplier<DataComponentType<ResourceLocation>> STORED_BLOCK = register("stored_block", builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final Supplier<DataComponentType<Integer>> STORED_EXPERIENCE = register("stored_experience", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final Supplier<DataComponentType<ResourceLocation>> STORED_RECIPE = register("stored_recipe", builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
	public static final Supplier<DataComponentType<GlobalPos>> TARGETED_STRUCTURE = register("targeted_structure", builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC));
	public static final Supplier<DataComponentType<WrappedPresentComponent>> WRAPPED_PRESENT = register("wrapped_present", builder -> builder.persistent(WrappedPresentComponent.CODEC).networkSynchronized(WrappedPresentComponent.PACKET_CODEC).cacheEncoding());
	public static final Supplier<DataComponentType<WithMilkComponent>> WITH_MILK = register("with_milk", builder -> builder.persistent(WithMilkComponent.CODEC).networkSynchronized(WithMilkComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<WorkstaffComponent>> WORKSTAFF = register("workstaff", builder -> builder.persistent(WorkstaffComponent.CODEC).networkSynchronized(WorkstaffComponent.PACKET_CODEC));
	public static final Supplier<DataComponentType<SimpleFluidContent>> FLUID_CONTENT = register("fluid_content", builder -> builder.persistent(SimpleFluidContent.CODEC));
	
	public static <T> Supplier<DataComponentType<T>> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return REGISTRAR.registerComponentType(id, builderOperator);
	}
	
	public static void register(IEventBus modEventBus) {
		REGISTRAR.register(modEventBus);
	}
	
}
