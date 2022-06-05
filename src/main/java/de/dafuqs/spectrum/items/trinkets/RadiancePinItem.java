package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RadiancePinItem extends SpectrumTrinketItem {
	
	public static final int CHECK_EVERY_X_TICKS = 20;
	public static final int MAX_LIGHT_LEVEL = 7;
	public static final BlockState LIGHT_BLOCK_STATE = SpectrumBlocks.DECAYING_LIGHT_BLOCK.getDefaultState().with(LightBlock.LEVEL_15, 15);
	public static final BlockState LIGHT_BLOCK_STATE_WATER = SpectrumBlocks.DECAYING_LIGHT_BLOCK.getDefaultState().with(LightBlock.LEVEL_15, 15).with(LightBlock.WATERLOGGED, true);
	
	public RadiancePinItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_radiance_pin"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.radiance_pin.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		World world = entity.getWorld();
		if (!world.isClient && world.getTime() % CHECK_EVERY_X_TICKS == 0) {
			if (entity instanceof PlayerEntity playerEntity && playerEntity.isSpectator()) {
				return;
			}
			
			if (world.getLightLevel(entity.getBlockPos()) <= MAX_LIGHT_LEVEL) {
				BlockState currentState = world.getBlockState(entity.getBlockPos());
				boolean placed = false;
				if (currentState.isAir()) {
					world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE, 3);
					placed = true;
				} else if (currentState.equals(Blocks.WATER.getDefaultState())) {
					world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE_WATER, 3);
					placed = true;
				} else if (currentState.isOf(SpectrumBlocks.DECAYING_LIGHT_BLOCK)) {
					if (currentState.get(LightBlock.WATERLOGGED)) {
						world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE_WATER, 3);
					} else {
						world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE, 3);
					}
					placed = true;
				}
				if (placed) {
					SpectrumS2CPacketSender.sendLightCreatedParticle(world, entity.getBlockPos());
					world.playSound(null, entity.getX() + 0.5, entity.getY() + 0.5, entity.getZ() + 0.5, SpectrumSoundEvents.LIGHT_STAFF_PLACE, SoundCategory.PLAYERS, 0.4F, 0.9F + world.random.nextFloat() * 0.2F);
				}
			}
		}
	}
	
}