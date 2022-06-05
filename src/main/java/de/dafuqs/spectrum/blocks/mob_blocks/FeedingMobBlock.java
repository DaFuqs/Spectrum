package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FeedingMobBlock extends MobBlock {
	
	protected static final int LOVE_TICKS = 600;
	protected int range;
	
	public FeedingMobBlock(Settings settings, ParticleEffect particleEffect, int range) {
		super(settings, particleEffect);
		this.range = range;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("block.spectrum.feeding_mob_block.tooltip", this.range));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		
		// Query both at once. Otherwise it will get computationally expensive
		List<AnimalEntity> animalEntities = world.getNonSpectatingEntities(AnimalEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		
		// put grown animals in love
		for (AnimalEntity animalEntity : animalEntities) {
			if (animalEntity.getBreedingAge() == 0 && !animalEntity.isInLove()) { // getBreedingAge() automatically checks for isChild (-1)
				for (ItemEntity itemEntity : itemEntities) {
					ItemStack stack = itemEntity.getStack();
					if (animalEntity.isBreedingItem(stack)) {
						stack.decrement(1);
						animalEntity.setLoveTicks(LOVE_TICKS);
						world.sendEntityStatus(animalEntity, (byte) 18);
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
						stack.decrement(1);
						int i = animalEntity.getBreedingAge();
						animalEntity.growUp((int) ((float) (-i / 20) * 0.1F), true);
						animalEntity.emitGameEvent(GameEvent.MOB_INTERACT, animalEntity.getCameraBlockPos());
					}
				}
			}
		}
		
		return true;
	}
	
}
