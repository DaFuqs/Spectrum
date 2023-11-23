package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ShootingStarDispenserBehavior extends ItemDispenserBehavior {

	@Override
	public ItemStack dispenseSilently(@NotNull BlockPointer pointer, @NotNull ItemStack stack) {
		Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);

		World world = pointer.getWorld();
		ShootingStarItem shootingStarItem = ((ShootingStarItem) stack.getItem());
		ShootingStarEntity shootingStarEntity = shootingStarItem.getEntityForStack(world,
				new Vec3d(pointer.getX() + direction.getOffsetX() * 1.125F,
						pointer.getY() + direction.getOffsetY() * 1.13F,
						pointer.getZ() + direction.getOffsetZ() * 1.125F), stack);
		shootingStarEntity.setYaw(direction.asRotation());
		shootingStarEntity.addVelocity(direction.getOffsetX() * 0.4, direction.getOffsetY() * 0.4, direction.getOffsetZ() * 0.4);
		world.spawnEntity(shootingStarEntity);

		stack.decrement(1);
		return stack;
	}

}
