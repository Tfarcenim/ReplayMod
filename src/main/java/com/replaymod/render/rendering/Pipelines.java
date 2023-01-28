package com.replaymod.render.rendering;

import com.replaymod.render.RenderSettings;
import com.replaymod.render.blend.BlendFrameCapturer;
import com.replaymod.render.capturer.*;
import com.replaymod.render.frame.*;
import com.replaymod.render.hooks.EntityRendererHandler;
import com.replaymod.render.processor.*;
import com.replaymod.render.utils.PixelBufferObject;

import java.util.Map;

public class Pipelines {
    public static Pipeline newPipeline(RenderSettings.RenderMethod method, RenderInfo renderInfo, FrameConsumer<BitmapFrame> consumer) {
        switch (method) {
            case DEFAULT:
                return newDefaultPipeline(renderInfo, consumer);
            case STEREOSCOPIC:
                return newStereoscopicPipeline(renderInfo, consumer);
            case CUBIC:
                return newCubicPipeline(renderInfo, consumer);
            case EQUIRECTANGULAR:
                return newEquirectangularPipeline(renderInfo, consumer);
            case ODS:
                return newODSPipeline(renderInfo, consumer);
            case BLEND:
                throw new UnsupportedOperationException("Use newBlendPipeline instead!");
        }
        throw new UnsupportedOperationException("Unknown method: " + method);
    }

    public static Pipeline<OpenGlFrame, BitmapFrame> newDefaultPipeline(RenderInfo renderInfo, FrameConsumer<BitmapFrame> consumer) {
        RenderSettings settings = renderInfo.getRenderSettings();
        LevelRenderer levelRenderer = new EntityRendererHandler(settings, renderInfo);
        FrameCapturer<OpenGlFrame> capturer;
        if (PixelBufferObject.SUPPORTED || settings.isDepthMap()) {
            capturer = new SimplePboOpenGlFrameCapturer(levelRenderer, renderInfo);
        } else {
            capturer = new SimpleOpenGlFrameCapturer(levelRenderer, renderInfo);
        }
        return new Pipeline<>(levelRenderer, capturer, new OpenGlToBitmapProcessor(), consumer);
    }

    public static Pipeline<StereoscopicOpenGlFrame, BitmapFrame> newStereoscopicPipeline(RenderInfo renderInfo, FrameConsumer<BitmapFrame> consumer) {
        RenderSettings settings = renderInfo.getRenderSettings();
        LevelRenderer levelRenderer = new EntityRendererHandler(settings, renderInfo);
        FrameCapturer<StereoscopicOpenGlFrame> capturer;
        if (PixelBufferObject.SUPPORTED || settings.isDepthMap()) {
            capturer = new StereoscopicPboOpenGlFrameCapturer(levelRenderer, renderInfo);
        } else {
            capturer = new StereoscopicOpenGlFrameCapturer(levelRenderer, renderInfo);
        }
        return new Pipeline<>(levelRenderer, capturer, new StereoscopicToBitmapProcessor(), consumer);
    }

    public static Pipeline<CubicOpenGlFrame, BitmapFrame> newCubicPipeline(RenderInfo renderInfo, FrameConsumer<BitmapFrame> consumer) {
        RenderSettings settings = renderInfo.getRenderSettings();
        LevelRenderer levelRenderer = new EntityRendererHandler(settings, renderInfo);
        FrameCapturer<CubicOpenGlFrame> capturer;
        if (PixelBufferObject.SUPPORTED || settings.isDepthMap()) {
            capturer = new CubicPboOpenGlFrameCapturer(levelRenderer, renderInfo, settings.getVideoWidth() / 4);
        } else {
            capturer = new CubicOpenGlFrameCapturer(levelRenderer, renderInfo, settings.getVideoWidth() / 4);
        }
        return new Pipeline<>(levelRenderer, capturer, new CubicToBitmapProcessor(), consumer);
    }

    public static Pipeline<CubicOpenGlFrame, BitmapFrame> newEquirectangularPipeline(RenderInfo renderInfo, FrameConsumer<BitmapFrame> consumer) {
        RenderSettings settings = renderInfo.getRenderSettings();
        LevelRenderer levelRenderer = new EntityRendererHandler(settings, renderInfo);

        EquirectangularToBitmapProcessor processor = new EquirectangularToBitmapProcessor(settings.getVideoWidth(),
                settings.getVideoHeight(), settings.getSphericalFovX());

        FrameCapturer<CubicOpenGlFrame> capturer;
        if (PixelBufferObject.SUPPORTED || settings.isDepthMap()) {
            capturer = new CubicPboOpenGlFrameCapturer(levelRenderer, renderInfo, processor.getFrameSize());
        } else {
            capturer = new CubicOpenGlFrameCapturer(levelRenderer, renderInfo, processor.getFrameSize());
        }
        return new Pipeline<>(levelRenderer, capturer, processor, consumer);
    }

    public static Pipeline<ODSOpenGlFrame, BitmapFrame> newODSPipeline(RenderInfo renderInfo, FrameConsumer<BitmapFrame> consumer) {
        RenderSettings settings = renderInfo.getRenderSettings();
        LevelRenderer levelRenderer = new EntityRendererHandler(settings, renderInfo);

        ODSToBitmapProcessor processor = new ODSToBitmapProcessor(settings.getVideoWidth(),
                settings.getVideoHeight(), settings.getSphericalFovX());

        FrameCapturer<ODSOpenGlFrame> capturer =
                new ODSFrameCapturer(levelRenderer, renderInfo, processor.getFrameSize());
        return new Pipeline<>(levelRenderer, capturer, processor, consumer);
    }

    public static Pipeline<BitmapFrame, BitmapFrame> newBlendPipeline(RenderInfo renderInfo) {
        RenderSettings settings = renderInfo.getRenderSettings();
        LevelRenderer levelRenderer = new EntityRendererHandler(settings, renderInfo);
        FrameCapturer<BitmapFrame> capturer = new BlendFrameCapturer(levelRenderer, renderInfo);
        FrameConsumer<BitmapFrame> consumer = new FrameConsumer<BitmapFrame>() {
            @Override
            public void consume(Map<Channel, BitmapFrame> channels) {
            }

            @Override
            public void close() {
            }
        };
        return new Pipeline<>(levelRenderer, capturer, new DummyProcessor<>(), consumer);
    }
}
