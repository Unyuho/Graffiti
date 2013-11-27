package net.minecraft.graffiti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.graffiti.kawo.Point;
import net.minecraft.graffiti.kawo.Vec2DImpl;
import net.minecraft.graffiti.kawo.Vec2DIntersectSupport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemGraffiti extends Item
{
	private int graffitiBlockID;
	private ItemBlock graffitiBlock = null;
	private int posionX = 0;
	private int posionY = 0;
	private int color = 0xFFFFFF;
	private int maxLength = 0;

    public ItemGraffiti(int itemID , int blockID, int posionX, int posionY, int color, int maxLength)
    {
		super(itemID);

		setCreativeTab(CreativeTabs.tabDecorations);

		//後で変える
		setMaxDamage(0);
		setHasSubtypes(true);

		graffitiBlockID = blockID;
		graffitiBlock = (ItemBlock)Item.itemsList[blockID];

		this.posionX = posionX;
		this.posionY = posionY;
		this.color = color;
		this.maxLength = maxLength;
	}

    @Override
    public void addInformation(ItemStack par1ItemStack,EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

    	par3List.add("LineWidth : " + getLineSize(par1ItemStack));

    }

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return this.getUnlocalizedName() + "_" + par1ItemStack.getItemDamage();
	}

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return ItemDye.dyeColors[15 - par1ItemStack.getItemDamage()];
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        itemIcon = Item.silk.getIconFromDamage(0);
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return Item.silk.getIconFromDamage(0);
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for(int i = 0;i < 16 ; i++){
    		par3List.add(new ItemStack(par1, 1, i));
    	}
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
    }

