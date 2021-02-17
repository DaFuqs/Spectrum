package de.dafuqs.pigment;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PigmentFluidTags {

    public static Tag<Fluid> LIQUID_CRYSTAL;
    public static Tag<Fluid> MUD;

    private static Tag<Fluid> register(String id) {
        return TagRegistry.fluid(new Identifier(PigmentCommon.MOD_ID, id));
    }

    public static void register() {
        LIQUID_CRYSTAL = register("liquid_crystal");
        MUD = register("mud");
    }
}
