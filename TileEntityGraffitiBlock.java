package net.minecraft.graffiti;

import java.io.DataOutputStream;
import java.io.IOException;
import com.google.common.io.ByteArrayDataInput;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGraffitiBlock extends TileEntity
{
	private LinePointManager manager;

    public TileEntityGraffitiBlock()
    {
		manager = new LinePointManager();
    }


	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		manager.readFromNBT(par1NBTTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		manager.writeToNBT(par1NBTTagCompound);
	}

    public boolean[] getSideArray()
    {
    	return manager.getSideArray();
	}

    public void clickPos(float posXstart, float posYstart, float posZstart, float posXend, float posYend, float posZend, int side, int color, int size)
    {
    	manager.addPoint(posXstart,posYstart,posZstart,posXend,posYend,posZend,side,color,size);
	}

    public boolean removePos()
    {
    	return manager.removePoint();
	}

    public LinePoint[] getPoints()
    {
    	return manager.getArray();
    }


	public void readToPacket(ByteArrayDataInput data)
	{
		manager.readToPacket(data);
	}


	public void writeToPacket(DataOutputStream dos)
	{
		manager.writeToPacket(dos);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		//パケットの取得
		return PacketHandler.getPacket(this);
	}

}
