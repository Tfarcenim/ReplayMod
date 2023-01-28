package com.replaymod.extras.advancedscreenshots;

import com.replaymod.core.versions.MCVer;
import com.replaymod.render.RenderSettings;
import com.replaymod.render.blend.BlendState;
import com.replaymod.render.capturer.RenderInfo;
import com.replaymod.render.hooks.ForceChunkLoadingHook;
import com.replaymod.render.rendering.Pipelines;
import de.johni0702.minecraft.gui.utils.lwjgl.Dimension;
import de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.CrashReport;

import java.util.function.Supplier;

import static com.replaymod.core.versions.MCVer.resizeWindow;

public class ScreenshotRenderer implements RenderInfo {

    private final Minecraft mc = MCVer.getMinecraft();

    private final RenderSettings settings;

    private int framesDone;

    public ScreenshotRenderer(RenderSettings settings) {
        this.settings = settings;
    }

    public boolean renderScreenshot() throws Throwable {
        try {
            var target = mc.getMainRenderTarget();
            int widthBefore = target.width;
            int heightBefore = target.height;
            boolean hideGuiBefore = mc.options.hideGui;
            mc.options.hideGui = true;

            ForceChunkLoadingHook clrg = new ForceChunkLoadingHook(mc.levelRenderer);

            if (settings.getRenderMethod() == RenderSettings.RenderMethod.BLEND) {
                BlendState.setState(new BlendState(settings.getOutputFile()));
                Pipelines.newBlendPipeline(this).run();
            } else {
                Pipelines.newPipeline(settings.getRenderMethod(), this,
                        new ScreenshotWriter(settings.getOutputFile())).run();
            }

            clrg.uninstall();

            mc.options.hideGui = hideGuiBefore;
            resizeWindow(mc, widthBefore, heightBefore);
            return true;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            CrashReport report = CrashReport.forThrowable(e, "Creating Equirectangular Screenshot");
            MCVer.getMinecraft().delayCrash(new Supplier<CrashReport>() {
                @Override
                public CrashReport get() {
                    return report;
                }
            });
        }
        return false;
    }

    @Override
    public ReadableDimension getFrameSize() {
        return new Dimension(settings.getVideoWidth(), settings.getVideoHeight());
    }

    @Override
    public int getFramesDone() {
        return framesDone;
    }

    @Override
    public int getTotalFrames() {
        // render 2 frames, because only the second contains all frames fully loaded
        return 2;
    }

    @Override
    public float updateForNextFrame() {
        framesDone++;
        return mc.getFrameTime();
    }

    @Override
    public RenderSettings getRenderSettings() {
        return settings;
    }
}
