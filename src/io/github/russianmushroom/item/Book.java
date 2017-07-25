package io.github.russianmushroom.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.meta.BookMeta;

import io.github.russianmushroom.files.LoadDefaults;

/**
 * Deal with books and their content.
 * They are to be saved int he same directory as player data under the name:
 * md5hash (no extension)
 * @author RussianMushroom
 *
 */
public class Book {

	private String bookTitle;
	private String bookAuthorName;
	private List<String> bookPages;
	
	
	public Book(String bookTitle, String bookAuthorName, List<String> bookPages) {
		this.bookTitle = bookTitle;
		this.bookAuthorName = bookAuthorName;
		this.bookPages = bookPages;
	}
	
	public String getHash(String bookContents) {
		MessageDigest md;
		String result = "";
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] bytes = bookContents.getBytes();
			
			md.update(bytes);

			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);

			while(result.length() < 32) {
				result = "0" + result;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * Returns the md5_hash of the book file.
	 * @return
	 */
	public String toMetaString() {
		return getHash(compressContents());
	}
	
	/**
	 * Compress all the book contents to one string
	 * @return
	 */
	private String compressContents() {
		StringBuilder sBuilder = new StringBuilder();
		
		sBuilder.append(bookTitle + "||##");
		sBuilder.append(bookAuthorName + "||##");
		
		bookPages.forEach(page -> {
			sBuilder.append(page + "##|");
		});
		
		return sBuilder.toString();
	}
	
	/**
	 * Create the file with the md5_hash part of the name
	 */
	public void createFile() {
		File file = new File(LoadDefaults.playerSaveBooks + File.separator + getHash(compressContents())); 
		
		try {
			FileWriter fWriter = new FileWriter(file);
			
			fWriter.write(compressContents());
			fWriter.flush();
			fWriter.close();
		} catch (IOException e) {
		}
	
	}
	
	public static String readFile(String md5Hash) {
		File file = new File(LoadDefaults.playerSaveBooks + File.separator + md5Hash);
		StringBuilder sBuilder = new StringBuilder();
		if(file.exists()) {
			try {
				BufferedReader bReader = new BufferedReader(new FileReader(file));
				String line = "";
				
				while((line = bReader.readLine()) != null) {
					sBuilder.append(line);
				}
				
				bReader.close();
			} catch (IOException e) {
			}
			return sBuilder.toString();
		}
		
		return "";
	}
	
	/**
	 * Get the loaded data from the book file and apply its contents to BookMeta.
	 * Contents include: bookTitle, bookAuthorName, bookPages
	 * @param bookData
	 * @return
	 */
	public static Optional<BookMeta> getBookMeta(BookMeta bMeta, String bookData) {
		if(!bookData.isEmpty()) {
			String[] data = bookData.split("\\|\\|##");
			bMeta.setTitle(data[0]);
			bMeta.setAuthor(data[1]);
			
			Arrays.asList(data[2].split("##\\|"))
				.forEach(page -> {
					bMeta.addPage(page);
				});
			
			return Optional.of(bMeta);
		}
		
		return Optional.empty();
	}
	
}
