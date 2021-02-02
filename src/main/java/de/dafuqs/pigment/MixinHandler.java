package de.dafuqs.pigment;

import de.dafuqs.pigment.enchantments.PigmentEnchantments;
import de.dafuqs.pigment.items.misc.Spawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MixinHandler {

    public static boolean checkResonanceForSpawnerMining(World world, PlayerEntity entity, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (blockState.equals(Blocks.SPAWNER.getDefaultState())) {
            if (EnchantmentHelper.getLevel(PigmentEnchantments.RESONANCE, stack) > 0) {
                if (blockEntity instanceof MobSpawnerBlockEntity) {
                    ItemStack itemStack = Spawner.fromBlockEntity(blockEntity);

                    Block.dropStack(world, pos, itemStack);
                    world.playSound(null, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    return true;
                }
            }

        }
        return false;
    }

}
