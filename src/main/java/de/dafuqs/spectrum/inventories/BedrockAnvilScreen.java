package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.LoreHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.networking.SpectrumC2SPackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BedrockAnvilScreen extends HandledScreen<BedrockAnvilScreenHandler> implements ScreenHandlerListener {

   private static final Identifier TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/bedrock_anvil.png");
   private TextFieldWidget nameField;
   private TextFieldWidget loreField;
   private final PlayerEntity player;

   public BedrockAnvilScreen(BedrockAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
      super(handler, inventory, title);
      this.player = inventory.player;

      this.titleX = 60;
      this.playerInventoryTitleY = 92;
      this.backgroundHeight = 190;
   }

   public void handledScreenTick() {
      super.handledScreenTick();
      this.nameField.tick();
      this.loreField.tick();
   }

   protected void init() {
      super.init();
      this.setup();
      handler.addListener(this);
   }

   public void removed() {
      super.removed();
      handler.removeListener(this);
      client.keyboard.setRepeatEvents(false);
   }

   public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
      renderBackground(matrices);
      super.render(matrices, mouseX, mouseY, delta);

      RenderSystem.disableBlend();
      renderForeground(matrices, mouseX, mouseY, delta);
      drawMouseoverTooltip(matrices, mouseX, mouseY);
   }

   protected void setup() {
      client.keyboard.setRepeatEvents(true);
      int i = (this.width - this.backgroundWidth) / 2;
      int j = (this.height - this.backgroundHeight) / 2;

      this.nameField = new TextFieldWidget(this.textRenderer, i + 62, j + 24, 103, 12, new TranslatableText("container.spectrum.bedrock_anvil"));
      this.nameField.setFocusUnlocked(false);
      this.nameField.setEditableColor(-1);
      this.nameField.setUneditableColor(-1);
      this.nameField.setDrawsBackground(false);
      this.nameField.setMaxLength(50);
      this.nameField.setText("");
      this.nameField.setChangedListener(this::onRenamed);
      this.addSelectableChild(this.nameField);
      this.setInitialFocus(this.nameField);
      this.nameField.setEditable(false);

      this.loreField = new TextFieldWidget(this.textRenderer, i + 45, j + 76, 127, 12, new TranslatableText("container.spectrum.bedrock_anvil.lore"));
      this.loreField.setFocusUnlocked(false);
      this.loreField.setEditableColor(-1);
      this.loreField.setUneditableColor(-1);
      this.loreField.setDrawsBackground(false);
      this.loreField.setMaxLength(200);
      this.loreField.setText("");
      this.loreField.setChangedListener(this::onLoreChanged);
      this.addSelectableChild(this.loreField);
      this.loreField.setEditable(false);
   }

   public void resize(MinecraftClient client, int width, int height) {
      String string = this.nameField.getText();
      init(client, width, height);
      nameField.setText(string);

      String string2 = this.loreField.getText();
      init(client, width, height);
      loreField.setText(string2);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
         client.player.closeHandledScreen();
      }

      if(keyCode == GLFW.GLFW_KEY_TAB) {
         Element focusedElement = getFocused();
         if(focusedElement == this.nameField) {
            this.nameField.setTextFieldFocused(false);
            setFocused(this.loreField);
         } else if(focusedElement == this.loreField) {
            this.loreField.setTextFieldFocused(false);
            setFocused(this.nameField);
         }
      }

      return this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.isActive()
              || this.loreField.keyPressed(keyCode, scanCode, modifiers) || this.loreField.isActive()
              || super.keyPressed(keyCode, scanCode, modifiers);
   }

   private void onRenamed(String name) {
      if (!name.isEmpty()) {
         String string = name;
         Slot slot = handler.getSlot(0);
         if (slot != null && slot.hasStack() && !slot.getStack().hasCustomName() && name.equals(slot.getStack().getName().getString())) {
            string = "";
         }

         handler.setNewItemName(string);

         PacketByteBuf packetByteBuf = PacketByteBufs.create();
         packetByteBuf.writeString(name);
         ClientPlayNetworking.send(SpectrumC2SPackets.RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID, packetByteBuf);
      }
   }

   private void onLoreChanged(String lore) {
      handler.setNewItemLore(lore);

      PacketByteBuf packetByteBuf = PacketByteBufs.create();
      packetByteBuf.writeString(lore);
      ClientPlayNetworking.send(SpectrumC2SPackets.ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID, packetByteBuf);
   }

   protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
      RenderSystem.disableBlend();
      super.drawForeground(matrices, mouseX, mouseY);

      this.textRenderer.draw(matrices, new TranslatableText("container.spectrum.bedrock_anvil.lore"), playerInventoryTitleX, 76, 4210752);

      int levelCost = (this.handler).getLevelCost();
      if (levelCost > 0 || this.handler.getSlot(2).hasStack()) {
         int textColor = 8453920;
         Text costText;
         if (!handler.getSlot(2).hasStack()) {
            costText = null;
         } else {
            costText = new TranslatableText("container.repair.cost", levelCost);
            if (!handler.getSlot(2).canTakeItems(this.player)) {
               textColor = 16736352;
            }
         }

         if (costText != null) {
            int k = this.backgroundWidth - 8 - this.textRenderer.getWidth(costText) - 2;
            fill(matrices, k - 2, 67 + 24, this.backgroundWidth - 8, 79 + 24, 1325400064);
            this.textRenderer.drawWithShadow(matrices, costText, (float)k, 93, textColor);
         }
      }
   }

   @Override
   protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, TEXTURE);
      int i = (this.width - this.backgroundWidth) / 2;
      int j = (this.height - this.backgroundHeight) / 2;

      // the background
      drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

      // the text field backgrounds
      drawTexture(matrices, i + 59, j + 20, 0, this.backgroundHeight + (handler.getSlot(0).hasStack() ? 0 : 16), 110, 16);
      drawTexture(matrices, i + 42, j + 72, 0, this.backgroundHeight + (handler.getSlot(0).hasStack() ? 32 : 48), 127, 16);

      if ((handler.getSlot(0).hasStack() || handler.getSlot(1).hasStack()) && !handler.getSlot(2).hasStack()) {
         drawTexture(matrices, i + 99, j + 45, this.backgroundWidth, 0, 28, 21);
      }
   }

   public void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
      this.nameField.render(matrices, mouseX, mouseY, delta);
      this.loreField.render(matrices, mouseX, mouseY, delta);
   }

   public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
      if (slotId == 0) {
         if(stack.isEmpty()) {
            this.nameField.setEditable(false);
            this.loreField.setEditable(false);
            this.nameField.setChangedListener(null);
            this.loreField.setChangedListener(null);
            this.nameField.setText("");
            this.loreField.setText("");
            this.nameField.setChangedListener(this::onRenamed);
            this.loreField.setChangedListener(this::onLoreChanged);
         } else {
            this.nameField.setEditable(true);
            this.loreField.setEditable(true);
            this.nameField.setText(stack.getName().getString());

            String loreString = LoreHelper.getStringFromLoreTextArray(LoreHelper.getLoreList(stack));
            this.loreField.setText(loreString);
         }
         this.setFocused(this.nameField);
      }
   }

   @Override
   public void onPropertyUpdate(ScreenHandler handler, int property, int value) {

   }

}
