package net.minecraft.graffiti;

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

    public ItemGraffiti(int itemID , int blockID, int posionX, int posionY, int color)
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