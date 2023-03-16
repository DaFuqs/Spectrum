package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OminousSaplingBlock extends PlantBlock implements BlockEntityProvider {
	
	public OminousSaplingBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			OminousSaplingBlockEntity ominousSaplingBlockEntity = getBlockEntity(world, pos);
			if (ominousSaplingBlockEntity != null) {
				SpectrumS2CPacketSender.sendHudMessage((ServerPlayerEntity) player, Text.of("Sapling owner UUID: " + ominousSaplingBlockEntity.getOwnerUUID()), false);
			} else {
				SpectrumS2CPacketSender.sendHudMessage((ServerPlayerEntity) player, Text.of("Sapling block entity putt :("), false);
			}
		}
		return ActionResult.SUCCESS;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.getLightLevel(pos.up()) >= 9 && random.nextInt(2) == 0) {
			this.generateOminousTree(world, pos, state, random);
		}
	}
	
	private void generateOminousTree(ServerWorld world, BlockPos pos, BlockState state, Random random) {
		OminousSaplingBlockEntity ominousSaplingBlockEntity = getBlockEntity(world, pos);
		if (ominousSaplingBlockEntity != null) {
			UUID ownerUUID = ominousSaplingBlockEntity.getOwnerUUID();
			PlayerEntity playerEntity = PlayerOwned.getPlayerEntityIfOnline(ownerUUID);
			if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
				Support.grantAdvancementCriterion(serverPlayerEntity, "endgame/grow_ominous_sapling", "grow");
			}
		}
		
		// TODO: grow!
	}
	
	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new OminousSaplingBlockEntity(SpectrumBlockEntities.OMINOUS_SAPLING, pos, state);
	}
	
	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
	private OminousSaplingBlockEntity getBlockEntity(World world, BlockPos blockPos) {
		BlockEntity saplingBlockEntity = world.getBlockEntity(blockPos);
		if (saplingBlockEntity instanceof OminousSaplingBlockEntity) {
			return (OminousSaplingBlockEntity) saplingBlockEntity;
		} else {
			return null;
		}
	}
	
}
