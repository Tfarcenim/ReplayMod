package com.replaymod.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nonnull;

@Mixin(LivingEntity.class)
public interface EntityLivingBaseAccessor {
    @Accessor("DATA_LIVING_ENTITY_FLAGS")
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    static EntityDataAccessor<Byte> getLivingFlags() {
        return null;
    }
}
