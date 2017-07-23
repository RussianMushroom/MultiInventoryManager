package io.github.russianmushroom.item;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.inventory.meta.BookMeta;

/**
 * Deal with books and their content.
 * They are to be saved int he same directory as player data under the name:
 * book_md5hash.yml
 * @author RussianMushroom
 *
 */
@SuppressWarnings("unused")
public class Book {

	private String bookTitle;
	private String bookAuthorName;
	private List<String> bookPages;
	
	public Book(String bookTitle, String bookAuthorName, List<String> bookPages) {
		this.bookTitle = bookTitle;
		this.bookAuthorName = bookAuthorName;
		this.bookPages = bookPages;
		
		//Check if 
	}
	
	public static String getHash(File file) throws 
		NoSuchAlgorithmException, 
		IOException{
		MessageDigest md = MessageDigest.getInstance("MD5");

		byte[] fileByte = Files.readAllBytes(file.toPath());
		md.update(fileByte);

		BigInteger hash = new BigInteger(1, md.digest());
		String result = hash.toString(16);

		while(result.length() < 32) {
			result = "0" + result;
		}

		return result;
	}
	
	/**
	 * Returns the md5_hash of the book file.
	 * @return
	 */
	public static String toMetaString() {
		return null;
	}
	
	/**
	 * Get the loaded data from the book file and apply its contents to BookMeta.
	 * Contents include: bookTitle, bookAuthorName, bookPages
	 * @param bookData
	 * @return
	 */
	public static Optional<BookMeta> getBookMeta(Map<String, Object> bookData) {
		BookMeta bMeta = null;
		if(!bookData.isEmpty()) {
			bMeta.setTitle(bookData.get("bookTitle").toString());
			bMeta.setAuthor(bookData.get("bookAuthorName").toString());
			
			Arrays.asList(bookData.get("bookPages").toString().split("\\~newpage\\~"))
				.stream()
				.forEach(page -> {
					bMeta.addPage(page);
				});
			
			return Optional.of(bMeta);
		}
		
		return Optional.empty();
	}
	
}
