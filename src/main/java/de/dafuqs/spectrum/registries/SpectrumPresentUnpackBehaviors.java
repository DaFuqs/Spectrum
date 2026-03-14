package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.boom.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class SpectrumPresentUnpackBehaviors {
	
	public static void register() {
		PresentBlock.registerBehavior(SpectrumItems.PIPE_BOMB, (stack, presentBlockEntity, world, pos, random) -> {
			stack.set(SpectrumDataComponentTypes.TIMESTAMP, world.getGameTime() - 70);
			stack.set(DataComponents.PROFILE, presentBlockEntity.getOwner());
			world.playSound(null, pos, SpectrumSoundEvents.INCANDESCENT_ARM, SoundSource.BLOCKS, 2.0F, 0.9F);
			return stack;
		});
		
		PresentBlock.registerBehavior(SpectrumItems.STORM_STONE, (stack, presentBlockEntity, world, pos, random) -> {
			if (world.canSeeSky(pos)) {
				LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.moveTo(Vec3.atBottomCenterOf(pos));
					world.addFreshEntity(lightningEntity);
				}
				return ItemStack.EMPTY;
			}
			return stack;
		});
		
		PresentBlock.registerBehavior(SpectrumBlocks.INCANDESCENT_AMALGAM, (stack, presentBlockEntity, world, pos, random) -> {
			IncandescentAmalgamBlock.explode(world, pos, presentBlockEntity.getOwnerIfOnline(world), stack);
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(Items.FIREWORK_ROCKET, (stack, presentBlockEntity, world, pos, random) -> {
			Vec3 centerPos = Vec3.atLowerCornerOf(pos);
			for (int i = 0; i < stack.getCount(); i++) {
				FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, presentBlockEntity.getOwnerIfOnline(world), centerPos.x + 0.35 + random.nextFloat() * 0.3, centerPos.y + 0.35 + random.nextFloat() * 0.3, centerPos.z + 0.35 + random.nextFloat() * 0.3, stack);
				world.addFreshEntity(fireworkRocketEntity);
			}
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(Items.GOAT_HORN, (stack, presentBlockEntity, world, pos, random) -> {
			Optional<Holder<Instrument>> optional = ((GoatHornItemAccessor) stack.getItem()).invokeGetInstrument(stack);
			if (optional.isPresent()) {
				Instrument instrument = optional.get().value();
				SoundEvent soundEvent = instrument.soundEvent().value();
				world.playSound(null, pos, soundEvent, SoundSource.RECORDS, instrument.range() / 16.0F, 1.0F);
			}
			return stack;
		});
		
		PresentBlock.registerBehavior(Items.BELL, (stack, presentBlockEntity, world, pos, random) -> {
			world.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
			return stack;
		});
		
		PresentBlock.registerBehavior(Items.TNT, (stack, presentBlockEntity, world, pos, random) -> {
			if (stack.getCount() > 0) {
				PrimedTnt tntEntity = null;
				for (int i = 0; i < stack.getCount(); i++) {
					tntEntity = new PrimedTnt(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, presentBlockEntity.getOwnerIfOnline(world));
					world.addFreshEntity(tntEntity);
				}
				world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
				world.gameEvent(null, GameEvent.PRIME_FUSE, pos);
			}
			return ItemStack.EMPTY;
		});
		
		PresentUnpackBehavior POTION_BEHAVIOR = (stack, presentBlockEntity, world, pos, random) -> {
			Vec3 centerPos = Vec3.atCenterOf(pos);
			for (int i = 0; i < stack.getCount(); i++) {
				ThrownPotion entity = new ThrownPotion(world, centerPos.x(), centerPos.y(), centerPos.z());
				entity.setItem(stack);
				world.addFreshEntity(entity);
			}
			var component = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
			world.levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, pos, component.getColor());
			return ItemStack.EMPTY;
		};
		PresentBlock.registerBehavior(Items.SPLASH_POTION, POTION_BEHAVIOR);
		PresentBlock.registerBehavior(Items.LINGERING_POTION, POTION_BEHAVIOR);
		
		PresentBlock.registerBehavior(Items.EXPERIENCE_BOTTLE, (stack, presentBlockEntity, world, pos, random) -> {
			int totalXP = 0;
			for (int i = 0; i < stack.getCount(); i++) {
				totalXP += 3 + random.nextInt(5) + random.nextInt(5);
			}
			
			world.levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, pos, new PotionContents(Potions.WATER).getColor());
			ExperienceOrb.award(world, Vec3.atCenterOf(pos), totalXP);
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(Items.EGG, (stack, presentBlockEntity, world, pos, random) -> {
			int chickenCount = stack.getCount(); // every egg hatches, unlike via EggEntity. New chicken farm just dropped?
			for (int i = 0; i < chickenCount; i++) {
				Chicken chickenEntity = EntityType.CHICKEN.create(world);
				if (chickenEntity != null) {
					chickenEntity.setAge(-24000);
					chickenEntity.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
					world.addFreshEntity(chickenEntity);
				}
			}
			
			return ItemStack.EMPTY;
		});
		
		PresentBlock.registerBehavior(SpectrumBlocks.MEMORY, (stack, presentBlockEntity, world, pos, random) -> {
			MemoryBlockEntity.manifest(world, pos, stack, presentBlockEntity.getOpenerUUID());
			return ItemStack.EMPTY;
		});
		
	}
	
	
}
