package de.dafuqs.spectrum.blocks.geology;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

public class ShimmerstoneOreBlock extends CloakedOreBlock {
	
	public static final MapCodec<ShimmerstoneOreBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			IntProvider.codec(0, 10).fieldOf("experience").forGetter(b -> ((ExperienceDroppingBlockAccessor) b).getXpRange()),
			propertiesCodec(),
			ResourceLocation.CODEC.fieldOf("advancement").forGetter(CloakedOreBlock::getCloakAdvancementIdentifier),
			BlockState.CODEC.fieldOf("cloak").forGetter(b -> b.getBlockStateCloaks().get(b.defaultBlockState()))
	).apply(instance, ShimmerstoneOreBlock::new));
	
	public ShimmerstoneOreBlock(IntProvider experienceDropped, Properties settings, ResourceLocation cloakAdvancementIdentifier, BlockState cloakBlockState) {
		super(experienceDropped, settings, cloakAdvancementIdentifier, cloakBlockState);
	}
	
	@Override
	public MapCodec<? extends ShimmerstoneOreBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		var random = world.getRandom();
		if (!world.isClientSide() && !entity.isSteppingCarefully() && random.nextInt(3) == 0) {
			PlayParticleAroundBlockSidesPayload.playParticleAroundBlockSides((ServerLevel) world, 1, pos, new Vec3(0, 0.05, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.UP);
			if (random.nextInt(3) == 0) {
				PlayParticleAroundBlockSidesPayload.playParticleAroundBlockSides((ServerLevel) world, 1, pos, new Vec3(0, 0.025, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.values());
				
			}
		}
		super.stepOn(world, pos, state, entity);
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClientSide()) {
			var random = world.getRandom();
			if (random.nextBoolean()) {
				var amount = (int) Math.ceil(Mth.clamp(fallDistance / 2, 1, 10));
				PlayParticleAroundBlockSidesPayload.playParticleAroundBlockSides((ServerLevel) world, amount, pos, new Vec3(0, 0.05 + amount / 30.0, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.UP);
			}
		}
		super.fallOn(world, state, pos, entity, fallDistance);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		
		if (isVisibleTo(Minecraft.getInstance().player)) {
			ParticleHelper.playParticleAroundBlockSides(world, SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, pos, Direction.values(), 1, new Vec3(0, 0.025, 0));
		}
	}
	
	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide()) {
			PlayParticleAroundBlockSidesPayload.playParticleAroundBlockSides((ServerLevel) world, 3, pos, new Vec3(0, 0.05, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, serverPlayer -> isVisibleTo(serverPlayer), Direction.values());
		}
		return super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		super.attack(state, world, pos, player);
		if (!world.isClientSide()) {
			PlayParticleAroundBlockSidesPayload.playParticleAroundBlockSides((ServerLevel) world, 1, pos, new Vec3(0, 0.01, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, serverPlayer -> isVisibleTo(serverPlayer), Direction.values());
		}
	}
}
