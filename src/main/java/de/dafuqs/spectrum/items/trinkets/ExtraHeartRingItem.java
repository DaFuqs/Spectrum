package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.energy.storage.FixedSingleInkDrain;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ExtraHeartRingItem extends SpectrumTrinketItem implements InkStorageItem<FixedSingleInkDrain> {
	
	/**
	 Ink	Extra Hearts
	 0	               0
	 1	               0
	 8	               1
	 64	               2
	 100	           2
	 800	           3
	 6400	           4
	 51200	           5
	 409600	           6
	 3276800	       7
	 26214400	       8
	 209715200	       9
	 1677721600	      10
	 13421772800	  11
	 1,07374E+11	  12
	 8,58993E+11	  13
	 6,87195E+12	  14
	 5,49756E+13	  15
	 4,39805E+14	  16
	 3,51844E+15	  17
	 2,81475E+16	  18
	 2,2518E+17	      19
	 1,80144E+18	  20
    */
	public static final InkColor INK_COLOR = InkColors.PINK;
	public static final long MAX_INK = (long) Math.pow(8, 20);
	
	public ExtraHeartRingItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_extra_heart_ring"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.extra_heart_ring.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		if(inkStorage.getStoredColor() == INK_COLOR) {
			long storedInk = inkStorage.getEnergy(INK_COLOR);
			int extraHearts = getExtraHearts(storedInk);
			if(extraHearts != 0) {
				modifiers.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(uuid, "spectrum:extra_heart_ring", extraHearts, EntityAttributeModifier.Operation.ADDITION));
			}
		}

		return modifiers;
	}
	
	public int getExtraHearts(long storedInk) {
		if(storedInk < 8) {
			return 0;
		} else {
			return (int) (Math.log(storedInk) / Math.log(8));
		}
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public FixedSingleInkDrain getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return FixedSingleInkDrain.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new FixedSingleInkDrain(MAX_INK, INK_COLOR);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, FixedSingleInkDrain storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	public ItemStack getFullStack() {
		return InkStorageItem.super.getFullStack();
	}
	
}