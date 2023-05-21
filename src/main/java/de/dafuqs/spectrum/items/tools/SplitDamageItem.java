package de.dafuqs.spectrum.items.tools;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public interface SplitDamageItem {

    DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage);

    class DamageComposition {

        private final List<Pair<DamageSource, Float>> damageSourcesWithPercentage = new ArrayList<>();

        public DamageComposition() {

        }

        public void addPlayerOrEntity(LivingEntity entity, float ratio) {
            if (entity instanceof PlayerEntity player) {
                this.damageSourcesWithPercentage.add(new Pair<>(player.getDamageSources().playerAttack(player), ratio));
            } else {
                this.damageSourcesWithPercentage.add(new Pair<>(entity.getDamageSources().mobAttack(entity), ratio));
            }
        }

        public void add(DamageSource damageSource, float ratio) {
            this.damageSourcesWithPercentage.add(new Pair<>(damageSource, ratio));
        }

        public List<Pair<DamageSource, Float>> get() {
            return damageSourcesWithPercentage;
        }

    }


}
