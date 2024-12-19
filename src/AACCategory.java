import java.lang.Thread.UncaughtExceptionHandler;
import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

import java.util.NoSuchElementException;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Anthony Castleberry
 *
 */
public class AACCategory implements AACPage {

	AssociativeArray<String, String> category;

	String categoryName;

	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.categoryName = name;
		category = new AssociativeArray<String, String>();
	} // AACCategory(String)
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
			category.set(imageLoc, text);
		} catch (NullKeyException e) {
			// try/catch is here to shut up warnings about NullKeyException again
			try {
				category.set(imageLoc, "");
			} catch (Exception ex) {
				// Should never arrive here, but VScode demands this exist.
			} // try/catch
		} // try/catch
	} // addItem

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
			return category.getKeys();
	} // getImageLocs

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.categoryName;
	} // getCategory

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) {
			try {
				return category.get(imageLoc);
			} catch (KeyNotFoundException e) {
				throw new NoSuchElementException();
			}
	} // select

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return category.hasKey(imageLoc);
	} // hasImage
} // class AACCategory
