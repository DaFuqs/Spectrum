package de.dafuqs.spectrum.helpers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public interface CustomItemRender {
    interface Provider {
        // Technically an Object, but in practice, CustomItemRender.Render.
        default Object getRender() {
            return null;
        }
    }
    @Environment(EnvType.CLIENT)
    interface Render {
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
                    private final CustomItemRender.Render.Stack.Extra stack;

                    public RenderRecursionGuard(CustomItemRender.Render.Stack.Extra stack) {
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
    class DefaultProviders {
        public static class ItemRender implements Render {
            public ItemRender() {}
            // default impl. Relies on the UNSTABLE CustomItemRender.Stack.Extra
            @Override
            public boolean shouldRender(ItemStack stack, ModelTransformationMode mode) {
                return CustomItemRender.Render.super.shouldRender(stack, mode) && !((Render.Stack.Extra)stack.getRender()).isCurrentlyRendering();
            }

            // Fallback. Only called if the item supporting custom rendering doesn't implement its own render method.
            @Override
            public void render(ItemRenderer instance, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
                try (Render.Stack.Extra.RenderRecursionGuard ignored = new Render.Stack.Extra.RenderRecursionGuard((CustomItemRender.Render.Stack.Extra)stack.getRender())) {
                    instance.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
                }
            }
        }

        public static class ItemStackRender implements CustomItemRender.Render.Stack, CustomItemRender.Render.Stack.Extra {
            private final ItemStack stack;
            public ItemStackRender(ItemStack s) {
                this.stack = s;
            }
            @Override
            public boolean shouldRender (ModelTransformationMode mode){
                CustomItemRender.Render item = (Render) stack.getItem().getRender();
                return (!isCurrentlyRendering() || item.allowRecursion(stack, mode)) && item.shouldRender(stack, mode);
            }

            @Override
            public void render (ItemRenderer instance, ModelTransformationMode mode,boolean leftHanded, MatrixStack
                    matrices, VertexConsumerProvider vertexConsumers,int light, int overlay, BakedModel model){
                ((CustomItemRender.Render)stack.getItem().getRender()).render(instance, stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
            }

            // UNSTABLE CustomItemRender.Stack.Extra implementation.
            boolean currentlyRendering = false;
            @Override
            public boolean isCurrentlyRendering () {
                return currentlyRendering;
            }
            @Override
            public void setCurrentlyRendering ( boolean value){
                currentlyRendering = value;
            }
        }
    }
}
