import java.util.Collections;
import java.util.List;

public class Shuffler extends Thread
{
	public List<AudioFile> list;
	
	public Shuffler(List<AudioFile> list)
	{
		super();
		this.list = list;
	}
	
	@Override
	/**
	 * Runs shuffle code in another thread
	 */
	public void run()
	{
		Collections.shuffle(list);

		PlayList.shuffling = false;
		System.out.println("Shuffling done.");
	}
}
