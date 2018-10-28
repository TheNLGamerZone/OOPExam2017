import java.util.Arrays;
import java.util.Objects;

public class Ad extends AudioFile
{
	private String advertiser;
	
	public Ad(String length, String advertiser)
	{
		super(length);
		
		this.advertiser = advertiser;
	}
	
	/**
	 * Creates a new Ad instance based on the given string.
	 * @param dataLine string containing values for the Ad instance
	 * @return A newly created Ad instance.
	 */
	public static Ad read(String dataLine)
	{
		// Split data into array
		String[] data = dataLine.split(",\\s");
		
		// Get data from array
		String length = data[1];
		String[] fullAdvertiser = data[0].split("\\s");
		String advertiser = String.join(" ", Arrays.copyOfRange(fullAdvertiser, 1, fullAdvertiser.length));
		
		// Create and return instance 
		return new Ad(length, advertiser);
	}
	
	/**
	 * Returns this ad's advertiser.
	 * @return this ad's advertiser.
	 */
	public String getAdvertiser()
	{
		return this.advertiser;
	}
	
	@Override
	/**
	 * Returns the serialized object.
	 */
	public String toSaveString()
	{
		return "ADD " + this.advertiser + ", " + this.getLength();
	}
	
	@Override
	/**
	 * Returns a nicely formatted string representing this object.
	 */
	public String toString()
	{
		return this.advertiser + " (" + this.getLength() + ")";
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
		
		if (!(object instanceof Ad))
		{
			return false;
		}
		
		Ad ad = (Ad) object;
		
		return ad.getLength().equals(this.getLength()) &&
				ad.getAdvertiser().equals(this.getAdvertiser());
	}
	
	@Override
	/**
	 * Returns this object's hash code.
	 */
	public int hashCode()
	{
		return Objects.hash(this.advertiser, this.getLength());
	}
}
