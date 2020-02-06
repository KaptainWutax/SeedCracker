package kaptainwutax.seedcracker.init;

import kaptainwutax.seedcracker.util.Log;
import net.minecraft.client.options.KeyBinding;

import java.util.LinkedHashSet;

public class KeyBindings {

	public static LinkedHashSet<KeyBinding> KEY_REGISTRY = new LinkedHashSet<>();
	public static LinkedHashSet<String> CATEGORY_REGISTRY = new LinkedHashSet<>();

	public static KeyBinding OPEN_MENU = new KeyBinding(getName("open_menu"), 'M', getCategory("seedcracker"));

	static {
		registerKeyBinding(OPEN_MENU);
	}

	/*
	 * Registers keys and their categories. Remember that everything is FIFO.
	 */
	private static void registerKeyBinding(KeyBinding keyBinding) {
		if(keyBinding == null) {
			throw new NullPointerException("Cannot register a null key binding!");
		} else {
			KEY_REGISTRY.add(keyBinding);
			CATEGORY_REGISTRY.add(keyBinding.getCategory());
			Log.debug("Registering key [" + keyBinding.getName() + ", " + keyBinding.getId() + "] from category [" + keyBinding.getCategory() + "].");
		}
	}

	/*
	 * Generates the category and key prefixes for the language files.
	 */
	public static String getCategory(String registryName) {
		return "key.categories." + registryName;
	}

	public static String getName(String registryName) {
		return "key." + registryName.toLowerCase();
	}

}
