package kaptainwutax.seedcracker.util.loot;

import kaptainwutax.seedutils.lcg.rand.JRand;
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

	public boolean test(long lootSeed, JRand rand) {
		rand.setSeed(lootSeed, true);

		LootContext lootContext = new LootBuilder().random(rand.toRandom())
				.parameter(LootContextParameters.POSITION, new BlockPos(0, 0, 0)).build(LootContextTypes.CHEST);

		List<ItemStack> itemStacks = this.lootTable.generateLoot(lootContext);
		Map<Item, Integer> lootItems = new HashMap<>();

		itemStacks.forEach(itemStack -> {
			lootItems.put(itemStack.getItem(), lootItems.getOrDefault(itemStack.getItem(), 0) + itemStack.getCount());
		});

		Set<Item> foundItems = new HashSet<>();

		for(Map.Entry<Item, Integer> lootItem: lootItems.entrySet()) {
			Item item = lootItem.getKey();
			List<Stack> stacks = this.stacksMap.get(item);
			if(stacks == null)continue;

			boolean matches = true;

			for(Stack stack: stacks) {
				if(!stack.test(lootItem.getValue())) {
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
