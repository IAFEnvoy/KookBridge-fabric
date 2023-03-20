package net.iafenvoy.kookbridge;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.iafenvoy.kookbridge.kook.BridgePlugin;
import net.iafenvoy.kookbridge.utils.FileUtils;
import snw.jkook.JKook;
import snw.jkook.Core;
import snw.jkook.config.InvalidConfigurationException;
import snw.jkook.config.file.YamlConfiguration;
import snw.jkook.entity.channel.Channel;
import snw.jkook.entity.channel.TextChannel;
import snw.kookbc.impl.CoreImpl;
import snw.kookbc.impl.KBCClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static net.minecraft.server.command.CommandManager.*;

public class KookBridge implements DedicatedServerModInitializer {
    private KBCClient client = null;

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("kook").then(argument("message", StringArgumentType.greedyString()).executes((command) -> {
                String message = StringArgumentType.getString(command, "message");
                Channel channel = this.client.getStorage().getChannel("9774422914851157");
                if (channel instanceof TextChannel) {
                    TextChannel textChannel = (TextChannel) channel;
                    textChannel.sendComponent(message);
                }
                return 0;
            })));
        });
        try {
            CoreImpl core = new CoreImpl();
            JKook.setCore(core);

            String config = FileUtils.readByLines(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("kbc.yaml"))));
            YamlConfiguration yamlConfig = new YamlConfiguration();
            yamlConfig.load(config);
            this.client = new KBCClient((CoreImpl) JKook.getCore(), yamlConfig, null, "YOUR TOKEN HERE");
            client.getCore().getPluginManager().addPlugin(new BridgePlugin());
            this.client.start();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
