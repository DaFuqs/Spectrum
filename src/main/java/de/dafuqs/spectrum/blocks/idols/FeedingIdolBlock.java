package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FeedingIdolBlock extends IdolBlock {
	
	protected static final int LOVE_TICKS = 600;
	protected final int range;
	
	public FeedingIdolBlock(Properties settings, ParticleOptions particleEffect, int range) {
		super(settings, particleEffect);
		this.range = range;
	}

	@Override
	public MapCodec<? extends FeedingIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.feeding_idol.tooltip", this.range));
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		
		// Query entities once and reuse below. Otherwise, it will get computationally expensive
		AABB box = AABB.ofSize(Vec3.atCenterOf(blockPos), boxSize, boxSize, boxSize);
		List<Animal> animalEntities = world.getEntitiesOfClass(Animal.class, box);
		List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, box);
		
		// put grown animals in love
		for (Animal animalEntity : animalEntities) {
			if (animalEntity.getAge() == 0 && !animalEntity.isInLove()) { // getBreedingAge() automatically checks for !isChild()
				for (ItemEntity itemEntity : itemEntities) {
					ItemStack stack = itemEntity.getItem();
					if (animalEntity.isFood(stack)) {
						InWorldInteractionHelper.decrementAndSpawnRemainder(itemEntity, 1);
						
						animalEntity.setInLoveTime(LOVE_TICKS);
						world.broadcastEntityEvent(animalEntity, EntityEvent.IN_LOVE_HEARTS);
					}
				}
			}
		}
		
		// use remaining items to grow up animals
		for (Animal animalEntity : animalEntities) {
			if (animalEntity.isBaby()) {
				for (ItemEntity itemEntity : itemEntities) {
					ItemStack stack = itemEntity.getItem();
					if (animalEntity.isFood(stack)) {
						InWorldInteractionHelper.decrementAndSpawnRemainder(itemEntity, 1);
						
						animalEntity.ageUp((int) ((float) (-animalEntity.getAge() / 20) * 0.1F), true);
						animalEntity.gameEvent(GameEvent.ENTITY_INTERACT);
					}
				}
			}
		}
		
		return true;
	}
	
}
