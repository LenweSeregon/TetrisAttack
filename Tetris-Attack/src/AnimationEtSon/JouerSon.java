package AnimationEtSon;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class JouerSon {

	private final int BUFFER_SIZE = 128000;
	private URL fichierSon;
	private AudioInputStream streamAudio;
	private AudioFormat formatAudio;
	private SourceDataLine sourceLine;

	private int nBytesRead;

	private boolean stop = false;
	private boolean boucle = true;
	
	private static boolean sonActif = true;

	/**
	 * @param filename
	 *            the name of the file that is going to be played
	 */
	public JouerSon(String fichier, boolean boucle) {

		// System.out.println("son1");
		try {
			fichierSon = this.getClass().getResource(fichier);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("son2");

		try {
			streamAudio = AudioSystem.getAudioInputStream(fichierSon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("son3");

		this.boucle = boucle;
		// System.out.println("son4");

		formatAudio = streamAudio.getFormat();
		// System.out.println("son5");

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				formatAudio);
		// System.out.println("son5.0");
		try {
			// System.out.println("son5.1");
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			// System.out.println("son5.2");
			sourceLine.open(formatAudio);
			// System.out.println("son5.3");
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("son6");
		sourceLine.start();
	}

	public static void couperSon(){
		sonActif = false;
	}
	
	public static void activerSon(){
		sonActif = true;
	}
	
	public void stopper() {
		this.stop = true;
	}

	public void play() {
		byte[] abData = new byte[BUFFER_SIZE];
		while (!stop){
			while (nBytesRead != -1 && sonActif && !stop) {
				try {
					nBytesRead = streamAudio.read(abData, 0, abData.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (nBytesRead >= 0) {
					@SuppressWarnings("unused")
					int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
				} else if (this.boucle) {
					try {
						streamAudio = AudioSystem.getAudioInputStream(fichierSon);
						nBytesRead = 0;
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		sourceLine.drain();
		sourceLine.close();
	}

}