package de.dafuqs.spectrum.items.magic_items.ampoules;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MalachiteGlassAmpouleItem extends GlassAmpouleItem implements InkPoweredPotionFillable {
	
	public MalachiteGlassAmpouleItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public boolean trigger(Level world, ItemStack stack, @Nullable LivingEntity attacker, @Nullable LivingEntity target, Vec3 position) {
		List<MobEffectInstance> e = new ArrayList<>();
		if (attacker instanceof Player player) {
			List<InkPoweredStatusEffectInstance> effects = InkPoweredPotionFillable.getEffects(stack);
			for (InkPoweredStatusEffectInstance effect : effects) {
				if (InkPowered.tryDrainEnergy(player, effect.getInkCost())) {
					e.add(effect.getStatusEffectInstance());
				}
			}
		}
		
		if (e.isEmpty()) {
			return false;
		}
		
		world.playLocalSound(BlockPos.containing(position), SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundSource.PLAYERS, 0.35F, 0.9F + world.getRandom().nextFloat() * 0.334F, true);
		LightMineEntity.summonBarrage(world, attacker, target, LightShardBaseEntity.MONSTER_TARGET, e, position, LightShardBaseEntity.DEFAULT_COUNT_PROVIDER);
		return true;
	}
	
	@Override
	public int maxEffectCount() {
		return 1;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return 0;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.malachite_glass_ampoule.tooltip").withStyle(ChatFormatting.GRAY));
		appendPotionFillableTooltip(stack, tooltip, Component.translatable("item.spectrum.malachite_glass_ampoule.tooltip.when_hit"), false, context.tickRate());
	}
	
}
