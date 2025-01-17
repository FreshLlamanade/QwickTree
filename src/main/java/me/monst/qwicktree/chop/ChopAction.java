package me.monst.qwicktree.chop;

import java.util.*;

import me.monst.qwicktree.QwickTree;
import me.monst.qwicktree.config.Configuration;
import me.monst.qwicktree.util.HouseIgnore;
import me.monst.qwicktree.util.Message;
import me.monst.qwicktree.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.monst.qwicktree.tree.Tree;
import me.monst.qwicktree.util.Logging;
import me.monst.qwicktree.util.debug.Debugger;

public class ChopAction {
	
	private final Configuration config;
	private final Player player;
	private final Tree tree;
	
	private final Stack<Block> logsToSearch;
	private final List<Location> baseBlocks;
	private final Location dropLocation;
	
	private final List<Block> logs;
	private final List<Block> leaves;
	private final List<Block> vines;
	
	private boolean 	doReplant;
	private int 		totalToReplant,
						leftToTake;
	
	private final Random rnd;
	
	private boolean ignoreHouseBlocks;
	private final Debugger debugger;
	
	public ChopAction(Configuration config, Player player, Tree tree, Block baseBlock) {
		this.config = config;
		this.player = player;
		this.tree = tree;
		
		logsToSearch = new Stack<>();
		logs = new LinkedList<>();
		leaves = new LinkedList<>();
		baseBlocks = new LinkedList<>();
		vines = new LinkedList<>();
		
		rnd = new Random();
		
		ignoreHouseBlocks = false;
		
		debugger = Debugger.get(player);
		
		logsToSearch.add(baseBlock);
		dropLocation = baseBlock.getLocation();
	}
	
	public void go() {
		debugger.addStage("CA.go"); //1
		//Check
		if (!check()) return;

		debugger.addStage("CA.go"); //2
		//Damage
		if (!damage()) return;

		debugger.addStage("CA.go"); //3
		//Chop
		chop();

		debugger.addStage("CA.go"); //4
		//Replant
		replant();

		debugger.addStage("CA.go"); //5
	}
	
	private boolean check() {
		debugger.addStage("CA.check"); //1
		//Log search
		if (!logSearch()) return false;

		debugger.addStage("CA.check"); //2
		//Leaf/other search
		if (!leafSearch()) return false;

		debugger.addStage("CA.check"); //3
		//Size check
		if (!checkSize()) return false;

		debugger.addStage("CA.check"); //4
		
		checkReplant();
		
		return true;
	}
	
	private boolean damage() {
		return doDamage();
	}
	
	private void chop() {
		QwickTree.get().addTreeChop(tree.getType());
		
		//Break all the leaves and vines
		debugger.addStage("CA.chop"); //1
		for (Block leaf: leaves)
			disappear(leaf);

		debugger.addStage("CA.chop"); //2
		for (Block vine: vines)
			disappear(vine);

		debugger.addStage("CA.chop"); //3
		//Check if autoCollect
		if (player.getGameMode() == GameMode.CREATIVE && config.creativeModeSettings.doAutoCollect.get() || tree.doAutoCollect()) {
			
			for (Block log: logs)
				disappear(log);
			
			dropToInventory();
		}
		//Check if groupDrops (enabled if tree has replant timer)
		else if (config.doGroupDrops.get() || tree.getReplantTimer() > 0) {
			for (Block log: logs)
				disappear(log);
			
			dropToGroup();
		}
		//Check normal
		else {
			logs.forEach(this::breakBlock);
			
			dropToWorld();
		}
		debugger.addStage("CA.chop"); //4
	}
	
