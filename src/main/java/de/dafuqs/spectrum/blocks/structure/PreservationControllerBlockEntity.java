package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;

import java.util.*;

public class PreservationControllerBlockEntity extends BlockEntity {
	
	private @Nullable Vec3i entranceOffset;
	private @Nullable Vec3i checkRange;
	private @Nullable ResourceLocation requiredAdvancement;
	private @Nullable MobEffect requiredEffect;
	private @Nullable String checkName;
	
	private @Nullable AABB checkBox;
	private @Nullable Vec3i checkBoxOffset;
	private @Nullable BlockPos destinationPos;
	
	private boolean spawnParticles;
	
	public PreservationControllerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_CONTROLLER.get(), pos, state);
	}
	
	public static void serverTick(Level world, BlockPos blockPos, BlockState blockState, PreservationControllerBlockEntity blockEntity) {
		if (world.getGameTime() % 20 == 0 && blockEntity.entranceOffset != null && blockEntity.checkRange != null && blockEntity.requiredAdvancement != null) {
			if (blockEntity.checkBox == null) {
				calculateLocationData(world, blockPos, blockState, blockEntity);
			}
			
			if (blockEntity.spawnParticles) {
				blockEntity.spawnParticles();
			}
			
			if (blockEntity.requiredAdvancement != null) {
				blockEntity.yeetUnworthyPlayersAndGrantAdvancement();
			}
		}
	}
	
	private static void calculateLocationData(Level world, BlockPos blockPos, BlockState blockState, PreservationControllerBlockEntity blockEntity) {
		BlockState state = world.getBlockState(blockPos);
		if (!state.is(SpectrumBlocks.PRESERVATION_CONTROLLER)) {
			return;
		}
		
		Direction facing = state.getValue(PreservationControllerBlock.FACING);
		BlockPos centerPos = blockPos;
		if (blockEntity.checkBoxOffset != null) {
			centerPos = Support.directionalOffset(blockEntity.worldPosition, blockEntity.checkBoxOffset, blockState.getValue(PreservationControllerBlock.FACING));
		}
		if (facing == Direction.NORTH || facing == Direction.SOUTH) {
			blockEntity.checkBox = AABB.ofSize(Vec3.atCenterOf(centerPos), blockEntity.checkRange.getX() * 2, blockEntity.checkRange.getY() * 2, blockEntity.checkRange.getZ() * 2);
		} else {
			blockEntity.checkBox = AABB.ofSize(Vec3.atCenterOf(centerPos), blockEntity.checkRange.getZ() * 2, blockEntity.checkRange.getY() * 2, blockEntity.checkRange.getX() * 2);
		}
		blockEntity.destinationPos = Support.directionalOffset(blockEntity.worldPosition, blockEntity.entranceOffset, blockState.getValue(PreservationControllerBlock.FACING));
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		
		if (this.entranceOffset != null) {
			nbt.putInt("EntranceOffsetX", this.entranceOffset.getX());
			nbt.putInt("EntranceOffsetY", this.entranceOffset.getY());
			nbt.putInt("EntranceOffsetZ", this.entranceOffset.getZ());
		}
		if (this.checkBoxOffset != null) {
			nbt.putInt("CheckBoxOffsetX", this.checkBoxOffset.getX());
			nbt.putInt("CheckBoxOffsetY", this.checkBoxOffset.getY());
			nbt.putInt("CheckBoxOffsetZ", this.checkBoxOffset.getZ());
		}
		if (this.checkRange != null) {
			nbt.putInt("CheckRangeX", this.checkRange.getX());
			nbt.putInt("CheckRangeY", this.checkRange.getY());
			nbt.putInt("CheckRangeZ", this.checkRange.getZ());
		}
		if (this.requiredAdvancement != null) {
			nbt.putString("RequiredAdvancement", this.requiredAdvancement.toString());
		}
		if (this.requiredEffect != null) {
			ResourceLocation effectIdentifier = BuiltInRegistries.MOB_EFFECT.getKey(this.requiredEffect);
			if (effectIdentifier != null) {
				nbt.putString("RequiredStatusEffect", effectIdentifier.toString());
			}
		}
		if (this.checkName != null) {
			nbt.putString("CheckName", this.checkName);
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		
		if (nbt.contains("EntranceOffsetX") && nbt.contains("EntranceOffsetY") && nbt.contains("EntranceOffsetZ")) {
			this.entranceOffset = new Vec3i(nbt.getInt("EntranceOffsetX"), nbt.getInt("EntranceOffsetY"), nbt.getInt("EntranceOffsetZ"));
		}
		if (nbt.contains("CheckBoxOffsetX") && nbt.contains("CheckBoxOffsetY") && nbt.contains("CheckBoxOffsetZ")) {
			this.checkBoxOffset = new Vec3i(nbt.getInt("CheckBoxOffsetX"), nbt.getInt("CheckBoxOffsetY"), nbt.getInt("CheckBoxOffsetZ"));
		}
		if (nbt.contains("CheckRangeX")) {
			this.checkRange = new Vec3i(nbt.getInt("CheckRangeX"), nbt.getInt("CheckRangeY"), nbt.getInt("CheckRangeZ"));
		}
		if (nbt.contains("RequiredStatusEffect", Tag.TAG_STRING)) {
			MobEffect statusEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(nbt.getString("RequiredStatusEffect")));
			if (this.requiredEffect != null) {
				this.requiredEffect = statusEffect;
			}
		}
		if (nbt.contains("RequiredAdvancement", Tag.TAG_STRING)) {
			this.requiredAdvancement = ResourceLocation.parse(nbt.getString("RequiredAdvancement"));
		}
		if (nbt.contains("CheckName", Tag.TAG_STRING)) {
			this.checkName = nbt.getString("CheckName");
		}
		// backwards compatibility with old preservation controller nbt
		if (nbt.contains("UnlockedAdvancement", Tag.TAG_STRING)) {
			ResourceLocation unlockedAdvancement = ResourceLocation.parse(nbt.getString("UnlockedAdvancement"));
			this.checkName = unlockedAdvancement.getPath(); // enter_color_mixing_puzzle_structure, enter_dike_gate_puzzle_structure, enter_wireless_redstone_puzzle_structure
		}
	}
	
	public void spawnParticles() {
		if (spawnParticles) {
			if (checkBox != null) {
				BlockPos centerPos = this.worldPosition;
				if (checkBoxOffset != null) {
					centerPos = Support.directionalOffset(worldPosition, checkBoxOffset, level.getBlockState(worldPosition).getValue(PreservationControllerBlock.FACING));
				}
				PlayParticleWithExactVelocityPayload.playParticles((ServerLevel) level, centerPos, ParticleTypes.FLAME, 1);
				
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) level, Vec3.atCenterOf(centerPos), ParticleTypes.SMOKE, 250,
						new Vec3(checkBox.getXsize() / 2, checkBox.getYsize() / 2, checkBox.getZsize() / 2),
						Vec3.ZERO);
			}
			
			if (destinationPos != null) {
				PlayParticleWithExactVelocityPayload.playParticles((ServerLevel) level, destinationPos, ParticleTypes.END_ROD, 1);
			}
		}
	}
	
	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}
	
	public void toggleParticles() {
		this.spawnParticles = true;
	}
	
	public void openExit() {
		boolean didSomething = false;
		BlockState state = level.getBlockState(worldPosition);
		if (!state.is(SpectrumBlocks.PRESERVATION_CONTROLLER)) {
			return;
		}
		
		Direction facing = state.getValue(PreservationControllerBlock.FACING);
		if (facing == Direction.NORTH || facing == Direction.SOUTH) {
			for (int x = -1; x < 2; x++) {
				for (int y = -3; y < 0; y++) {
					BlockPos offsetPos = worldPosition.offset(x, y, 0);
					BlockState offsetState = level.getBlockState(offsetPos);
					if (offsetState.is(SpectrumBlockTags.UNBREAKABLE_STRUCTURE_BLOCKS)) {
						level.setBlockAndUpdate(offsetPos, SpectrumBlocks.POLISHED_CALCITE.get().defaultBlockState());
						level.globalLevelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, offsetPos, Block.getId(offsetState));
						didSomething = true;
					}
				}
			}
		} else {
			for (int z = -1; z < 2; z++) {
				for (int y = -3; y < 0; y++) {
					BlockPos offsetPos = worldPosition.offset(0, y, z);
					BlockState offsetState = level.getBlockState(offsetPos);
					if (offsetState.is(SpectrumBlockTags.UNBREAKABLE_STRUCTURE_BLOCKS)) {
						level.setBlockAndUpdate(offsetPos, SpectrumBlocks.POLISHED_CALCITE.get().defaultBlockState());
						level.globalLevelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, offsetPos, Block.getId(offsetState));
						didSomething = true;
					}
				}
			}
		}
		
		if (didSomething) {
			level.playSound(null, worldPosition, SpectrumSoundEvents.STRUCTURE_SUCCESS, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	public void yeetPlayer(Player player) {
		if (this.destinationPos != null) {
			player.hurt(SpectrumDamageTypes.dike(player.level()), 1.0F);
			Vec3 vec = Vec3.atCenterOf(destinationPos);
			player.teleportTo(vec.x(), vec.y(), vec.z());
			level.playSound(null, destinationPos, SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}
	
	public void yeetUnworthyPlayersAndGrantAdvancement() {
		if (checkBox != null) {
			List<Player> players = level.getEntities(EntityType.PLAYER, checkBox, LivingEntity::isAlive);
			for (Player playerEntity : players) {
				if (!playerEntity.isCreative() && !playerEntity.isSpectator()) {
					if (this.requiredAdvancement != null && AdvancementHelper.hasAdvancement(playerEntity, requiredAdvancement)) {
						SpectrumAdvancementCriteria.PRESERVATION_CHECK.trigger((ServerPlayer) playerEntity, checkName, true);
					} else {
						// yeet
						SpectrumAdvancementCriteria.PRESERVATION_CHECK.trigger((ServerPlayer) playerEntity, checkName, false);
						yeetPlayer(playerEntity);
					}
				}
			}
		}
	}
	
}
