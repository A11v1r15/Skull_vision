package net.a11v1r15.skullvision.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.enums.Instrument;
import net.minecraft.block.Block;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.SkullItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable {

 @Shadow
 protected abstract void loadPostProcessor(Identifier identifier);

	@Inject(at = @At("TAIL"), method = "onCameraEntitySet(Lnet/minecraft/entity/Entity;)V")
	private void skullVision$testEntityWearingSkull(Entity entity, CallbackInfo info) {
		if (entity instanceof LivingEntity){
			ItemStack head = ((LivingEntity)entity).getEquippedStack(EquipmentSlot.HEAD);
			if (head.isIn(ItemTags.NOTEBLOCK_TOP_INSTRUMENTS)){
				Item headItem = head.getItem();
				if (headItem instanceof SkullItem){
					Block headBlock = ((SkullItem)headItem).getBlock();
					Instrument headInstrument =	headBlock.getDefaultState().getInstrument();
					String soundName = "";
					if (headInstrument.hasCustomSound()){
						soundName = SkullItem.getBlockEntityNbt(head).getString("note_block_sound");
					} else {
						soundName = headInstrument.getSound().value().getId().getPath();
					}
					String mobName = "";
					for (String element : soundName.split(".")) {
						if(!EntityType.get(element).isEmpty()){
							mobName = element;
							break;
						}
					}
					this.loadPostProcessor(new Identifier("shaders/post/" + mobName + ".json"));
				}
			}
		}
	}

	@Override
	public void close() throws Exception {
		throw new UnsupportedOperationException("Unimplemented method 'close'");
	}
}