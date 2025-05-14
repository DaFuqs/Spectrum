package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.goat.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.InWorldInteractionHelper.*;

public class MilkingIdolBlock extends IdolBlock {
	
	protected static final int BUCKET_SEARCH_RANGE = 7;
	protected final int milkingRange;
	
	public MilkingIdolBlock(Properties settings, ParticleOptions particleEffect, int milkingRange) {
		super(settings, particleEffect);
		this.milkingRange = milkingRange;
	}

	@Override
	public MapCodec<? extends MilkingIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public boolean trigger(@NotNull ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = milkingRange + milkingRange;
		
		// Goats
		List<Goat> goatEntities = world.getEntitiesOfClass(Goat.class, AABB.ofSize(Vec3.atCenterOf(blockPos), boxSize, boxSize, boxSize));
		for (Goat goatEntity : goatEntities) {
			if (!goatEntity.isBaby()) {
				boolean emptyBucketFound = findAndDecreaseClosestItemEntityOfItem(world, goatEntity.position(), Items.BUCKET, BUCKET_SEARCH_RANGE);
				if (emptyBucketFound) {
					SoundEvent soundEvent = goatEntity.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_MILK : SoundEvents.GOAT_MILK;
					world.playSound(null, goatEntity.blockPosition(), soundEvent, SoundSource.NEUTRAL, 1.0F, 1.0F);
					spawnItemStackAtEntity(world, goatEntity, Items.MILK_BUCKET.getDefaultInstance());
				}
			}
		}

		// Cows (includes Mooshrooms)
		List<Cow> cowEntities = world.getEntitiesOfClass(Cow.class, AABB.ofSize(Vec3.atCenterOf(blockPos), boxSize, boxSize, boxSize));
		for (Cow cowEntity : cowEntities) {
			if (!cowEntity.isBaby()) {
				boolean emptyBucketFound = findAndDecreaseClosestItemEntityOfItem(world, cowEntity.position(), Items.BUCKET, BUCKET_SEARCH_RANGE);
				if (emptyBucketFound) {
					world.playSound(null, cowEntity.blockPosition(), SoundEvents.COW_MILK, SoundSource.NEUTRAL, 1.0F, 1.0F);
					spawnItemStackAtEntity(world, cowEntity, Items.MILK_BUCKET.getDefaultInstance());
				}
			}
		}
		
		// Egg Laying Wooly Pigs
		List<EggLayingWoolyPigEntity> eggLayingWoolyPigEntities = world.getEntitiesOfClass(EggLayingWoolyPigEntity.class, AABB.ofSize(Vec3.atCenterOf(blockPos), boxSize, boxSize, boxSize));
		for (EggLayingWoolyPigEntity eggLayingWoolyPigEntity : eggLayingWoolyPigEntities) {
			if (!eggLayingWoolyPigEntity.isBaby()) {
				boolean emptyBucketFound = findAndDecreaseClosestItemEntityOfItem(world, eggLayingWoolyPigEntity.position(), Items.BUCKET, BUCKET_SEARCH_RANGE);
				if (emptyBucketFound) {
					world.playSound(null, eggLayingWoolyPigEntity.blockPosition(), SoundEvents.COW_MILK, SoundSource.NEUTRAL, 1.0F, 1.0F);
					spawnItemStackAtEntity(world, eggLayingWoolyPigEntity, Items.MILK_BUCKET.getDefaultInstance());
				}
			}
		}
		
		// Mooshrooms (Mooshroom Stew / Suspicious Stew)
		List<MushroomCow> mooshroomEntities = world.getEntitiesOfClass(MushroomCow.class, AABB.ofSize(Vec3.atCenterOf(blockPos), boxSize, boxSize, boxSize));
		for (MushroomCow mooshroomEntity : mooshroomEntities) {
			if (!mooshroomEntity.isBaby()) {
				boolean emptyBowlFound = findAndDecreaseClosestItemEntityOfItem(world, mooshroomEntity.position(), Items.BOWL, BUCKET_SEARCH_RANGE);
				if (emptyBowlFound) {
					MooshroomEntityAccessor accessor = (MooshroomEntityAccessor) mooshroomEntity;
					
					SoundEvent soundEvent;
					ItemStack resultStack;
					if (accessor.getStewEffects() != null) {
						resultStack = new ItemStack(Items.SUSPICIOUS_STEW);
						resultStack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, accessor.getStewEffects());
						accessor.setStewEffects(null);
						soundEvent = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
					} else {
						resultStack = new ItemStack(Items.MUSHROOM_STEW);
						soundEvent = SoundEvents.MOOSHROOM_MILK;
					}
					
					world.playSound(null, mooshroomEntity.blockPosition(), soundEvent, SoundSource.NEUTRAL, 1.0F, 1.0F);
					spawnItemStackAtEntity(world, mooshroomEntity, resultStack);
				}
			}
		}
		return true;
	}
	
	private void spawnItemStackAtEntity(ServerLevel world, @NotNull LivingEntity livingEntity, ItemStack itemStack) {
		ItemEntity itemEntity = new ItemEntity(world, livingEntity.position().x(), livingEntity.position().y() + 0.5, livingEntity.position().z(), itemStack);
		itemEntity.push(0, -0.2F, 0);
		world.addFreshEntity(itemEntity);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.milking_idol.tooltip", this.milkingRange));
		tooltip.add(Component.translatable("block.spectrum.milking_idol.tooltip2"));
	}
	
}
