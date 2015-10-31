/**
 * 
 */
package redgear.geocraft.mines;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.util.SimpleItem;
import redgear.core.world.Location;
import redgear.geocraft.api.mine.MineFault;

/**
 * @author JamBox
 *
 */
public class MinePipe extends MineFault {

	SimpleItem lava= new SimpleItem(Blocks.lava);

	
	public MinePipe(String name, ISimpleItem block, ISimpleItem target,
			float mineRarity, float mineSize, int veinSize) {
		super(name, block, target, mineRarity, mineSize, veinSize);
		
	}
	
	@Override
	protected void genVein(World world, Random rand, Location veinStart,
			Location mineStart, int length, int horRot45, int verRot45,
			ForgeDirection orient, int[] modifiers, Boolean debug) {
		
		 int iRad=modifiers[0];
		  int oRad=iRad+intVeinSize;

		  for (int z=-oRad; z<=oRad; z++) {
		    for (int y=-oRad; y<=oRad; y++) {
		       double d= Math.sqrt((z*z)+(y*y));
		       if (d<=oRad) {
		       
		         Location nextBlock=(orient == ForgeDirection.NORTH ? new Location(z,0,y).translate(veinStart): new Location(0,y,z).translate(veinStart));
		         nextBlock=rotate45(nextBlock, mineStart, horRot45, true);
		         nextBlock=rotate45(nextBlock, mineStart, verRot45, false);
		         
		         if (d<=iRad) {
		           if(debug) {
		              nextBlock.placeBlock(world, lava); 
		            }
		           else {
		              nextBlock.placeBlock(world, lava, target);
		             }          
		            }
		         else {
		            if(debug) {
		              nextBlock.placeBlock(world, block);
		              }
		            else {
		              nextBlock.placeBlock(world, block, target);
		             }
	              }
		       }
		    }
		  }
	}
		       
		
	
}
