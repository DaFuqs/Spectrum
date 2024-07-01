package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.ExpandedStatTooltip;
import de.dafuqs.spectrum.api.item.Stampable;
import de.dafuqs.spectrum.helpers.BlockReference;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

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
            var potentialTarget = getData(player, reference, world);

            if (potentialTarget.isEmpty())
                return ActionResult.PASS;

            var source = potentialData.get();
            var target = potentialTarget.get();

            if (!source.verifyStampData(target) || !target.canUserStamp(player)) {
                tryPlaySound(player, SpectrumSoundEvents.SHATTER_LIGHT);
                return ActionResult.FAIL;
            }
            var interactable = target.source();

            var targetChanged = interactable.handleImpression(source.stamper(), player, source.reference(), world);
            source.notifySourceOfChange(target, targetChanged);

            if (!targetChanged) {
                tryPlaySound(player, SpectrumSoundEvents.SHATTER_HEAVY);
                return ActionResult.FAIL;
            }

            //Allow for 'rolling' linking for flow.
            player.ifPresent(user -> {
                var newSource = target.source().recordStampData(player, reference, world);
                saveToNbt(stack, newSource);
                tryPlaySound(player, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME);
            });

            return ActionResult.success(world.isClient());
        }
        else {
            var candidate = getData(player, reference, world);

            //Blank an interactable if shift clicking without a saved reference
            if (player.map(Entity::isSneaking).orElse(false)) {
                if (candidate.map(d -> d.canUserStamp(player)).orElse(false)) {
                    candidate.get().source().clearImpression();
                    tryPlaySound(player, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);
                }
                return ActionResult.success(world.isClient());
            }

            if (candidate.isPresent() && candidate.get().canUserStamp(player)) {
                saveToNbt(stack, candidate.get());
                tryPlaySound(player, SpectrumSoundEvents.CRYSTAL_STRIKE);
                return ActionResult.success(world.isClient());
            }
        }

        return super.useOnBlock(context);
    }

    public void clearData(Optional<PlayerEntity> player, ItemStack stack) {
        stack.getOrCreateNbt().remove(DATA);
        tryPlaySound(player, SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC);
    }


    private void tryPlaySound(Optional<PlayerEntity> player, SoundEvent sound) {
        player.ifPresent(p -> p.getWorld().playSoundFromEntity(null, p, sound, SoundCategory.PLAYERS, 0.75F, 0.9F + p.getRandom().nextFloat() / 5F));
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