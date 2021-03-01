package de.dafuqs.pigment.blocks.detector;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DaylightDetectorBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockLightDetectorBlock extends DetectorBlock {

    public BlockLightDetectorBlock(Settings settings) {
        super(settings);
    }

    protected void updateState(BlockState state, World world, BlockPos pos) {
        int power = world.getLightLevel(pos);

        boolean bl = state.get(INVERTED);
        if (bl) {
            power = 15 - power;
        }

        if (state.get(POWER) != power) {
            world.setBlockState(pos, state.with(POWER, power), 3);
        }
    }

    @Override
    int getUpdateFrequencyTicks() {
        return 20;
    }

}
