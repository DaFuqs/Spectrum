package de.dafuqs.spectrum.blocks.spirit_sallow;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OminousSaplingBlock extends BushBlock implements EntityBlock {
	
	public OminousSaplingBlock(Properties settings) {
		super(settings);
	}
	
	public static final MapCodec<OminousSaplingBlock> CODEC = simpleCodec(OminousSaplingBlock::new);

	@Override
	public MapCodec<? extends OminousSaplingBlock> codec() {
		return CODEC;
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!world.isClientSide()) {
			OminousSaplingBlockEntity ominousSaplingBlockEntity = getBlockEntity(world, pos);
			if (ominousSaplingBlockEntity != null) {
				player.displayClientMessage(Component.nullToEmpty("Sapling owner UUID: " + ominousSaplingBlockEntity.getOwnerUUID()), true);
			} else {
				player.displayClientMessage(Component.nullToEmpty("Sapling block entity putt :("), true);
			}
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (world.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(2) == 0) {
			this.generateOminousTree(world, pos, state, random);
		}
	}
	
	private void generateOminousTree(ServerLevel world, BlockPos pos, BlockState state, RandomSource random) {
		OminousSaplingBlockEntity ominousSaplingBlockEntity = getBlockEntity(world, pos);
		if (ominousSaplingBlockEntity != null) {
			UUID ownerUUID = ominousSaplingBlockEntity.getOwnerUUID();
			Player playerEntity = PlayerOwned.getPlayerEntityIfOnline(ownerUUID);
			if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
				Support.grantAdvancementCriterion(serverPlayerEntity, "endgame/grow_ominous_sapling", "grow");
			}
		}
		
		// TODO: grow!
	}
	
	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new OminousSaplingBlockEntity(SpectrumBlockEntities.OMINOUS_SAPLING, pos, state);
	}
	
	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
	private OminousSaplingBlockEntity getBlockEntity(Level world, BlockPos blockPos) {
		BlockEntity saplingBlockEntity = world.getBlockEntity(blockPos);
		if (saplingBlockEntity instanceof OminousSaplingBlockEntity) {
			return (OminousSaplingBlockEntity) saplingBlockEntity;
		} else {
			return null;
		}
	}
	
}
