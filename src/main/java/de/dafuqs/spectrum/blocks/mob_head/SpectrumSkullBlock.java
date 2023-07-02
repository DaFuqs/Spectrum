package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.pattern.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.predicate.block.*;
import net.minecraft.server.network.*;
import net.minecraft.tag.*;
import net.minecraft.util.function.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class SpectrumSkullBlock extends SkullBlock {
	
	@Nullable
	private static BlockPattern witherBossPattern;
	
	public SpectrumSkullBlock(SkullType skullType, Settings settings) {
		super(skullType, settings);
	}
	
	private static BlockPattern getWitherSkullPattern() {
		if (witherBossPattern == null) {
			witherBossPattern = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', (pos) ->
							pos.getBlockState().isIn(BlockTags.WITHER_SUMMON_BASE_BLOCKS))
					.where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(SpectrumBlocks.getMobHead(SpectrumSkullBlockType.WITHER))
							.or(BlockStatePredicate.forBlock(SpectrumBlocks.getMobWallHead(SpectrumSkullBlockType.WITHER)))))
					.where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}
		
		return witherBossPattern;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpectrumSkullBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		// Trigger advancement if player builds a wither structure using wither skulls instead of wither skeleton skulls
		if (getSkullType().equals(SpectrumSkullBlockType.WITHER) && placer instanceof ServerPlayerEntity serverPlayerEntity) {
			if (pos.getY() >= world.getBottomY()) {
				BlockPattern blockPattern = getWitherSkullPattern();
				BlockPattern.Result result = blockPattern.searchAround(world, pos);
				if (result != null) {
					Support.grantAdvancementCriterion(serverPlayerEntity, "midgame/build_wither_using_wither_heads", "built_wither_using_wither_heads");
				}
			}
		}
	}
	
}
