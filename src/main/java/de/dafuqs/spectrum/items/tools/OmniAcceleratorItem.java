package de.dafuqs.spectrum.items.tools;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.*;

import java.util.*;

public class OmniAcceleratorItem extends BundleItem implements InkPowered, ExtendedItemBarProvider, SlotBackgroundEffectProvider {
	
	protected static final InkAmount COST = new InkAmount(InkColors.YELLOW, 20);
	protected static final int CHARGE_TIME = 10;
	
	public OmniAcceleratorItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, user, hand);
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return CHARGE_TIME;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (!(user instanceof ServerPlayer player)) return stack;
		
		Optional<ItemStack> shootStackOptional = getFirstStack(world.registryAccess(), stack);
		if (shootStackOptional.isEmpty()) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
			return stack;
		}
		
		if (!InkPowered.tryDrainEnergy(player, COST)) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
			return stack;
		}
		
		ItemStack shootStack = shootStackOptional.get();
		OmniAcceleratorProjectile projectile = OmniAcceleratorProjectile.get(shootStack);
		if (projectile.createProjectile(shootStack, user, world, stack) != null) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), projectile.getSoundEffect(), SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!player.isCreative()) {
				decrementFirstItem(stack);
			}
		}
		
		return stack;
	}
	
	public static void decrementFirstItem(ItemStack acceleratorStack) {
		var comp = acceleratorStack.get(DataComponents.BUNDLE_CONTENTS);
		if (comp == null) return;
		
		var builder = new BundleContents.Mutable(BundleContents.EMPTY);
		var first = true;
		for (var stack : comp.itemsCopy()) {
			if (first) {
				stack.shrink(1);
				first = false;
			}
			if (!stack.isEmpty())
				builder.tryInsert(stack);
		}
		
		acceleratorStack.set(DataComponents.BUNDLE_CONTENTS, builder.toImmutable());
	}
	
	public static Optional<ItemStack> getFirstStack(HolderLookup.Provider wrapperLookup, ItemStack stack) {
		var contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
		return contents.isEmpty() ? Optional.empty() : Optional.of(contents.getItemUnsafe(0).copy());
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(COST.color());
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		addInkPoweredTooltip(tooltip);
	}
	
	public static class Renderer implements DynamicItemRenderer {
		public Renderer() {
		}
		
		@Override
		public void render(ItemRenderer renderer, ItemStack stack, ItemDisplayContext mode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model) {
			renderer.render(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
			Minecraft client = Minecraft.getInstance();
			if (mode != ItemDisplayContext.GUI || client.level == null) return;
			
			Optional<ItemStack> optionalStack = getFirstStack(client.level.registryAccess(), stack);
			if (optionalStack.isEmpty()) {
				return;
			}
			ItemStack bundledStack = optionalStack.get();
			
			BakedModel bundledModel = renderer.getModel(bundledStack, client.level, client.player, 0);
			
			matrices.pushPose();
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.translate(0.5F, 0.5F, 0.5F);
			renderer.render(bundledStack, mode, leftHanded, matrices, vertexConsumers, light, overlay, bundledModel);
			matrices.popPose();
		}
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, COST);
		return usable ? SlotEffect.BORDER_FADE : SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return InkColors.YELLOW_COLOR;
	}
	
	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean allowVanillaDurabilityBarRendering(@Nullable Player player, ItemStack stack) {
		if (player == null || player.getItemInHand(player.getUsedItemHand()) != stack)
			return true;
		
		return !player.isUsingItem();
	}
	
	@Override
	public ExtendedItemBarProvider.BarSignature getSignature(@Nullable Player player, ItemStack stack, int index) {
		if (player == null || !player.isUsingItem())
			return ExtendedItemBarProvider.PASS;
		
		var activeStack = player.getItemInHand(player.getUsedItemHand());
		if (activeStack != stack)
			return ExtendedItemBarProvider.PASS;
		
		var progress = Math.round(Mth.clampedLerp(0, 13, ((float) player.getTicksUsingItem() / CHARGE_TIME)));
		return new ExtendedItemBarProvider.BarSignature(2, 13, 13, progress, 1, InkColors.YELLOW_COLOR, 2, ExtendedItemBarProvider.DEFAULT_BACKGROUND_COLOR);
	}
}
