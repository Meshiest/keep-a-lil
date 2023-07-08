package io.reheatedcake.keepalil.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
  public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
    super(world, pos, yaw, gameProfile);
  }

  private int xpToDrop;

  public int updateLevelsToDrop() {
    var levels = this.experienceLevel;
    xpToDrop = (int) MathHelper.clamp(((float) getNextLevelExperience()) * experienceProgress, 0, Integer.MAX_VALUE);

    for (experienceLevel = 0; experienceLevel < levels; experienceLevel++) {
      xpToDrop += getNextLevelExperience();
    }

    experienceLevel = 0;
    experienceProgress = 0;
    totalExperience = 0;

    return xpToDrop;
  }

  @Inject(method = "onDeath", at = @At("HEAD"))
  public void onDeathHead(CallbackInfo ci) {
    if (!this.isSpectator())
      updateLevelsToDrop();
    setScore(0);
  }

  @Override
  protected void dropXp() {
    if (this.getWorld() instanceof ServerWorld && !this.isExperienceDroppingDisabled()) {
      var world = getWorld();
      var pos = getPos();
      var entity = new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), xpToDrop);
      entity.setVelocity(Vec3d.ZERO);
      world.spawnEntity(entity);
      xpToDrop = 0;
      experienceLevel = 0;
    }
  }
}
