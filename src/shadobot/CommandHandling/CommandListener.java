package shadobot.CommandHandling;

import shadobot.CommandHandling.CommandAssemblyComponents.Command;
import shadobot.CommandHandling.CommandAssemblyComponents.CommandBuilder;
import shadobot.CommandHandling.CommandAssemblyComponents.CommandData;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.HashMap;

public class CommandListener implements IListener<MessageReceivedEvent> {
    private String prefix;
    private HashMap<String,Command> registeredCommands = new HashMap<String, Command>();

    public CommandListener(String prefix) {
        this.prefix = prefix;
    }

    public void handle(MessageReceivedEvent event) {
        final IMessage message = event.getMessage();

        final String[] splitMessage = message.getContent().split(" ");
        final Command command = registeredCommands.get(splitMessage[0]);

        if (command!=null) if (command.check(message)) {
            System.out.println("executed command "+command.getClass().getName());
            command.execute(message, (splitMessage.length > 1) ? splitMessage[1] : "");
        }
    }

    public void register(CommandBuilder command){
        CommandData annotation = command.getClass().getAnnotation(CommandData.class);

        if (annotation != null) {
            for (String alias : annotation.aliases()) {
                registeredCommands.put(
                        (annotation.requiresPrefix() ? prefix:"")+alias.toLowerCase(),
                        command.buildCommand()
                );

                System.out.println("registered "+command.getClass().getName()+" as "+alias);
            }
        }else{
            System.out.println(command.getClass().getName()+" has no annotation");
        }
    }
}
