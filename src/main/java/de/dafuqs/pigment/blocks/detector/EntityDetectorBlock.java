package de.dafuqs.pigment.blocks.detector;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class EntityDetectorBlock extends DetectorBlock {

    public EntityDetectorBlock(Settings settings) {
        super(settings);
    }

    protected void updateState(BlockState state, World world, BlockPos pos) {
        List<LivingEntity> entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), getBoxWithRadius(pos, 10), LivingEntity::isAlive);

        int power = Math.min(entities.size(), 15);

        if (state.get(POWER) != power) {
            world.setBlockState(pos, state.with(POWER, power), 3);
        }
    }

    @Override
    int getUpdateFrequencyTicks() {
        return 20;
    }

}
