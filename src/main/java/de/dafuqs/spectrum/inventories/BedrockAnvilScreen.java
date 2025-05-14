package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.lwjgl.glfw.*;

@Environment(EnvType.CLIENT)
public class BedrockAnvilScreen extends ItemCombinerScreen<BedrockAnvilScreenHandler> {
	
	private static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/gui/container/bedrock_anvil.png");
	private final Player player;
	private EditBox nameField;
	private EditBox loreField;
	
	public BedrockAnvilScreen(BedrockAnvilScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title, TEXTURE);
		this.player = inventory.player;
		
		this.titleLabelX = 60;
		this.titleLabelY = this.titleLabelY + 2;
		this.inventoryLabelY = 95;
		this.imageHeight = 190;
	}
	
	@Override
	protected void subInit() {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		
		this.nameField = new EditBox(this.font, i + 62, j + 24, 98, 12, Component.translatable("container.spectrum.bedrock_anvil"));
		this.nameField.setTextColor(-1);
		this.nameField.setTextColorUneditable(-1);
		this.nameField.setBordered(false);
		this.nameField.setMaxLength(AnvilMenu.MAX_NAME_LENGTH);
		this.nameField.setResponder(this::onRenamed);
		this.nameField.setValue("");
		this.addWidget(this.nameField);
		this.nameField.setEditable((this.menu).getSlot(0).hasItem());
		
		this.loreField = new EditBox(this.font, i + 45, j + 76, 116, 12, Component.translatable("container.spectrum.bedrock_anvil.lore"));
		this.loreField.setTextColor(-1);
		this.loreField.setTextColorUneditable(-1);
		this.loreField.setBordered(false);
		this.loreField.setMaxLength(BedrockAnvilScreenHandler.MAX_LORE_LENGTH);
		this.loreField.setResponder(this::onLoreChanged);
		this.loreField.setValue("");
		this.addWidget(this.loreField);
		this.loreField.setEditable((this.menu).getSlot(0).hasItem());
		
		this.nameField.setEditable(false);
		this.loreField.setEditable(false);
	}
	
	@Override
	protected void setInitialFocus() {
		this.setInitialFocus(this.nameField);
	}
	
	@Override
	public void resize(Minecraft client, int width, int height) {
		String name = this.nameField.getValue();
		String lore = this.loreField.getValue();
		this.init(client, width, height);
		this.nameField.setValue(name);
		this.loreField.setValue(lore);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_TAB) {
			super.keyPressed(keyCode, scanCode, modifiers);
			return true;
		}
		
		GuiEventListener focused = this.getFocused();
		return (focused == null || !focused.keyPressed(keyCode, scanCode, modifiers)) || focused instanceof EditBox textFieldWidget && textFieldWidget.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);

	}
	
	private void onRenamed(String name) {
		Slot slot = this.menu.getSlot(0);
		if (slot.hasItem()) {
			String string = name;
			if (!slot.getItem().has(DataComponents.CUSTOM_NAME) && string.equals(slot.getItem().getHoverName().getString())) {
				string = "";
			}
			
			if ((this.menu).setNewItemName(string)) {
				ClientPlayNetworking.send(new RenameItemInBedrockAnvilPayload(name));
			}
		}
	}
	
	private void onLoreChanged(String lore) {
		Slot slot = this.menu.getSlot(0);
		if (slot.hasItem()) {
			String string = lore;
			if (!LoreHelper.hasLore(slot.getItem()) && string.equals(LoreHelper.getStringFromLoreTextArray(LoreHelper.getLoreList(slot.getItem())))) {
				string = "";
			}
			
			if (this.menu.setNewItemLore(string)) {
				ClientPlayNetworking.send(new AddLoreBedrockAnvilPayload(lore));
			}
		}
	}
	
	@Override
	protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
		super.renderLabels(context, mouseX, mouseY);
		
		context.drawString(font, Component.translatable("container.spectrum.bedrock_anvil.lore"), inventoryLabelX, 76, 4210752, false);
		
		int levelCost = (this.menu).getLevelCost();
		if (levelCost > 0 || this.menu.getSlot(2).hasItem()) {
			int textColor = 8453920;
			Component costText;
			if (!menu.getSlot(2).hasItem()) {
				costText = null;
			} else {
				costText = Component.translatable("container.repair.cost", levelCost);
				if (!menu.getSlot(2).mayPickup(this.player)) {
					textColor = 16736352;
				}
			}
			
			if (costText != null) {
				int k = this.imageWidth - 8 - this.font.width(costText) - 2;
				context.fill(k - 2, 67 + 24, this.imageWidth - 8, 79 + 24, 1325400064);
				context.drawString(font, costText, k, 93, textColor, true);
			}
		}
	}
	
	@Override
	protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
		super.renderBg(context, delta, mouseX, mouseY);
		
		// the text field backgrounds
		boolean hasStack = menu.getSlot(0).hasItem();
		context.blit(TEXTURE, this.leftPos + 59, this.topPos + 20, 0, this.imageHeight + (hasStack ? 0 : 16), 110, 16);
		context.blit(TEXTURE, this.leftPos + 42, this.topPos + 72, 0, this.imageHeight + (hasStack ? 32 : 48), 127, 16);
	}
	
	@Override
	public void renderFg(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		this.nameField.render(drawContext, mouseX, mouseY, delta);
		this.loreField.render(drawContext, mouseX, mouseY, delta);
	}
	
	@Override
	protected void renderErrorIcon(GuiGraphics context, int x, int y) {
		if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
			context.blit(TEXTURE, x + 99, y + 45, this.imageWidth, 0, 28, 21);
		}
	}
	
	@Override
	public void slotChanged(AbstractContainerMenu handler, int slotId, ItemStack stack) {
		// TODO: test & cleanup pigment code
		if (slotId == 0) {
			boolean stackEmpty = stack.isEmpty();
			
			this.nameField.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
			if (!(this.menu.getSlot(1).getItem().getItem() instanceof PigmentItem)) {
				if (stack.getHoverName() instanceof MutableComponent mutableText) {
					if (mutableText.getStyle().getColor() == null) {
						this.nameField.setTextColor(-1);
					} else {
						this.nameField.setTextColor(mutableText.getStyle().getColor().getValue());
					}
				} else {
					this.nameField.setTextColor(-1);
				}
			}
			
			this.nameField.setEditable(!stack.isEmpty());
			this.nameField.setCanLoseFocus(!stackEmpty);
			
			this.loreField.setValue(stackEmpty ? "" : LoreHelper.getStringFromLoreTextArray(LoreHelper.getLoreList(stack)));
			this.loreField.setEditable(!stackEmpty);
			this.nameField.setCanLoseFocus(!stackEmpty);
			
			this.setFocused(this.nameField);
		}
		if (slotId == 1) {
			if (stack.getItem() instanceof PigmentItem pigmentItem) {
				this.nameField.setTextColor(pigmentItem.getInkColor().getColorInt());
			} else {
				if (this.menu.getSlot(0).getItem().getHoverName() instanceof MutableComponent mutableText) {
					if (mutableText.getStyle().getColor() == null) {
						this.nameField.setTextColor(-1);
					} else {
						this.nameField.setTextColor(mutableText.getStyle().getColor().getValue());
					}
				} else {
					this.nameField.setTextColor(-1);
				}
			}
		}
	}
	
}
