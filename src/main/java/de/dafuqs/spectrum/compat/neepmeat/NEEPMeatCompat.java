package de.dafuqs.spectrum.compat.neepmeat;

import com.neep.neepmeat.enlightenment.EnlightenmentManager;
import com.neep.neepmeat.enlightenment.PlayerEnlightenmentManager;
import com.neep.neepmeat.init.NMComponents;
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
            EnlightenmentManager manager = NMComponents.ENLIGHTENMENT_MANAGER.get(user);
            double acuteEnlightenment = manager.getAcute();
            double chronicEnlightenment = manager.getChronic();
            if(acuteEnlightenment>0)
            {
                manager.setAcute(Math.max(0, acuteEnlightenment*0.75 - 1));
            }
            if(chronicEnlightenment>0 && (Math.random()>0.9))
            {
                manager.setChronic(Math.max(0,chronicEnlightenment-5));
            }
        }
    }

}