	private void replant() {
		if (!doReplant)
			return;
		
		//If there are any saplings left to take, we'll need to look in the player's inventory
		if (leftToTake > 0)
			takeSaplingsFromInventory();
		
		
		int toReplant = totalToReplant - leftToTake;
		
		debugger.setStage("Saplings Taken", toReplant);
		debugger.setStage("Saplings Remain", leftToTake);
		
		if (player.getGameMode() == GameMode.CREATIVE)
			toReplant = totalToReplant;
		
		TreeReplanter replanter = new TreeReplanter(tree, baseBlocks, toReplant);
		
		if (tree.getReplantTimer() > 0)
			Bukkit.getScheduler().scheduleSyncDelayedTask(QwickTree.get(), replanter, tree.getReplantTimer());
		else
			Bukkit.getScheduler().scheduleSyncDelayedTask(QwickTree.get(), replanter);
	}
	
	private void takeSaplingsFromInventory() {
		
		for (ItemStack item : player.getInventory()) {
			if (item == null)
				continue;
			
			if (tree.isValidSapling(item)) {
				int toRemove = Math.min(item.getAmount(), leftToTake);
				
				item.setAmount(item.getAmount() - toRemove);
				leftToTake -= toRemove;
				
				if (leftToTake <= 0)
					break;
			}
		}
	}
	
	/* ### CHECK ### */
	private boolean logSearch() {
		while (!logsToSearch.isEmpty()) {
			//Get the next block to search around
			Block current = logsToSearch.pop();
			
			//Process it...
			if (!processCurrentLog(current)) return false;
			
			//Then search around it.
			if (!searchCurrentLog(current)) return false;
		}
		
		return true;
	}
	
	private boolean processCurrentLog(Block block) {
		//Don't add it if it's already in the list
		if (logs.contains(block)) return true;
		
		//Add it to the list
		logs.add(block);
		
		//Check against max tree size
		if (logs.size() > tree.getLogMax()) return false;
		
		//If it's a standing block, then add it to base blocks too, but only if there's space
		if (tree.isValidStandingBlock(block.getRelative(BlockFace.DOWN)) && baseBlocks.size() < 4)
			baseBlocks.add(block.getLocation());
		
		return true;
	}
	
	private boolean searchCurrentLog(Block current) {
		int yStart = tree.getAnyBlock() ? -1 : 0;
		
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				for (int y = yStart; y <= 1; y++) {
					Block block = current.getRelative(x, y, z);
					
					if (!houseBlockSearch(block)) return false;
					
					if (!tree.isValidLog(block) ||				//If it's not a valid log...
							logsToSearch.contains(block) ||		//...or is already set to search around...
							logs.contains(block) ||				//...or has already been searched around...
							current.equals(block))				//...or is the current one...
						continue;								//...then skip to the next loop.
					
					logsToSearch.push(block);
				}
		
		return true;
	}
	
