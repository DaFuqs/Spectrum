package de.dafuqs.pigment.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.UUID;

public interface PlayerOwned {


    //public abstract void setOwnerUUID(UUID ownerUUID);
    public abstract UUID getOwnerUUID();

    //public abstract void setOwnerName(String name);
    public abstract String getOwnerName();

    public void setOwner(PlayerEntity playerEntity);

    public default boolean hasOwner() {
        return getOwnerUUID() != null;
    }

    public default boolean isOwner(PlayerEntity playerEntity) {
        return playerEntity.getUuid().equals(getOwnerUUID());
    }


}
