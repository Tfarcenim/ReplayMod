package com.replaymod.mixin;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TitleScreen.class)
public interface GuiMainMenuAccessor {
    @Accessor("realmsNotificationsScreen")
    Screen getRealmsNotification();

    @Accessor("realmsNotificationsScreen")
    void setRealmsNotification(Screen value);
}
