package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.phys.*;

public class StormStoneItem extends CloakedItem implements DamageAwareItem {
	
	public StormStoneItem(Properties settings, ResourceLocation cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
		if (source.is(DamageTypeTags.IS_EXPLOSION)) {
			doLightningExplosion(itemEntity);
		}
	}
	
	private void doLightningExplosion(ItemEntity itemEntity) {
		ItemStack thisItemStack = itemEntity.getItem();
		Level world = itemEntity.getCommandSenderWorld();
		
		BlockPos blockPos = itemEntity.blockPosition();
		Vec3 pos = itemEntity.position();
		int count = thisItemStack.getCount();
		
		// remove the itemEntity before dealing damage, otherwise it would cause a stack overflow
		itemEntity.remove(Entity.RemovalReason.KILLED);
		
		// strike lightning...
		if (world.canSeeSky(itemEntity.blockPosition())) {
			LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
				world.addFreshEntity(lightningEntity);
			}
		}
		
		// ...and boom!
		float powerMod = 1.0F;
		Biome biomeAtPos = world.getBiome(blockPos).value();
		if (!biomeAtPos.hasPrecipitation() && !biomeAtPos.coldEnoughToSnow(blockPos)) {
			// there is no rain/thunder in deserts or snowy biomes
			powerMod = world.isThundering() ? 1.5F : world.isRaining() ? 1.25F : 1.0F;
		}
		
		world.explode(itemEntity, pos.x(), pos.y(), pos.z(), count * powerMod, Level.ExplosionInteraction.MOB);
	}
	
}
