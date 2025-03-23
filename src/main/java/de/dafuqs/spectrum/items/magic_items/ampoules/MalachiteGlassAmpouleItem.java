package de.dafuqs.spectrum.items.magic_items.ampoules;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MalachiteGlassAmpouleItem extends BaseGlassAmpouleItem implements InkPoweredPotionFillable {
    
    public MalachiteGlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
	public boolean trigger(World world, ItemStack stack, @Nullable LivingEntity attacker, @Nullable LivingEntity target, Vec3d position) {
        List<StatusEffectInstance> e = new ArrayList<>();
        if (attacker instanceof PlayerEntity player) {
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
		
		world.playSoundAtBlockCenter(BlockPos.ofFloored(position), SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundCategory.PLAYERS, 0.35F, 0.9F + world.getRandom().nextFloat() * 0.334F, true);
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.spectrum.malachite_glass_ampoule.tooltip").formatted(Formatting.GRAY));
        appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.malachite_glass_ampoule.tooltip.when_hit"), false);
    }
    
}
