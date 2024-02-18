package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class SpectrumOmniAcceleratorBehaviors {

	public static void register() {
		OmniAcceleratorBehavior.register(SpectrumItems.STORM_STONE, new OmniAcceleratorBehavior() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity itemProjectileEntity, ItemStack stack, Entity owner, EntityHitResult hitResult) {
				return strikeLightning(hitResult.getEntity().getWorld(), hitResult.getEntity().getBlockPos());
			}

			@Override
			public boolean onBlockHit(ItemProjectileEntity itemProjectileEntity, ItemStack stack, Entity owner, BlockHitResult hitResult) {
				return strikeLightning(itemProjectileEntity.getWorld(), hitResult.getBlockPos());
			}

			private boolean strikeLightning(World world, BlockPos pos) {
				if (world.isSkyVisible(pos)) {
					LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
					if (lightningEntity != null) {
						lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
						world.spawnEntity(lightningEntity);
						return true;
					}
				}
				return false;
			}
		});
	}

}
