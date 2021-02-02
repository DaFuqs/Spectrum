package de.dafuqs.pigment.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PigmentDamageSources {

    public static final DamageSource DECAY = new DecayDamageSource("pigment_decay");

    public static class DecayDamageSource extends DamageSource {

        private final boolean bypassesArmor;
        private final boolean unblockable;

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
            return livingEntity != null ? new TranslatableText(string, entity.getDisplayName(), livingEntity.getDisplayName()) : new TranslatableText(string, entity.getDisplayName());
        }


    }


}
