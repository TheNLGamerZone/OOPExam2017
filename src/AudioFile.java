
public abstract class AudioFile implements Saveable 
{
	private String albumString;
	
	private String length;
	
	public AudioFile(String length)
	{
		this.length = length;
	}
	
	/**
	 * Returns length of audio file.
	 * @return Audio file's length.
	 */
	public String getLength()
	{
		return this.length;
	}
	
	/**
	 * Sets current album string, if none is present.
	 * @param string album string
	 */
	public void setAlbumString(String string)
	{
		// Check if we can safely write 
		if (!this.hasAlbumString())
		{
			this.albumString = string;
		}
	}
	
	/**
	 * Returns album string of this audio file.
	 * @return Audio file's album string.
	 */
	public String getAlbumString()
	{
		return this.albumString;
	}
	
	/**
	 * Check whether this audio file has an album string
	 * @return True if this audio file has an album string, false otherwise.
	 */
	public boolean hasAlbumString()
	{
		return this.albumString != null && !this.albumString.equals("null's null");
	}
		
	@Override
	/**
	 * Returns the serialized object.
	 */
	public abstract String toSaveString();
	
	@Override
	/**
	 * Returns a nicely formatted string representing this object.
	 */
	public abstract String toString();
	
	@Override
	/**
	 * Checks whether the given object is equal to this object.
	 * @return boolean true if the objects are equal, false if not.
	 */
	public abstract boolean equals(Object object);
	
	@Override
	/**
	 * Returns this object's hash code.
	 */
	public abstract int hashCode();
}
