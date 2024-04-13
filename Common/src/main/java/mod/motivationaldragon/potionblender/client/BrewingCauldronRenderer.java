package mod.motivationaldragon.potionblender.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.motivationaldragon.potionblender.block.BrewingCauldron;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class BrewingCauldronRenderer<T extends BrewingCauldronBlockEntity> implements BlockEntityRenderer<T> {


	public BrewingCauldronRenderer(BlockEntityRendererProvider.Context context) {
		super();
	}

	private static final Material WATER_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("block/water_still"));



	@Override
	public void render(T brewingCauldronBlock, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		NonNullList<ItemStack> inventory = brewingCauldronBlock.getInventory();
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.get(i);
			if (!stack.isEmpty()) {
				poseStack.pushPose();
				float percentageOfInventoryIterated = ((float) i / inventory.size());
				poseStack.translate(0.5, ModUtils.lerp(0.5f, 0.8f, percentageOfInventoryIterated), 0.5);
				poseStack.scale(0.5f, 0.5f, 0.5f);
				poseStack.mulPose(Axis.XP.rotationDegrees(90f));
				poseStack.mulPose(Axis.ZP.rotationDegrees(i * 27f));
				renderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, brewingCauldronBlock.getLevel(), 0);
				poseStack.popPose();
			}
		}
		if (Boolean.TRUE.equals(brewingCauldronBlock.getBlockState().getValue(BrewingCauldron.HAS_FLUID))) {

			// Code for rendering the transparent water in the cauldron
			//Copied and adapted from https://github.com/maxoduke/Potion-Cauldron/tree/1.20.4/stable/common/src/main/java/dev/maxoduke/mods/potioncauldron/block under the MIT License
			int waterColor = brewingCauldronBlock.getWaterColor();
			int red = waterColor >> 16 & 255;
			int green = waterColor >> 8 & 255;
			int blue = waterColor & 255;
			int alpha = 190;

			TextureAtlasSprite water = WATER_MATERIAL.sprite();

			poseStack.pushPose();
			poseStack.translate(0, 1, 0);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.translucentMovingBlock());
			Matrix4f matrix = poseStack.last().pose();

			float sizeFactor = 0.875F;
			float maxV = (water.getV1() - water.getV0()) * sizeFactor;
			float minV = (water.getV1() - water.getV0()) * (1 - sizeFactor);

			float cauldronFullness =((float)brewingCauldronBlock.getNumberOfItems() / brewingCauldronBlock.getInventory().size());
			float maxHeight = 0.4F;
			float minHeight = 0.5F;
			float height = -minHeight + cauldronFullness * maxHeight;

			consumer.vertex(matrix, sizeFactor, height, 1 - sizeFactor).color(red, green, blue, alpha).uv(water.getU0(), water.getV0() + maxV).uv2(packedLight).overlayCoords(packedOverlay).normal(1, 1, 1).endVertex();
			consumer.vertex(matrix, 1 - sizeFactor, height, 1 - sizeFactor).color(red, green, blue, alpha).uv(water.getU1(), water.getV0() + maxV).uv2(packedLight).overlayCoords(packedOverlay).normal(1, 1, 1).endVertex();
			consumer.vertex(matrix, 1 - sizeFactor, height, sizeFactor).color(red, green, blue, alpha).uv(water.getU1(), water.getV0() + minV).uv2(packedLight).overlayCoords(packedOverlay).normal(1, 1, 1).endVertex();
			consumer.vertex(matrix, sizeFactor, height, sizeFactor).color(red, green, blue, alpha).uv(water.getU0(), water.getV0() + minV).uv2(packedLight).overlayCoords(packedOverlay).normal(1, 1, 1).endVertex();

			poseStack.popPose();
		}
	}
}
