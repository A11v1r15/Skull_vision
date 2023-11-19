package net.a11v1r15.skullvision.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.a11v1r15.skullvision.SkullVision;
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
  public PlayerEntityMixin(EntityType <? extends LivingEntity> type, World world) {
    super(type, world);
  }

  @Shadow
  protected abstract boolean isMainPlayer();

  @Inject(at = @At(value = "RETURN"), method =
    "equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V")
  private void skullVision$checkEquippingSkull(EquipmentSlot slot, ItemStack stack, CallbackInfo info) {
    SkullVision.LOGGER.info("Function equipStack called, slot is " + slot.getName().getItem());
    SkullVision.LOGGER.info("stack is " + stack.getItem() + " and current thing in head is " + this.getEquippedStack(EquipmentSlot.HEAD));
    if (slot == EquipmentSlot.HEAD) {
      Perspective perspective = MinecraftClient.getInstance().options.getPerspective();
      if (perspective.isFirstPerson()) {
          MinecraftClient.getInstance().gameRenderer.onCameraEntitySet(this);
        }
    }
  }
}