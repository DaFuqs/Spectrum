package de.dafuqs.spectrum.items.magic_items.ampoules;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BloodstoneGlassAmpouleItem extends GlassAmpouleItem implements PrioritizedEntityInteraction {
	
	protected static final float EXTRA_REACH = 12.0F;
	protected static final ResourceLocation REACH_ENTITY_INTERACTION_MODIFIER_ID = SpectrumCommon.locate("bloodstone_glass_ampoule_reach");
	
	public BloodstoneGlassAmpouleItem(Properties settings) {
		super(settings);
	}
	
	public static ItemAttributeModifiers createAttributeModifiers() {
		return ItemAttributeModifiers.builder()
				.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(REACH_ENTITY_INTERACTION_MODIFIER_ID, EXTRA_REACH, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.build();
	}
	
	@Override
	public boolean trigger(Level world, ItemStack stack, LivingEntity attacker, @Nullable LivingEntity target, Vec3 position) {
		world.playLocalSound(BlockPos.containing(position), SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundSource.PLAYERS, 0.35F, 0.9F + world.getRandom().nextFloat() * 0.334F, true);
		LightSpearEntity.summonBarrage(world, attacker, target, LightShardBaseEntity.MONSTER_TARGET, position, LightShardBaseEntity.DEFAULT_COUNT_PROVIDER);
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.bloodstone_glass_ampoule.tooltip").withStyle(ChatFormatting.GRAY));
	}

}
