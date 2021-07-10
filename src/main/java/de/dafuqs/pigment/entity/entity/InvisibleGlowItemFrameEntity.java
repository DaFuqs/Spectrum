package de.dafuqs.pigment.entity.entity;

import de.dafuqs.pigment.entity.PigmentEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InvisibleGlowItemFrameEntity extends ItemFrameEntity {

    public InvisibleGlowItemFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    public InvisibleGlowItemFrameEntity(World world, BlockPos pos, Direction facing) {
        this(PigmentEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, world, pos, facing);
    }

    public InvisibleGlowItemFrameEntity(EntityType<? extends ItemFrameEntity> type, World world, BlockPos pos, Direction facing) {
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

    public SoundEvent getRemoveItemSound() {
        return SoundEvents.ENTITY_GLOW_ITEM_FRAME_REMOVE_ITEM;
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.ENTITY_GLOW_ITEM_FRAME_BREAK;
    }

    public SoundEvent getPlaceSound() {
        return SoundEvents.ENTITY_GLOW_ITEM_FRAME_PLACE;
    }

    public SoundEvent getAddItemSound() {
        return SoundEvents.ENTITY_GLOW_ITEM_FRAME_ADD_ITEM;
    }

    public SoundEvent getRotateItemSound() {
        return SoundEvents.ENTITY_GLOW_ITEM_FRAME_ROTATE_ITEM;
    }

    protected ItemStack getAsItemStack() {
        return new ItemStack(Items.GLOW_ITEM_FRAME);
    }

}
