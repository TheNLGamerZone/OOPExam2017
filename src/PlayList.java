import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayList {
	public static AtomicBoolean shuffling;
	private List<AudioFile> audioList;
	private Spotify spotify;
	
	public PlayList(List<AudioFile> audioList, Spotify spotify)
	{
		shuffling = new AtomicBoolean();
		this.audioList = audioList;
		this.spotify = spotify;
	}
	
	/**
	 * Plays the next song in the play list.
	 * Will not display properly after shuffle.
	 */
	public void play()
	{
		// Get current song
		AudioFile currentFile = this.audioList.get(0);
		Album album = this.spotify.getAlbum(currentFile.getAlbumString());
		
		// Print result
		System.out.println("Album: " + currentFile.getAlbumString());
		System.out.println("     Track: " + currentFile.toString());
		System.out.println("Next " + (this.audioList.get(1).hasAlbumString() ? "song" : "ad")+ ": " + 
				this.audioList.get(1).toString());
		
		
		// Get new list
		this.audioList = this.audioList.subList(2, this.audioList.size());
		
		// Remove song from album if it was not an ad
		if (album != null)
		{
			album.removeFile(currentFile);
		}
	}
	
	/**
	 * Returns a list containing all audio files in this play list.
	 * @return A list containing all audio files in this play list.
	 */
	public List<AudioFile> getAudioFiles()
	{
		return this.audioList;
	}
	
	/**
	 * Will shuffle this play list on another thread.
	 */
	public void shuffle()
	{
		// Check if we are already shuffling
		if (shuffling.compareAndSet(false, true))
		{
			// If not set flag and start shuffling
			Shuffler s = new Shuffler(this.audioList);
			
			s.start();
		}
	}
	
	@Override
	/**
	 * Returns a nicely formatted string representing this object.
	 */
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i < this.audioList.size(); i++)
		{
			// Current audio file is a song
			if (this.audioList.get(i).hasAlbumString())
			{
				AudioFile song = this.audioList.get(i);
				stringBuilder.append("Album: " + song.getAlbumString() + "\n");
				stringBuilder.append("     Track " + song.toString() + "\n");
			}
			
			// Current audio file is an ad
			else
			{
				AudioFile ad = this.audioList.get(i);
				stringBuilder.append("Next ad: " + ad.toString() + "\n");
			}
		}
		
		return stringBuilder.toString();
	}
}
