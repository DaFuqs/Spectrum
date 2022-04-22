package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PreservationControllerBlockEntity extends BlockEntity {
	
	private Vec3i entranceOffset;
	private Vec3i checkRange;
	private Identifier requiredAdvancement;
	private StatusEffect requiredEffect;
	
	private Identifier unlockedAdvancement;
	private String unlockedAdvancementCriterion;
	
	private Box checkBox;
	private BlockPos destinationPos;
	
	private boolean spawnParticles;
	
	public PreservationControllerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.PRESERVATION_CONTROLLER, pos, state);
	}
	
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		
		if(this.entranceOffset !=  null) {
			tag.putInt("EntranceOffsetX", this.entranceOffset.getX());
			tag.putInt("EntranceOffsetY", this.entranceOffset.getY());
			tag.putInt("EntranceOffsetZ", this.entranceOffset.getZ());
		}
		if(this.checkRange !=  null) {
			tag.putInt("CheckRangeX", this.checkRange.getX());
			tag.putInt("CheckRangeY", this.checkRange.getY());
			tag.putInt("CheckRangeZ", this.checkRange.getZ());
		}
		if(this.requiredAdvancement != null) {
			tag.putString("RequiredAdvancement", this.requiredAdvancement.toString());
		}
		if(this.requiredEffect != null) {
			Identifier effectIdentifier = Registry.STATUS_EFFECT.getId(this.requiredEffect);
			if(effectIdentifier != null) {
				tag.putString("RequiredStatusEffect", effectIdentifier.toString());
			}
		}
		if(this.unlockedAdvancement != null && this.unlockedAdvancementCriterion != null) {
			tag.putString("UnlockedAdvancement", this.unlockedAdvancement.toString());
			tag.putString("UnlockedAdvancementCriterion", this.unlockedAdvancementCriterion);
		}
	}
	
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		
		if(tag.contains("EntranceOffsetX")) {
			this.entranceOffset = new Vec3i(tag.getInt("EntranceOffsetX"), tag.getInt("EntranceOffsetY"), tag.getInt("EntranceOffsetZ"));
		}
		if(tag.contains("CheckRangeX")) {
			this.checkRange = new Vec3i(tag.getInt("CheckRangeX"), tag.getInt("CheckRangeY"), tag.getInt("CheckRangeZ"));
		}
		if(tag.contains("RequiredStatusEffect", NbtElement.STRING_TYPE)) {
			StatusEffect statusEffect = Registry.STATUS_EFFECT.get(new Identifier(tag.getString("RequiredStatusEffect")));
			if(this.requiredEffect != null) {
				this.requiredEffect = statusEffect;
			}
		}
		if(tag.contains("RequiredAdvancement", NbtElement.STRING_TYPE)) {
			this.requiredAdvancement = new Identifier(tag.getString("RequiredAdvancement"));
		} else {
			this.checkRange = new Vec3i(6, 6,11);
			this.entranceOffset = new Vec3i(0, -4, -14);
			this.requiredAdvancement = new Identifier(SpectrumCommon.MOD_ID, "midgame/get_azure_dike_charge");
			this.unlockedAdvancement = new Identifier(SpectrumCommon.MOD_ID, "enter_ancient_ruins");
			this.unlockedAdvancementCriterion = "code_triggered";
		}
		if(tag.contains("UnlockedAdvancement", NbtElement.STRING_TYPE) && tag.contains("UnlockedAdvancementCriterion", NbtElement.STRING_TYPE)) {
			this.unlockedAdvancement = Identifier.tryParse(tag.getString("UnlockedAdvancement"));
			this.unlockedAdvancementCriterion = tag.getString("UnlockedAdvancementCriterion");
		}
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, PreservationControllerBlockEntity blockEntity) {
		if(world.getTime() % 20 == 0 && blockEntity.entranceOffset != null && blockEntity.checkRange != null && blockEntity.requiredAdvancement != null) {
			if(blockEntity.checkBox == null) {
				calculateLocationData(blockPos, blockState, blockEntity);
			}
			
			if(blockEntity.spawnParticles) {
				blockEntity.spawnParticles();
			}
			
			if(blockEntity.requiredAdvancement != null) {
				blockEntity.yeetUnworthyPlayersAndGrantAdvancement();
			}
		}
	}
	
	private static void calculateLocationData(BlockPos blockPos, @NotNull BlockState blockState, @NotNull PreservationControllerBlockEntity blockEntity) {
		blockEntity.checkBox = Box.of(Vec3d.ofCenter(blockPos), blockEntity.checkRange.getX() * 2, blockEntity.checkRange.getY() * 2, blockEntity.checkRange.getZ() * 2);
		switch (blockState.get(PreservationControllerBlock.FACING)) {
			case NORTH -> {
				blockEntity.destinationPos = blockEntity.pos.add(blockEntity.entranceOffset.getX(), blockEntity.entranceOffset.getY(), blockEntity.entranceOffset.getZ());
			}
			case EAST -> {
				blockEntity.destinationPos = blockEntity.pos.add(blockEntity.entranceOffset.getZ(), blockEntity.entranceOffset.getY(), blockEntity.entranceOffset.getX());
			}
			case SOUTH -> {
				blockEntity.destinationPos = blockEntity.pos.add(-blockEntity.entranceOffset.getX(), blockEntity.entranceOffset.getY(), blockEntity.entranceOffset.getZ());
			}
			default -> {
				blockEntity.destinationPos = blockEntity.pos.add(-blockEntity.entranceOffset.getZ(), blockEntity.entranceOffset.getY(), blockEntity.entranceOffset.getX());
			}
		}
	}
	
	public void spawnParticles() {
		if(spawnParticles && checkBox != null && destinationPos != null) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, Vec3d.ofCenter(pos), ParticleTypes.FLAME, 250,
					new Vec3d(checkBox.getXLength(), checkBox.getYLength(), checkBox.getZLength()),
					new Vec3d(0, 0, 0));
		}
	}
	
	public void toggleParticles() {
		this.spawnParticles = true;
	}
	
	public void openExit() {
		boolean didSomething = false;
		Direction facing = world.getBlockState(pos).get(PreservationControllerBlock.FACING);
		if(facing.getAxis().isHorizontal()) {
			for(int x = -1; x < 2; x++) {
				for(int y = -3; y < 0; y++) {
					BlockPos offsetPos = pos.add(x, y, 0);
					BlockState offsetState = world.getBlockState(offsetPos);
					if(offsetState.isIn(SpectrumBlockTags.UNBREAKABLE_STRUCTURE_BLOCKS)) {
						world.setBlockState(offsetPos, SpectrumBlocks.POLISHED_CALCITE.getDefaultState());
						didSomething = true;
					}
				}
			}
		} else {
			for(int z = -1; z < 2; z++) {
				for(int y = -3; y < 0; y++) {
					BlockPos offsetPos = pos.add(0, y, z);
					BlockState offsetState = world.getBlockState(offsetPos);
					if(offsetState.isIn(SpectrumBlockTags.UNBREAKABLE_STRUCTURE_BLOCKS)) {
						world.setBlockState(offsetPos, SpectrumBlocks.POLISHED_CALCITE.getDefaultState());
						didSomething = true;
					}
				}
			}
		}
		
		if(didSomething) {
			world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	public void yeetPlayer(@NotNull PlayerEntity player) {
		if(this.destinationPos != null) {
			//playerEntity.damage(DamageSource.MAGIC, 5.0F);
			Vec3d vec = Vec3d.ofCenter(destinationPos);
			player.requestTeleport(vec.getX(), vec.getY(), vec.getZ());
			world.playSound(null, destinationPos, SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
	}
	
	public void yeetUnworthyPlayersAndGrantAdvancement() {
		if(checkBox != null) {
			List<PlayerEntity> players = world.getEntitiesByType(EntityType.PLAYER, checkBox, LivingEntity::isAlive);
			for (PlayerEntity playerEntity : players) {
				if (playerEntity.isCreative() || playerEntity.isSpectator()) {
					// fine
				} else if(this.requiredAdvancement != null && Support.hasAdvancement(playerEntity, requiredAdvancement)) {
					Support.grantAdvancementCriterion((ServerPlayerEntity) playerEntity, unlockedAdvancement, unlockedAdvancementCriterion);
				} else if(this.requiredEffect != null && playerEntity.hasStatusEffect(this.requiredEffect)) {
					Support.grantAdvancementCriterion((ServerPlayerEntity) playerEntity, unlockedAdvancement, unlockedAdvancementCriterion);
				} else {
					// yeet
					yeetPlayer(playerEntity);
				}
			}
		}
	}
	
}
