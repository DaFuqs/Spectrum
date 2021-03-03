package de.dafuqs.pigment.blocks.head;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.dafuqs.pigment.blocks.head.PigmentSkullBlock;
import de.dafuqs.pigment.registries.PigmentBlockEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

// since SkullBlockEntity uses the fixed BlockEntityType.SKULL we have to create our own block entity :(
// but since there is no player type / redstone interaction it should be a bit more performant than the vanilla one
public class PigmentSkullBlockEntity extends BlockEntity {

    public PigmentSkullBlockEntity(BlockPos pos, BlockState state) {
        super(PigmentBlockEntityTypes.SKULL, pos, state);
    }

    private PigmentSkullBlock.Type skullType;


    public CompoundTag writeNbt(CompoundTag tag) {
        super.writeNbt(tag);
        if (this.skullType != null) {
            tag.putString("SkullType", this.skullType.toString());
        }

        return tag;
    }

    public void readNbt(CompoundTag tag) {
        super.readNbt(tag);
        if (tag.contains("SkullType", 8)) {
            PigmentSkullBlock.Type skullType = PigmentSkullBlock.Type.valueOf(tag.getString("SkullType"));
            this.skullType = skullType;
        }
    }

    @Environment(EnvType.CLIENT)
    public PigmentSkullBlock.Type getSkullType() {
        return this.skullType;
    }

    @org.jetbrains.annotations.Nullable
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 4, this.toInitialChunkDataNbt());
    }

    public CompoundTag toInitialChunkDataNbt() {
        return this.writeNbt(new CompoundTag());
    }

    public void setSkullType(PigmentSkullBlock.Type skullType) {
        this.skullType = skullType;
        this.markDirty();
    }

}
