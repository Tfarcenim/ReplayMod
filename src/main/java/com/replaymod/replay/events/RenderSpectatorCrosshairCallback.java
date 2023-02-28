package com.replaymod.replay.events;

import com.replaymod.gui.utils.Event;

public interface RenderSpectatorCrosshairCallback {
    Event<RenderSpectatorCrosshairCallback> EVENT = Event.create((listeners) ->
            () -> {
                for (RenderSpectatorCrosshairCallback listener : listeners) {
                    Boolean state = listener.shouldRenderSpectatorCrosshair();
                    if (state != null) {
                        return state;
                    }
                }
                // Uncertain
                return true;
            }
    );

    Boolean shouldRenderSpectatorCrosshair();
}
