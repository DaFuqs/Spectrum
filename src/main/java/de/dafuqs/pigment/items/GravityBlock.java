package de.dafuqs.pigment.items;

import net.minecraft.block.Block;

public class GravityBlock extends Block {

    private final float gravityMod;

    public GravityBlock(Settings settings, float gravityMod) {
        super(settings);
        this.gravityMod = gravityMod;
    }

    public float getGravityMod() {
        return gravityMod;
    }

}
