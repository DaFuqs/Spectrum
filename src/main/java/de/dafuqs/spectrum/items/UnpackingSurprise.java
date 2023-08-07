package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.blocks.present.PresentBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

/**
 * Doing a lil trolling
 */
public interface UnpackingSurprise {

    void unpackSurprise(ItemStack stack, PresentBlockEntity presentBlockEntity, ServerWorld world, BlockPos pos, Random random);
}
