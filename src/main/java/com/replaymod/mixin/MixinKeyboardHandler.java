package com.replaymod.mixin;

import com.replaymod.core.events.KeyMappingEventCallback;
import com.replaymod.core.events.KeyEventCallback;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {

    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"), cancellable = true)
    private void beforeKeyMappingTick(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (KeyEventCallback.EVENT.invoker().keyPress(key, scanCode, action, modifiers)) {
            ci.cancel();
        }
    }

    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V", shift = At.Shift.AFTER))
    private void afterKeyMappingTick(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        KeyMappingEventCallback.EVENT.invoker().onKeybindingEvent();
    }
}
