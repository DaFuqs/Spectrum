package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.entity.entity.BlockFlooderProjectile;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockFlooderItem extends Item {
	
	public BlockFlooderItem(Settings settings) {
		super(settings);
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			BlockFlooderProjectile blockFlooderProjectile = new BlockFlooderProjectile(world, user);
			blockFlooderProjectile.setItem(itemStack);
			blockFlooderProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
			world.spawnEntity(blockFlooderProjectile);
		}
		
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}
		
		return TypedActionResult.success(itemStack, world.isClient());
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.block_flooder.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.block_flooder.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.block_flooder.tooltip3").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.block_flooder.tooltip4").formatted(Formatting.GRAY));
	}
	
}