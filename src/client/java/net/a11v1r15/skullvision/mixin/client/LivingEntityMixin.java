package net.a11v1r15.skullvision.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.a11v1r15.skullvision.SkullVision;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
extends Entity {
  public LivingEntityMixin(EntityType < ? > type, World world) {
    super(type, world);
  }

  @Shadow
  protected abstract ItemStack getEquippedStack(EquipmentSlot slot);

  @Inject(at = @At(value = "RETURN"), method =
    "onEquipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V")
  private void skullVision$checkEquippingSkull(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
    SkullVision.LOGGER.info("Function onEquipStack called");
    SkullVision.LOGGER.info(this);
    SkullVision.LOGGER.info(client.getCameraEntity());
    SkullVision.LOGGER.info(((this == client.getCameraEntity()) ? "is Player" : " Not player"));
    SkullVision.LOGGER.info(((client.options.getPerspective().isFirstPerson()) ? "is 1st" : " Not first"));
    SkullVision.LOGGER.info("slot is " + slot.getName() + "oldStack is " + oldStack.getItem() + ", newStack is " + newStack.getItem() + " and current thing in head is " + this.getEquippedStack(EquipmentSlot.HEAD).getItem());
    if (slot == EquipmentSlot.HEAD) {
      MinecraftClient client = MinecraftClient.getInstance();
      if (this == client.getCameraEntity() && client.options.getPerspective().isFirstPerson()) {
        MinecraftClient.getInstance().gameRenderer.onCameraEntitySet(this);
      }
    }
  }
}