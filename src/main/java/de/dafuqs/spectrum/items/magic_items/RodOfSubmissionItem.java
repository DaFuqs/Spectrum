package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.compat.claims.GenericClaimModsCompat;
import de.dafuqs.spectrum.energy.InkCost;
import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.items.PrioritizedEntityInteraction;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RodOfSubmissionItem extends Item implements InkPowered, PrioritizedEntityInteraction {

    public static final InkColor USED_COLOR = InkColors.GRAY;
    public static final InkCost RIDE_ENTITY_COST = new InkCost(USED_COLOR, 1000);

    public RodOfSubmissionItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);

        tooltip.add(Text.translatable("item.spectrum.rod_of_submission.tooltip").formatted(Formatting.GRAY));
        addInkPoweredTooltip(tooltip);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        Vec3d pos = entity.getPos();

        if (!GenericClaimModsCompat.canInteractWith(world, entity, user)) {
            return ActionResult.FAIL;
        }

        if (!world.isClient) {
            if (rideEntity(user, entity)) {
                SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, entity.getPos(), SpectrumParticleTypes.GRAY_SPARKLE_RISING, 10, Vec3d.ZERO, new Vec3d(0.2, 0.2, 0.2));
                SpectrumS2CPacketSender.playParticleWithExactVelocity((ServerWorld) world, entity.getPos(), SpectrumParticleTypes.GRAY_EXPLOSION, 1, Vec3d.ZERO);
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundCategory.PLAYERS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            } else {
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            }
        }
        return ActionResult.success(world.isClient);
    }

    public boolean rideEntity(PlayerEntity user, Entity entity) {
        if (entity.hasPassengers()) return false;
        if (InkPowered.tryDrainEnergy(user, RIDE_ENTITY_COST)) {
            user.startRiding(entity, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BRUSH;
    }

    @Override
    public List<InkColor> getUsedColors() {
        return List.of(USED_COLOR);
    }
}
