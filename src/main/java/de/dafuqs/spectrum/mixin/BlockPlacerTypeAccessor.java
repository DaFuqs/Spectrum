package de.dafuqs.spectrum.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.BlockPlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockPlacerType.class)
public interface BlockPlacerTypeAccessor {

    @Invoker(value = "<init>")
    static <P extends BlockPlacer> BlockPlacerType<P> createBlockPlacerType(Codec<P> codec) {
        throw new UnsupportedOperationException();
    }

}