package de.dafuqs.pigment.blocks.head;

import de.dafuqs.pigment.registries.PigmentBlockEntityRegistry;
import de.dafuqs.pigment.registries.PigmentBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

// since SkullBlockEntity uses the fixed BlockEntityType.SKULL we have to create our own block entity :(
// but since there is no player type / redstone interaction it should be a bit more performant than the vanilla one
public class PigmentSkullBlockEntity extends BlockEntity {

    public PigmentSkullBlockEntity(BlockPos pos, BlockState state) {
        super(PigmentBlockEntityRegistry.SKULL, pos, state);
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        return tag;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
    }

    @Environment(EnvType.CLIENT)
    public PigmentSkullBlock.Type getSkullType() {
        return PigmentBlocks.getSkullType(world.getBlockState(this.pos).getBlock());
    }

}
