package de.dafuqs.spectrum.blocks.threat_conflux;

import de.dafuqs.spectrum.blocks.FluidLogging;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ThreatConfluxBlock extends BlockWithEntity implements FluidLogging.SpectrumFluidLoggable {

    public static final BooleanProperty ARMED = BooleanProperty.of("armed");
    public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.ANY_INCLUDING_NONE;

    public ThreatConfluxBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ARMED, false).with(LOGGED, FluidLogging.State.NOT_LOGGED));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        var be = world.getBlockEntity(pos);
        if (be instanceof ThreatConfluxBlockEntity conflux) {
            conflux.tryDetonate(state);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, SpectrumBlockEntities.THREAT_CONFLUX, ThreatConfluxBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThreatConfluxBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ARMED, LOGGED);
    }
}
