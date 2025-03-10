package de.dafuqs.spectrum.blocks.item_bowl;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemBowlBlockEntity extends InWorldInteractionBlockEntity {
	
	protected static final int INVENTORY_SIZE = 1;
	
	public ItemBowlBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.ITEM_BOWL, pos, state, INVENTORY_SIZE);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.items);
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.items);
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		this.checkLootInteraction(null);
		return super.toInitialChunkDataNbt();
	}
	
	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, ItemBowlBlockEntity itemBowlBlockEntity) {
		ItemStack storedStack = itemBowlBlockEntity.getStack(0);
		if (!storedStack.isEmpty()) {
			Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(storedStack.getItem());
			if (optionalItemColor.isPresent()) {
				int particleCount = Support.getIntFromDecimalWithChance(Math.max(0.1, (float) storedStack.getCount() / (storedStack.getMaxCount() * 2)), world.random);
				spawnRisingParticles(world, blockPos, storedStack, particleCount);
			}
		}
	}
	
	public static void spawnRisingParticles(World world, BlockPos blockPos, ItemStack itemStack, int amount) {
		if (amount > 0) {
			Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(itemStack.getItem());
			if (optionalItemColor.isPresent()) {
				ParticleEffect particleEffect = SpectrumParticleTypes.getSparkleRisingParticle(optionalItemColor.get());
				
				for (int i = 0; i < amount; i++) {
					float randomX = 0.1F + world.random.nextFloat() * 0.8F;
					float randomZ = 0.1F + world.random.nextFloat() * 0.8F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY() + 0.75, blockPos.getZ() + randomZ, 0.0D, 0.05D, 0.0D);
				}
			}
		}
	}
	
	public int decrementBowlStack(Vec3d orbTargetPos, int amount, boolean doEffects) {
		ItemStack storedStack = this.getStack(0);
		if (storedStack.isEmpty()) {
			return 0;
		}
		
		int decrementAmount = Math.min(amount, storedStack.getCount());
		ItemStack remainder = storedStack.getRecipeRemainder();
		if (!remainder.isEmpty()) {
			if (storedStack.getCount() == 1) {
				setStack(0, remainder);
			} else {
				getStack(0).decrement(decrementAmount);
				remainder.setCount(decrementAmount);
				
				ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, remainder);
				itemEntity.addVelocity(0, 0.1, 0);
				world.spawnEntity(itemEntity);
			}
		} else {
			getStack(0).decrement(decrementAmount);
		}
		
		if (decrementAmount > 0) {
			if (doEffects) {
				spawnOrbParticles(orbTargetPos);
			}
			updateInClientWorld();
			markDirty();
		}
		
		return decrementAmount;
	}
	
	public void spawnOrbParticles(Vec3d orbTargetPos) {
		ItemStack storedStack = this.getStack(0);
		if (!storedStack.isEmpty()) {
			Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(storedStack.getItem(), DyeColor.PURPLE);
			if (optionalItemColor.isPresent()) {
				ParticleEffect sparkleRisingParticleEffect = SpectrumParticleTypes.getSparkleRisingParticle(optionalItemColor.get());
				
				if (this.getWorld() instanceof ServerWorld serverWorld) {
					SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
							new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
							sparkleRisingParticleEffect, 50,
							new Vec3d(0.4, 0.2, 0.4), new Vec3d(0.06, 0.16, 0.06));
					
					SpectrumS2CPacketSender.playColorTransmissionParticle(serverWorld, new ColoredTransmission(new Vec3d(this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D), new ExactPositionSource(orbTargetPos), 20, optionalItemColor.get()));
				} else if (this.getWorld() instanceof ClientWorld clientWorld) {
					for (int i = 0; i < 50; i++) {
						float randomOffsetX = pos.getX() + 0.3F + world.random.nextFloat() * 0.6F;
						float randomOffsetY = pos.getY() + 0.3F + world.random.nextFloat() * 0.6F;
						float randomOffsetZ = pos.getZ() + 0.3F + world.random.nextFloat() * 0.6F;
						float randomVelocityX = 0.03F - world.random.nextFloat() * 0.06F;
						float randomVelocityY = world.random.nextFloat() * 0.16F;
						float randomVelocityZ = 0.03F - world.random.nextFloat() * 0.06F;
						
						clientWorld.addParticle(sparkleRisingParticleEffect,
								randomOffsetX, randomOffsetY, randomOffsetZ,
								randomVelocityX, randomVelocityY, randomVelocityZ);
					}
					
					ParticleEffect sphereParticleEffect = new ColoredTransmissionParticleEffect(new ExactPositionSource(orbTargetPos), 20, optionalItemColor.get());
					clientWorld.addParticle(sphereParticleEffect, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, (orbTargetPos.getX() - this.pos.getX()) * 0.045, 0, (orbTargetPos.getZ() - this.pos.getZ()) * 0.045);
				}
				
				world.playSound(null, this.pos, SpectrumSoundEvents.CRAFTING_DING, SoundCategory.BLOCKS, SpectrumCommon.CONFIG.BlockSoundVolume, 0.7F + world.random.nextFloat() * 0.6F);
			}
		}
	}
	
}
