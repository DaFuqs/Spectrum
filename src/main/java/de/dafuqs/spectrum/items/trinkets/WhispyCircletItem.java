package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import javax.annotation.*;
import top.theillusivec4.curios.api.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WhispyCircletItem extends SpectrumCurioItem {
	
	public static final ResourceLocation ATTRIBUTE_ID = SpectrumCommon.locate("whispy_circlet_mental_presence");
	public static final Predicate<MobEffectInstance> EFFECT_CLEAR_PREDICATE = instance -> {
		Holder<MobEffect> holder = instance.getEffect();
		return holder.value().getCategory() == MobEffectCategory.HARMFUL && !holder.is(SpectrumMobEffectTags.BYPASSES_WHISPY_CIRCLET);
	};
	
	private final static int TRIGGER_EVERY_X_TICKS = 100;
	
	public WhispyCircletItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/whispy_circlet"));
	}
	
	public static void preventPhantomSpawns(ServerPlayer serverPlayerEntity) {
		serverPlayerEntity.getStats().setValue(serverPlayerEntity, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), 0);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.whispy_circlet.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.whispy_circlet.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.whispy_circlet.tooltip3").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		super.curioTick(slotContext, stack);
		LivingEntity entity = slotContext.entity();
		
		Level world = entity.level();
		if (!world.isClientSide()) {
			long time = entity.level().getGameTime();
			if (time % TRIGGER_EVERY_X_TICKS == 0) {
				MobEffectHelper.shortenEffects(entity, EFFECT_CLEAR_PREDICATE);
			}
			if (time % 10000 == 0 && entity instanceof ServerPlayer serverPlayer) {
				preventPhantomSpawns(serverPlayer);
			}
		}
	}
	
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getAttributeModifiers(slotContext, id, stack);
		modifiers.put(SpectrumEntityAttributes.MENTAL_PRESENCE, new AttributeModifier(ATTRIBUTE_ID, 0.3, AttributeModifier.Operation.ADD_VALUE));
		return modifiers;
	}
	
}
