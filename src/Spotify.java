import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Spotify {
	private List<Album> albums;
	private Album ads;
	private PlayList playlist;
	
	public Spotify()
	{
		this.albums = new ArrayList<>();		
	}
	
	/**
	 * Initialize the albums and play list.
	 */
	public void init()
	{
		BufferedReader reader = null;
		
		try 
		{
			// Creating reader
			reader = new BufferedReader(new FileReader(Main.fileName));
			
			String currentLine;
			List<String> matchingData = new ArrayList<>();
			
			// Read data file
			while ((currentLine = reader.readLine()) != null)
			{
				this.parseLine(currentLine, matchingData);
			}
			
			// Done with reading, so we just have to create a new 'ads album'
			this.ads = Album.read(matchingData.toArray(new String[matchingData.size()]), false);
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		} finally
		{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Merge albums with ads
		this.mergeAds();
	}
	
	/**
	 * Parse the given line.
	 * @param dataLine line to parse
	 * @param matchingData list to use
	 */
	private void parseLine(String dataLine, List<String> matchingData)
	{
		// Check if a new CD starts
		if (dataLine.startsWith("CD") &&
				!dataLine.equals("CDS"))
		{
			// Load previous cd
			this.loadCD(matchingData);
			
			// Clear old data
			matchingData.clear();
			
			// Add useful data
			matchingData.add(dataLine);
		}
		
		// Check if we are starting to load ads
		if (dataLine.startsWith("ADDS"))
		{
			// Load previous CD
			this.loadCD(matchingData);
			
			// Clear old data
			matchingData.clear();
		}
		
		
		// Check if this line contains data that we need to add to the array
		if (dataLine.startsWith("SONG") ||
				dataLine.startsWith("ADD"))
		{
			// Just add it to the current album
			matchingData.add(dataLine);
		}
	}
	
	/**
	 * Creates new album from given strings and add them to the album list.
	 * @param data data to create album from
	 */
	private void loadCD(List<String> data)
	{
		// Check if the data is useful
		if (!data.isEmpty())
		{
			// If so, add them to the list
			this.albums.add(Album.read(data.toArray(new String[data.size()]), true));
		}
	}
	
	/**
	 * Merges ads into normal albums and creates the complete play list.
	 */
	private void mergeAds()
	{
		Album mergedAlbum = null;
		
		// Looping through albums and merging them
		for (Album album : this.albums)
		{
			if (mergedAlbum == null)
			{
				mergedAlbum = album;
				continue;
			}
			
			// Create new album containing all songs
			mergedAlbum = mergedAlbum.merge(album, false);
		}

		// Merge ads
		mergedAlbum = mergedAlbum.merge(ads, true);
		
		// Create play list
		this.playlist = new PlayList(mergedAlbum.getAudioFiles(), this);
	}
	
	/**
	 * Plays the current song.
	 */
	public void play()
	{
		this.playlist.play();
	}
	
	/**
	 * Shuffles the play list.
	 */
	public void shuffle()
	{
		System.out.println("Shuffling playlist...");
		this.playlist.shuffle();		
	}
	
	/**
	 * Saves all albums and songs that have yet to be played.
	 */
	public void exit()
	{
		// Check which songs we have to save
		this.albums = this.getUnplayedAlbums();
		
		// Now we only have to save the files
		try {
			PrintWriter printWriter = new PrintWriter(new File(Main.fileName));
			
			// Save albums
			printWriter.append("CDS\n");
			
			// Loop through all albums and saves them
			for (Album album : this.albums)
			{
				System.out.println("Saving " + album.toString());
				printWriter.append(album.toString() + "\n");
				
				// Loops through all audio files of current album and save them
				for (AudioFile audioFile : album.getAudioFiles())
				{
					printWriter.append(audioFile.toSaveString() + "\n");
				}
			}
			
			
			// Save ads
			printWriter.append("ADDS\n");

			// Loop through all audio files of the ads album and save them
			for (AudioFile audioFile : this.ads.getAudioFiles())
			{
				printWriter.append(audioFile.toSaveString() + "\n");
			}
			
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a list containing all albums that have yet to be played.
	 * @return A list containing all albums that have yet to be played.
	 */
	private List<Album> getUnplayedAlbums()
	{
		List<Album> notPlayedAlbums = new ArrayList<>();
				
		// Loop through audio files that have yet to be played
		for (AudioFile audioFile : this.playlist.getAudioFiles())
		{
			Album album = this.getAlbum(audioFile.getAlbumString());
					
			if (album == null)
			{
				continue;
			}
			
			// If the album was not null, it contained songs that have yet to played
			if (!notPlayedAlbums.contains(album))
			{
				notPlayedAlbums.add(album);
			}
		}
		
		return notPlayedAlbums;
	}
	
	/**
	 * Adds a new album to the list and creates a new play list.
	 * @param album album to be added
	 */
	public void addAlbum(Album album)
	{
		this.albums.add(album);
		this.mergeAds();
	}
	
	/**
	 * Removes album from the list.
	 * @param album album to be removed
	 */
	public void removeAlbum(Album album)
	{
		this.albums.remove(album);
	}
	
	/**
	 * Returns album which corresponds to the given album string.
	 * @param albumString album string to check
	 * @return An album which corresponds to the given album string, or null if none was found.
	 */
	public Album getAlbum(String albumString)
	{
		String creator = albumString.split("'s\\s")[0];
		String albumName = albumString.split("'s\\s")[1];
		
		// Loop through all albums and check if the album string is of this album
		for (Album album : this.albums)
		{
			if (creator.equals(album.getCreator()) && albumName.equals(album.getAlbumName()))
			{
				return album;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the current play list.
	 * @return The current play list.
	 */
	public PlayList getPlayList()
	{
		return this.playlist;
	}
	
	/**
	 * Returns the album containing all ads.
	 * @return The album containing all ads.
	 */
	public Album getAds()
	{
		return this.ads;
	}
}
