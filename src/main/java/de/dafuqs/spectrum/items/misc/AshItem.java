package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;

import java.util.*;

public class AshItem extends Item {
	
	public AshItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		var world = context.getWorld();
		var random = world.getRandom();
		var stack = context.getStack();
		var reference = BlockReference.of(world, context.getBlockPos());
		
		if (!reference.isOf(SpectrumBlocks.BLACKSLAG))
			return ActionResult.FAIL;
		
		world.setBlockState(reference.pos, SpectrumBlocks.ASHEN_BLACKSLAG.getDefaultState());
		
		if (!world.isClient()) {
			world.addBlockBreakParticles(reference.pos, SpectrumBlocks.ASH.getDefaultState());
			world.playSoundAtBlockCenter(reference.pos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 0.5F, 0.5F + random.nextFloat() * 0.5F, true);
		}
		
		for (int i = 0; i < 4 + random.nextInt(4); i++) {
			world.addParticle(SpectrumParticleTypes.FALLING_ASH, reference.pos.getX() + random.nextFloat(), reference.pos.getY() + 1.1 + random.nextFloat() * 0.4F, reference.pos.getZ() + random.nextFloat(), 0, 0, 0);
		}
		
		if (Optional.ofNullable(context.getPlayer()).map(p -> !p.getAbilities().creativeMode).orElse(true))
			stack.decrement(1);
		
		return ActionResult.success(world.isClient());
	}
}
