import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;


/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Anthony Castleberry
 *
 */
public class AACMappings implements AACPage {

	AssociativeArray<String, AACCategory> map = new AssociativeArray<String, AACCategory>();

	AACCategory home = new AACCategory("");
 
	AACCategory current = home;

	String allLines = "";
	
	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) throws IOException{
			BufferedReader eyes = new BufferedReader(new FileReader(filename));
			String category = "";
			String line;

			while ((line = eyes.readLine()) != null) {
				if (line.length() < 2) {
				} else if (line.charAt(0) != '>') {
					String image = line.substring(0, line.indexOf(" "));
					String name = line.substring(line.indexOf(" ") + 1, line.length());
					home.addItem(image, name);
					category = image;
					try {
						map.set(image, new AACCategory(name));
					} catch (NullKeyException e) {
						throw new IOException("amp ain't workin");
					} // try/catch
				} else {
					String image = line.substring(1, line.indexOf(" "));
					String name = line.substring(line.indexOf(" ") + 1, line.length());
					try {
					map.get(category).addItem(image, name);
					} catch (Exception e) {
					} // try/catch
				} // if
			} // while

			eyes.close();
	}
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) {
		if (current.equals(home)) {
			 try {
				 current = map.get(imageLoc);
				return "";
		 	} catch (KeyNotFoundException e) {
		 		throw new NoSuchElementException();
			}
		} else {
			return current.select(imageLoc);
		} // if
	} // select(String)
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		if (!current.equals(home)) {
		return current.getImageLocs();
		} else {
			return map.getKeys();
		}
	}
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		current = home; 
	}
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
		try {
		FileWriter pen = new FileWriter(filename);
		String[] images = home.getImageLocs();
		int homeLength = images.length;

		for (int i = 0; i < homeLength; i++) {
			String categoryName = home.select(images[i]);
			pen.write(images[i] + " " + categoryName);
			for (int j = 0; j < map.get(categoryName).category.size(); i++) {
				String[] categoryImages = map.get(categoryName).getImageLocs();
				pen.write(">" + categoryImages[i] + " " + map.get(categoryName).select(categoryImages[i]));
			} // for
		} // for
		pen.close();
		} catch (Exception e) {
		} // try/catch
	} // writeToFile
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		if (current.equals(home)) {
			try {
			map.set(imageLoc, new AACCategory(text));
			} catch (Exception e) {
			}
		} else {
			current.addItem(imageLoc, text);
		}
	}


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
			return current.getCategory();
	}


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if (home.hasImage(imageLoc)) {
			return true;
		} else {
			String[] categories = home.getImageLocs();
			for (int i = 0; i < categories.length; i++) {
				try {
					if (!map.get(home.select(categories[i])).hasImage(imageLoc)) {
						return false;
					}
				} catch (Exception e) {
					return false;
				} // try/catch
			} // for
		} // if
		return true;
	} // hasImage(String)
} // class AACMapings
