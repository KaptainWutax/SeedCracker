package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.util.loot.LootBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.BiPredicate;

public class ChestLootData {

	private LootTable lootTable;
	private Map<Item, List<Stack>> stacksMap = new HashMap<>();

	public ChestLootData(LootTable lootTable, Stack... stacks) {
		this.lootTable = lootTable;

		for(Stack stack: stacks) {
			Item item = stack.item;

			if(!this.stacksMap.containsKey(item)) {
				this.stacksMap.put(item, new ArrayList<>());
			}

			this.stacksMap.get(item).add(stack);
		}
	}

	public boolean test(long seed) {
		Random random = new Random(seed);
		LootContext lootContext = new LootBuilder().setRandom(random).put(LootContextParameters.POSITION, new BlockPos(0, 0, 0)).build(LootContextTypes.CHEST);
		List<ItemStack> itemStacks = this.lootTable.getDrops(lootContext);

		Set<Item> foundItems = new HashSet<>();

		for(ItemStack itemStack: itemStacks) {
			Item item = itemStack.getItem();
			List<Stack> stacks = this.stacksMap.get(item);
			if(stacks == null)continue;

			boolean matches = true;

			for(Stack stack: stacks) {
				if(!stack.test(itemStack.getCount())) {
					matches = false;
					break;
				}
			}

			if(matches) {
				foundItems.add(item);
			}
		}

		return foundItems.size() == this.stacksMap.size();
	}

	public static class Stack {

		private Item item;
		private BiPredicate<Integer, Integer> predicate;
		private int amount;

		public Stack(Item item, BiPredicate<Integer, Integer> predicate, int amount) {
			this.item = item;
			this.predicate = predicate;
			this.amount = amount;
		}

		public boolean test(int count) {
			return this.predicate.test(count, this.amount);
		}

		@Override
		public int hashCode() {
			return item.hashCode() * 31 + this.amount;
		}

	}

}
