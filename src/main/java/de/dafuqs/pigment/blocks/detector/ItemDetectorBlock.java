package de.dafuqs.pigment.blocks.detector;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class ItemDetectorBlock extends DetectorBlock {

    public ItemDetectorBlock(Settings settings) {
        super(settings);
    }

    protected void updateState(BlockState state, World world, BlockPos pos) {
        List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, getBoxWithRadius(pos, 10), Entity::isAlive);

        int power;
        if(items.size() > 0) {
            int amount = 0;
            for(ItemEntity itementity : items) {
                ItemStack itemStack = itementity.getStack();
                amount += itemStack.getCount();
                if(amount >= 64) {
                    break;
                }
            }
            power = Math.max(1, Math.min(amount / 4, 15));
        } else {
            power = 0;
        }

        if (state.get(POWER) != power) {
            world.setBlockState(pos, state.with(POWER, power), 3);
        }
    }

    private Box getBoxWithRadius(BlockPos blockPos, int radius) {
        return Box.of(Vec3d.ofCenter(blockPos), radius, radius, radius);
    }

    @Override
    int getUpdateFrequencyTicks() {
        return 20;
    }

}
