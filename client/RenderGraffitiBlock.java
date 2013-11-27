package net.minecraft.graffiti.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.graffiti.GraffitiBlock;
import net.minecraft.graffiti.LinePoint;
import net.minecraft.graffiti.TileEntityGraffitiBlock;
import net.minecraft.graffiti.mod_Graffiti;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class RenderGraffitiBlock extends RenderBlocks
{
	private float lineWidth = mod_Graffiti.getLineWidth();

    public RenderGraffitiBlock(IBlockAccess par1IBlockAccess)
    {
        super(par1IBlockAccess);
    }

    public boolean renderBlockByRenderTypeCustom(GraffitiBlock block, int posX, int posY, int oosZ)
    {
        enableAO = false;
        Tessellator tessellator = Tessellator.instance;

        Icon icon = getBlockIcon(block,blockAccess, posX, posY, oosZ, 0);
        int l = block.getMixedBrightnessForBlock(this.blockAccess, posX, posY, oosZ);
        block.setBlockBoundsBasedOnState(blockAccess, posX, posY, oosZ);

        TileEntityGraffitiBlock tileentity = (TileEntityGraffitiBlock)blockAccess.getBlockTileEntity(posX, posY, oosZ);
        LinePoint[] points = tileentity.getPoints();

        if(points.length > 0){
        	for(int cnt = 0 ; cnt < points.length; cnt++)
        	{
        		GL11.glPushMatrix();
        		Tessellator.instance.startDrawingQuads();

        		LinePoint point = points[cnt];
        		setColorOpaque(point);
        		int renderSide = point.getSide();
        		if(renderSide == 0)
        		{
                    //tessellator.setBrightness(this.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX, posY - 1, oosZ));
        			renderLineFaceYPos(block, posX, posY, oosZ, icon , points[cnt], cnt);
        		}
        		else if(renderSide == 1)
        		{
                    //tessellator.setBrightness(this.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX, posY + 1, oosZ));
        			renderLineFaceYNeg(block, posX, posY, oosZ, icon , points[cnt], cnt);
        		}
        		else if(renderSide == 2)
        		{
                    //tessellator.setBrightness(this.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX, posY, oosZ - 1));
        			renderLineFaceZPos(block, posX, posY, oosZ, icon , points[cnt], cnt);
        		}
        		else if(renderSide == 3)
        		{
                    //tessellator.setBrightness(this.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX - 1, posY, oosZ));
        			renderLineFaceZNeg(block, posX, posY, oosZ, icon , points[cnt], cnt);
        		}
        		else if(renderSide == 4)
        		{
                    //tessellator.setBrightness(this.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX - 1, posY, oosZ));
        			renderLineFaceXPos(block, posX, posY, oosZ, icon , points[cnt], cnt);
        		}
        		else if(renderSide == 5)
        		{
                    //tessellator.setBrightness(this.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX + 1, posY, oosZ));
        			renderLineFaceXNeg(block, posX, posY, oosZ, icon , points[cnt], cnt);
        		}

        		Tessellator.instance.draw();
        		GL11.glPopMatrix();
        	}
        }

        return true;
    }

    private void setColorOpaque(LinePoint point)
    {
        int color = point.getColor();
        float rr = (float)(color >> 16 & 0xff) / 255F;
        float gg = (float)(color >> 8 & 0xff) / 255F;
        float bb = (float)(color & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
        	rr = (rr * 30F + gg * 59F + bb * 11F) / 100F;
        	gg = (rr * 30F + gg * 70F) / 100F;
        	bb = (rr * 30F + bb * 70F) / 100F;
        }

        enableAO = false;
        Tessellator tessellator = Tessellator.instance;

        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float f7 = f4 * rr;
        float f8 = f4 * gg;
        float f9 = f4 * bb;
        float f10 = f3;
        float f11 = f5;
        float f12 = f6;
        float f13 = f3;
        float f14 = f5;
        float f15 = f6;
        float f16 = f3;
        float f17 = f5;
        float f18 = f6;

        f10 = f3 * rr;
        f11 = f5 * rr;
        f12 = f6 * rr;
        f13 = f3 * gg;
     	f14 = f5 * gg;
     	f15 = f6 * gg;
     	f16 = f3 * bb;
     	f17 = f5 * bb;
     	f18 = f6 * bb;

		int renderSide = point.getSide();
		if(renderSide == 0)
		{
            tessellator.setColorOpaque_F(f10, f13, f16);
		}
		else if(renderSide == 1)
		{
            tessellator.setColorOpaque_F(f7, f8, f9);
		}
		else if(renderSide == 2)
		{
            tessellator.setColorOpaque_F(f11, f14, f17);
		}
		else if(renderSide == 3)
		{
            tessellator.setColorOpaque_F(f12, f15, f18);
		}
		else if(renderSide == 4)
		{
            tessellator.setColorOpaque_F(f12, f15, f18);
		}
		else if(renderSide == 5)
		{
            tessellator.setColorOpaque_F(f12, f15, f18);
		}
    }

    public void renderLineFaceYNeg(Block par1Block, double par2, double par4, double par6, Icon icon , LinePoint point, int cnt)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            icon = this.overrideBlockTexture;
        }

        setRenderBounds(0.0D, 0.0D, 0.0D, point.getAngleYlength(), 0.0D, point.getLineSize());

        double minU = (double)icon.getInterpolatedU(renderMinX * 16.0D);
        double maxU = (double)icon.getInterpolatedU(renderMaxX * 16.0D);
        double maxV = (double)icon.getInterpolatedV(renderMinZ * 16.0D);
        double minV = (double)icon.getInterpolatedV(renderMaxZ * 16.0D);

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
            minU = (double)icon.getMinU();
            maxU = (double)icon.getMaxU();
        }

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
        	minV = (double)icon.getMinV();
            maxV = (double)icon.getMaxV();
        }

        double minX = renderMinX;
        double maxX = renderMaxX;
        double minY = renderMinY;
        double maxY = renderMaxY;
        double minZ = renderMinZ;
        double maxZ = renderMaxZ;

        double exRadians = Math.toRadians(point.getAngleY());
        double size = point.getLineSize() / 2.0D;
        GL11.glTranslated((-1.0D) * Math.sin(exRadians) * size, 0.0D, (-1.0D) * Math.cos(exRadians) * size);
        GL11.glTranslated(point.getMinX(), 0.001D * (cnt+1), point.getMinZ());
        GL11.glRotatef(point.getAngleY(), 0.0F, 1.0F, 0.0F);

        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);

    }


    public void renderLineFaceYPos(Block par1Block, double par2, double par4, double par6, Icon icon , LinePoint point, int cnt)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            icon = this.overrideBlockTexture;
        }

        setRenderBounds(0.0D, 0.0D, 0.0D, point.getAngleYlength(), 0.0D, point.getLineSize());

        double minU = (double)icon.getInterpolatedU(renderMinX * 16.0D);
        double maxU = (double)icon.getInterpolatedU(renderMaxX * 16.0D);
        double maxV = (double)icon.getInterpolatedV(renderMinZ * 16.0D);
        double minV = (double)icon.getInterpolatedV(renderMaxZ * 16.0D);

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
            minU = (double)icon.getMinU();
            maxU = (double)icon.getMaxU();
        }

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
        	minV = (double)icon.getMinV();
            maxV = (double)icon.getMaxV();
        }

        double minX = renderMinX;
        double maxX = renderMaxX;
        double minY = renderMinY;
        double maxY = renderMaxY;
        double minZ = renderMinZ;
        double maxZ = renderMaxZ;

        double exRadians = Math.toRadians(point.getAngleY());
        double size = point.getLineSize() / 2.0D;
        GL11.glTranslated((-1.0D) * Math.sin(exRadians) * size, 0.0D, (-1.0D) * Math.cos(exRadians) * size);
        GL11.glTranslated(point.getMinX(), 1.0D - 0.001D * (cnt+1), point.getMinZ());
        GL11.glRotatef(point.getAngleY(), 0.0F, 1.0F, 0.0F);

        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);

        tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
    }


    public void renderLineFaceXNeg(Block par1Block, double par2, double par4, double par6, Icon icon , LinePoint point, int cnt)
    {
        Tessellator tessellator = Tessellator.instance;

        if (this.hasOverrideBlockTexture())
        {
        	icon = this.overrideBlockTexture;
        }

        setRenderBounds(0.0D, 0.0D, 0.0D, 0.0D, point.getLineSize(), point.getAngleXlength());

        double minU = (double)icon.getInterpolatedU(renderMinZ * 16.0D);
        double maxU = (double)icon.getInterpolatedU(renderMaxZ * 16.0D);
        double maxV = (double)icon.getInterpolatedV(16.0D - renderMaxY * 16.0D);
        double minV = (double)icon.getInterpolatedV(16.0D - renderMinY * 16.0D);

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
        	minU = (double)icon.getMinU();
        	maxU = (double)icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
        	minV = (double)icon.getMinV();
        	maxV = (double)icon.getMaxV();
        }

        double minX = renderMinX;
        double maxX = renderMaxX;
        double minY = renderMinY;
        double maxY = renderMaxY;
        double minZ = renderMinZ;
        double maxZ = renderMaxZ;

        double exRadians = Math.toRadians(point.getAngleX());
        double size = point.getLineSize() / 2.0D;
        GL11.glTranslated(0.0D, (-1.0D) * Math.cos(exRadians) * size , (-1.0D) * Math.sin(exRadians) * size);

        GL11.glTranslated(0.001D * (cnt+1), point.getMinY(), point.getMinZ());
        GL11.glRotatef(point.getAngleX(), 1.0F, 0.0F, 0.0F);

        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, minV);
        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);

        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, minV);
    }

    public void renderLineFaceXPos(Block par1Block, double par2, double par4, double par6, Icon icon , LinePoint point, int cnt)
    {
        Tessellator tessellator = Tessellator.instance;

        if (this.hasOverrideBlockTexture())
        {
        	icon = this.overrideBlockTexture;
        }

        setRenderBounds(0.0D, 0.0D, 0.0D, 0.0D, point.getLineSize(), point.getAngleXlength());

        double minU = (double)icon.getInterpolatedU(renderMinZ * 16.0D);
        double maxU = (double)icon.getInterpolatedU(renderMaxZ * 16.0D);
        double maxV = (double)icon.getInterpolatedV(16.0D - renderMaxY * 16.0D);
        double minV = (double)icon.getInterpolatedV(16.0D - renderMinY * 16.0D);

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
        	minU = (double)icon.getMinU();
        	maxU = (double)icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
        	minV = (double)icon.getMinV();
        	maxV = (double)icon.getMaxV();
        }

        double minX = renderMinX;
        double maxX = renderMaxX;
        double minY = renderMinY;
        double maxY = renderMaxY;
        double minZ = renderMinZ;
        double maxZ = renderMaxZ;

        double exRadians = Math.toRadians(point.getAngleX());
        double size = point.getLineSize() / 2.0D;
        GL11.glTranslated(0.0D, (-1.0D) * Math.cos(exRadians) * size , (-1.0D) * Math.sin(exRadians) * size);

        GL11.glTranslated(1.0D - 0.001D * (cnt+1), point.getMinY(), point.getMinZ());
        GL11.glRotatef(point.getAngleX(), 1.0F, 0.0F, 0.0F);

        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, minV);
        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);

        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, minV);
    }


    public void renderLineFaceZNeg(Block par1Block, double par2, double par4, double par6, Icon icon , LinePoint point, int cnt)
    {
        Tessellator tessellator = Tessellator.instance;

        if (this.hasOverrideBlockTexture())
        {
        	icon = this.overrideBlockTexture;
        }

        setRenderBounds(0.0D, 0.0D, 0.0D, point.getLineSize(), point.getAngleZlength(), 0.0D);

        double minU = (double)icon.getInterpolatedU(renderMinX * 16.0D);
        double maxU = (double)icon.getInterpolatedU(renderMaxX * 16.0D);
        double maxV = (double)icon.getInterpolatedV(16.0D - renderMaxY * 16.0D);
        double minV = (double)icon.getInterpolatedV(16.0D - renderMinY * 16.0D);

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
        	minU = (double)icon.getMinU();
        	maxU = (double)icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
        	minV = (double)icon.getMinV();
        	maxV = (double)icon.getMaxV();
        }

        double minX = renderMinX;
        double maxX = renderMaxX;
        double minY = renderMinY;
        double maxY = renderMaxY;
        double minZ = renderMinZ;
        double maxZ = renderMaxZ;

        double exRadians = Math.toRadians(point.getAngleZ());
        double size = point.getLineSize() / 2.0D;
        GL11.glTranslated((-1.0D) * Math.cos(exRadians) * size , (-1.0D) * Math.sin(exRadians) * size, 0.0D);
        GL11.glTranslated(point.getMinX(), point.getMinY(), 0.001D * (cnt+1));
        GL11.glRotatef(point.getAngleZ(), 0.0F, 0.0F, 1.0F);

        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
        tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);

    }


    public void renderLineFaceZPos(Block par1Block, double par2, double par4, double par6, Icon icon , LinePoint point, int cnt)
    {
        Tessellator tessellator = Tessellator.instance;

        if (this.hasOverrideBlockTexture())
        {
        	icon = this.overrideBlockTexture;
        }

        setRenderBounds(0.0D, 0.0D, 0.0D, point.getLineSize(), point.getAngleZlength(), 0.0D);

        double minU = (double)icon.getInterpolatedU(renderMinX * 16.0D);
        double maxU = (double)icon.getInterpolatedU(renderMaxX * 16.0D);
        double maxV = (double)icon.getInterpolatedV(16.0D - renderMaxY * 16.0D);
        double minV = (double)icon.getInterpolatedV(16.0D - renderMinY * 16.0D);

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
        	minU = (double)icon.getMinU();
        	maxU = (double)icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
        	minV = (double)icon.getMinV();
        	maxV = (double)icon.getMaxV();
        }

        double minX = renderMinX;
        double maxX = renderMaxX;
        double minY = renderMinY;
        double maxY = renderMaxY;
        double minZ = renderMinZ;
        double maxZ = renderMaxZ;

        double exRadians = Math.toRadians(point.getAngleZ());
        double size = point.getLineSize() / 2.0D;
        GL11.glTranslated((-1.0D) * Math.cos(exRadians) * size , (-1.0D) * Math.sin(exRadians) * size, 0.0D);
        GL11.glTranslated(point.getMinX(), point.getMinY(), 1.0D - 0.001D * (cnt+1));
        GL11.glRotatef(point.getAngleZ(), 0.0F, 0.0F, 1.0F);

        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
        tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);

    }
}
