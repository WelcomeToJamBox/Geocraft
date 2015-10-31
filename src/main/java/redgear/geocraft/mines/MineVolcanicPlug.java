package redgear.geocraft.mines;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.util.SimpleItem;
import redgear.core.world.Location;
import redgear.geocraft.api.mine.MineSpecialStone;

public class MineVolcanicPlug extends MineSpecialStone {


	/*Generates a surface plug with a tail, not very dense in ore
	*/
	protected transient int intRarity;
	protected transient int intMineSize;
	protected transient int intVeinSize;

	float mineRarity;
	float mineSize;
	float veinSize;
	
	public MineVolcanicPlug(String name, ISimpleItem block,int mineRarity, int mineSize, int veinSize, ISimpleItem target,
			ISimpleItem bearer) {
		super(name, block, target, bearer);
		this.mineSize = mineSize;
		this.mineRarity = mineRarity;
		this.veinSize = veinSize;
	}

	@Override
	public void generateMine(World world, Random rand, int chunkX, int chunkZ,
			Boolean debug, int commandY) {

		if(rand.nextInt(intRarity)==0 || debug) {
			
			  int centreX=(debug ? chunkX: chunkX*16+rand.nextInt(16));
			  int centreZ=(debug ? chunkZ: chunkZ*16+rand.nextInt(16));
			  int height=-4; //for now, could let it be worked out by surface finder
			  //think height might be yaxis-underground part since this form opens downwards from origin.
			  //TODO use ISurfaceOrientable
			  int centreY=(debug ? commandY : world.getHeightValue(chunkX, chunkZ)-height);
			//greater than 10 will be every, time change later
			  int ratio=(intMineSize<7 ? Math.max(intVeinSize-intMineSize/2, 1): intVeinSize);
			  int adjMineSize=(intMineSize>=20 ? 20 : (intMineSize<7 ? 7 : intMineSize));
			  //int plugChance=(intMineSize>=20 ? Math.max(10-(intMineSize-20)/2, 1) : 10);
			  int xzAxis=rand.nextInt(adjMineSize-5)+5;			 
			  int yAxis=adjMineSize-xzAxis;
			  
			  Location start= new Location(centreX, centreY, centreZ);
			  genPlug(world, rand, start, ratio, height, xzAxis, yAxis);
			  genTail(world, rand, start, adjMineSize, xzAxis, ratio, debug); 
		}
	}
	
	private void genPlug(World world, Random rand, Location centre, int ratio, int height, int xzAxis, int yAxis ) {
		
		float xzAxisSqr=(xzAxis*xzAxis);
		
		SimpleItem grassBlock= new SimpleItem(Blocks.grass);
		SimpleItem dirtBlock= new SimpleItem(Blocks.dirt);
		
		for (int x=-xzAxis; x<=xzAxis; x++) {
			for (int z=-xzAxis; z<=xzAxis; z++) {
				for (int y=yAxis; y>=0; y--) {
			
			float c1=((x*x)/xzAxisSqr);
				if(Float.isNaN(c1)) {c1=0; }
				float c2=((z*z)/xzAxisSqr);
				if(Float.isNaN(c2)) {c2=0; }
				float c3=(y/(-yAxis));
					if(Float.isNaN(c3)) {c3=0; }
				float bounds=Math.abs(c1+c2-c3+height);
		
				if(bounds<=0) {
					Location loc=new Location(x,y,z).translate(centre);
		
		    			if(bounds==0 && rand.nextInt(3)==0) {
		    				if(loc.translate(ForgeDirection.UP, 1).isAir(world)) {loc.placeBlock(world,grassBlock); }
		    				else {loc.placeBlock(world,dirtBlock); }
		    			}
		    			else {
		    				if(rand.nextInt(10)<=ratio)  {loc.placeBlock(world,block); }
		    				else {loc.placeBlock(world, bearer); }
		    			}
					}
				}
			}
		}
	}
		 
	
	private void genTail(World world, Random rand, Location start, int remaining, int rad, int ratio, Boolean debug) {
		  Location next= start.translate(rand.nextInt(2)-1,-1,rand.nextInt(2)-1);
		   if(remaining>=1) {
			   
		     int radius= (rad>=1 ? rad/2 : 1);
		     
		     if (radius==1) {
		    	 
		    	 if (debug) {next.placeBlock(world,bearer); }
		    	 else {next.placeBlock(world,bearer,target); }
		     }
		     
		     else {
			     for(int x=-radius; x<=radius; x++) {
			        for(int z=-radius; z<=radius; z++) {
			        	
			           if(Math.sqrt((x*x)+(z*z))<=radius) {
			        	   
			           Location blockLoc= new Location(x,0,z).translate(next);
			           
			           if(rand.nextInt(11)<=ratio) {
			        	   if(debug) {blockLoc.placeBlock(world,block); }
			        	   else {blockLoc.placeBlock(world,block,target); }
			            	}
			           
			           else{ 
			        	   if(debug) {blockLoc.placeBlock(world,bearer); }
			        	   else {blockLoc.placeBlock(world,bearer,target); }
			            	} 
			           }
			        }
			     }
		     }
		     
		     genTail(world, rand, next, remaining--, ratio, radius, debug);
		     
		 } 
	}
	
	
	
	@Override
	public void generate(World world, Random rand, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
	
	}



	@Override
	public void init() {
		super.init();
		intRarity = (int) (mineRarity * getRegistry().rarityModifier());
		if (intRarity <= 0)
			intRarity = 1;
		intMineSize = (int) (mineSize * getRegistry().volumeModifier() / 2);
		intVeinSize = (int) (veinSize * getRegistry().volumeModifier() / 2);
	}

}
