package com.hbm.tileentity.machine;

import java.util.ArrayList;
import java.util.List;

import com.hbm.config.WeaponConfig;
import com.hbm.interfaces.IConsumer;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.packet.AuxElectricityPacket;
import com.hbm.packet.PacketDispatcher;
import com.hbm.tileentity.TileEntityTickingBase;

import api.hbm.entity.IRadarDetectable;
import api.hbm.entity.IRadarDetectable.RadarTargetType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMachineRadar extends TileEntityTickingBase implements ITickable, IConsumer {

	public List<int[]> nearbyMissiles = new ArrayList<int[]>();
	int pingTimer = 0;
	int lastPower;
	final static int maxTimer = 40;

	public long power = 0;
	public static final int maxPower = 100000;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		power = compound.getLong("power");
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setLong("power", power);
		return super.writeToNBT(compound);
	}
	
	@Override
	public String getInventoryName() {
		return "";
	}
	
	@Override
	public void update() {
		if(pos.getY() < WeaponConfig.radarAltitude)
			return;
		
		int lastPower = getRedPower();
		
		if(!world.isRemote) {
			nearbyMissiles.clear();

			if(power > 0) {
				allocateMissiles();

				power -= 500;

				if(power < 0)
					power = 0;
			}
			
			if(lastPower != getRedPower())
				world.notifyNeighborsOfStateChange(pos, getBlockType(), true);

			sendMissileData();

			pingTimer++;

			if(power > 0 && pingTimer >= maxTimer) {
				this.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), HBMSoundHandler.sonarPing, SoundCategory.BLOCKS, 5.0F, 1.0F);
				pingTimer = 0;
			}
			
			PacketDispatcher.wrapper.sendToAllAround(new AuxElectricityPacket(pos, power), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 10));
		}
	}
	
	private void allocateMissiles() {
		
		nearbyMissiles.clear();
		
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.getX() + 0.5 - WeaponConfig.radarRange, 0, pos.getZ() + 0.5 - WeaponConfig.radarRange, pos.getX() + 0.5 + WeaponConfig.radarRange, 5000, pos.getZ() + 0.5 + WeaponConfig.radarRange));

		for(Entity e : list) {

			if(e instanceof EntityPlayer && e.posY >= pos.getY() + WeaponConfig.radarBuffer) {
				nearbyMissiles.add(new int[] { (int)e.posX, (int)e.posZ, RadarTargetType.PLAYER.ordinal(), (int)e.posY });
			}
			
			if(e instanceof IRadarDetectable && e.posY >= pos.getY() + WeaponConfig.radarBuffer) {
				nearbyMissiles.add(new int[] { (int)e.posX, (int)e.posZ, ((IRadarDetectable)e).getTargetType().ordinal(), (int)e.posY });
			}
		}
	}
	
	public int getRedPower() {
		
		if(!nearbyMissiles.isEmpty()) {
			
			double maxRange = WeaponConfig.radarRange * Math.sqrt(2D);
			
			int power = 0;
			
			for(int i = 0; i < nearbyMissiles.size(); i++) {
				
				int[] j = nearbyMissiles.get(i);
				double dist = Math.sqrt(Math.pow(j[0] - pos.getX(), 2) + Math.pow(j[1] - pos.getZ(), 2));
				int p = 15 - (int)Math.floor(dist / maxRange * 15);
				
				if(p > power)
					power = p;
			}
			
			return power;
		}
		
		return 0;
	}
	
	private void sendMissileData() {
		
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setInteger("count", this.nearbyMissiles.size());

		for(int i = 0; i < this.nearbyMissiles.size(); i++) {
			data.setInteger("x" + i, this.nearbyMissiles.get(i)[0]);
			data.setInteger("z" + i, this.nearbyMissiles.get(i)[1]);
			data.setInteger("type" + i, this.nearbyMissiles.get(i)[2]);
			data.setInteger("y" + i, this.nearbyMissiles.get(i)[3]);
		}

		this.networkPack(data, 15);
	}

	@Override
	public void networkUnpack(NBTTagCompound data) {
		this.nearbyMissiles.clear();
		this.power = data.getLong("power");

		int count = data.getInteger("count");

		for(int i = 0; i < count; i++) {

			int x = data.getInteger("x" + i);
			int z = data.getInteger("z" + i);
			int type = data.getInteger("type" + i);
			int y = data.getInteger("y" + i);

			this.nearbyMissiles.add(new int[] {x, z, type, y});
		}
	}
	
	public long getPowerScaled(long i) {
		return (power * i) / maxPower;
	}

	@Override
	public void setPower(long i) {
		if(power != i)
			markDirty();
		power = i;
	}

	@Override
	public long getPower() {
		return power;
	}

	@Override
	public long getMaxPower() {
		return maxPower;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536.0D;
	}

}