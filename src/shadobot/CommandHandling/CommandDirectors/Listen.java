package shadobot.CommandHandling.CommandDirectors;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import shadobot.CommandHandling.CommandAssemblyComponents.Command;
import shadobot.CommandHandling.CommandAssemblyComponents.CommandData;
import shadobot.CommandHandling.CommandAssemblyComponents.CommandNetwork;
import shadobot.Shadobot;
import sx.blah.discord.handle.audio.impl.AudioManager;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


//this doesnt do anything yet

public class VoiceRecognition extends CommandNetwork {
    StreamSpeechRecognizer recognizer;

    @CommandData(
            aliases = {"listen"},
            description = "makes the bot listen"
    )
    public class Listen extends Command {

        public void execute(IVoiceChannel voiceChannel, IGuild guild, IChannel channel) throws RateLimitException,
                DiscordException,
                MissingPermissionsException {
            Configuration configuration = new Configuration();

            configuration
                    .setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration
                    .setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            configuration
                    .setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

            try {
                StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(
                        configuration);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream stream = new ByteArrayInputStream(new AudioManager(guild).getAudioProvider().provide());
            recognizer.startRecognition(stream);

            SpeechResult result;
            while ((result = recognizer.getResult()) != null) {
                Shadobot.UI.logAdd(result.getHypothesis());
            }

        }
    }

    @CommandData(
            aliases = {"stoplistening"},
            description = "makes the bot stop listening"
    )
    public class StopListen extends Command {

        public void execute() throws RateLimitException, DiscordException, MissingPermissionsException {
            recognizer.stopRecognition();
        }
    }
}