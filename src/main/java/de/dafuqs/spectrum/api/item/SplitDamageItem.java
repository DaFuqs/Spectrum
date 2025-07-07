package de.dafuqs.spectrum.api.item;

import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

import java.util.*;

public interface SplitDamageItem {

    DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage);

    class DamageComposition {
		
		private final List<Tuple<DamageSource, Float>> damageSourcesWithPercentage = new ArrayList<>();

        public DamageComposition() {

        }

        public void addPlayerOrEntity(LivingEntity entity, float ratio) {
			if (entity instanceof Player player) {
				this.damageSourcesWithPercentage.add(new Tuple<>(player.damageSources().playerAttack(player), ratio));
            } else {
				this.damageSourcesWithPercentage.add(new Tuple<>(entity.damageSources().mobAttack(entity), ratio));
            }
        }
		
		public DamageSource getPlayerOrEntity(LivingEntity entity) {
			if (entity instanceof Player player) {
				return player.damageSources().playerAttack(player);
			} else {
				return entity.damageSources().mobAttack(entity);
			}
		}

        public void add(DamageSource damageSource, float ratio) {
			this.damageSourcesWithPercentage.add(new Tuple<>(damageSource, ratio));
        }
		
		public List<Tuple<DamageSource, Float>> get() {
            return damageSourcesWithPercentage;
        }

    }


}
