package com.hbm.blocks.machine;

import com.hbm.tileentity.conductor.TileEntityFFDuctBase;
import com.hbm.tileentity.conductor.TileEntityFFFluidDuct;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFluidDuct extends BlockContainer {

	public BlockFluidDuct(Material p_i45386_1_) {
		super(p_i45386_1_);
		float p = 1F/16F;
		this.setBlockBounds(11 * p / 2, 11 * p / 2, 11 * p / 2, 1 - 11 * p / 2, 1 - 11 * p / 2, 1 - 11 * p / 2);
		this.useNeighborBrightness = true;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		if(world.getTileEntity(x, y, z) instanceof TileEntityFFFluidDuct) {
			TileEntityFFFluidDuct cable = (TileEntityFFFluidDuct)world.getTileEntity(x, y, z);

		if(cable != null)
		{
			float p = 1F/16F;
			float minX = 11 * p / 2 - (cable.connections[5] != null ? (11 * p / 2) : 0);
			float minY = 11 * p / 2 - (cable.connections[1] != null ? (11 * p / 2) : 0);
			float minZ = 11 * p / 2 - (cable.connections[2] != null ? (11 * p / 2) : 0);
			float maxX = 1 - 11 * p / 2 + (cable.connections[3] != null ? (11 * p / 2) : 0);
			float maxY = 1 - 11 * p / 2 + (cable.connections[0] != null ? (11 * p / 2) : 0);
			float maxZ = 1 - 11 * p / 2 + (cable.connections[4] != null ? (11 * p / 2) : 0);
			
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}
		}
		return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if(world.getTileEntity(x, y, z) instanceof TileEntityFFFluidDuct) {
			TileEntityFFFluidDuct cable = (TileEntityFFFluidDuct)world.getTileEntity(x, y, z);

		if(cable != null)
		{
			float p = 1F/16F;
			float minX = 11 * p / 2 - (cable.connections[5] != null ? (11 * p / 2) : 0);
			float minY = 11 * p / 2 - (cable.connections[1] != null ? (11 * p / 2) : 0);
			float minZ = 11 * p / 2 - (cable.connections[2] != null ? (11 * p / 2) : 0);
			float maxX = 1 - 11 * p / 2 + (cable.connections[3] != null ? (11 * p / 2) : 0);
			float maxY = 1 - 11 * p / 2 + (cable.connections[0] != null ? (11 * p / 2) : 0);
			float maxZ = 1 - 11 * p / 2 + (cable.connections[4] != null ? (11 * p / 2) : 0);
			
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityFFFluidDuct();
	}
	
	@Override
	public int getRenderType(){
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int whatever){
		if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityFFDuctBase) {
			((TileEntityFFDuctBase)world.getTileEntity(x, y, z)).breakBlock();
		}
		super.breakBlock(world, x, y, z, block, whatever);
		
	}
	
	@Override
	 public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
	
		if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityFFDuctBase) {
			((TileEntityFFDuctBase)world.getTileEntity(x, y, z)).onNeighborBlockChange();
		}
		
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		//if(!world.isRemote)
		//	System.out.println(((TileEntityFFDuctBase)world.getTileEntity(x, y, z)).getNetworkTrue().getTankInfo(ForgeDirection.UNKNOWN)[0].fluid.amount);
		return super.onBlockActivated(world, x, y, z, p_149727_5_, p_149727_6_, p_149727_7_,
				p_149727_8_, p_149727_9_);
	}

}
