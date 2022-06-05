package de.dafuqs.spectrum.blocks.mob_blocks;

import de.dafuqs.spectrum.mixin.accessors.MooshroomEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MilkingMobBlock extends MobBlock {
	
	protected static final int BUCKET_SEARCH_RANGE = 7;
	protected int milkingRange;
	
	public MilkingMobBlock(Settings settings, ParticleEffect particleEffect, int milkingRange) {
		super(settings, particleEffect);
		this.milkingRange = milkingRange;
	}
	
	@Override
	public boolean trigger(@NotNull ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = milkingRange + milkingRange;
		
		// Goats
		List<GoatEntity> goatEntities = world.getNonSpectatingEntities(GoatEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		for (GoatEntity goatEntity : goatEntities) {
			if (!goatEntity.isBaby()) {
				boolean emptyBucketFound = findAndDecreaseClosestItemEntityOfItem(world, goatEntity, Items.BUCKET, BUCKET_SEARCH_RANGE);
				if (emptyBucketFound) {
					SoundEvent soundEvent = goatEntity.isScreaming() ? SoundEvents.ENTITY_GOAT_SCREAMING_MILK : SoundEvents.ENTITY_GOAT_MILK;
					world.playSound(null, goatEntity.getBlockPos(), soundEvent, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					spawnItemStackAtEntity(world, goatEntity, Items.MILK_BUCKET.getDefaultStack());
				}
			}
		}
		
		// Cows
		List<CowEntity> cowEntities = world.getNonSpectatingEntities(CowEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		for (CowEntity cowEntity : cowEntities) {
			if (!cowEntity.isBaby()) {
				boolean emptyBucketFound = findAndDecreaseClosestItemEntityOfItem(world, cowEntity, Items.BUCKET, BUCKET_SEARCH_RANGE);
				if (emptyBucketFound) {
					world.playSound(null, cowEntity.getBlockPos(), SoundEvents.ENTITY_COW_MILK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					spawnItemStackAtEntity(world, cowEntity, Items.MILK_BUCKET.getDefaultStack());
				}
			}
		}
		
		// Mooshrooms (Mooshroom Stew / Suspicious Stew)
		List<MooshroomEntity> mooshroomEntities = world.getNonSpectatingEntities(MooshroomEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		for (MooshroomEntity mooshroomEntity : mooshroomEntities) {
			if (!mooshroomEntity.isBaby()) {
				boolean emptyBowlFound = findAndDecreaseClosestItemEntityOfItem(world, mooshroomEntity, Items.BOWL, BUCKET_SEARCH_RANGE);
				if (emptyBowlFound) {
					MooshroomEntityAccessor accessor = (MooshroomEntityAccessor) mooshroomEntity;
					
					SoundEvent soundEvent;
					ItemStack resultStack;
					if (accessor.getStewEffect() != null) {
						resultStack = new ItemStack(Items.SUSPICIOUS_STEW);
						SuspiciousStewItem.addEffectToStew(resultStack, accessor.getStewEffect(), accessor.getStewEffectDuration());
						accessor.setStewEffect(null);
						accessor.setStewEffectDuration(0);
						soundEvent = SoundEvents.ENTITY_MOOSHROOM_SUSPICIOUS_MILK;
					} else {
						resultStack = new ItemStack(Items.MUSHROOM_STEW);
						soundEvent = SoundEvents.ENTITY_MOOSHROOM_MILK;
					}
					
					world.playSound(null, mooshroomEntity.getBlockPos(), soundEvent, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					spawnItemStackAtEntity(world, mooshroomEntity, resultStack);
				}
			}
		}
		return true;
	}
	
	private void spawnItemStackAtEntity(ServerWorld world, @NotNull LivingEntity livingEntity, ItemStack itemStack) {
		ItemEntity itemEntity = new ItemEntity(world, livingEntity.getPos().getX(), livingEntity.getPos().getY() + 0.5, livingEntity.getPos().getZ(), itemStack);
		itemEntity.addVelocity(0, -0.2F, 0);
		world.spawnEntity(itemEntity);
	}
	
	public boolean findAndDecreaseClosestItemEntityOfItem(@NotNull ServerWorld world, @NotNull Entity entity, Item item, int range) {
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, Box.of(entity.getPos(), range, range, range));
		for (ItemEntity itemEntity : itemEntities) {
			ItemStack stack = itemEntity.getStack();
			if (stack.isOf(item)) {
				stack.decrement(1);
				if (stack.isEmpty()) {
					itemEntity.discard();
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("block.spectrum.milking_mob_block.tooltip", this.milkingRange));
		tooltip.add(new TranslatableText("block.spectrum.milking_mob_block.tooltip2"));
	}
	
}
