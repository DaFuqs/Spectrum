package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
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

import java.util.Collection;
import java.util.List;

public class WhispyCircletItem extends SpectrumTrinketItem {
	
    private final int TRIGGER_EVERY_X_TICKS = 100;
    private final int NEGATIVE_EFFECT_SHORTENING_TICKS = 100;
	
	public WhispyCircletItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_whispy_circlet"));
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
		
		if(!entity.world.isClient) {
			long time = entity.getWorld().getTime();
			if (time % TRIGGER_EVERY_X_TICKS == 0) {
				shortenNegativeStatusEffects(entity, NEGATIVE_EFFECT_SHORTENING_TICKS);
			}
			if (time % 10000 == 0 && entity instanceof ServerPlayerEntity serverPlayerEntity) {
				preventPhantomSpawns(serverPlayerEntity);
			}
		}
	}
	
	public static void removeNegativeStatusEffects(@NotNull LivingEntity entity) {
		Collection<StatusEffectInstance> currentEffects = entity.getStatusEffects();
		for(StatusEffectInstance instance : currentEffects) {
			if(instance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
				entity.removeStatusEffect(instance.getEffectType());
			}
		}
	}
	
	public static void shortenNegativeStatusEffects(@NotNull LivingEntity entity, int duration) {
		Collection<StatusEffectInstance> currentEffects = entity.getStatusEffects();
		// remove them first, so hidden "stacked" effects are preserved
		for(StatusEffectInstance instance : currentEffects) {
			if(instance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
				entity.removeStatusEffect(instance.getEffectType());
			}
		}
		
		for(StatusEffectInstance instance : currentEffects) {
			if(instance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
				int newDurationTicks = instance.getDuration() - duration;
				if(newDurationTicks > 0) {
					entity.addStatusEffect(new StatusEffectInstance(instance.getEffectType(), newDurationTicks, instance.getAmplifier(), instance.isAmbient(), true));
				}
			}
		}
	}
	
	public static void preventPhantomSpawns(@NotNull ServerPlayerEntity serverPlayerEntity) {
		serverPlayerEntity.getStatHandler().setStat(serverPlayerEntity, Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST), 0);
	}
	
}