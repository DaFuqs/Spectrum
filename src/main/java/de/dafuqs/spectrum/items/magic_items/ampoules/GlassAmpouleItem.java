package de.dafuqs.spectrum.items.magic_items.ampoules;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassAmpouleItem extends BaseGlassAmpouleItem {
    
    public GlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
	public boolean trigger(World world, ItemStack stack, LivingEntity attacker, @Nullable LivingEntity target, Vec3d position) {
		world.playSoundAtBlockCenter(BlockPos.ofFloored(position), SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundCategory.PLAYERS, 0.35F, 0.9F + world.getRandom().nextFloat() * 0.334F, true);
		LightShardEntity.summonBarrage(world, attacker, target, LightShardBaseEntity.MONSTER_TARGET, position, LightShardBaseEntity.DEFAULT_COUNT_PROVIDER);
        return true;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.azurite_glass_ampoule.tooltip").formatted(Formatting.GRAY));
    }
    
}
