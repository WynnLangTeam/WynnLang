package ru.artfect.wynnlang.hooklib.minecraft;

import net.minecraftforge.fml.common.Loader;
import ru.artfect.wynnlang.hooklib.asm.Hook;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class SecondaryTransformerHook {

    /**
     * Регистрирует хук-трансформер последним.
     */
    @Hook
    public static void injectData(Loader loader, Object... data) {
        ClassLoader classLoader = SecondaryTransformerHook.class.getClassLoader();
        if (classLoader instanceof LaunchClassLoader) {
            ((LaunchClassLoader)classLoader).registerTransformer(MinecraftClassTransformer.class.getName());
        } else {
            System.out.println("HookLib was not loaded by LaunchClassLoader. Hooks will not be injected.");
        }
    }

}
