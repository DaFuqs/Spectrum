package de.dafuqs.spectrum.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import vazkii.patchouli.client.RenderHelper;

public class GuiOverlay {
	
	private static final TranslatableText noneText = new TranslatableText("item.spectrum.placement_staff.tooltip.none_in_inventory");
	
	private static ItemStack itemStackToRender;
	private static int amount;
	
	public static void register() {
		// That one is on Patchouli. ty <3
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			if (amount > -1 && itemStackToRender != null) {
				// Render the item stack next to the cursor
				Window window = MinecraftClient.getInstance().getWindow();
				int x = window.getScaledWidth() / 2 + 3;
				int y = window.getScaledHeight() / 2 + 3;
				
				matrixStack.push();
				matrixStack.scale(0.5F, 0.5F, 1F);
				RenderHelper.renderItemStackInGui(matrixStack, itemStackToRender, (x + 8) * 2, (y + 8) * 2);
				matrixStack.scale(2F, 2F, 1F);
				MinecraftClient.getInstance().textRenderer.draw(matrixStack, itemStackToRender.getName(), x + 18, y + 8, 0xFFFFFF);
				if (amount == 0) {
					MinecraftClient.getInstance().textRenderer.draw(matrixStack, noneText, x + 18, y + 19, 0xDDDDDD);
				} else {
					MinecraftClient.getInstance().textRenderer.draw(matrixStack, amount + "x", x + 18, y + 19, 0xDDDDDD);
				}
				matrixStack.pop();
			}
		});
	}
	
	public static void setItemStackToRender(ItemStack itemStack, int amount) {
		GuiOverlay.itemStackToRender = itemStack;
		GuiOverlay.amount = amount;
	}
	
	public static void doNotRenderOverlay() {
		GuiOverlay.amount = -1;
	}
	
	
}
