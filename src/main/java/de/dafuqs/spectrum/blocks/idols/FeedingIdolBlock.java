package de.dafuqs.spectrum.blocks.idols;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FeedingIdolBlock extends IdolBlock {
	
	protected static final int LOVE_TICKS = 600;
	protected final int range;
	
	public FeedingIdolBlock(Settings settings, ParticleEffect particleEffect, int range) {
		super(settings, particleEffect);
		this.range = range;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.feeding_idol.tooltip", this.range));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		
		// Query entities once and reuse below. Otherwise, it will get computationally expensive
		Box box = Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize);
		List<AnimalEntity> animalEntities = world.getNonSpectatingEntities(AnimalEntity.class, box);
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, box);
		
		// put grown animals in love
		for (AnimalEntity animalEntity : animalEntities) {
			if (animalEntity.getBreedingAge() == 0 && !animalEntity.isInLove()) { // getBreedingAge() automatically checks for !isChild()
				for (ItemEntity itemEntity : itemEntities) {
					ItemStack stack = itemEntity.getStack();
					if (animalEntity.isBreedingItem(stack)) {
						InWorldInteractionHelper.decrementAndSpawnRemainder(itemEntity, 1);
						
						animalEntity.setLoveTicks(LOVE_TICKS);
						world.sendEntityStatus(animalEntity, EntityStatuses.ADD_BREEDING_PARTICLES);
					}
				}
			}
		}
		
		// use remaining items to grow up animals
		for (AnimalEntity animalEntity : animalEntities) {
			if (animalEntity.isBaby()) {
				for (ItemEntity itemEntity : itemEntities) {
					ItemStack stack = itemEntity.getStack();
					if (animalEntity.isBreedingItem(stack)) {
						InWorldInteractionHelper.decrementAndSpawnRemainder(itemEntity, 1);
						
						animalEntity.growUp((int) ((float) (-animalEntity.getBreedingAge() / 20) * 0.1F), true);
						animalEntity.emitGameEvent(GameEvent.ENTITY_INTERACT);
					}
				}
			}
		}
		
		return true;
	}
	
}
