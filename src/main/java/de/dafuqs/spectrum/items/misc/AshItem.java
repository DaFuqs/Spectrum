package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;

import java.util.*;

public class AshItem extends Item {
	
	public AshItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		var world = context.getLevel();
		var random = world.getRandom();
		var stack = context.getItemInHand();
		var reference = BlockReference.of(world, context.getClickedPos());
		
		if (!reference.isOf(SpectrumBlocks.BLACKSLAG))
			return InteractionResult.FAIL;
		
		world.setBlockAndUpdate(reference.pos, SpectrumBlocks.ASHEN_BLACKSLAG.defaultBlockState());
		
		if (!world.isClientSide()) {
			world.addDestroyBlockEffect(reference.pos, SpectrumBlocks.ASH.defaultBlockState());
			world.playLocalSound(reference.pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 0.5F, 0.5F + random.nextFloat() * 0.5F, true);
		}
		
		for (int i = 0; i < 4 + random.nextInt(4); i++) {
			world.addParticle(SpectrumParticleTypes.FALLING_ASH, reference.pos.getX() + random.nextFloat(), reference.pos.getY() + 1.1 + random.nextFloat() * 0.4F, reference.pos.getZ() + random.nextFloat(), 0, 0, 0);
		}
		
		if (Optional.ofNullable(context.getPlayer()).map(p -> !p.getAbilities().instabuild).orElse(true))
			stack.shrink(1);
		
		return InteractionResult.sidedSuccess(world.isClientSide());
	}
}
