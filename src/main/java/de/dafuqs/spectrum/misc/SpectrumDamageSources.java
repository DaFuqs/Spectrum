package de.dafuqs.spectrum.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SpectrumDamageSources {

    public static final DamageSource DECAY = new DecayDamageSource("spectrum_decay");


    public static class DecayDamageSource extends DamageSource {

        private boolean bypassesArmor;
        private boolean unblockable;

        protected DecayDamageSource(String name) {
            super(name);
            bypassesArmor = true;
            unblockable = true;
        }

        @Override
        public boolean bypassesArmor() {
            return this.bypassesArmor;
        }

        @Override
        public boolean isUnblockable() {
            return this.unblockable;
        }

        public Text getDeathMessage(LivingEntity entity) {
            LivingEntity livingEntity = entity.getPrimeAdversary();
            String string = "death.attack." + this.name;
            return livingEntity != null ? new TranslatableText(string, new Object[]{entity.getDisplayName(), livingEntity.getDisplayName()}) : new TranslatableText(string, new Object[]{entity.getDisplayName()});
        }


    }


}
