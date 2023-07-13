package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PreservationControllerBlockEntity extends BlockEntity {
	
	private Vec3i entranceOffset;
	private Vec3i checkRange;
	private Identifier requiredAdvancement;
	private StatusEffect requiredEffect;
	private String checkName;
	
	private Box checkBox;
	private Vec3i checkBoxOffset;
	private BlockPos destinationPos;
	
	private boolean spawnParticles;
	
	public PreservationControllerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_CONTROLLER, pos, state);
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PreservationControllerBlockEntity blockEntity) {
		if (world.getTime() % 20 == 0 && blockEntity.entranceOffset != null && blockEntity.checkRange != null && blockEntity.requiredAdvancement != null) {
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
	
	private static void calculateLocationData(World world, BlockPos blockPos, @NotNull BlockState blockState, @NotNull PreservationControllerBlockEntity blockEntity) {
		BlockState state = world.getBlockState(blockPos);
		if(!state.isOf(SpectrumBlocks.PRESERVATION_CONTROLLER)) {
			return;
		}
		
		Direction facing = state.get(PreservationControllerBlock.FACING);
		BlockPos centerPos = blockPos;
		if (blockEntity.checkBoxOffset != null) {
			centerPos = Support.directionalOffset(blockEntity.pos, blockEntity.checkBoxOffset, blockState.get(PreservationControllerBlock.FACING));
		}
		if (facing == Direction.NORTH || facing == Direction.SOUTH) {
			blockEntity.checkBox = Box.of(Vec3d.ofCenter(centerPos), blockEntity.checkRange.getX() * 2, blockEntity.checkRange.getY() * 2, blockEntity.checkRange.getZ() * 2);
		} else {
			blockEntity.checkBox = Box.of(Vec3d.ofCenter(centerPos), blockEntity.checkRange.getZ() * 2, blockEntity.checkRange.getY() * 2, blockEntity.checkRange.getX() * 2);
		}
		blockEntity.destinationPos = Support.directionalOffset(blockEntity.pos, blockEntity.entranceOffset, blockState.get(PreservationControllerBlock.FACING));
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		
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
			Identifier effectIdentifier = Registries.STATUS_EFFECT.getId(this.requiredEffect);
			if (effectIdentifier != null) {
				nbt.putString("RequiredStatusEffect", effectIdentifier.toString());
			}
		}
		if (this.checkName != null) {
			nbt.putString("CheckName", this.checkName);
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		if (nbt.contains("EntranceOffsetX") && nbt.contains("EntranceOffsetY") && nbt.contains("EntranceOffsetZ")) {
			this.entranceOffset = new Vec3i(nbt.getInt("EntranceOffsetX"), nbt.getInt("EntranceOffsetY"), nbt.getInt("EntranceOffsetZ"));
		}
		if (nbt.contains("CheckBoxOffsetX") && nbt.contains("CheckBoxOffsetY") && nbt.contains("CheckBoxOffsetZ")) {
			this.checkBoxOffset = new Vec3i(nbt.getInt("CheckBoxOffsetX"), nbt.getInt("CheckBoxOffsetY"), nbt.getInt("CheckBoxOffsetZ"));
		}
		if (nbt.contains("CheckRangeX")) {
			this.checkRange = new Vec3i(nbt.getInt("CheckRangeX"), nbt.getInt("CheckRangeY"), nbt.getInt("CheckRangeZ"));
		}
		if (nbt.contains("RequiredStatusEffect", NbtElement.STRING_TYPE)) {
			StatusEffect statusEffect = Registries.STATUS_EFFECT.get(new Identifier(nbt.getString("RequiredStatusEffect")));
			if (this.requiredEffect != null) {
				this.requiredEffect = statusEffect;
			}
		}
		if (nbt.contains("RequiredAdvancement", NbtElement.STRING_TYPE)) {
			this.requiredAdvancement = new Identifier(nbt.getString("RequiredAdvancement"));
		}
		if (nbt.contains("CheckName", NbtElement.STRING_TYPE)) {
			this.checkName = nbt.getString("CheckName");
		}
		// backwards compatibility with old preservation controller nbt
		if (nbt.contains("UnlockedAdvancement", NbtElement.STRING_TYPE)) {
			Identifier unlockedAdvancement = new Identifier(nbt.getString("UnlockedAdvancement"));
			this.checkName = unlockedAdvancement.getPath(); // enter_color_mixing_puzzle_structure, enter_dike_gate_puzzle_structure, enter_wireless_redstone_puzzle_structure
		}
	}
	
	public void spawnParticles() {
		if (spawnParticles) {
			if (checkBox != null) {
                BlockPos centerPos = this.pos;
                if (checkBoxOffset != null) {
                    centerPos = Support.directionalOffset(pos, checkBoxOffset, world.getBlockState(pos).get(PreservationControllerBlock.FACING));
                }
                SpectrumS2CPacketSender.playParticles((ServerWorld) world, centerPos, ParticleTypes.FLAME, 1);

				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, Vec3d.ofCenter(centerPos), ParticleTypes.SMOKE, 250,
						new Vec3d(checkBox.getXLength() / 2, checkBox.getYLength() / 2, checkBox.getZLength() / 2),
						Vec3d.ZERO);
			}
			
			if (destinationPos != null) {
				SpectrumS2CPacketSender.playParticles((ServerWorld) world, destinationPos, ParticleTypes.END_ROD, 1);
			}
		}
	}
	
	@Override
	public boolean copyItemDataRequiresOperator() {
		return true;
	}
	
	public void toggleParticles() {
		this.spawnParticles = true;
	}
	
	public void openExit() {
		boolean didSomething = false;
		BlockState state = world.getBlockState(pos);
		if (!state.isOf(SpectrumBlocks.PRESERVATION_CONTROLLER)) {
			return;
		}
		
		Direction facing = state.get(PreservationControllerBlock.FACING);
		if (facing == Direction.NORTH || facing == Direction.SOUTH) {
			for (int x = -1; x < 2; x++) {
				for (int y = -3; y < 0; y++) {
					BlockPos offsetPos = pos.add(x, y, 0);
					BlockState offsetState = world.getBlockState(offsetPos);
					if (offsetState.isIn(SpectrumBlockTags.UNBREAKABLE_STRUCTURE_BLOCKS)) {
						world.setBlockState(offsetPos, SpectrumBlocks.POLISHED_CALCITE.getDefaultState());
						world.syncGlobalEvent(WorldEvents.BLOCK_BROKEN, offsetPos, Block.getRawIdFromState(offsetState));
						didSomething = true;
					}
				}
			}
		} else {
			for (int z = -1; z < 2; z++) {
				for (int y = -3; y < 0; y++) {
					BlockPos offsetPos = pos.add(0, y, z);
					BlockState offsetState = world.getBlockState(offsetPos);
					if (offsetState.isIn(SpectrumBlockTags.UNBREAKABLE_STRUCTURE_BLOCKS)) {
						world.setBlockState(offsetPos, SpectrumBlocks.POLISHED_CALCITE.getDefaultState());
						world.syncGlobalEvent(WorldEvents.BLOCK_BROKEN, offsetPos, Block.getRawIdFromState(offsetState));
						didSomething = true;
					}
				}
			}
		}
		
		if (didSomething) {
			world.playSound(null, pos, SpectrumSoundEvents.STRUCTURE_SUCCESS, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	public void yeetPlayer(@NotNull PlayerEntity player) {
		if (this.destinationPos != null) {
			player.damage(SpectrumDamageSources.dike(player.world), 1.0F);
			Vec3d vec = Vec3d.ofCenter(destinationPos);
			player.requestTeleport(vec.getX(), vec.getY(), vec.getZ());
			world.playSound(null, destinationPos, SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
	}
	
	public void yeetUnworthyPlayersAndGrantAdvancement() {
		if (checkBox != null) {
			List<PlayerEntity> players = world.getEntitiesByType(EntityType.PLAYER, checkBox, LivingEntity::isAlive);
			for (PlayerEntity playerEntity : players) {
				if (playerEntity.isCreative() || playerEntity.isSpectator()) {
					// fine
				} else if (this.requiredAdvancement != null && AdvancementHelper.hasAdvancement(playerEntity, requiredAdvancement)) {
					SpectrumAdvancementCriteria.PRESERVATION_CHECK.trigger((ServerPlayerEntity) playerEntity, checkName, true);
				} else {
					// yeet
					SpectrumAdvancementCriteria.PRESERVATION_CHECK.trigger((ServerPlayerEntity) playerEntity, checkName, false);
					yeetPlayer(playerEntity);
				}
			}
		}
	}
	
}
