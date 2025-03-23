package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TuningStampItem extends Item implements ExpandedStatTooltip {

    public static final String DATA = Stampable.STAMPING_DATA_TAG;

    public TuningStampItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    /**
     * This is set up such that it can easily be extended for other uses later.
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var player = Optional.ofNullable(context.getPlayer());
        var reference = BlockReference.of(world, pos);

        var potentialData = Optional.<Stampable.StampData>empty();

        if (stack.getOrCreateNbt().contains(DATA)) {
            potentialData = Stampable.loadStampingData(world, stack.getSubNbt(DATA));
        }

        if (potentialData.isPresent()) {
			if (!world.isClient) {
				var potentialTarget = getData(player, reference, world);
				
				if (potentialTarget.isEmpty())
					return ActionResult.PASS;
				
				var source = potentialData.get();
				var target = potentialTarget.get();
				
				if (!source.verifyStampData(target) || !target.canUserStamp(player)) {
					tryPlaySound(player, SpectrumSoundEvents.SHATTER_LIGHT, 0.75F);
					return ActionResult.FAIL;
				}
				var interactable = target.source();
				
				var targetChanged = interactable.handleImpression(source.stamper(), player, source.reference(), world);
				source.notifySourceOfChange(target, targetChanged);
				
				if (!targetChanged) {
					tryPlaySound(player, SpectrumSoundEvents.SHATTER_HEAVY, 0.45F);
					return ActionResult.FAIL;
				}
				
				//Allow for 'rolling' linking for flow.
				player.ifPresent(user -> {
					if (!user.isSneaking()) {
						var newSource = target.source().recordStampData(player, reference, world);
						saveToNbt(stack, newSource);
						tryPlaySound(player, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.825F);
					} else {
						tryPlaySound(player, SpectrumSoundEvents.BLOCK_ONYX_BLOCK_CHIME, 0.825F);
					}
				});
			}

            return ActionResult.success(world.isClient());
        }
        else {
            var candidate = getData(player, reference, world);

            //Blank an interactable if shift clicking without a saved reference
            if (player.map(Entity::isSneaking).orElse(false)) {
                if (candidate.map(d -> d.canUserStamp(player)).orElse(false)) {
					if (!world.isClient) {
						candidate.get().source().clearImpression();
					}
                    tryPlaySound(player, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, 0.825F);
                }
                return ActionResult.success(world.isClient());
            }

            if (candidate.isPresent() && candidate.get().canUserStamp(player)) {
                saveToNbt(stack, candidate.get());
                tryPlaySound(player, SpectrumSoundEvents.CRYSTAL_STRIKE, 0.75F);
                return ActionResult.success(world.isClient());
            }
        }

        return super.useOnBlock(context);
    }

    public void clearData(Optional<PlayerEntity> player, ItemStack stack) {
        stack.getOrCreateNbt().remove(DATA);
        tryPlaySound(player, SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC, 1F);
    }


    private void tryPlaySound(Optional<PlayerEntity> player, SoundEvent sound, float volume) {
        player.ifPresent(p -> p.getWorld().playSoundFromEntity(null, p, sound, SoundCategory.PLAYERS, volume, 0.9F + p.getRandom().nextFloat() / 5F));
    }

    private void saveToNbt(ItemStack stack, Stampable.StampData data) {
        var stackNbt = stack.getOrCreateNbt();
        stackNbt.put(DATA, Stampable.saveStampingData(data));
    }

    private Optional<Stampable.StampData> getData(Optional<PlayerEntity> player, BlockReference reference, World world) {
        var data = Optional.<Stampable.StampData>empty();

        findData: {
            if (reference.getState().getBlock() instanceof Stampable interactable)
                data = Optional.ofNullable(interactable.recordStampData(player, reference, world));

            if (data.isPresent())
                break findData;

            data = reference.tryGetBlockEntity().map(be -> {
                if (be instanceof Stampable interactable)
                    return interactable.recordStampData(player, reference, world);
                return null;
            });
        }

        return data;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateNbt().contains(DATA)) {
            var data = Stampable.loadStampingData(world, stack.getSubNbt(DATA));

            if (data.isEmpty()) {
                tooltip.add(Text.translatable("item.spectrum.tuning_stamp.tooltip.missing").styled(style -> style.withColor(0xff757a)));
                return;
            }

            var stampData = data.get();
            var pos = stampData.reference().pos;

            tooltip.add(Text.translatable("item.spectrum.tuning_stamp.tooltip.linked", stampData.reference().getState().getBlock().getName()).styled(style -> style.withColor(0xffc98c)));
            tooltip.add(Text.translatable("item.spectrum.tuning_stamp.tooltip2", pos.getX(), pos.getY(), pos.getZ()).styled(style -> style.withColor(0xf99b89).withItalic(true)));
            return;
        }

        tooltip.add(Text.translatable("item.spectrum.tuning_stamp.tooltip").formatted(Formatting.GRAY));
    }

    @Override
    public void expandTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.spectrum.tuning_stamp.controls").styled(style -> style.withColor(0x66ff99)));
            tooltip.add(Text.translatable("item.spectrum.tuning_stamp.controls2").styled(style -> style.withColor(0x66ff99)));
            tooltip.add(Text.translatable("item.spectrum.tuning_stamp.controls3").styled(style -> style.withColor(0x66ff99)));
        } else {
            tooltip.add(Text.translatable("spectrum.tooltip.press_shift_for_controls").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
        }
    }
}