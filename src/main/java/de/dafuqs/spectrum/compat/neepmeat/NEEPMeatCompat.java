package de.dafuqs.spectrum.compat.neepmeat;

import com.neep.neepmeat.enlightenment.PlayerEnlightenmentManager;
import de.dafuqs.spectrum.compat.SpectrumIntegrationPacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class NEEPMeatCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
    public void register() {

    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerClient() {

    }

    public static void sedateEnlightenment(LivingEntity user)
    {
        if(user.isPlayer())
        {
            PlayerEnlightenmentManager enlightenmentManager = new PlayerEnlightenmentManager((PlayerEntity) user);
            NbtCompound tag = new NbtCompound();
            enlightenmentManager.writeToNbt(tag);
            double acuteEnlightenment = tag.getDouble("acute");
            if(acuteEnlightenment>0)
            {
                tag.putDouble("acute", Math.max(0, acuteEnlightenment*0.75 - 1));
                enlightenmentManager.readFromNbt(tag);
            }
        }
    }

}
