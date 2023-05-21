package de.dafuqs.spectrum.blocks.mob_blocks;

import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SlimeSizingMobBlock extends MobBlock {
	
	protected final int maxSize; // Huge Chungus
	protected final int range;
	
	public SlimeSizingMobBlock(Settings settings, ParticleEffect particleEffect, int range, int maxSize) {
		super(settings, particleEffect);
		this.range = range;
		this.maxSize = maxSize;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.slime_sizing_mob_block.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		List<SlimeEntity> slimeEntities = world.getNonSpectatingEntities(SlimeEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		for (SlimeEntity slimeEntity : slimeEntities) {
			if (slimeEntity.getSize() < maxSize) {
				int newSize = slimeEntity.getSize() + 1;
				// make bigger
				((SlimeEntityAccessor) slimeEntity).invokeSetSize(newSize, true);
				
				// play particles and sound
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, Vec3d.ofCenter(blockPos), ((SlimeEntityAccessor) slimeEntity).invokeGetParticles(), 16, new Vec3d(0.75, 0.75, 0.75), new Vec3d(0.1, 0.1, 0.1));
				
				Box boundingBox = slimeEntity.getBoundingBox();
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, slimeEntity.getPos().add(0, boundingBox.getYLength() / 2, 0), ((SlimeEntityAccessor) slimeEntity).invokeGetParticles(), newSize * 8, new Vec3d(boundingBox.getXLength(), boundingBox.getYLength(), boundingBox.getZLength()), new Vec3d(0.1, 0.1, 0.1));
				slimeEntity.playSound(((SlimeEntityAccessor) slimeEntity).invokeGetSquishSound(), ((SlimeEntityAccessor) slimeEntity).invokeGetSoundVolume(), ((world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
				
				// grant advancements
				if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
					SpectrumAdvancementCriteria.SLIME_SIZING.trigger(serverPlayerEntity, newSize);
				}
				return true;
			}
		}
		return true;
	}
	
}
