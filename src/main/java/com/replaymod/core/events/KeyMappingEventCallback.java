package com.replaymod.core.events;

import com.replaymod.gui.utils.Event;

public interface KeyMappingEventCallback {
    Event<KeyMappingEventCallback> EVENT = Event.create((listeners) ->
            () -> {
                for (KeyMappingEventCallback listener : listeners) {
                    listener.onKeybindingEvent();
                }
            }
    );

    void onKeybindingEvent();
}
