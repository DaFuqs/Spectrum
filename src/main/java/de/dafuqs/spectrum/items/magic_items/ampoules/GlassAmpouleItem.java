package de.dafuqs.spectrum.items.magic_items.ampoules;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassAmpouleItem extends BaseGlassAmpouleItem {
    
    public GlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public boolean trigger(ItemStack stack, LivingEntity attacker, @Nullable LivingEntity target) {
        if (!attacker.world.isClient) {
            var world = attacker.world;
            world.playSound(null, attacker.getBlockPos(), SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundCategory.PLAYERS, 0.35F, 0.9F + attacker.getRandom().nextFloat() * 0.334F);
            LightShardEntity.summonBarrage(attacker.world, attacker, target);
        }
        return true;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.spectrum.glass_ampoule.tooltip").formatted(Formatting.GRAY));
    }
    
}
