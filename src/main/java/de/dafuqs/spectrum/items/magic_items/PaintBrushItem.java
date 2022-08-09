package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.inventories.CraftingTabletScreenHandler;
import de.dafuqs.spectrum.inventories.PaintbrushScreenHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
			if(serverPlayerEntity.isSneaking() && serverPlayerEntity.currentScreenHandler instanceof PlayerScreenHandler) {
				serverPlayerEntity.openHandledScreen(createScreenHandlerFactory(world, serverPlayerEntity, stack));
			}
		}
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
	
}
