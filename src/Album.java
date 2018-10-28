import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album
{
	private List<AudioFile> audioFiles;
	
	private String creator;
	private String albumName;
	private String yearOfRelease;
	
	public Album(List<AudioFile> audioFiles)
	{
		this.audioFiles = audioFiles;
		
		// Saves me a few extra checks later on in the equals() method
		this.creator = "null";
		this.albumName = "null";
		this.yearOfRelease = "null";
	}
	
	public Album(List<AudioFile> audioFiles, String creator, String albumName, String yearOfRelease)
	{
		this.audioFiles = audioFiles;
		this.creator = creator;
		this.albumName = albumName;
		this.yearOfRelease = yearOfRelease;
	}
	
	/**
	 * Returns a list containing all audio files in this album.
	 * @return A list containing all audio files in this album.
	 */
	public List<AudioFile> getAudioFiles()
	{
		return this.audioFiles;
	}
	
	/**
	 * Returns the creator of this album.
	 * @return This album's creator.
	 */
	public String getCreator()
	{
		return this.creator;
	}
	
	/**
	 * Returns the name of this album.
	 * @return This album's name.
	 */
	public String getAlbumName()
	{
		return this.albumName;
	}
	
	/**
	 * Returns the year of release of this album.
	 * @return This album's year of release.
	 */
	public String getYearOfRelease()
	{
		return this.yearOfRelease;
	}
		
	/**
	 * Remove a given audio file from this album.
	 * @param file audio file to be removed
	 */
	public void removeFile(AudioFile file)
	{
		this.audioFiles.remove(file);
	}
	
	/**
	 * Extends the current album to a given length by adding audio files.
	 * @param extendTo the desired length
	 */
	public void extendTo(int extendTo)
	{
		int additions = extendTo - this.audioFiles.size();
		
		// Check if we have to extend
		if (additions <= 0)
		{
			return;
		}
		
		List<AudioFile> addedFiles = new ArrayList<>();
		
		for (int i = 0; i < additions; i++)
		{
			// Calculate index
			int index = i % this.audioFiles.size();
			
			addedFiles.add(this.audioFiles.get(index));
		}
		
		this.audioFiles.addAll(addedFiles);
	}
	
	/**
	 * Merge a given album into this album, in sequential order or by interweaving the songs.
	 * @param album the album that will be merged into this one
	 * @param interweave whether the merge should be done sequential or not
	 * @return A new album that is the result of mergin two albums.
	 */
	public Album merge(Album album, boolean interweave)
	{
		// Extend other album
		if (interweave)
		{
			album.extendTo(this.audioFiles.size());
		}
				
		return new Album(interweave ? this.mergeInterweaving(album) : this.mergeSequential(album), 
				this.creator, 
				this.albumName, 
				this.yearOfRelease);
	}
	
	/**
	 * Returns a list of merged audio files of this album and the given one. 
	 * The audio files will be interweaved.
	 * @param album the album that will be merged into this one
	 * @return A list of merged audio files of this album and the given one. 
	 */
	private List<AudioFile> mergeInterweaving(Album album)
	{
		List<AudioFile> mergedAudioFiles = new ArrayList<>();

		// Loop through all audio files
		for (int i = 0; i < this.audioFiles.size(); i++)
		{
			AudioFile firstAudioFile = this.audioFiles.get(i);
			AudioFile secondAudioFile = album.audioFiles.get(i);

			// Set album strings
			firstAudioFile.setAlbumString(this.creator + "'s " + this.albumName);
			secondAudioFile.setAlbumString(album.creator + "'s " + album.albumName);
			
			mergedAudioFiles.add(firstAudioFile);
			mergedAudioFiles.add(secondAudioFile);
		}
		
		return mergedAudioFiles;
	}
	
	/**
	 * Returns a list of merged audio files of this album and the given one. 
	 * The audio files will be sequential.
	 * @param album the album that will be merged into this one
	 * @return A list of merged audio files of this album and the given one. 
	 */
	private List<AudioFile> mergeSequential(Album album)
	{
		List<AudioFile> mergedAudioFiles = new ArrayList<>();

		// Loop through the audio files of this album
		for (AudioFile audioFile : this.audioFiles)
		{
			audioFile.setAlbumString(this.creator + "'s " + this.albumName);
			mergedAudioFiles.add(audioFile);
		}
		
		// Loop through the audio files of the given album
		for (AudioFile audioFile : album.audioFiles)
		{
			audioFile.setAlbumString(album.creator + "'s " + album.albumName);
			mergedAudioFiles.add(audioFile);
		}
		
		return mergedAudioFiles;
	}
		
	/**
	 * Creates a new Album instance based on the given strings.
	 * @param dataLine strings containing values for the Album instance
	 * @param containsSongs true if this album contains songs
	 * @return A newly created Album instance.
	 */
	public static Album read(String[] dataLines, boolean containsSongs)
	{
		String[] albumInfo = null;
		
		if (containsSongs)
		{
			// Get album info into array
			albumInfo = getAlbumInfo(dataLines);
		}
		
		// Create song list
		List<AudioFile> audioFiles = new ArrayList<>();
		
		// Loop over all strings
		for (int i = 1; i < dataLines.length; i++)
		{
			String dataLine = dataLines[i];
					
			// Create new audio file based on string
			AudioFile currentAudioFile = containsSongs ? Song.read(dataLine) : Ad.read(dataLine);
			
			audioFiles.add(currentAudioFile);
		}
		
		return containsSongs ? new Album(audioFiles, albumInfo[0], albumInfo[1], albumInfo[2]) : 
			new Album(audioFiles);
	}
	
	/**
	 * Splits and sanitizes the given string array to read the album information.
	 * @param dataLines string containing the album information
	 * @return A new string array containing all album information
	 */
	private static String[] getAlbumInfo(String[] dataLines)
	{
		// Get album info into array
		String[] albumInfo = dataLines[0].split(",\\s");
		
		// Load album info from array
		String creator = albumInfo[0].split("\\s")[1];
		String albumName = albumInfo[1];
		String yearOfRelease = albumInfo[2];
		
		return new String[] {creator, albumName, yearOfRelease};
	}
	
	@Override
	/**
	 * Checks whether the given object is equal to this object.
	 * @return boolean true if the objects are equal, false if not.
	 */
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof Album))
		{
			return false;
		}
		
		Album album = (Album) object;
		
	
		return album.getAudioFiles().equals(this.getAudioFiles()) &&
				album.getAlbumName().equals(this.getAlbumName()) &&
				album.getCreator().equals(this.getCreator()) &&
				album.getYearOfRelease().equals(this.getYearOfRelease());
	}
	
	@Override
	/**
	 * Returns this object's hash code.
	 */
	public int hashCode()
	{
		return Objects.hash(this.getAudioFiles(), this.getCreator(), this.getAlbumName(), this.getYearOfRelease());
	}
	
	@Override
	/**
	 * Returns the serialized object.
	 */
	public String toString()
	{
		return "CD " + this.creator + ", " + this.albumName + ", " + this.yearOfRelease;
	}
}
