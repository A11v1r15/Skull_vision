package net.a11v1r15.skullvision;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;

public class SkullVisionClient implements ClientModInitializer {
  public static String[] lastPostProcessors = {"null", "null"};

  public static String getSkullNameFrom(Entity entity) {
    if (entity instanceof LivingEntity) {
      ItemStack skull = ((LivingEntity) entity).getEquippedStack(EquipmentSlot.HEAD);
      if (skull.isIn(ItemTags.NOTEBLOCK_TOP_INSTRUMENTS)) {
        Item skullItem = skull.getItem();
        if (skullItem instanceof BlockItem) {
          Block skullBlock = ((BlockItem) skullItem).getBlock();
          Instrument skullInstrument = skullBlock.getDefaultState().getInstrument();
          String soundName = "";
          if (skullInstrument.hasCustomSound()) {
            NbtCompound skullNBT = SkullItem.getBlockEntityNbt(skull);
            if (skullNBT != null)
              soundName = skullNBT.getString("note_block_sound");
          } else {
            soundName = skullInstrument.getSound().value().getId().getPath();
          }
          for (String element: soundName.split("\\.")) {
            if (!EntityType.get(element).isEmpty()) {
              return element;
            }
          }
        }
      }
    }
    return "null";
  }

  @Override
  public void onInitializeClient() {
    // This entrypoint is suitable for setting up client-specific logic, such as rendering.
  }
}
