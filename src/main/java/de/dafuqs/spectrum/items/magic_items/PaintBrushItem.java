package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.inventories.PaintbrushScreenHandler;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PaintBrushItem extends Item {
	
	public static final Identifier UNLOCK_ADVANCEMENT_ID = SpectrumCommon.locate("progression/unlock_paintbrush");
	public static final Identifier UNLOCK_COLORING_ADVANCEMENT_ID = SpectrumCommon.locate("collect_pigment");
	
	public static final String COLOR_NBT_STRING = "Color";
	private static final Text GUI_TITLE = new TranslatableText("item.spectrum.paintbrush");
	
	public PaintBrushItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if(selected && entity instanceof ServerPlayerEntity serverPlayerEntity) {
			if(serverPlayerEntity.isSneaking() && canColor(serverPlayerEntity) && serverPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler) {
				serverPlayerEntity.openHandledScreen(createScreenHandlerFactory(world, serverPlayerEntity, stack));
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		Optional<InkColor> color = getColor(stack);
		color.ifPresent(inkColor -> tooltip.add(new TranslatableText("spectrum.ink.color." + inkColor)));
	}
	
	public static boolean canColor(PlayerEntity player) {
		return AdvancementHelper.hasAdvancement(player, UNLOCK_COLORING_ADVANCEMENT_ID);
	}
	
	public NamedScreenHandlerFactory createScreenHandlerFactory(World world, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new PaintbrushScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, serverPlayerEntity.getBlockPos()), itemStack), GUI_TITLE);
	}
	
	public static void setColor(ItemStack stack, @Nullable InkColor color) {
		NbtCompound compound = stack.getOrCreateNbt();
		if(color == null) {
			compound.remove(COLOR_NBT_STRING);
		} else {
			compound.putString(COLOR_NBT_STRING, color.toString());
		}
		stack.setNbt(compound);
	}
	
	public static Optional<InkColor> getColor(ItemStack stack) {
		NbtCompound compound = stack.getNbt();
		if (compound != null && compound.contains(COLOR_NBT_STRING)) {
			return Optional.of(InkColor.of(compound.getString(COLOR_NBT_STRING)));
		}
		return Optional.empty();
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if(canColor(context.getPlayer()) && cursedColor(context)) {
			return ActionResult.success(context.getWorld().isClient);
		}
		return super.useOnBlock(context);
	}
	
	private boolean cursedColor(ItemUsageContext context) {
		Optional<InkColor> inkColor = getColor(context.getStack());
		if(inkColor.isEmpty()) {
			return false;
		}
		
		// todo: drain ink / consume pigment
		
		Block newBlock = ColorHelper.cursedBlockColorVariant(context.getWorld(), context.getBlockPos(), inkColor.get().getDyeColor());
		if(newBlock == Blocks.AIR) {
			return false;
		}
		
		if(!context.getWorld().isClient) {
			context.getWorld().setBlockState(context.getBlockPos(), newBlock.getDefaultState());
			context.getWorld().playSound(null, context.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
		return true;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return super.use(world, user, hand);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		return super.useOnEntity(stack, user, entity, hand);
	}
	
}
