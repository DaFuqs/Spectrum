package de.dafuqs.pigment.interfaces;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.UUID;

public interface PlayerOwned {

    public abstract void setOwnerUUID(UUID ownerUUID);
    public abstract UUID getOwnerUUID();

}
