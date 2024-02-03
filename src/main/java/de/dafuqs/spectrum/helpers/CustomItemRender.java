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
    // Whether to render the current item using the custom mechanism or not.
    // Not directly responsible for recursion, for that use Stack.shouldRender.
    default boolean shouldRender(ItemStack stack, ModelTransformationMode mode) {
        return false;
    }

    // Allow calling the custom render function even if the item stack is currently marked as being used for custom rendering.
    default boolean allowRecursion(ItemStack stack, ModelTransformationMode mode) {
        return false;
    }
    // Actual render code.
    default void render(ItemRenderer instance, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
    }

    // Subtly different from parent interface.
    interface Stack {
        // Functions as a *directly* Stack-aware shouldRender.
        // Current responsibility: recursion handling.
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
            // NOTE: soft guard. Does not actually prevent code running if recursion is detected.
            // This guard serves more as a marker that affects the output of Stack.shouldRender.
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