/*
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int posX, int posY, int posZ, int side, float xCoord, float yCoord, float zCoord)
    {
    	if(par3World.isRemote)
    	{
    		return false;
    	}

    	int checkPosX = posX;
    	int checkPosY = posY;
    	int checkPosZ = posZ;

        if (side == 0){
        	//下から叩いた場合
            --checkPosY;
        }else if(side == 1){
        	//上から叩いた場合
        	++checkPosY;
        }else if(side == 2){
            --checkPosZ;
        }else if(side == 3){
        	++checkPosZ;
        }else if(side == 4){
            --checkPosX;
        }else if(side == 5){
        	++checkPosX;
        }

    	float beforeXCoord = 0.0F;
    	float beforeYCoord = 0.0F;
    	float beforeZCoord = 0.0F;

    	NBTTagCompound stackTagCompound = getNBT(par1ItemStack);

    	if(stackTagCompound.getInteger("posX") != checkPosX ||stackTagCompound.getInteger("posY") != checkPosY || stackTagCompound.getInteger("posZ") != checkPosZ)
    	{
    		//位置情報を保持して処理終了
    		stackTagCompound.setInteger("posX", checkPosX);
    		stackTagCompound.setInteger("posY", checkPosY);
    		stackTagCompound.setInteger("posZ", checkPosZ);
    		stackTagCompound.setInteger("side", side);

    		stackTagCompound.setFloat("xCoord", xCoord);
    		stackTagCompound.setFloat("yCoord", yCoord);
    		stackTagCompound.setFloat("zCoord", zCoord);
    		par1ItemStack.setTagCompound(stackTagCompound);
    		return true;
    	}else{
    		int beforeside = stackTagCompound.getInteger("side");
        	beforeXCoord = stackTagCompound.getFloat("xCoord");
        	beforeYCoord = stackTagCompound.getFloat("yCoord");
        	beforeZCoord = stackTagCompound.getFloat("zCoord");

        	stackTagCompound.removeTag("posX");
        	stackTagCompound.removeTag("posY");
        	stackTagCompound.removeTag("posZ");
        	stackTagCompound.removeTag("xCoord");
        	stackTagCompound.removeTag("yCoord");
        	stackTagCompound.removeTag("zCoord");
        	stackTagCompound.setInteger("side", -1);
        	par1ItemStack.setTagCompound(stackTagCompound);

       		if(beforeside != side)
    		{
       			return true;
    		}
    	}


    	//ブロック配置判定
    	int blockID = par3World.getBlockId(posX, posY, posZ);
    	if(blockID != graffitiBlockID)
    	{
    		Block block = Block.blocksList[blockID];
    		if(!isEnable(block , side))
    		{
    			//特殊形状はめんどいので対象外
    			//そのうちチェストとかは考える
    			return false;
    		}

    		blockID = par3World.getBlockId(checkPosX, checkPosY, checkPosZ);
        	if(blockID != graffitiBlockID)
        	{
        		//新規の場合
        		ItemStack itemstack = new ItemStack(graffitiBlock);
        		if(!graffitiBlock.onItemUse(itemstack, par2EntityPlayer, par3World, posX, posY, posZ, side, xCoord, yCoord, zCoord))
        		{
        			return false;
        		}
        	}

    		posX = checkPosX;
    		posY = checkPosY;
    		posZ = checkPosZ;
    	}

    	//座標設定
    	TileEntityGraffitiBlock tileEntity = (TileEntityGraffitiBlock)par3World.getBlockTileEntity(posX, posY, posZ);

    	tileEntity.clickPos(beforeXCoord, beforeYCoord, beforeZCoord, xCoord, yCoord, zCoord, side, par1ItemStack.getItemDamage(), getLineSize(par1ItemStack));
    	par3World.markBlockForUpdate(posX, posY, posZ);

    	return true;
    }
*/

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int posX, int posY, int posZ, int side, float xCoord, float yCoord, float zCoord)
    {
    	if(par3World.isRemote)
    	{
    		return false;
    	}

    	int checkPosX = posX;
    	int checkPosY = posY;
    	int checkPosZ = posZ;

    	int blockID = par3World.getBlockId(posX, posY, posZ);
    	if(blockID != graffitiBlock.getBlockID())
    	{
    		if (side == 0){
    			//下から叩いた場合
    			--checkPosY;
    		}else if(side == 1){
    			//上から叩いた場合
    			++checkPosY;
    		}else if(side == 2){
    			--checkPosZ;
    		}else if(side == 3){
    			++checkPosZ;
    		}else if(side == 4){
    			--checkPosX;
    		}else if(side == 5){
    			++checkPosX;
    		}
    	}

        NBTTagCompound stackTagCompound = getNBT(par1ItemStack);
    	int beforePosX = stackTagCompound.getInteger("posX");
    	int beforePosY = stackTagCompound.getInteger("posY");
    	int beforePosZ = stackTagCompound.getInteger("posZ");

    	//以下の場合は、位置情報の更新
    	//　座標の全てが不一致である場合
    	//　設置面情報を未保持
    	if( (beforePosX != checkPosX && beforePosY != checkPosY & beforePosZ != checkPosZ) ||
    		(stackTagCompound.getInteger("side") == -1))
    	{
    		//位置情報を保持して処理終了
    		stackTagCompound.setInteger("posX", checkPosX);
    		stackTagCompound.setInteger("posY", checkPosY);
    		stackTagCompound.setInteger("posZ", checkPosZ);
    		stackTagCompound.setInteger("side", side);

    		stackTagCompound.setFloat("xCoord", xCoord);
    		stackTagCompound.setFloat("yCoord", yCoord);
    		stackTagCompound.setFloat("zCoord", zCoord);
    		par1ItemStack.setTagCompound(stackTagCompound);
    		return true;
    	}

		int beforeside = stackTagCompound.getInteger("side");
		float beforeXCoord = stackTagCompound.getFloat("xCoord");
		float beforeYCoord = stackTagCompound.getFloat("yCoord");
		float beforeZCoord = stackTagCompound.getFloat("zCoord");

    	stackTagCompound.removeTag("posX");
    	stackTagCompound.removeTag("posY");
    	stackTagCompound.removeTag("posZ");
    	stackTagCompound.removeTag("xCoord");
    	stackTagCompound.removeTag("yCoord");
    	stackTagCompound.removeTag("zCoord");
    	stackTagCompound.setInteger("side", -1);
    	par1ItemStack.setTagCompound(stackTagCompound);

    	//20マス以上離れている場合は処理停止
    	if( Math.abs(checkPosX - beforePosX) > 10 || Math.abs(checkPosY - beforePosY) > 10 || Math.abs(checkPosZ - beforePosZ) > 10)
    	{
    		return false;
    	}

    	//設置面が異なる場合は処理停止
   		if(beforeside != side)
		{
   			return true;
		}

   		//複数ブロックの配置判定
		if(side == 0 || side == 1)
		{
	   		if(checkPosX < beforePosX)
	   		{
	   			int tmpPosX = checkPosX;
	   			int tmpPosY = checkPosY;
	   			int tmpPosZ = checkPosZ;
	   			float tmpXCoord = xCoord;
	   			float tmpYCoord = yCoord;
	   			float tmpZCoord = zCoord;

	   			checkPosX = beforePosX;
	   			checkPosY = beforePosY;
	   			checkPosZ = beforePosZ;
	   			xCoord = beforeXCoord;
	   			yCoord = beforeYCoord;
	   			zCoord = beforeZCoord;

	   			beforePosX = tmpPosX;
	   			beforePosY = tmpPosY;
	   			beforePosZ = tmpPosZ;
	   			beforeXCoord = tmpXCoord;
	   			beforeYCoord = tmpYCoord;
	   			beforeZCoord = tmpZCoord;
	   		}

	   		List<PosLinePoint> list = getPosLinePoint(checkPosX, beforePosX, checkPosZ, beforePosZ, xCoord, beforeXCoord, zCoord, beforeZCoord);

	   		//配置チェック
	   		Iterator<PosLinePoint> iterator = list.iterator();
	   		while(iterator.hasNext())
	   		{

	   			PosLinePoint plpoint = iterator.next();

	   			beforePosX = plpoint.getPosX();
	   			beforePosZ = plpoint.getPosY();

	   			int tmpBlockID = par3World.getBlockId(beforePosX, checkPosY, beforePosZ);
	   			if(tmpBlockID != 0 && tmpBlockID != graffitiBlockID)
	   			{
	   				return false;
	   			}

	   			tmpBlockID = par3World.getBlockId(beforePosX, posY, beforePosZ);
	   			if(tmpBlockID != graffitiBlockID)
	   			{
	   	    		Block block = Block.blocksList[tmpBlockID];
	   	    		if(!isEnable(block , side))
	   	    		{
	   	    			//特殊形状はめんどいので対象外
	   	    			//そのうちチェストとかは考える
	   	    			return false;
	   	    		}
	   			}
	   		}

	   		//配置
	   		iterator = list.iterator();
	   		while(iterator.hasNext())
	   		{

	   			PosLinePoint plpoint = iterator.next();

	   			beforePosX = plpoint.getPosX();
	   			beforePosZ = plpoint.getPosY();

	   			LinePoint lpoint = new LinePoint(plpoint.getXCoordFrom(), 0, plpoint.getYCoordFrom(), plpoint.getXCoordTo(),  0, plpoint.getYCoordTo(),side, par1ItemStack.getItemDamage(), getLineSize(par1ItemStack));

	   			int tmpBlockID = par3World.getBlockId(beforePosX, checkPosY, beforePosZ);
	   			if(tmpBlockID == 0)
	   			{
		    		ItemStack itemstack = new ItemStack(graffitiBlock);
		    		if(!graffitiBlock.onItemUse(itemstack, par2EntityPlayer, par3World, beforePosX, checkPosY, beforePosZ, side, xCoord, yCoord, zCoord))
		    		{
		    			return false;
		    		}
	   			}

		    	TileEntityGraffitiBlock tileEntity = (TileEntityGraffitiBlock)par3World.getBlockTileEntity(beforePosX, checkPosY, beforePosZ);
		    	if(tileEntity != null)
		    	{
			    	tileEntity.clickPos(lpoint);
			    	par3World.markBlockForUpdate(beforePosX, checkPosY, beforePosZ);
		    	}
	   		}

		}
		else if(side == 2 || side == 3)
		{
	   		if(checkPosX < beforePosX)
	   		{
	   			int tmpPosX = checkPosX;
	   			int tmpPosY = checkPosY;
	   			int tmpPosZ = checkPosZ;
	   			float tmpXCoord = xCoord;
	   			float tmpYCoord = yCoord;
	   			float tmpZCoord = zCoord;

	   			checkPosX = beforePosX;
	   			checkPosY = beforePosY;
	   			checkPosZ = beforePosZ;
	   			xCoord = beforeXCoord;
	   			yCoord = beforeYCoord;
	   			zCoord = beforeZCoord;

	   			beforePosX = tmpPosX;
	   			beforePosY = tmpPosY;
	   			beforePosZ = tmpPosZ;
	   			beforeXCoord = tmpXCoord;
	   			beforeYCoord = tmpYCoord;
	   			beforeZCoord = tmpZCoord;
	   		}

	   		List<PosLinePoint> list = getPosLinePoint(checkPosX, beforePosX, checkPosY, beforePosY, xCoord, beforeXCoord, yCoord, beforeYCoord);

	   		//配置チェック
	   		Iterator<PosLinePoint> iterator = list.iterator();
	   		while(iterator.hasNext())
	   		{

	   			PosLinePoint plpoint = iterator.next();

	   			beforePosX = plpoint.getPosX();
	   			beforePosY = plpoint.getPosY();

	   			int tmpBlockID = par3World.getBlockId(beforePosX, beforePosY, checkPosZ);
	   			if(tmpBlockID != 0 && tmpBlockID != graffitiBlockID)
	   			{
	   				return false;
	   			}

	   			tmpBlockID = par3World.getBlockId(beforePosX, beforePosY, posZ);
	   			if(tmpBlockID != graffitiBlockID)
	   			{
	   	    		Block block = Block.blocksList[tmpBlockID];
	   	    		if(!isEnable(block , side))
	   	    		{
	   	    			//特殊形状はめんどいので対象外
	   	    			//そのうちチェストとかは考える
	   	    			return false;
	   	    		}
	   			}
	   		}

	   		//配置
	   		iterator = list.iterator();
	   		while(iterator.hasNext())
	   		{

	   			PosLinePoint plpoint = iterator.next();

	   			beforePosX = plpoint.getPosX();
	   			beforePosY = plpoint.getPosY();

	   			LinePoint lpoint = new LinePoint(plpoint.getXCoordFrom(), plpoint.getYCoordFrom(), 0, plpoint.getXCoordTo(), plpoint.getYCoordTo(), 0, side, par1ItemStack.getItemDamage(), getLineSize(par1ItemStack));

	   			int tmpBlockID = par3World.getBlockId(beforePosX, beforePosY, checkPosZ);
	   			if(tmpBlockID == 0)
	   			{
		    		ItemStack itemstack = new ItemStack(graffitiBlock);
		    		if(!graffitiBlock.onItemUse(itemstack, par2EntityPlayer, par3World, beforePosX, beforePosY, checkPosZ, side, xCoord, yCoord, zCoord))
		    		{
		    			return false;
		    		}
	   			}

		    	TileEntityGraffitiBlock tileEntity = (TileEntityGraffitiBlock)par3World.getBlockTileEntity(beforePosX, beforePosY, checkPosZ);
		    	if(tileEntity != null)
		    	{
			    	tileEntity.clickPos(lpoint);
			    	par3World.markBlockForUpdate(beforePosX, beforePosY, checkPosZ);
		    	}
	   		}

		}
		else if(side == 4 || side == 5)
		{
	   		if(checkPosZ < beforePosZ)
	   		{
	   			int tmpPosX = checkPosX;
	   			int tmpPosY = checkPosY;
	   			int tmpPosZ = checkPosZ;
	   			float tmpXCoord = xCoord;
	   			float tmpYCoord = yCoord;
	   			float tmpZCoord = zCoord;

	   			checkPosX = beforePosX;
	   			checkPosY = beforePosY;
	   			checkPosZ = beforePosZ;
	   			xCoord = beforeXCoord;
	   			yCoord = beforeYCoord;
	   			zCoord = beforeZCoord;

	   			beforePosX = tmpPosX;
	   			beforePosY = tmpPosY;
	   			beforePosZ = tmpPosZ;
	   			beforeXCoord = tmpXCoord;
	   			beforeYCoord = tmpYCoord;
	   			beforeZCoord = tmpZCoord;
	   		}

	   		List<PosLinePoint> list = getPosLinePoint(checkPosZ, beforePosZ, checkPosY, beforePosY, zCoord, beforeZCoord, yCoord, beforeYCoord);

	   		//配置チェック
	   		Iterator<PosLinePoint> iterator = list.iterator();
	   		while(iterator.hasNext())
	   		{

	   			PosLinePoint plpoint = iterator.next();

	   			beforePosZ = plpoint.getPosX();
	   			beforePosY = plpoint.getPosY();

	   			int tmpBlockID = par3World.getBlockId(checkPosX, beforePosY, beforePosZ);
	   			if(tmpBlockID != 0 && tmpBlockID != graffitiBlockID)
	   			{
	   				return false;
	   			}

	   			tmpBlockID = par3World.getBlockId(posX, beforePosY, beforePosZ);
	   			if(tmpBlockID != graffitiBlockID)
	   			{
	   	    		Block block = Block.blocksList[tmpBlockID];
	   	    		if(!isEnable(block , side))
	   	    		{
	   	    			//特殊形状はめんどいので対象外
	   	    			//そのうちチェストとかは考える
	   	    			return false;
	   	    		}
	   			}
	   		}

	   		//配置
	   		iterator = list.iterator();
	   		while(iterator.hasNext())
	   		{

	   			PosLinePoint plpoint = iterator.next();

	   			beforePosZ = plpoint.getPosX();
	   			beforePosY = plpoint.getPosY();

	   			LinePoint lpoint = new LinePoint(0, plpoint.getYCoordFrom(), plpoint.getXCoordFrom(), 0, plpoint.getYCoordTo(), plpoint.getXCoordTo(), side, par1ItemStack.getItemDamage(), getLineSize(par1ItemStack));

	   			int tmpBlockID = par3World.getBlockId(checkPosX, beforePosY, beforePosZ);
	   			if(tmpBlockID == 0)
	   			{
		    		ItemStack itemstack = new ItemStack(graffitiBlock);
		    		if(!graffitiBlock.onItemUse(itemstack, par2EntityPlayer, par3World, checkPosX, beforePosY, beforePosZ, side, xCoord, yCoord, zCoord))
		    		{
		    			return false;
		    		}
	   			}

		    	TileEntityGraffitiBlock tileEntity = (TileEntityGraffitiBlock)par3World.getBlockTileEntity(checkPosX, beforePosY, beforePosZ);
		    	if(tileEntity != null)
		    	{
			    	tileEntity.clickPos(lpoint);
			    	par3World.markBlockForUpdate(checkPosX, beforePosY, beforePosZ);
		    	}
	   		}
		}

    	return true;
    }


    private List<PosLinePoint> getPosLinePoint(int checkPosX, int beforePosX, int checkPosY, int beforePosY, float xCoord, float beforeXCoord, float yCoord, float beforeYCoord)
    {
    	List<PosLinePoint> list = new ArrayList<PosLinePoint>();

   		Vec2DImpl vec2 = new Vec2DImpl(beforePosX + beforeXCoord , beforePosY + beforeYCoord, checkPosX + xCoord , checkPosY + yCoord);

   		int addY = (checkPosY < beforePosY ? -1 : 1);
   		int heightY = (checkPosY < beforePosY ? 0 : 1);
   		int tmpPosX = beforePosX;
   		int tmpPosY = beforePosY;

   		Vec2DImpl tmpVec2;
   		Point p;

   		Point pointX;
   		Point pointY;

   		do
   		{
	   		Vec2DImpl tmpVecX = new Vec2DImpl(tmpPosX +1, tmpPosY + 0, tmpPosX +1, tmpPosY + 1);
	   		pointX = Vec2DIntersectSupport.getIntercectPoint(vec2, tmpVecX);

	   		Vec2DImpl tmpVecY = new Vec2DImpl(tmpPosX + 0 , tmpPosY + heightY, tmpPosX + 1 , tmpPosY + heightY);
	   		pointY = Vec2DIntersectSupport.getIntercectPoint(vec2, tmpVecY);

	   		PosLinePoint lpoint = new PosLinePoint(beforePosX, beforePosY, beforeXCoord, 1.0F, beforeYCoord, (float)heightY);


	   		if(pointX == null && pointY == null)
	   		{
	   			lpoint.setEndCoordY(yCoord);
	   			lpoint.setEndCoordX(xCoord);
	   		}
	   		else if(pointX != null && pointY != null)
	   		{
	   			lpoint.setEndCoordY(1.0F);
	   			lpoint.setEndCoordX((float)heightY);
	   			tmpPosX++;
	   			tmpPosY += addY;

	   			beforeXCoord = 0.0F;
	   			beforeYCoord = (float)heightY - (float)addY;
	   		}
	   		else if(pointX != null)
		   	{
	   			lpoint.setEndCoordY((float)(pointX.y - tmpPosY));
		   		tmpPosX++;

		   		beforeXCoord = 0.0F;
		   		beforeYCoord = (float)(pointX.y - tmpPosY);
		   	}
	   		else if(pointY != null)
		   	{
		   		lpoint.setEndCoordX((float)(pointY.x - tmpPosX));
		   		tmpPosY += addY;

		   		beforeXCoord = (float)(pointY.x - tmpPosX);
		   		beforeYCoord = (float)heightY - (float)addY;
	   		}

	    	beforePosX = tmpPosX;
	    	beforePosY = tmpPosY;

	    	list.add(lpoint);

   		}while(pointX != null || pointY != null);

    	return list;
    }


    private NBTTagCompound getNBT(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound;
    	if(!itemstack.hasTagCompound()){
    		stackTagCompound = new NBTTagCompound("clickPos");
    		stackTagCompound.setInteger("lineSize", 1);
    		stackTagCompound.setInteger("side", -1);
    		itemstack.setTagCompound(stackTagCompound);
    	}else{
    		stackTagCompound = itemstack.getTagCompound();
    	}

    	return stackTagCompound;
    }

    public boolean isState(ItemStack itemstack)
    {
    	return getSide(itemstack) != -1;
    }

    public int getSide(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBT(itemstack);
    	return stackTagCompound.getInteger("side");
    }

    public float getXCoord(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBT(itemstack);
    	return stackTagCompound.getFloat("xCoord");
    }

    public float getYCoord(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBT(itemstack);
    	return stackTagCompound.getFloat("yCoord");
    }

    public float getZCoord(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBT(itemstack);
    	return stackTagCompound.getFloat("zCoord");
    }

    public void setLineSize(ItemStack itemstack, int size)
    {
    	NBTTagCompound stackTagCompound = getNBT(itemstack);
    	if(size > 50) size = 50;
    	stackTagCompound.setInteger("lineSize", size);

    	itemstack.setTagCompound(stackTagCompound);
    }

    public void addLineSize(ItemStack itemstack, ItemStack additemstack)
    {
    	int size = getLineSize(itemstack) + getLineSize(additemstack);
    	setLineSize(itemstack,size);
    }

    public int getLineSize(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBT(itemstack);
    	return stackTagCompound.getInteger("lineSize");
    }

    public boolean isEnable(Block block, int side)
    {
    	return (block == null) || block.isOpaqueCube() || (block.blockID == Block.glass.blockID);
    }

    @SideOnly(Side.CLIENT)
    public void displayItemInfo(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBT(itemstack);

    	String Info = "LineWidth : " + getLineSize(itemstack) + " , State : " + (stackTagCompound.getInteger("side") == -1 ? "false" : "true");

        Minecraft mc = Minecraft.getMinecraft();
        GameSettings gameSettings = mc.gameSettings;
        int w = mc.displayWidth;
        int h = mc.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(gameSettings, w, h);
        w = scaledresolution.getScaledWidth();
        h = scaledresolution.getScaledHeight();
        mc.fontRenderer.drawString(Info, w - 150 + ((-1) * posionY), h - 40 + ((-1) * posionX), color);
    }
}