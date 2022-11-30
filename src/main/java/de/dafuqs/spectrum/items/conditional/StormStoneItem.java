package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.items.DamageAwareItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.explosion.Explosion;

public class StormStoneItem extends CloakedItem implements DamageAwareItem {
	
	public StormStoneItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
		if (source.isExplosive()) {
			doLightningExplosion(itemEntity);
		}
	}
	
	private void doLightningExplosion(ItemEntity itemEntity) {
		ItemStack thisItemStack = itemEntity.getStack();
		World world = itemEntity.getEntityWorld();
		
		BlockPos blockPos = itemEntity.getBlockPos();
		Vec3d pos = itemEntity.getPos();
		int count = thisItemStack.getCount();
		
		// remove the itemEntity before dealing damage, otherwise it would cause a stack overflow
		itemEntity.remove(Entity.RemovalReason.KILLED);
		
		// strike lightning...
		if (world.isSkyVisible(itemEntity.getBlockPos())) {
			LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
				world.spawnEntity(lightningEntity);
			}
		}
		
		// ...and boom!
		float powerMod = 1.0F;
		Biome biomeAtPos = world.getBiome(blockPos).value();
		if (!biomeAtPos.isHot(blockPos) && !biomeAtPos.isCold(blockPos)) {
			// there is no rain/thunder in deserts or snowy biomes
			powerMod = world.isThundering() ? 1.5F : world.isRaining() ? 1.25F : 1.0F;
		}
		
		world.createExplosion(itemEntity, pos.getX(), pos.getY(), pos.getZ(), count * powerMod, Explosion.DestructionType.BREAK);
	}
	
}
