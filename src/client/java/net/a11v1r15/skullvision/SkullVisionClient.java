package net.a11v1r15.skullvision;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SkullVisionClient implements ClientModInitializer {
    public static String[] lastPostProcessors = {"null", "null"};

    public static String getSkullNameFrom(Entity entity) {
        if (entity instanceof LivingEntity) {
            ItemStack skull = ((LivingEntity) entity).getEquippedStack(EquipmentSlot.HEAD);
            String soundName = "";
            if (skull.getComponents().contains(DataComponentTypes.NOTE_BLOCK_SOUND)) {
                Identifier noteBlockSound = skull.getComponents().get(DataComponentTypes.NOTE_BLOCK_SOUND);
                soundName = noteBlockSound == null ? "" : noteBlockSound.toString();
            } else if (skull.getDefaultComponents().contains(DataComponentTypes.NOTE_BLOCK_SOUND)) {
                Identifier noteBlockSound = skull.getDefaultComponents().get(DataComponentTypes.NOTE_BLOCK_SOUND);
                soundName = noteBlockSound == null ? "" : noteBlockSound.toString();
            } else if (skull.isIn(ItemTags.NOTEBLOCK_TOP_INSTRUMENTS)) {
                Item skullItem = skull.getItem();
                if (skullItem instanceof BlockItem skullBlock){
                    NoteBlockInstrument skullInstrument = skullBlock.getBlock().getDefaultState().getInstrument();
                    soundName = skullInstrument.getSound().value().getId().getPath();
                }
            }
            for (String element : soundName.split("\\.")) {
                if (EntityType.get(element).isPresent()) {
                    return element;
                }
            }
        }
        return "null";
    }

    public static Identifier asId(String path) {
        return Identifier.of(SkullVision.MOD_ID, path);
    }

    @Override
    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer(SkullVision.MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(asId("loop_mobs"), container, Text.of("Loop's Mobs"), ResourcePackActivationType.NORMAL);
        });
    }
}
