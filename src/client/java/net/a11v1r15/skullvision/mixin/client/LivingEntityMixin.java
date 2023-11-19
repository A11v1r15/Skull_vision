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
    SkullVision.LOGGER.info("Function onEquipStack called, " + ((Object) this instanceof ClientPlayerEntity ? "is Player" : " Not player") + " and slot is " + slot.getName());
    SkullVision.LOGGER.info("oldStack is " + oldStack.getItem() + ", newStack is " + newStack.getItem() + " and current thing in head is " + this.getEquippedStack(EquipmentSlot.HEAD).getItem());
    if ((Object) this instanceof ClientPlayerEntity) {
      if (slot == EquipmentSlot.HEAD) {
        MinecraftClient.getInstance().setCameraEntity(this);
      }
    }
  }
}