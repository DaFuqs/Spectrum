package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;
import javax.annotation.*;

public class ShootingStarDispenserBehavior extends DefaultDispenseItemBehavior {
	
	@Override
	public ItemStack execute(BlockSource pointer, ItemStack stack) {
		Direction direction = pointer.state().getValue(DispenserBlock.FACING);
		
		Level world = pointer.level();
		ShootingStarItem shootingStarItem = ((ShootingStarItem) stack.getItem());
		Vec3 spawnPos = new Vec3(pointer.pos().getX() + direction.getStepX() * 1.125D, pointer.pos().getY() + direction.getStepY() * 1.13D, pointer.pos().getZ() + direction.getStepZ() * 1.125D);
		ShootingStarEntity shootingStarEntity = shootingStarItem.getEntityForStack(world, spawnPos, stack);
		shootingStarEntity.setYRot(direction.toYRot());
		shootingStarEntity.push(direction.getStepX() * 0.4, direction.getStepY() * 0.4, direction.getStepZ() * 0.4);
		world.addFreshEntity(shootingStarEntity);
		
		stack.shrink(1);
		return stack;
	}
	
}
