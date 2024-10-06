package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.boom.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.registry.entry.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

import java.util.*;

public class SpectrumPresentUnpackBehaviors {
	
	public static void register() {
		PresentBlock.registerBehavior(SpectrumItems.PIPE_BOMB, (stack, presentBlockEntity, world, pos, random) -> {
			NbtCompound nbt = stack.getOrCreateNbt();
			nbt.putBoolean("armed", true);
			nbt.putLong("timestamp", world.getTime() - 70);
			UUID owner = presentBlockEntity.getOwnerUUID();
			if (owner != null) {
				nbt.putUuid("owner", presentBlockEntity.getOwnerUUID());
			}
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
				return ItemStack.EMPTY;
			}
			return stack;
		});
		
		PresentBlock.registerBehavior(SpectrumBlocks.INCANDESCENT_AMALGAM, (stack, presentBlockEntity, world, pos, random) -> {
			IncandescentAmalgamBlock.explode(world, pos, presentBlockEntity.getOwnerIfOnline(), stack);
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(Items.FIREWORK_ROCKET, (stack, presentBlockEntity, world, pos, random) -> {
			Vec3d centerPos = Vec3d.of(pos);
			for (int i = 0; i < stack.getCount(); i++) {
				FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, presentBlockEntity.getOwnerIfOnline(), centerPos.x + 0.35 + random.nextFloat() * 0.3, centerPos.y + 0.35 + random.nextFloat() * 0.3, centerPos.z + 0.35 + random.nextFloat() * 0.3, stack);
				world.spawnEntity(fireworkRocketEntity);
			}
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(Items.GOAT_HORN, (stack, presentBlockEntity, world, pos, random) -> {
			Optional<RegistryEntry<Instrument>> optional = ((GoatHornItemAccessor) stack.getItem()).invokeGetInstrument(stack);
			if (optional.isPresent()) {
				Instrument instrument = optional.get().value();
				SoundEvent soundEvent = instrument.soundEvent().value();
				world.playSound(null, pos, soundEvent, SoundCategory.RECORDS, instrument.range() / 16.0F, 1.0F);
			}
			return stack;
		});
		
		PresentBlock.registerBehavior(Items.BELL, (stack, presentBlockEntity, world, pos, random) -> {
			world.playSound(null, pos, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0F, 1.0F);
			return stack;
		});
		
		PresentBlock.registerBehavior(Items.TNT, (stack, presentBlockEntity, world, pos, random) -> {
			if (stack.getCount() > 0) {
				TntEntity tntEntity = null;
				for (int i = 0; i < stack.getCount(); i++) {
					tntEntity = new TntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, presentBlockEntity.getOwnerIfOnline());
					world.spawnEntity(tntEntity);
				}
				if (tntEntity != null) {
					world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				world.emitGameEvent(null, GameEvent.PRIME_FUSE, pos);
			}
			return ItemStack.EMPTY;
		});
		
		PresentUnpackBehavior POTION_BEHAVIOR = (stack, presentBlockEntity, world, pos, random) -> {
			Vec3d centerPos = Vec3d.ofCenter(pos);
			for (int i = 0; i < stack.getCount(); i++) {
				PotionEntity entity = new PotionEntity(world, centerPos.getX(), centerPos.getY(), centerPos.getZ());
				entity.setItem(stack);
				world.spawnEntity(entity);
			}
			world.syncWorldEvent(WorldEvents.SPLASH_POTION_SPLASHED, pos, PotionUtil.getColor(Potions.WATER));
			return ItemStack.EMPTY;
		};
		PresentBlock.registerBehavior(Items.SPLASH_POTION, POTION_BEHAVIOR);
		PresentBlock.registerBehavior(Items.LINGERING_POTION, POTION_BEHAVIOR);
		
		PresentBlock.registerBehavior(Items.EXPERIENCE_BOTTLE, (stack, presentBlockEntity, world, pos, random) -> {
			int totalXP = 0;
			for (int i = 0; i < stack.getCount(); i++) {
				totalXP += 3 + random.nextInt(5) + random.nextInt(5);
			}
			
			world.syncWorldEvent(WorldEvents.SPLASH_POTION_SPLASHED, pos, PotionUtil.getColor(Potions.WATER));
			ExperienceOrbEntity.spawn(world, Vec3d.ofCenter(pos), totalXP);
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(Items.EGG, (stack, presentBlockEntity, world, pos, random) -> {
			int chickenCount = stack.getCount(); // every egg hatches, unlike via EggEntity. New chicken farm just dropped?
			for (int i = 0; i < chickenCount; i++) {
				ChickenEntity chickenEntity = EntityType.CHICKEN.create(world);
				chickenEntity.setBreedingAge(-24000);
				chickenEntity.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
				world.spawnEntity(chickenEntity);
			}
			
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(SpectrumBlocks.MEMORY, (stack, presentBlockEntity, world, pos, random) -> {
			MemoryBlockEntity.manifest(world, pos, stack, presentBlockEntity.getOpenerUUID());
			return ItemStack.EMPTY;
		});
		
	}
	
	
}
