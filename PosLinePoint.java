package net.minecraft.graffiti;

import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class PosLinePoint
{
	private LinePoint point;
	private int posX;
	private int posY;
	private float xCoordFrom;
	private float xCoordTo;
	private float yCoordFrom;
	private float yCoordTo;


    public PosLinePoint(int posX, int posY, float xCoordFrom, float xCoordTo, float yCoordFrom, float yCoordTo)
    {
        this.posX = posX;
        this.posY = posY;
        this.xCoordFrom = xCoordFrom;
        this.xCoordTo = xCoordTo;
        this.yCoordFrom = yCoordFrom;
        this.yCoordTo = yCoordTo;
    }

    public void setPosX(int posX)
    {
        this.posX = posX;
    }

    public void setPosY(int posY)
    {
        this.posY = posY;
    }

    public void setEndCoordX(float xCoordTo)
    {
        this.xCoordTo = xCoordTo;
    }

    public void setEndCoordY(float yCoordTo)
    {
        this.yCoordTo = yCoordTo;
    }


    public int getPosX()
    {
        return this.posX;
    }

    public int getPosY()
    {
    	return this.posY;
    }

    public float getXCoordTo()
    {
    	return this.xCoordTo;
    }

    public float getXCoordFrom()
    {
    	return this.xCoordFrom;
    }

    public float getYCoordFrom()
    {
    	return this.yCoordFrom;
    }

    public float getYCoordTo()
    {
    	return this.yCoordTo;
    }


}