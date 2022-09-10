package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.compat.gofish.GoFishCompat;
import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import de.dafuqs.spectrum.interfaces.PlayerEntityAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SpectrumFishingRodItem extends FishingRodItem {
	
	public SpectrumFishingRodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		int i;
		PlayerEntityAccessor playerEntityAccessor = ((PlayerEntityAccessor) user);
		if (playerEntityAccessor.getSpectrumBobber() != null) {
			if (!world.isClient) {
				i = playerEntityAccessor.getSpectrumBobber().use(itemStack);
				itemStack.damage(i, user, (p) -> {
					p.sendToolBreakStatus(hand);
				});
			}
			
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.emitGameEvent(user, GameEvent.FISHING_ROD_REEL_IN, user);
		} else {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!world.isClient) {
				i = EnchantmentHelper.getLure(itemStack);
				int j = EnchantmentHelper.getLuckOfTheSea(itemStack);
				world.spawnEntity(new SpectrumFishingBobberEntity(user, world, j, i));
			}
			
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			world.emitGameEvent(user, GameEvent.FISHING_ROD_CAST, user);
		}
		
		return TypedActionResult.success(itemStack, world.isClient());
	}
	
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(FluidTags.WATER);
	}
	
	public boolean shouldAutosmelt(ItemStack itemStack) {
		return GoFishCompat.hasDeepfry(itemStack);
	}
	
}