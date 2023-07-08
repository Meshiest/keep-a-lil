package io.reheatedcake.keepalil.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.reheatedcake.keepalil.KeepALil;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
  @Shadow
  @Final
  public List<DefaultedList<ItemStack>> combinedInventory;

  @Shadow
  @Final
  public PlayerEntity player;

  @Inject(method = "dropAll", at = @At("HEAD"), cancellable = true)
  public void dropAll(CallbackInfo ci) {
    ci.cancel();

    for (var list : combinedInventory) {
      for (int i = 0; i < list.size(); ++i) {
        ItemStack itemStack = (ItemStack) list.get(i);
        if (itemStack.isEmpty())
          continue;

        var isVanishing = EnchantmentHelper.hasVanishingCurse(itemStack);

        if (KeepALil.isKeptItem(itemStack) && !isVanishing)
          continue;

        list.set(i, ItemStack.EMPTY);

        if (isVanishing)
          continue;

        var entity = player.dropItem(itemStack, true, false);
        entity.setVelocity(Vec3d.ZERO);
        entity.setCovetedItem();
      }
    }
  }
}
