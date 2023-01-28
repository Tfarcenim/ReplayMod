package com.replaymod.recording.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.replaymod.core.SettingsRegistry;
import com.replaymod.gui.GuiRenderer;
import com.replaymod.gui.MinecraftGuiRenderer;
import com.replaymod.gui.utils.EventRegistrations;
import com.replaymod.gui.versions.callbacks.RenderHudCallback;
import com.replaymod.recording.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;

import static com.replaymod.core.ReplayMod.TEXTURE;
import static com.replaymod.core.ReplayMod.TEXTURE_SIZE;

/**
 * Renders overlay during recording.
 */
public class GuiRecordingOverlay extends EventRegistrations {
    private final Minecraft minecraft;
    private final SettingsRegistry settingsRegistry;
    private final GuiRecordingControls guiControls;

    public GuiRecordingOverlay(Minecraft mc, SettingsRegistry settingsRegistry, GuiRecordingControls guiControls) {
        this.minecraft = mc;
        this.settingsRegistry = settingsRegistry;
        this.guiControls = guiControls;
    }

    /**
     * Render the recording icon and text in the top left corner of the screen.
     */ {
        on(RenderHudCallback.EVENT, (stack, partialTicks) -> renderRecordingIndicator(stack));
    }

    private void renderRecordingIndicator(PoseStack stack) {
        if (guiControls.isStopped()) {
            return;
        }
        if (settingsRegistry.get(Setting.INDICATOR)) {
            Font fontRenderer = minecraft.font;
            String text = guiControls.isPaused() ? I18n.get("replaymod.gui.paused") : I18n.get("replaymod.gui.recording");
            fontRenderer.draw(
                    stack,
                    text.toUpperCase(), 30, 18 - (fontRenderer.lineHeight / 2), 0xffffffff);
            minecraft.getTextureManager().bindForSetup(TEXTURE);
            RenderSystem.enableDepthTest();
            GuiRenderer renderer = new MinecraftGuiRenderer(stack);
            renderer.drawTexturedRect(10, 10, 58, 20, 16, 16, 16, 16, TEXTURE_SIZE, TEXTURE_SIZE);
        }
    }
}
