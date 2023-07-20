package de.dafuqs.spectrum.items.magic_items.ampoules;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FractalGlassAmpouleItem extends BaseGlassAmpouleItem implements InkPoweredPotionFillable {
    
    public FractalGlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public boolean trigger(ItemStack stack, LivingEntity attacker, @Nullable LivingEntity target) {
        List<StatusEffectInstance> e = new ArrayList<>();
        if (attacker instanceof PlayerEntity player) {
            List<InkPoweredStatusEffectInstance> effects = getEffects(stack);
            for (InkPoweredStatusEffectInstance effect : effects) {
                if (InkPowered.tryDrainEnergy(player, effect.getInkCost())) {
                    e.add(effect.getStatusEffectInstance());
                }
            }
        }
        LightMineEntity.summonBarrage(attacker.getWorld(), attacker, target, e);
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
        tooltip.add(Text.translatable("item.spectrum.fractal_glass_ampoule.tooltip").formatted(Formatting.GRAY));
        appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.fractal_glass_ampoule.tooltip"), false);
    }
    
}
