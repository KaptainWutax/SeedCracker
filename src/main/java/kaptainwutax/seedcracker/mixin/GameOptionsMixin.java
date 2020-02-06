package kaptainwutax.seedcracker.mixin;

import kaptainwutax.seedcracker.init.KeyBindings;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

	@Shadow public KeyBinding[] keysAll;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(CallbackInfo ci) {
		KeyBinding[] oldKeys = this.keysAll;
		KeyBinding[] newKeys = new KeyBinding[oldKeys.length + KeyBindings.KEY_REGISTRY.size()];
		Iterator<KeyBinding> keyBindingIterator = KeyBindings.KEY_REGISTRY.iterator();

 		for(int i = 0; i < newKeys.length; i++) {
			if(i < oldKeys.length) {
				newKeys[i] = oldKeys[i];
			} else {
				newKeys[i] = keyBindingIterator.next();
			}
		}

 		this.keysAll = newKeys;
	}

}
