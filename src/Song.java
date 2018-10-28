import java.util.Objects;

public class Song extends AudioFile
{
	private int trackNumber;
	private String title;
	
	public Song(String length, int trackNumber, String title) 
	{
		super(length);
		
		this.trackNumber = trackNumber;
		this.title = title;
	}
	
	/**
	 * Creates a new Song instance based on the given string.
	 * @param dataLine string containing values for the Song instance
	 * @return A newly created Song instance.
	 */
	public static Song read(String dataLine)
	{
		// Split data into array
		String[] data = dataLine.split(",\\s");
		
		// Get data from array
		String title = data[1];
		String length = data[2];
		
		// This one is a bit tricky, since this will throw an exception should the string not be an int
		// Since we were told this would never be the case I will neglect checking/catching
		int trackNumber = Integer.valueOf(data[0].split("\\s")[1]);
		
		// Create and return instance
		return new Song(length, trackNumber, title);
	}
	
	/**
	 * Returns this song's track number.
	 * @return Track number of this song.
	 */
	public int getTrackNumber()
	{
		return this.trackNumber;
	}
	
	/**
	 * Returns this song's title.
	 * @return Title of this song.
	 */
	public String getTitle()
	{
		return this.title;
	}
	
	@Override
	/**
	 * Returns the serialized object.
	 */
	public String toSaveString()
	{
		return "SONG " + this.trackNumber + ", " + this.title + ", " + this.getLength();
	}
	
	@Override
	/**
	 * Returns a nicely formatted string representing this object.
	 */
	public String toString()
	{
		
		return this.title + " (" + this.getLength() + ")";
	}
	
	@Override
	/**
	 * Checks whether the given object is equal to this object.
	 * @return boolean True if the objects are equal, false if not.
	 */
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof Song))
		{
			return false;
		}
		
		Song song = (Song) object;
		
		return song.getLength().equals(this.getLength()) &&
				song.getTitle().equals(this.getTitle()) &&
				song.getTrackNumber() == this.getTrackNumber();
	}
	
	@Override
	/**
	 * Returns this object's hash code.
	 */
	public int hashCode()
	{
		return Objects.hash(this.title, this.trackNumber, this.getLength());
	}
}
