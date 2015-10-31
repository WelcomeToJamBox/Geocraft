package redgear.geocraft.generation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import redgear.core.world.Location;
import redgear.geocraft.api.mine.Mine;
import redgear.geocraft.mines.MineMetioricDeposit;

import java.util.Arrays;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class DebugMineGenCommand implements ICommand {

	private List<String> aliases;
	//TODO Create this as list from all mines registered
	private String[] mineNames= {"metioric","laccolith"};
	public DebugMineGenCommand() {
		this.aliases = new ArrayList<String>();
		this.aliases.add("geogen");
		this.aliases.add("DebugMineGen");
		//for debug
		this.aliases.add("gen");
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		
		return "GeocraftDebugGen";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
    //TODO maybe add ability to add co-ords
		return "geogen <minealias> OR geogen <'list'> for list of aliases, followed by x,y,z for position";
	}

	@Override
	public List getCommandAliases() {
	
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] argument) {
		World world = sender.getEntityWorld();
			if(world.isRemote) {
				System.out.println("GeoGen called on client returning");
				sender.addChatMessage(new ChatComponentText("you called GeoGen on client...please dont do that"));
			}
			else {
				System.out.println("GeoGen called");
				if(argument.length==0) {
					sender.addChatMessage(new ChatComponentText("invald: no argument"));
					return;
				}
				else if(argument[0].equals("list")) {
					sender.addChatMessage(new ChatComponentText(Arrays.toString(mineNames)));
					return;
					}
				else {					
					
					int num=Arrays.asList(mineNames).indexOf(argument[0]);
					if(num==-1) {
						sender.addChatMessage(new ChatComponentText("Generating Failed ["+argument[0]+"] is not a valid mine name"));
						return;}
					
					sender.addChatMessage(new ChatComponentText("Generating; "+argument[0]));
					ChunkCoordinates chunkCoords = sender.getPlayerCoordinates();
					Location playerLocation = new Location(chunkCoords.posX, chunkCoords.posY, chunkCoords.posZ);
					//get minecrafts random
					Random newRandom = new Random();
					int x=chunkCoords.posX;
					int y=chunkCoords.posY+10;
					int z=chunkCoords.posZ;
					
					switch (num) {
					case 0: getMine("Taenite").generateMine(world, newRandom, x, z, true, y);
					case 1: getMine("debugLaccolith").generateMine(world, newRandom, x, z, true, y);
					default: return;
					}
				}
			}

	}

    private Mine getMine(String name) {
    	Mine theMine;
    	Set<Mine> mineSet = MineGenerator.reg.mines;
    	for(Mine mine: mineSet) {
    		if(mine.name.equalsIgnoreCase(name)) {
    			theMine=mine;
    			return theMine;
    		}
    	}
		return null;
    }
    
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		// TODO Auto-generated method stub
		//add creative only
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		// TODO Auto-generated method stub
		return false;
	}

}
