package de.dafuqs.pigment.blocks.melon;

import de.dafuqs.pigment.registries.PigmentBlocks;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.MelonBlock;
import net.minecraft.block.StemBlock;

public class GlisteringMelonBlock extends MelonBlock {

    public GlisteringMelonBlock(Settings settings) {
        super(settings);
    }

    public StemBlock getStem() {
        return (StemBlock) PigmentBlocks.GLISTERING_MELON_STEM;
    }

    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock) PigmentBlocks.ATTACHED_GLISTERING_MELON_STEM;
    }

}
