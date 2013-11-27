package net.minecraft.graffiti;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockGraffiti extends ItemBlock
{
    public ItemBlockGraffiti(int blockID)
    {
		super(blockID);
	}

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        Block block = Block.blocksList[getBlockID()];
        int j1 = this.getMetadata(par1ItemStack.getItemDamage());
        int k1 = Block.blocksList[getBlockID()].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, j1);

        if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, k1))
        {
            --par1ItemStack.stackSize;
        }

        return true;
    }
}