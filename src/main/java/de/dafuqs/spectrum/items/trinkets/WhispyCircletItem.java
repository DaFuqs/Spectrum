package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhispyCircletItem extends SpectrumTrinketItem {
	
	private final int TRIGGER_EVERY_X_TICKS = 100;
	private final int NEGATIVE_EFFECT_SHORTENING_TICKS = 200;
	
	public WhispyCircletItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_whispy_circlet"));
	}
	
	public static void removeNegativeStatusEffects(@NotNull LivingEntity entity) {
		Collection<StatusEffectInstance> currentEffects = entity.getStatusEffects();
		for (StatusEffectInstance instance : currentEffects) {
			if (affects(instance.getEffectType())) {
				entity.removeStatusEffect(instance.getEffectType());
			}
		}
	}
	
	public static void shortenNegativeStatusEffects(@NotNull LivingEntity entity, int duration) {
		Collection<StatusEffectInstance> newEffects = new ArrayList<>();
		Collection<StatusEffect> effectTypesToClear = new ArrayList<>();
		
		// remove them first, so hidden "stacked" effects are preserved
		for (StatusEffectInstance instance : entity.getStatusEffects()) {
			if (affects(instance.getEffectType())) {
				int newDurationTicks = instance.getDuration() - duration;
				if (newDurationTicks > 0) {
					newEffects.add(new StatusEffectInstance(instance.getEffectType(), newDurationTicks, instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), true));
				}
				if (!effectTypesToClear.contains(instance.getEffectType())) {
					effectTypesToClear.add(instance.getEffectType());
				}
			}
		}
		
		for (StatusEffect effectTypeToClear : effectTypesToClear) {
			entity.removeStatusEffect(effectTypeToClear);
		}
		for (StatusEffectInstance newEffect : newEffects) {
			entity.addStatusEffect(newEffect);
		}
	}
	
	public static boolean affects(StatusEffect statusEffect) {
		return statusEffect.getCategory() == StatusEffectCategory.HARMFUL && !SpectrumStatusEffects.isUncurable(statusEffect);
	}
	
	public static void preventPhantomSpawns(@NotNull ServerPlayerEntity serverPlayerEntity) {
		serverPlayerEntity.getStatHandler().setStat(serverPlayerEntity, Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST), 0);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.whispy_circlet.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.whispy_circlet.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.whispy_circlet.tooltip3").formatted(Formatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		if (!entity.world.isClient) {
			long time = entity.getWorld().getTime();
			if (time % TRIGGER_EVERY_X_TICKS == 0) {
				shortenNegativeStatusEffects(entity, NEGATIVE_EFFECT_SHORTENING_TICKS);
			}
			if (time % 10000 == 0 && entity instanceof ServerPlayerEntity serverPlayerEntity) {
				preventPhantomSpawns(serverPlayerEntity);
			}
		}
	}
	
}