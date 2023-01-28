package com.replaymod.core.versions.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.replaymod.core.events.*;
import com.replaymod.gui.utils.EventRegistrations;
import com.replaymod.replay.events.RenderHotbarCallback;
import com.replaymod.replay.events.RenderSpectatorCrosshairCallback;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventsAdapter extends EventRegistrations {
    @SubscribeEvent
    public void keyPress(InputEvent.KeyInputEvent event) {
        KeyMappingEventCallback.EVENT.invoker().onKeybindingEvent();
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        KeyMappingEventCallback.EVENT.invoker().onKeybindingEvent();
    }

    @SubscribeEvent
    public void preRender(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        PreRenderCallback.EVENT.invoker().preRender();
    }

    @SubscribeEvent
    public void postRender(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        PostRenderCallback.EVENT.invoker().postRender();
    }

    @SubscribeEvent
    public void renderCameraPath(RenderLevelStageEvent event) {
        PostRenderWorldCallback.EVENT.invoker().postRenderWorld(new PoseStack());
    }

    @SubscribeEvent
    public void oRenderHand(RenderHandEvent event) {
        if (PreRenderHandCallback.EVENT.invoker().preRenderHand()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void preRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        Boolean result = true;
        switch (event.getType()) {
            case LAYER:
                result = RenderSpectatorCrosshairCallback.EVENT.invoker().shouldRenderSpectatorCrosshair()
                && RenderHotbarCallback.EVENT.invoker().shouldRenderHotbar();
                break;
            default:
                break;
        }
        if (result.equals(Boolean.FALSE)) {
            event.setCanceled(true);
        }
    }
}
