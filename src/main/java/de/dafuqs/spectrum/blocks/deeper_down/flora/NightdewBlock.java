package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;

public class NightdewBlock extends TriStateVineBlock {

    public static final float BASE_BURGEON_CHANCE = 1;

    public NightdewBlock(Settings settings) {
        super(settings, 6, 1F, 0.3F, 0.85F);
    }

    @Override
    public boolean canPlantOnTop(BlockState roof, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(roof, world, pos) && roof.isIn(SpectrumBlockTags.NIGHTDEW_SOILS);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return SpectrumItems.NIGHTDEW_SPROUT.getDefaultStack();
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        var random = world.getRandom();
        var dropChance = MathHelper.clampedLerp(BASE_BURGEON_CHANCE, 200, 0);

        if (random.nextFloat() < 1 / dropChance)
            dropStack(world, pos, SpectrumItems.NECTARDEW_BURGEON.getDefaultStack());
    }

    @Override
    boolean hasGrowthActions() {
        return false;
    }
}
