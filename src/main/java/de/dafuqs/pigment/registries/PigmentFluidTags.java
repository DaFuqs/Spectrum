package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.mixin.FluidTagsAccessor;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public class PigmentFluidTags {

    public static Tag<Fluid> LIQUID_CRYSTAL;
    public static Tag<Fluid> MUD;

    private static Tag<Fluid> register(String id) {
        return FluidTagsAccessor.invokeRegister(PigmentCommon.MOD_ID + ":" + id);
    }

    public static void register() {
        LIQUID_CRYSTAL = register("liquid_crystal");
        MUD = register("mud");
    }
}
