package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.chests.SpectrumChestBlock;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;

public class TreasureChestBlock extends SpectrumChestBlock {
	
	public TreasureChestBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof TreasureChestBlockEntity treasureChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				if (treasureChestBlockEntity.canOpen(player)) {
					player.openHandledScreen(treasureChestBlockEntity);
				} else {
					world.playSound(null, pos, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TreasureChestBlockEntity(pos, state);
	}
	
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? checkType(type, SpectrumBlockEntityRegistry.TREASURE_CHEST, TreasureChestBlockEntity::clientTick) : null;
	}
	
	public SpriteIdentifier getTexture() {
		return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(SpectrumCommon.MOD_ID, "entity/treasure_chest"));
	}
	
}
