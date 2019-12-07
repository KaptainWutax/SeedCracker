package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcracker.finder.FinderConfig;
import kaptainwutax.seedcracker.finder.FinderQueue;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class FinderCommand extends ClientCommand {

    @Override
    public String getName() {
        return "finder";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        for(FinderConfig.Type finderType: FinderConfig.Type.values()) {
            builder.then(literal("type")
                    .executes(context -> this.printFinderType(finderType))
                    .then(literal(finderType.toString())
                            .then(literal("ON").executes(context -> this.setFinderType(finderType, true)))
                            .then(literal("OFF").executes(context -> this.setFinderType(finderType, false))))
            );
        }

        for(FinderConfig.Category finderCategory: FinderConfig.Category.values()) {
            builder.then(literal("category")
                    .executes(context -> this.printFinderCategory(finderCategory))
                    .then(literal(finderCategory.toString())
                            .then(literal("ON").executes(context -> this.setFinderCategory(finderCategory, true)))
                            .then(literal("OFF").executes(context -> this.setFinderCategory(finderCategory, false))))
            );
        }
    }

    private int printFinderCategory(FinderConfig.Category finderCategory) {
        FinderConfig.Type.getForCategory(finderCategory).forEach(this::printFinderType);
        return 0;
    }

    private int printFinderType(FinderConfig.Type finderType) {
        this.sendFeedback("Finder " + finderType + " is set to [" + String.valueOf(FinderQueue.get().finderConfig.getTypeState(finderType)).toUpperCase() + "].", false);
        return 0;
    }

    private int setFinderCategory(FinderConfig.Category finderCategory, boolean flag) {
        FinderConfig.Type.getForCategory(finderCategory).forEach(finderType -> this.setFinderType(finderType, flag));
        return 0;
    }

    private int setFinderType(FinderConfig.Type finderType, boolean flag) {
        FinderQueue.get().finderConfig.setTypeState(finderType, flag);
        this.sendFeedback("Finder " + finderType + " has been set to [" + String.valueOf(flag).toUpperCase() + "].", false);
        return 0;
    }

}
