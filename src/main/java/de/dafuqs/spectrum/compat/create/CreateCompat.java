package de.dafuqs.spectrum.compat.create;

import com.simibubi.create.api.event.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.compat.*;
import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class CreateCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
    
    @Override
    public void register() {
        PipeCollisionEvent.FLOW.register(event -> {
            final BlockState result = handleBidirectionalCollision(event.getLevel(), event.getFirstFluid(), event.getSecondFluid());
            if (result != null) event.setState(result);
        });
        
        PipeCollisionEvent.SPILL.register(event -> {
            final BlockState result = handleBidirectionalCollision(event.getLevel(), event.getPipeFluid(), event.getWorldFluid());
            if (result != null) event.setState(result);
        });
    }
    
    // NOTE: firstFluid and secondFluid are assumed to be not null without checking,
    // since the default Create event handlers for pipe collisions would throw a NullPointerException otherwise.
    private BlockState handleBidirectionalCollision(World world, @NotNull Fluid firstFluid, @NotNull Fluid secondFluid) {
        final FluidState firstState = firstFluid.getDefaultState();
        final FluidState secondState = secondFluid.getDefaultState();
        
        // Handle fluid 1
        final BlockState result = spectrumFluidCollision(world, firstState, secondState);
        if (result != null) return result;
        
        // Handle fluid 2
        return spectrumFluidCollision(world, secondState, firstState);
    }
    
    private BlockState spectrumFluidCollision(World world, FluidState state, FluidState otherState) {
        if (state.getBlockState().getBlock() instanceof SpectrumFluidBlock spectrumFluid)
            return spectrumFluid.handleFluidCollision(world, state, otherState);
        return null;
    }
    
    @Override
    public void registerClient() {
    
    }

}
