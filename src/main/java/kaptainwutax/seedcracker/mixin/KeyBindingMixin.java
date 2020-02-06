package kaptainwutax.seedcracker.mixin;

import kaptainwutax.seedcracker.init.KeyBindings;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

	@Shadow @Final private static Map<String, Integer> categoryOrderMap;

	static {
		int start = categoryOrderMap.size();

		for(String category: KeyBindings.CATEGORY_REGISTRY) {
			categoryOrderMap.put(category, start++);
		}
	}

}
