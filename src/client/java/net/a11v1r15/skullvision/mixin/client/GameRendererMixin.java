package net.a11v1r15.skullvision.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.a11v1r15.skullvision.SkullVision;
import net.a11v1r15.skullvision.SkullVisionClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable {

  @Shadow
  protected ResourceManager resourceManager;

  @Shadow
  protected MinecraftClient client;

  @Shadow
  protected abstract void loadPostProcessor(Identifier identifier);

  @Shadow
  protected abstract void onCameraEntitySet(Entity entity);

  @Inject(at = @At("RETURN"), method = "onCameraEntitySet(Lnet/minecraft/entity/Entity;)V")
  private void skullVision$testEntityWearingSkull(Entity entity, CallbackInfo info) {
    String entityName = ((entity != null) ? EntityType.getId(entity.getType()).getPath() : "null");
    String skullName = SkullVisionClient.getSkullNameFrom(entity);
    SkullVision.LOGGER.info("Function onCameraEntitySet called on " + entityName);

    Identifier entityShaderId = new Identifier("shaders/post/" + entityName + ".json");
    Identifier skullShaderId = new Identifier("shaders/post/" + skullName + ".json");
    String[] postProcessors = {entityName, skullName};
    SkullVisionClient.lastPostProcessors = postProcessors;
    //SkullVision.LOGGER.info(entityName + " " + SkullVisionClient.lastPostProcessors[0] + " / " + skullName + " " + SkullVisionClient.lastPostProcessors[1]);
    this.resourceManager.getResource(skullShaderId).ifPresentOrElse(
      r -> { this.loadPostProcessor(skullShaderId); },
      () -> { this.resourceManager.getResource(entityShaderId).ifPresent(
        s -> { this.loadPostProcessor(entityShaderId); }
      );}
    );
  }

  @Inject(at = @At(value = "RETURN"), method = "tick()V")
  private void skullVision$tickEquippingSkull(CallbackInfo info) {
    if (this.client.options.getPerspective().isFirstPerson()) {
      Entity entity = this.client.getCameraEntity();
      String entityName = ((entity != null) ? EntityType.getId(entity.getType()).getPath() : "null");
      String skullName = SkullVisionClient.getSkullNameFrom(entity);
      
      //SkullVision.LOGGER.info(entityName + " " + SkullVisionClient.lastPostProcessors[0] + " / " + skullName + " " + SkullVisionClient.lastPostProcessors[1]);
      if (entity != null && (!entityName.equals(SkullVisionClient.lastPostProcessors[0]) || !skullName.equals(SkullVisionClient.lastPostProcessors[1])))
        this.onCameraEntitySet(entity);
    }
  }
}