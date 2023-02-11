package de.dafuqs.spectrum.particle.render;

import net.minecraft.client.render.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import org.lwjgl.system.*;

import java.nio.*;

public class TransparentVertexConsumerProvider implements VertexConsumerProvider {

    private final VertexConsumerProvider delegate;

    public TransparentVertexConsumerProvider(final VertexConsumerProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public VertexConsumer getBuffer(final RenderLayer layer) {
        return new WrappedVertexConsumer(delegate.getBuffer(layer));
    }

    private static final class WrappedVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;

        private WrappedVertexConsumer(final VertexConsumer delegate) {
            this.delegate = delegate;
        }

        @Override
        public VertexConsumer vertex(final double x, final double y, final double z) {
            return delegate.vertex(x, y, z);
        }

        @Override
        public VertexConsumer color(final int red, final int green, final int blue, final int alpha) {
            return delegate.color(red, green, blue, alpha >> 1);
        }

        @Override
        public VertexConsumer texture(final float u, final float v) {
            return delegate.texture(u, v);
        }

        @Override
        public VertexConsumer overlay(final int u, final int v) {
            return delegate.overlay(u, v);
        }

        @Override
        public VertexConsumer light(final int u, final int v) {
            return delegate.light(u, v);
        }

        @Override
        public VertexConsumer normal(final float x, final float y, final float z) {
            return delegate.normal(x, y, z);
        }

        @Override
        public void next() {
            delegate.next();
        }

        @Override
        public void vertex(final float x, final float y, final float z, final float red, final float green, final float blue, final float alpha, final float u, final float v, final int overlay, final int light, final float normalX, final float normalY, final float normalZ) {
            delegate.vertex(x, y, z, red, green, blue, alpha * 0.5f, u, v, overlay, light, normalX, normalY, normalZ);
        }

        @Override
        public void fixedColor(final int red, final int green, final int blue, final int alpha) {
            delegate.fixedColor(red, green, blue, alpha);
        }

        @Override
        public void unfixColor() {
            delegate.unfixColor();
        }

        @Override
        public VertexConsumer color(final float red, final float green, final float blue, final float alpha) {
            return delegate.color(red, green, blue, alpha * 0.5F);
        }

        @Override
        public VertexConsumer color(final int argb) {
            return color(ColorHelper.Argb.getRed(argb), ColorHelper.Argb.getGreen(argb), ColorHelper.Argb.getBlue(argb), ColorHelper.Argb.getAlpha(argb) >> 1);
        }

        @Override
        public VertexConsumer light(final int uv) {
            return delegate.light(uv);
        }

        @Override
        public VertexConsumer overlay(final int uv) {
            return delegate.overlay(uv);
        }

        @Override
        public void quad(final MatrixStack.Entry matrixEntry, final BakedQuad quad, final float red, final float green, final float blue, final int light, final int overlay) {
            quad(matrixEntry, quad, new float[]{1.0F, 1.0F, 1.0F, 1.0F}, red, green, blue, new int[]{light, light, light, light}, overlay, false);
        }

        @Override
        public void quad(final MatrixStack.Entry matrixEntry, final BakedQuad quad, final float[] brightnesses, final float red, final float green, final float blue, final int[] lights, final int overlay, final boolean useQuadColorData) {
            final float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
            final int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
            final int[] js = quad.getVertexData();
            final Vec3i vec3i = quad.getFace().getVector();
            final Vec3f vec3f = new Vec3f((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            final Matrix4f matrix4f = matrixEntry.getPositionMatrix();
            vec3f.transform(matrixEntry.getNormalMatrix());
            final int j = js.length / 8;
            final MemoryStack memoryStack = MemoryStack.stackPush();

            try {
                final ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
                final IntBuffer intBuffer = byteBuffer.asIntBuffer();

                for (int k = 0; k < j; ++k) {
                    intBuffer.clear();
                    intBuffer.put(js, k * 8, 8);
                    final float f = byteBuffer.getFloat(0);
                    final float g = byteBuffer.getFloat(4);
                    final float h = byteBuffer.getFloat(8);
                    final float o;
                    final float p;
                    final float q;
                    float m;
                    float n;
                    if (useQuadColorData) {
                        final float l = (float) (byteBuffer.get(12) & 255) / 255.0F;
                        m = (float) (byteBuffer.get(13) & 255) / 255.0F;
                        n = (float) (byteBuffer.get(14) & 255) / 255.0F;
                        o = l * fs[k] * red;
                        p = m * fs[k] * green;
                        q = n * fs[k] * blue;
                    } else {
                        o = fs[k] * red;
                        p = fs[k] * green;
                        q = fs[k] * blue;
                    }

                    final int r = is[k];
                    m = byteBuffer.getFloat(16);
                    n = byteBuffer.getFloat(20);
                    final Vector4f vector4f = new Vector4f(f, g, h, 1.0F);
                    vector4f.transform(matrix4f);
                    vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), o, p, q, 1.0F, m, n, overlay, r, vec3f.getX(), vec3f.getY(), vec3f.getZ());
                }
            } catch (final Throwable var33) {
                try {
                    memoryStack.close();
                } catch (final Throwable var32) {
                    var33.addSuppressed(var32);
                }

                throw var33;
            }

            memoryStack.close();
        }

        @Override
        public VertexConsumer vertex(final Matrix4f matrix, final float x, final float y, final float z) {
            return delegate.vertex(matrix, x, y, z);
        }

        @Override
        public VertexConsumer normal(final Matrix3f matrix, final float x, final float y, final float z) {
            return delegate.normal(matrix, x, y, z);
        }
    }
}