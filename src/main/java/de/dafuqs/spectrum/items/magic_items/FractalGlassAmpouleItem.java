package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FractalGlassAmpouleItem extends Item implements PotionFillable {
    
    public FractalGlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        
        if (!world.isClient) {
            List<InkPoweredStatusEffectInstance> effects = getEffects(stack);
            if (effects.size() == 0) {
                LightMineEntity.summonBarrage(user.getWorld(), user, null, null);
            } else if (InkPowered.tryDrainEnergy(user, effects.get(0).getInkCost())) {
                LightMineEntity.summonBarrage(user.getWorld(), user, null, effects.get(0).getStatusEffectInstance());
            } else {
                // deny sound
            }
        }
        
        return user.isCreative() ? super.use(world, user, hand) : TypedActionResult.consume(stack);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            List<InkPoweredStatusEffectInstance> effects = getEffects(stack);
            if (effects.size() == 0) {
                LightMineEntity.summonBarrage(attacker.getWorld(), attacker, target, null);
            } else if (InkPowered.tryDrainEnergy(player, effects.get(0).getInkCost())) {
                LightMineEntity.summonBarrage(attacker.getWorld(), attacker, target, effects.get(0).getStatusEffectInstance());
            } else {
                // deny sound
            }
        }
        
        if (!(attacker instanceof PlayerEntity player && player.isCreative()))
            stack.decrement(1);
        
        return super.postHit(stack, target, attacker);
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
        appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.fractal_glass_ampoule.when_hit"), false);
    }
    
}
