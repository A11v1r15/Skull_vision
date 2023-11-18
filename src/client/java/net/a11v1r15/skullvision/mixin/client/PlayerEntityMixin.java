package net.a11v1r15.skullvision.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
  public PlayerEntityMixin(EntityType < ? > type, World world) {
    super(type, world);
  }

  @Shadow
  protected abstract boolean isMainPlayer();

  @Inject(at = @At(value = "TAIL"), method =
    "equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V")
  private void skullVision$checkEquippingSkull(EquipmentSlot slot, ItemStack stack, CallbackInfo info) {
    if (this.isMainPlayer() && slot == EquipmentSlot.HEAD) {
    Perspective perspective = MinecraftClient.getInstance().options.getPerspective();
    if (perspective.isFirstPerson()) {
        MinecraftClient.getInstance().gameRenderer.onCameraEntitySet(this);
      }
    }
  }
}