	private boolean houseBlockSearch(Block log) {
		//Don't bother if tree protection is disabled
		if (!config.treeProtection.enabled.get())
			return true;
		
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				for (int y = -1; y <= 1; y++) {
					Block current = log.getRelative(x, y, z);
					
					//Check for house block
					if (!ignoreHouseBlocks && config.houseMaterials.contains(current)) {
						if (HouseIgnore.get().ignoreHouseBlocks(player))
							ignoreHouseBlocks = true;
						else {
							//Check whether to log to console
							if (config.treeProtection.message.console.get())
								Message.NOTIFY.info(player.getName(), formatLocation(current));
							
							//Check whether to send in chat
							if (config.treeProtection.message.chat.get())
								Message.NOTIFY.send(Permission.NOTIFY, player.getName(), formatLocation(current));
							
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean leafSearch() {
		int leafReach = getLeafReach();
		
		for (Block log: logs) { // For each log
			for (int x = -leafReach; x <= leafReach; x++) {
				for (int z = -leafReach; z <= leafReach; z++) {
					for (int y = 0; y <= leafReach; y++) {
						Block current = log.getRelative(x, y, z);
						
						if (!processCurrentLeaf(current)) return false;
					}
				}
			}
		}
		
		return true;
	}
	
	// TODO: Does not need to return a boolean
	private boolean processCurrentLeaf(Block current) {
		//Check for vines
		if (current.getType() == Material.VINE && vines.size() < config.maxVines.get())
			vines.add(current);
		
		//Check for leaves
		if (tree.isValidLeaf(current) &&		// If it's a valid leaf...
				!leaves.contains(current) &&	// ...and the leaf has not already been found...
				!groundInReach(current))		// ...and it's not within ground reach...
			leaves.add(current);				// ...then add it to the list.
		
		return true;
	}
	
	
	private boolean checkSize() {
		debugger.setStage("Log Size", logs.size());
		debugger.setStage("Leaf Size", leaves.size());
		
		if (logs.size() < tree.getLogMin()) return false;
		if (logs.size() > tree.getLogMax()) return false;
		
		return leaves.size() >= tree.getLeafMin();
	}
	
	private void checkReplant() {
		doReplant = tree.doReplant();
		
		if (player.getGameMode() == GameMode.CREATIVE && !config.creativeModeSettings.replant.get())
			doReplant = false;
		
		totalToReplant = baseBlocks.size();
		leftToTake = totalToReplant;
		
		debugger.setStage("Base Blocks", leftToTake);
	}
	
	/* ### DAMAGE ### */
	private boolean doDamage() {
		//If player is creative and shouldn't do damage, then return
		if (player.getGameMode() == GameMode.CREATIVE && !config.creativeModeSettings.doDamage.get()) return true;
		
		//Work out base damage
		int damageAmt;
		
		switch (tree.getDamageType()) {
			case NONE:
			default:
				damageAmt = 0;
				break;
			case NORM:
				damageAmt = (short) logs.size();
				break;
			case FIXED:
				damageAmt = (short) tree.getDamageAmount();
				break;
			case MULT:
				damageAmt = (short) (tree.getDamageAmount() * logs.size());
		}
		
		//Work out unbreaking
		damageAmt = calculateUnbreaking(damageAmt);
		
		//Check we can do this damage
		
		ItemStack item = player.getInventory().getItemInMainHand();
		short newDurability = (short) (item.getDurability() + damageAmt); //Figure out the new durability of the item
		
		if (newDurability > item.getType().getMaxDurability()) return false; //If the item cannot take this much damage, then return
		
		//Apply damage
		item.setDurability(newDurability);
		
		return true;
	}
	
	private int calculateUnbreaking(int damageAmt) {
		int unbreakingLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY);
		
		//If the item doesn't have unbreaking, or the damage amount is already nothing (or less?!), then don't do anything
		if (unbreakingLevel == 0 || damageAmt <= 0) return damageAmt;
		
		int newDamageAmt = 0;
		double chance = 0.5;
		if (unbreakingLevel == 2) chance = 0.6666;
		else if (unbreakingLevel == 3) chance = 0.75;
		
		//Since unbreaking is applied for EACH point of damage, I'll do the same.
		for (int point = 0; point < damageAmt; point++)
			if (rnd.nextDouble() >= chance)
				newDamageAmt ++;
		
		return newDamageAmt;
	}
	
	/* ### CHOP ### */
	private void dropToInventory() {
		ItemStack[] items = combineItems();
		HashMap<Integer, ItemStack> returned = player.getInventory().addItem(items);
		
		ItemStack[] returnedItems = returned.values().toArray(new ItemStack[0]);
		
		dropAt(dropLocation, returnedItems);
	}
	
	private void dropToGroup() {
		dropAt(dropLocation, combineItems());
	}
	
	private void dropToWorld() {
		processDrops().forEach(this::dropAt);
	}
	
	
	private ItemStack[] combineItems() {
		Collection<ItemStack> drops = processDrops().values();
		HashMap<Material, Integer> combinedDrops = new HashMap<>();
		
		//First get how many of each item we should have
		for (ItemStack drop: drops) {
			int qty = drop.getAmount();
			
			if (combinedDrops.containsKey(drop.getType()))
				qty += combinedDrops.get(drop.getType());
			
			combinedDrops.put(drop.getType(), qty);
		}
		
		//Add the logs, since everything here is being combined...
		combinedDrops.put(tree.getType().getLogType(), logs.size());
		
		//And then the vines if there are any.
		if (vines.size() > 0)
			combinedDrops.put(Material.VINE, vines.size());
		
		//Then create ItemStacks of this
		List<ItemStack> combinedList = new ArrayList<>(combinedDrops.size());
		
		for (Material material: combinedDrops.keySet())
			combinedList.add(new ItemStack(material, combinedDrops.get(material)));
		
		return combinedList.toArray(new ItemStack[0]);
	}
	
	private HashMap<Location, ItemStack> processDrops() {
		HashMap<Location, ItemStack> drops = new HashMap<>();
		
		//For each leaf...
		for (Block leaf: leaves) {
			//Get a random material to use.
			Material drop = getRandomDrop();
			if (drop == null) continue;
			
			//Check here for replants and take if needed
			if (player.getGameMode() != GameMode.CREATIVE && doReplant && leftToTake > 0 && tree.isValidSapling(drop)) {
				leftToTake--;
				continue;
			}
			
			drops.put(leaf.getLocation(), new ItemStack(drop, 1));
		}
		
		return drops;
	}
	
	// TODO: Take another look at this, it's a bit messy.
	private Material getRandomDrop() {
		Material chanceDrop = tree.getDrops().entrySet().stream()
				.filter(entry -> rnd.nextDouble() <= entry.getKey())
				.map(Map.Entry::getValue)
				.findFirst()
				.orElse(null);
		if (chanceDrop != null) return chanceDrop;
		// Choose a random drop from the list
		for (Material drop : tree.getDrops().values())
			return drop;
		return null;
	}
	
	private void dropAt(Location location, ItemStack... items) {
		for (ItemStack item: items)
			if (tree.isValidSapling(item)) {
				debugger.setStage("saplings", item.getAmount());
				debugger.setStage("bases", baseBlocks.size());
			}
			else
				location.getWorld().dropItemNaturally(location, item);
	}

	/* ### OTHER ### */
	
	private int getLeafReach() {
		int baseLeafReach = tree.getLeafReach();
		
		Tree.Type type = tree.getType();
		int size = logs.size();
		Biome biome = logs.get(0).getBiome();
		
		// TODO: Add more tree types here
		if (type == Tree.Type.OAK)
			if (biome == Biome.SWAMP || biome == Biome.MANGROVE_SWAMP)
				baseLeafReach += 1;		//Oak in swamp, increase by 1
			else if (size >= 15)
				baseLeafReach += 1;		//Large oak elsewhere, increase by 1
		
		if (type == Tree.Type.PINE && size >= 20)
			baseLeafReach += 1;			//Large pine, increase by 1
		
		if (type == Tree.Type.JUNGLE && size >= 20)
			baseLeafReach += 2;			//Large jungle, increase by 1
		
		return baseLeafReach;
	}
	
	private String formatLocation(Block block) {
		return  block.getWorld().getName() + ", " +
				block.getX() + ", " +
				block.getY() + ", " +
				block.getZ();
	}
	
	private boolean groundInReach(Block block) {
		// -1 because we are starting from one block down
		return groundInReach(block.getRelative(BlockFace.DOWN), tree.getLeafGroundOffset() - 1);
	}
	
	private boolean groundInReach(Block block, int distance) {
		if (distance == 0)
			return false;
		Block nextBlock = block.getRelative(BlockFace.DOWN);
		if (tree.isValidStandingBlock(nextBlock))
			return true;
		return groundInReach(nextBlock, distance - 1);
	}
	
	private void disappear(Block block) {
		Logging.logBreak(player, block);
		block.setType(Material.AIR);
	}
	
	private void breakBlock(Block block) {
		Logging.logBreak(player, block);
		block.breakNaturally();
	}
	
}
