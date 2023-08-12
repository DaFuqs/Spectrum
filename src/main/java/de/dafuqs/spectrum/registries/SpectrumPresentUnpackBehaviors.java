package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.present.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;

public class SpectrumPresentUnpackBehaviors {
	
	public static void register() {
		PresentBlock.registerBehavior(SpectrumItems.PIPE_BOMB, (stack, presentBlockEntity, world, pos, random) -> {
			NbtCompound nbt = stack.getOrCreateNbt();
			nbt.putBoolean("armed", true);
			nbt.putLong("timestamp", world.getTime() - 70);
			nbt.putUuid("owner", presentBlockEntity.getOwnerUUID());
			world.playSound(null, pos, SpectrumSoundEvents.INCANDESCENT_ARM, SoundCategory.BLOCKS, 2.0F, 0.9F);
			return stack;
		});
		
		PresentBlock.registerBehavior(SpectrumItems.STORM_STONE, (stack, presentBlockEntity, world, pos, random) -> {
			if (world.isSkyVisible(pos)) {
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
					world.spawnEntity(lightningEntity);
				}
			}
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(Items.FIREWORK_ROCKET, (stack, presentBlockEntity, world, pos, random) -> {
			if (!world.isClient) {
				Vec3d centerPos = Vec3d.of(pos);
				for (int i = 0; i < stack.getCount(); i++) {
					FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, presentBlockEntity.getOwnerIfOnline(), centerPos.x + 0.35 + random.nextFloat() * 0.3, centerPos.y + 0.35 + random.nextFloat() * 0.3, centerPos.z + 0.35 + random.nextFloat() * 0.3, stack);
					world.spawnEntity(fireworkRocketEntity);
				}
			}
			return ItemStack.EMPTY;
		});
	}
	
}
