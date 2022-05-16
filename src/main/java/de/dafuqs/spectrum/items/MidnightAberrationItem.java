package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.confitional.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MidnightAberrationItem extends CloakedItem {
	
	public MidnightAberrationItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		if(!world.isClient && world.getTime() % 20 == 0 && entity instanceof PlayerEntity playerEntity && world.random.nextFloat() < 0.2F) {
			stack.decrement(1);
			Support.givePlayer(playerEntity, Items.GUNPOWDER.getDefaultStack());
			playerEntity.playSound(SpectrumSoundEvents.MIDNIGHT_ABERRATION_CRUMBLING, 0.5F, 1.0F);
		}
	}
	
	
}
