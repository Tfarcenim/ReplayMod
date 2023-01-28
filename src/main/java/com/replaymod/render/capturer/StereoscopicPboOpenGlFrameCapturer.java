package com.replaymod.render.capturer;

import com.replaymod.render.frame.OpenGlFrame;
import com.replaymod.render.frame.StereoscopicOpenGlFrame;

public class StereoscopicPboOpenGlFrameCapturer
        extends PboOpenGlFrameCapturer<StereoscopicOpenGlFrame, StereoscopicOpenGlFrameCapturer.Data> {

    public StereoscopicPboOpenGlFrameCapturer(LevelRenderer levelRenderer, RenderInfo renderInfo) {
        super(levelRenderer, renderInfo, StereoscopicOpenGlFrameCapturer.Data.class,
                renderInfo.getFrameSize().getWidth() / 2 * renderInfo.getFrameSize().getHeight());
    }

    @Override
    protected int getFrameWidth() {
        return super.getFrameWidth() / 2;
    }

    @Override
    protected StereoscopicOpenGlFrame create(OpenGlFrame[] from) {
        return new StereoscopicOpenGlFrame(from[0], from[1]);
    }
}
