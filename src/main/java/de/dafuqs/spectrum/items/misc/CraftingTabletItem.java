package de.dafuqs.spectrum.items.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.inventories.CraftingTabletScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class CraftingTabletItem extends Item {

    private static final Text TITLE = new TranslatableText("item.spectrum.crafting_tablet");

    public CraftingTabletItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            Recipe storedRecipe = getStoredRecipe(world, itemStack);
            if(storedRecipe == null || user.isSneaking()) {
                user.openHandledScreen(createScreenHandlerFactory(world, (ServerPlayerEntity) user, itemStack));
            } else {
                tryCraftRecipe((ServerPlayerEntity) user, storedRecipe);
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
    }

    public NamedScreenHandlerFactory createScreenHandlerFactory(World world, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new CraftingTabletScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, serverPlayerEntity.getBlockPos()), itemStack), TITLE);
    }

    public static void setStoredRecipe(ItemStack craftingTabletItemStack, Recipe recipe) {
        NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateTag();
        nbtCompound.putString("recipe", recipe.getId().toString());
        craftingTabletItemStack.setTag(nbtCompound);
    }

    public static void clearStoredRecipe(ItemStack craftingTabletItemStack) {
        NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateTag();
        if (nbtCompound.contains("recipe")) {
            nbtCompound.remove("recipe");
            craftingTabletItemStack.setTag(nbtCompound);
        }
    }

    public static Recipe getStoredRecipe(World world, ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getTag();

        if(nbtCompound != null && nbtCompound.contains("recipe")) {
            String recipeString = nbtCompound.getString("recipe");
            Identifier recipeIdentifier = new Identifier(recipeString);

            Optional<? extends Recipe> optional = world.getRecipeManager().get(recipeIdentifier);
            if(optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }

    private void tryCraftRecipe(ServerPlayerEntity serverPlayerEntity, Recipe recipe) {
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
        Inventory playerInventory = serverPlayerEntity.getInventory();
        if (InventoryHelper.removeFromInventory(ingredients, playerInventory, true)) {
            InventoryHelper.removeFromInventory(ingredients, playerInventory, false);

            ItemStack craftingResult = recipe.getOutput().copy();
            boolean insertInventorySuccess = serverPlayerEntity.getInventory().insertStack(craftingResult);
            ItemEntity itemEntity;
            if (insertInventorySuccess && craftingResult.isEmpty()) {
                craftingResult.setCount(1);
                itemEntity = serverPlayerEntity.dropItem(craftingResult, false);
                if (itemEntity != null) {
                    itemEntity.setDespawnImmediately();
                }

                serverPlayerEntity.world.playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((serverPlayerEntity.getRandom().nextFloat() - serverPlayerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                serverPlayerEntity.currentScreenHandler.sendContentUpdates();
            } else {
                itemEntity = serverPlayerEntity.dropItem(craftingResult, false);
                if (itemEntity != null) {
                    itemEntity.resetPickupDelay();
                    itemEntity.setOwner(serverPlayerEntity.getUuid());
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        Recipe recipe = getStoredRecipe(world, itemStack);
        if (recipe == null) {
            tooltip.add(new TranslatableText("item.spectrum.crafting_tablet.tooltip.no_recipe").formatted(Formatting.GRAY));
        }
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        Recipe storedRecipe = CraftingTabletItem.getStoredRecipe(MinecraftClient.getInstance().world, stack);
        if(storedRecipe != null) {
            return Optional.of(new CraftingTabletTooltipData(storedRecipe));
        } else {
            return Optional.empty();
        }
    }

    public static class CraftingTabletTooltipData implements TooltipData {

        private final ItemStack itemStack;
        private final TranslatableText description;

        public CraftingTabletTooltipData(Recipe recipe) {
            this.itemStack = recipe.getOutput();
            this.description = new TranslatableText("item.spectrum.crafting_tablet.tooltip.recipe", this.itemStack.getCount(), this.itemStack.getName().getString());
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }

        public TranslatableText getDescription() {
            return this.description;
        }

    }

    @Environment(EnvType.CLIENT)
    public static class CraftingTabletTooltipComponent implements TooltipComponent {
        public static final Identifier TEXTURE = new Identifier("textures/gui/container/bundle.png");
        private final ItemStack itemStack;
        private final OrderedText description;

        public CraftingTabletTooltipComponent(CraftingTabletTooltipData data) {
            this.itemStack = data.getItemStack();
            this.description = data.getDescription().asOrderedText();
        }

        public int getHeight() {
            return this.getRows() * 20 + 2 + 4;
        }

        public int getWidth(TextRenderer textRenderer) {
            return this.getColumns() * 18 + 2;
        }

        public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z, TextureManager textureManager) {
            int n = x + 1;
            int o = y + 1;
            this.drawSlot(n, o, 0, textRenderer, matrices, itemRenderer, z, textureManager);

            this.drawOutline(x, y, 1, 1, matrices, z, textureManager);
        }

        @Override
        public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix4f, VertexConsumerProvider.Immediate immediate) {
            textRenderer.draw(this.description, (float)x + 26, (float) y + 6, 11053224, true, matrix4f, immediate, false, 0, 15728880);
        }

        private void drawSlot(int x, int y, int index, TextRenderer textRenderer, MatrixStack matrices, ItemRenderer itemRenderer, int z, TextureManager textureManager) {
            this.draw(matrices, x, y, z, textureManager, CraftingTabletTooltipComponent.Sprite.SLOT);
            itemRenderer.renderInGuiWithOverrides(itemStack, x + 1, y + 1, index);
            itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x + 1, y + 1);
            if (index == 0) {
                HandledScreen.drawSlotHighlight(matrices, x + 1, y + 1, z);
            }
        }

        private void drawOutline(int x, int y, int columns, int rows, MatrixStack matrices, int z, TextureManager textureManager) {
            this.draw(matrices, x, y, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_CORNER_TOP);
            this.draw(matrices, x + columns * 18 + 1, y, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_CORNER_TOP);

            int j;
            for(j = 0; j < columns; ++j) {
                this.draw(matrices, x + 1 + j * 18, y, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_HORIZONTAL_TOP);
                this.draw(matrices, x + 1 + j * 18, y + rows * 20, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_HORIZONTAL_BOTTOM);
            }

            for(j = 0; j < rows; ++j) {
                this.draw(matrices, x, y + j * 20 + 1, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_VERTICAL);
                this.draw(matrices, x + columns * 18 + 1, y + j * 20 + 1, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_VERTICAL);
            }

            this.draw(matrices, x, y + rows * 20, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_CORNER_BOTTOM);
            this.draw(matrices, x + columns * 18 + 1, y + rows * 20, z, textureManager, CraftingTabletTooltipComponent.Sprite.BORDER_CORNER_BOTTOM);
        }

        private void draw(MatrixStack matrices, int x, int y, int z, TextureManager textureManager, CraftingTabletTooltipComponent.Sprite sprite) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);
            DrawableHelper.drawTexture(matrices, x, y, z, (float)sprite.u, (float)sprite.v, sprite.width, sprite.height, 128, 128);
        }

        private int getColumns() {
            return Math.max(2, (int)Math.ceil(Math.sqrt((double)1 + 1.0D)));
        }

        private int getRows() {
            return (int)Math.ceil(((double) 1 + 1.0D) / (double)this.getColumns());
        }

        @Environment(EnvType.CLIENT)
        private static enum Sprite {
            SLOT(0, 0, 18, 20),
            BLOCKED_SLOT(0, 40, 18, 20),
            BORDER_VERTICAL(0, 18, 1, 20),
            BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
            BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
            BORDER_CORNER_TOP(0, 20, 1, 1),
            BORDER_CORNER_BOTTOM(0, 60, 1, 1);

            public final int u;
            public final int v;
            public final int width;
            public final int height;

            private Sprite(int u, int v, int width, int height) {
                this.u = u;
                this.v = v;
                this.width = width;
                this.height = height;
            }
        }
    }



}
