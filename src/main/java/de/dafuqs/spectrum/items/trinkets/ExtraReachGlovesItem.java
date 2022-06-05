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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ExtraReachGlovesItem extends SpectrumTrinketItem implements InkStorageItem<FixedSingleInkDrain> {
	
	public static final InkColor INK_COLOR = InkColors.LIGHT_BLUE;
	public static final long MAX_INK = (long) Math.pow(8, 20); // 10 blocks of extra reach
	
	public ExtraReachGlovesItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_extra_reach_gloves"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.reach_gloves.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		if(inkStorage.getStoredColor() == INK_COLOR) {
			long storedInk = inkStorage.getEnergy(INK_COLOR);
			double extraReach = getExtraReach(storedInk);
			if(extraReach != 0) {
				modifiers.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(uuid, "spectrum:reach_gloves", extraReach, EntityAttributeModifier.Operation.ADDITION));
				modifiers.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(uuid, "spectrum:reach_gloves", extraReach, EntityAttributeModifier.Operation.ADDITION));
			}
		}
		
		return modifiers;
	}
	
	public double getExtraReach(long storedInk) {
		if(storedInk < 64) {
			return 0;
		} else {
			return Math.log(storedInk) / Math.log(64) - 1;
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