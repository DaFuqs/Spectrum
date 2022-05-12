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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;

public class AmethystChestBlock extends TreasureChestBlock {
	
	public AmethystChestBlock(Settings settings) {
		super(settings);
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AmethystChestBlockEntity(pos, state);
	}
	
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? checkType(type, SpectrumBlockEntityRegistry.AMETHYST_CHEST, TreasureChestBlockEntity::clientTick) : null;
	}
	
	public SpriteIdentifier getTexture() {
		return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(SpectrumCommon.MOD_ID, "entity/amethyst_chest"));
	}
	
}
