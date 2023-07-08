package io.reheatedcake.keepalil.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow
    int totalExperience;

    @Shadow
    PlayerInventory inventory;

    @Inject(method = "getXpToDrop", at = @At("HEAD"), cancellable = true)
    public void getXpToDrop(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(totalExperience);
    }

    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    public void dropInventory(CallbackInfo ci) {
        inventory.dropAll();
        ci.cancel();
    }
}
