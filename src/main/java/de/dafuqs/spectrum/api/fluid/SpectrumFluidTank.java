package de.dafuqs.spectrum.api.fluid;

import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.templates.*;

public class SpectrumFluidTank extends FluidTank {
    private final Callback updateCallback;

    public SpectrumFluidTank(int capacity, Callback updateCallback) {
        super(capacity);
        this.updateCallback = updateCallback;
    }

    protected void onContentsChanged() {
        super.onContentsChanged();
        this.updateCallback.onFluidContentsChanged();
    }

    public void setFluid(FluidStack stack) {
        super.setFluid(stack);
        this.updateCallback.onFluidContentsChanged();
    }
	
	public interface Callback {
		FluidTank getFluidTank();
		void onFluidContentsChanged();
	}
}