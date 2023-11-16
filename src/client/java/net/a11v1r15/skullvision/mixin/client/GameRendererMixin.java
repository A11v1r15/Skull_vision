package net.a11v1r15.skullvision.mixin.client;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements AutoCloseable {
	@Inject(at = @At("TAIL"), method = "onCameraEntitySet(Lnet/minecraft/entity/Entity)V")
	private void skullVision$testEntityWearingSkull(CallbackInfo info, Entity entity) {
		if (entity instanceof LivingEntity){
			ItemStack head = (LivingEntity)entity.getEquippedStack(EquipmentSlot.HEAD);
			if (head.isIn(ItemTags.NOTEBLOCK_TOP_INSTRUMENTS)){
				Item headItem = head.getItem();
				if (headItem instanceof BlockItem){
					Block headBlock = (BlockItem)headItem.getBlock();
					if (headBlock instanceof SkullBlockEntity){
						String soundName = (SkullBlockEntity)headBlock.getNoteBlockSound().getPath();
						String mobName = "";
						for (String element : soundName.split(".")) {
							if(EntityType.get(element) != Optional.empty()){
								mobName = element;
								break;
							}
						}
						this.loadPostProcessor(new Identifier("shaders/post/" + mobName + ".json"));
					}
				}
			}
		}
	}
}