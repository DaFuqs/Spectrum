package de.dafuqs.pigment.items.item_frame;

import de.dafuqs.pigment.entity.PigmentEntityTypes;
import de.dafuqs.pigment.entity.entity.InvisibleGlowItemFrameEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InvisibleGlowItemFrameItem extends PigmentItemFrameItem {

    public InvisibleGlowItemFrameItem(EntityType<? extends AbstractDecorationEntity> entityType, Settings settings) {
        super(entityType, settings);
    }

    public ItemFrameEntity getItemFrameEntity(World world, BlockPos blockPos, Direction direction) {
        return new InvisibleGlowItemFrameEntity(PigmentEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, world, blockPos, direction);
    }

}
