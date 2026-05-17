package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class AbyssalVineBlock extends TriStateVineBlock {
	
	public static final MapCodec<AbyssalVineBlock> CODEC = simpleCodec(AbyssalVineBlock::new);
	
	public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
	
	public AbyssalVineBlock(Properties settings) {
        super(settings, 5, 0.3F, 0.4F, 0.667F);
		registerDefaultState(defaultBlockState().setValue(BERRIES, false));
    }

    @Override
	public MapCodec<? extends AbyssalVineBlock> codec() {
        return CODEC;
    }

    @Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return !state.getValue(BERRIES);
    }

    @Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		var res = super.useWithoutItem(state, world, pos, player, hit);
		
		if (res.indicateItemUse())
			return res;
		
        if (!state.getValue(BERRIES))
			return InteractionResult.FAIL;

        state = state.setValue(BERRIES, false);
        world.setBlockAndUpdate(pos, state);
		world.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(world.getRandom(), 0.8F, 1.2F));
		player.getInventory().placeItemBackInInventory(SpectrumItems.FISSURE_PLUM.getDefaultInstance());
		
		world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
		return InteractionResult.SUCCESS;
    }

    @Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        var growthChance = 0.8F;

        for (int offset = 0; true; offset++) {
			var other = world.getBlockState(pos.offset(0, offset, 0));

            if (other.is(SpectrumBlocks.SHALE_CLAY))
                return;

            if (other.is(SpectrumBlockTags.GROWTH_ACCELERATORS)) {
                growthChance = 0.5F;
            }

            if (!other.is(this))
                break;
        }

        if (random.nextFloat() < growthChance)
            return;

        if (!state.getValue(BERRIES))
            tryGrowBerries(state, pos, world);
    }

    @Override
	public boolean isRandomlyTicking(BlockState state) {
		return super.isRandomlyTicking(state) || !state.getValue(BERRIES);
    }

    @Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return SpectrumItems.FISSURE_PLUM.getDefaultInstance();
    }

    @Override
    boolean hasGrowthActions() {
        return true;
    }
	
	public void tryGrowBerries(BlockState state, BlockPos pos, Level world) {
        int berryCount = 0;

        for (int i = 0; i < 3; i++) {
			var uRef = world.getBlockState(pos.offset(0,  i, 0));
			var dRef = world.getBlockState(pos.offset(0, -i, 0));

            berryCount += checkForBerries(uRef);
            berryCount += checkForBerries(dRef);

            if (i == 1 && (pos.getY() % 5 == 0 && berryCount == 2) || (pos.getY() % 7 == 0 && berryCount == 1))
                return;
        }

        if (berryCount >= 3)
            return;

        world.setBlockAndUpdate(pos, state.setValue(BERRIES, true));
    }

    private int checkForBerries(BlockState state) {
        return state.is(this) && state.getValue(BERRIES) ? 1 : 0;
    }

    @Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
        builder.add(BERRIES);
    }
}
