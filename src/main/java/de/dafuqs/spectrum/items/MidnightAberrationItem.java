package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MidnightAberrationItem extends CloakedItem {
	
	public MidnightAberrationItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		if(!world.isClient && world.getTime() % 20 == 0 && entity instanceof PlayerEntity playerEntity) {
			NbtCompound compound = stack.getNbt();
			if(compound != null && compound.getBoolean("Stable")) {
				return;
			}
			
			if(world.random.nextFloat() < 0.2F) {
				stack.decrement(1);
				Support.givePlayer(playerEntity, Items.GUNPOWDER.getDefaultStack());
				world.playSoundFromEntity(null, playerEntity, SpectrumSoundEvents.MIDNIGHT_ABERRATION_CRUMBLING, SoundCategory.PLAYERS, 0.5F, 1.0F);
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		NbtCompound compound = stack.getNbt();
		if(compound != null && compound.getBoolean("Stable")) {
			tooltip.add(new TranslatableText("item.spectrum.midnight_aberration.tooltip.stable"));
		}
	}
	
	public ItemStack getStableStack() {
		ItemStack stack = getDefaultStack();
		NbtCompound compound = stack.getOrCreateNbt();
		compound.putBoolean("Stable", true);
		stack.setNbt(compound);
		return stack;
	}
	
}
