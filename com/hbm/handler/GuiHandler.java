package com.hbm.handler;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.container.ContainerDiFurnace;
import com.hbm.inventory.container.ContainerMachinePress;
import com.hbm.inventory.gui.GUIMachinePress;
import com.hbm.inventory.gui.GUITestDiFurnace;
import com.hbm.tileentity.machine.TileEntityDiFurnace;
import com.hbm.tileentity.machine.TileEntityMachinePress;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		if (entity != null) {
			switch (ID) {
			case ModBlocks.guiID_machine_press: {
				if (entity instanceof TileEntityMachinePress) {
					return new ContainerMachinePress(player.inventory, (TileEntityMachinePress) entity);
				}
				return null;
			}
			case ModBlocks.guiID_test_difurnace:
			{
				if(entity instanceof TileEntityDiFurnace)
				{
					return new ContainerDiFurnace(player.inventory, (TileEntityDiFurnace) entity);
				}
				return null;
			}
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		if (entity != null) {
			switch(ID){
			case ModBlocks.guiID_machine_press: {
				if (entity instanceof TileEntityMachinePress) {
					return new GUIMachinePress(player.inventory, (TileEntityMachinePress) entity);
				}
				return null;
			}
			case ModBlocks.guiID_test_difurnace:
			{
				if(entity instanceof TileEntityDiFurnace)
				{
					return new GUITestDiFurnace(player.inventory, (TileEntityDiFurnace) entity);
				}
				return null;
			}
			}
		}
		return null;
	}

}