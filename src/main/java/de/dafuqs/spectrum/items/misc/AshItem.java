package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.state.*;

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
		var pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		
		if (!state.is(SpectrumBlocks.BLACKSLAG.get()))
			return InteractionResult.FAIL;
		
		world.setBlockAndUpdate(pos, SpectrumBlocks.ASHEN_BLACKSLAG.get().defaultBlockState());
		
		if (!world.isClientSide()) {
			world.addDestroyBlockEffect(pos, SpectrumBlocks.ASH.get().defaultBlockState());
			world.playLocalSound(pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 0.5F, 0.5F + random.nextFloat() * 0.5F, true);
		}
		
		for (int i = 0; i < 4 + random.nextInt(4); i++) {
			world.addParticle(SpectrumParticleTypes.FALLING_ASH, pos.getX() + random.nextFloat(), pos.getY() + 1.1 + random.nextFloat() * 0.4F, pos.getZ() + random.nextFloat(), 0, 0, 0);
		}
		
		if (Optional.ofNullable(context.getPlayer()).map(p -> !p.getAbilities().instabuild).orElse(true))
			stack.shrink(1);
		
		return InteractionResult.sidedSuccess(world.isClientSide());
	}
}
