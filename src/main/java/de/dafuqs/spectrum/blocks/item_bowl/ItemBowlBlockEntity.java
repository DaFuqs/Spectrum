package de.dafuqs.spectrum.blocks.item_bowl;

import de.dafuqs.spectrum.blocks.InWorldInteractionBlockEntity;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.particle.effect.Transphere;
import de.dafuqs.spectrum.particle.effect.TransphereParticleEffect;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemBowlBlockEntity extends InWorldInteractionBlockEntity {
	
	protected static final int INVENTORY_SIZE = 1;
	
	public ItemBowlBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.ITEM_BOWL, pos, state, INVENTORY_SIZE);
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
					float randomX = 0.1F + world.getRandom().nextFloat() * 0.8F;
					float randomZ = 0.1F + world.getRandom().nextFloat() * 0.8F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY() + 0.75, blockPos.getZ() + randomZ, 0.0D, 0.05D, 0.0D);
				}
			}
		}
	}
	
	public int decrementBowlStack(BlockPos particleTargetBlockPos, int amount, boolean doEffects) {
		ItemStack storedStack = this.inventory.getStack(0);
		if (storedStack.isEmpty()) {
			return 0;
		}
		
		int decrementAmount = Math.min(amount, storedStack.getCount());
		Item recipeRemainderItem = storedStack.getItem().getRecipeRemainder();
		if (recipeRemainderItem != null) {
			if (storedStack.getCount() == 1) {
				inventory.setStack(0, recipeRemainderItem.getDefaultStack());
			} else {
				inventory.getStack(0).decrement(decrementAmount);
				
				ItemStack remainderStack = recipeRemainderItem.getDefaultStack();
				remainderStack.setCount(decrementAmount);
				
				ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, remainderStack);
				itemEntity.addVelocity(0, 0.1, 0);
				world.spawnEntity(itemEntity);
			}
		} else {
			inventory.getStack(0).decrement(decrementAmount);
		}
		
		if (decrementAmount > 0) {
			if (doEffects) {
				spawnSphereParticlesTo(particleTargetBlockPos);
			}
			updateInClientWorld(world, pos);
			markDirty();
		}
		
		return decrementAmount;
	}
	
	public void spawnSphereParticlesTo(BlockPos blockPos) {
		ItemStack storedStack = this.getStack(0);
		if (!storedStack.isEmpty()) {
			Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(storedStack.getItem(), DyeColor.PURPLE);
			if (optionalItemColor.isPresent()) {
				ParticleEffect sparkleRisingParticleEffect = SpectrumParticleTypes.getSparkleRisingParticle(optionalItemColor.get());
				
				if (this.world instanceof ServerWorld serverWorld) {
					SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
							new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
							sparkleRisingParticleEffect, 50,
							new Vec3d(0.4, 0.2, 0.4), new Vec3d(0.06, 0.16, 0.06));
					
					SpectrumS2CPacketSender.playTransphereParticle(serverWorld, new Transphere(this.pos, new BlockPositionSource(blockPos), 20, optionalItemColor.get()));
				} else if (this.world instanceof ClientWorld clientWorld) {
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
					
					ParticleEffect sphereParticleEffect = new TransphereParticleEffect(new Transphere(this.pos, new BlockPositionSource(blockPos), 20, optionalItemColor.get()));
					clientWorld.addParticle(sphereParticleEffect, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, (blockPos.getX() - this.pos.getX()) * 0.045, 0, (blockPos.getZ() - this.pos.getZ()) * 0.045);
				}
				
				world.playSound(null, this.pos, SpectrumSoundEvents.ENCHANTER_DING, SoundCategory.BLOCKS, 1.0F, 0.7F + this.world.random.nextFloat() * 0.6F);
			}
		}
	}
	
}
