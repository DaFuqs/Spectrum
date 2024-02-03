package de.dafuqs.spectrum.helpers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public interface CustomItemRender {
    // Useful for recursion detection when e.g. the custom render method calls the vanilla render method.
    default boolean shouldRender(ItemStack stack, ModelTransformationMode mode) {
        return false;
    }

    // Allow calling the custom render function even if the item stack is currently marked as being used for custom rendering.
    default boolean allowRecursion(ItemStack stack, ModelTransformationMode mode) {
        return false;
    }
    default void render(ItemRenderer instance, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
    }

    // Subtly different from parent interface.
    interface Stack {
        default boolean shouldRender(ModelTransformationMode mode) {
            return false;
        }

        default void render(ItemRenderer instance, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        }

        // Subject to change at a moments notice.
        interface Extra {
            // CURRENT: Simple per-ItemStack boolean field for recursion checking.
            default boolean isCurrentlyRendering() {
                return false;
            }

            default void setCurrentlyRendering(boolean value) {
            }
            // RAII-style recursion guard.
            class RenderRecursionGuard implements AutoCloseable {
                private final ItemStack stack;
                public RenderRecursionGuard(ItemStack stack) {
                    this.stack = stack;
                    this.stack.setCurrentlyRendering(true);
                }
                @Override
                public void close() {
                    this.stack.setCurrentlyRendering(false);
                }
            }
        }
    }
}
