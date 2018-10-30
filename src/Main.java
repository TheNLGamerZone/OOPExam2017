import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static String fileName = "";
	
	/**
	 * Main method where all the fun stuff happens.
	 */
	public static void main(String... args)
	{
		Scanner scanner = new Scanner(System.in);
		
		// Getting file name
		System.out.print("File name: ");
		fileName = scanner.nextLine();
		
		// Create spotify instance
		Spotify spotify = new Spotify();
		
		// Initialize it
		spotify.init();
		
		// Main input loop
		main:
		while (true)
		{
			System.out.println("Please make your choice:");
			System.out.println("   1 - Show the current playlist");
			System.out.println("   2 - Add a new CD including songs");
			System.out.println("   3 - Play");
			System.out.println("   4 - Shuffle");
			System.out.println("   5 - Stop the program");
			
			String option = scanner.nextLine();
			
			switch (option)
			{			
			case "1":
				System.out.println(spotify.getPlayList().toString());
				break;
			case "2":
				if (!PlayList.shuffling.get())
				{
					spotify.addAlbum(addCD(scanner));
				} else
				{
					System.out.println("You cannot add albums while you're shuffling!");
				}
				break;
			case "3":
				spotify.play();
				break;
			case "4":
				spotify.shuffle();
				break;
			case "5":
				break main;
			default:
				System.out.println("That's not a valid option!\n");
				break;
			}
		}
			
		// Exit program
		spotify.exit();
	}
	
	/**
	 * Creates a new album using input from given scanner.
	 * @param scanner scanner to read input from
	 * @return A newly created album.
	 */
	private static Album addCD(Scanner scanner)
	{		
		// Creator
		System.out.print("Creator of album: ");
		String creator = scanner.nextLine();
			
		// Name
		System.out.print("Name of album: ");
		String name = scanner.nextLine();
			
		// Year of release
		System.out.print("Year of release of album: ");
		String year = scanner.nextLine();
		
		// Create tracks
		System.out.println("Create songs (type -1 to stop making songs and finish the album): ");
		List<AudioFile> songs = new ArrayList<>();
		int trackNumber = 1;
		
		// Loop where we create songs
		while (true)
		{
			Song song = addSong(scanner, trackNumber);
			
			// Check if the user wants to finish the album
			if (song == null)
			{
				break;
			}
			
			songs.add(song);
			trackNumber++;
		}
		
		return new Album(songs, creator, name, year);
	}
	
	/**
	 * Creates a new song using input from given scanner.
	 * @param scanner scanner to read input from
	 * @param trackNumber number of song in album
	 * @return A newly created song, or null if the user wants to stop creating songs.
	 */
	private static Song addSong(Scanner scanner, int trackNumber)
	{		
		System.out.print("Title of song: ");
		String name = scanner.nextLine();
		
		// Check if we need to stop making songs
		if (name.equals("-1"))
		{
			return null;
		}
		
		System.out.print("Length of song: ");
		String length = scanner.nextLine();
		
		return new Song(length, trackNumber, name);
	}
}
