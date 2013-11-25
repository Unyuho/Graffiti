package net.minecraft.graffiti;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GraffitiBlock extends BlockContainer
{
    private final int renderType;
    private float width = mod_Graffiti.getWidth();

    public GraffitiBlock(int i, int renderType)
    {
        super(i, Block.planks.blockMaterial);
        this.renderType = renderType;

        setStepSound(soundClothFootstep);
        setHardness(0.3F);
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
    	return Block.cloth.getIcon(0, 0);
    }

    @Override
    public int getRenderType()
    {
        return renderType;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
    	super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase,par6ItemStack);

    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	TileEntityGraffitiBlock tileentity = (TileEntityGraffitiBlock)par1IBlockAccess.getBlockTileEntity(par2, par3, par4);
    	boolean[] sides = tileentity.getSideArray();

    	for(int cnt = 0; cnt < 6 ; cnt++)
    	{
    		if(sides[cnt])
    		{
        		setBlockBoundsBasedOnSide(cnt);
        		return;
    		}
    	}
    	setBlockBoundsBasedOnSide(0);
    }

    private void setBlockBoundsBasedOnSide(int side)
    {
        if (side == 0)
        {
        	this.setBlockBounds(0.0F, 1.0F - width , 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (side == 1)
        {
        	this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, width, 1.0F);
        }
        else if (side == 2)
        {
        	this.setBlockBounds(0.0F, 0.0F, 1.0F - width, 1.0F, 1.0F, 1.0F);
        }
        else if (side == 3)
        {
        	this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, width);
        }
        else if (side == 4)
        {
            this.setBlockBounds(1.0F - width, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (side == 5)
        {
        	this.setBlockBounds(0.0F, 0.0F, 0.0F, width, 1.0F, 1.0F);
        }
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntityGraffitiBlock();
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3,int par4, EntityPlayer par5EntityPlayer, int par6, float par7,float par8, float par9)
    {
	    ItemStack itemstack = par5EntityPlayer.getCurrentEquippedItem();
	    if(itemstack != null && itemstack.itemID == Item.paper.itemID)
	    {
	        if(!par1World.isRemote)
	        {
		    	TileEntityGraffitiBlock tileentity = (TileEntityGraffitiBlock)par1World.getBlockTileEntity(par2, par3, par4);
		    	if(tileentity.removePos()){
		    		par1World.markBlockForUpdate(par2, par3, par4);
		    	}else{
		    		par1World.setBlock(par2, par3, par4, 0);
		    	}
	    	}
	        return true;
    	}
	    else if(itemstack == null || itemstack.itemID != mod_Graffiti.graffitiItem.itemID)
	    {
	    	int checkPosX = par2;
	    	int checkPosY = par3;
	    	int checkPosZ = par4;

	        if (par6 == 0){
	        	//下から叩いた場合
	        	++checkPosY;
	        }else if(par6 == 1){
	        	//上から叩いた場合
	        	--checkPosY;
	        }else if(par6 == 2){
	            ++checkPosZ;
	        }else if(par6 == 3){
	        	--checkPosZ;
	        }else if(par6 == 4){
	            ++checkPosX;
	        }else if(par6 == 5){
	        	--checkPosX;
	        }
	        int blockID = par1World.getBlockId(checkPosX, checkPosY, checkPosZ);
		    Block block = Block.blocksList[blockID];
	        return block.onBlockActivated(par1World, checkPosX, checkPosY,checkPosZ, par5EntityPlayer, par6, par7,par8, par9);
    	}
    	return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer,par6, par7, par8, par9);
    }

    @Override
    public boolean isBlockReplaceable(World world, int x, int y, int z)
    {
        return true;
    }
}