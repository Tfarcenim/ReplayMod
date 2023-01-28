package com.replaymod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.replaymod.render.blend.BlendState;
import com.replaymod.render.blend.exporters.ItemExporter;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinRenderItem {
    @Inject(method = "renderModelLists", at = @At("HEAD"))
    private void onRenderModel(BakedModel model, ItemStack stack, int int_1, int int_2, PoseStack matrixStack_1, VertexConsumer vertexConsumer_1, CallbackInfo ci) {
        BlendState blendState = BlendState.getState();
        if (blendState != null) {
            blendState.get(ItemExporter.class).onRender(this, model, stack);
        }
    }
}
