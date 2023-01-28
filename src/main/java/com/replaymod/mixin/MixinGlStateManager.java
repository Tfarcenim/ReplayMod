package com.replaymod.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.replaymod.render.hooks.FogStateCallback;
import com.replaymod.render.hooks.Texture2DStateCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public abstract class MixinGlStateManager {
    @Shadow
    private static int activeTexture;

    /*@Inject(method = "_enableFog", at = @At("HEAD"))
    private static void enableFog(CallbackInfo ci) {
        FogStateCallback.EVENT.invoker().fogStateChanged(true);
    }

    @Inject(method = "_disableFog", at = @At("HEAD"))
    private static void disableFog(CallbackInfo ci) {
        FogStateCallback.EVENT.invoker().fogStateChanged(false);
    }*/

    @Inject(method = "_enableTexture", at = @At("HEAD"))
    private static void enableTexture(CallbackInfo ci) {
        Texture2DStateCallback.EVENT.invoker().texture2DStateChanged(MixinGlStateManager.activeTexture, true);
    }

    @Inject(method = "_disableTexture", at = @At("HEAD"))
    private static void disableTexture(CallbackInfo ci) {
        Texture2DStateCallback.EVENT.invoker().texture2DStateChanged(MixinGlStateManager.activeTexture, false);
    }
}
