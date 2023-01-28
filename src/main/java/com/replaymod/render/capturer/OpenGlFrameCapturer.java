package com.replaymod.render.capturer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.replaymod.core.versions.MCVer;
import com.replaymod.render.frame.OpenGlFrame;
import com.replaymod.render.rendering.Frame;
import com.replaymod.render.rendering.FrameCapturer;
import com.replaymod.render.utils.ByteBufferPool;
import de.johni0702.minecraft.gui.utils.lwjgl.Dimension;
import de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import de.johni0702.minecraft.gui.utils.lwjgl.WritableDimension;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.nio.ByteBuffer;

import static com.mojang.blaze3d.platform.GlStateManager.*;
import static com.replaymod.core.versions.MCVer.resizeWindow;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public abstract class OpenGlFrameCapturer<F extends Frame, D extends CaptureData> implements FrameCapturer<F> {
    protected final LevelRenderer levelRenderer;
    protected final RenderInfo renderInfo;
    protected int framesDone;
    private RenderTarget frameBuffer;

    protected final Minecraft mc = MCVer.getMinecraft();

    public OpenGlFrameCapturer(LevelRenderer levelRenderer, RenderInfo renderInfo) {
        this.levelRenderer = levelRenderer;
        this.renderInfo = renderInfo;
    }

    protected final ReadableDimension frameSize = new ReadableDimension() {
        @Override
        public int getWidth() {
            return getFrameWidth();
        }

        @Override
        public int getHeight() {
            return getFrameHeight();
        }

        @Override
        public void getSize(WritableDimension dest) {
            dest.setSize(getWidth(), getHeight());
        }
    };

    protected int getFrameWidth() {
        return renderInfo.getFrameSize().getWidth();
    }

    protected int getFrameHeight() {
        return renderInfo.getFrameSize().getHeight();
    }

    protected RenderTarget frameBuffer() {
        if (frameBuffer == null) {
            frameBuffer = mc.getMainRenderTarget();
        }
        return frameBuffer;
    }

    @Override
    public boolean isDone() {
        return framesDone >= renderInfo.getTotalFrames();
    }

    protected OpenGlFrame renderFrame(int frameId, float partialTicks) {
        return renderFrame(frameId, partialTicks, null);
    }

    protected OpenGlFrame renderFrame(int frameId, float partialTicks, D captureData) {
        resizeWindow(mc, getFrameWidth(), getFrameHeight());

        GL11.glPushMatrix();
        frameBuffer().bindWrite(true);

        RenderSystem.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT
                , false
        );
        RenderSystem.enableTexture();

        levelRenderer.renderWorld(partialTicks, captureData);

        frameBuffer().unbindWrite();
        GL11.glPopMatrix();

        return captureFrame(frameId, captureData);
    }

    protected OpenGlFrame captureFrame(int frameId, D captureData) {
        ByteBuffer buffer = ByteBufferPool.allocate(getFrameWidth() * getFrameHeight() * 4);
        frameBuffer().bindWrite(true);
        GL11.glReadPixels(0, 0, getFrameWidth(), getFrameHeight(), GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer);
        frameBuffer().unbindWrite();
        buffer.rewind();

        return new OpenGlFrame(frameId, new Dimension(getFrameWidth(), getFrameHeight()), 4, buffer);
    }

    @Override
    public void close() throws IOException {
    }
}
