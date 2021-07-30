package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InvisibleItemFrameEntity extends ItemFrameEntity {

    public InvisibleItemFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    public InvisibleItemFrameEntity(World world, BlockPos pos, Direction facing) {
        this(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, world, pos, facing);
    }

    public InvisibleItemFrameEntity(EntityType<? extends ItemFrameEntity> type, World world, BlockPos pos, Direction facing) {
        super(type, world, pos, facing);
    }

    @Override
    public boolean isInvisible() {
        if(this.getHeldItemStack().isEmpty()) {
            return super.isInvisible();
        } else {
            return true;
        }
    }

}
