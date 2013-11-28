package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileAsString {

	public static String convert(File txtfile) throws FileNotFoundException {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(txtfile), "UTF8"));
			String tmp;
			StringBuilder out = new StringBuilder();

			while ((tmp = in.readLine()) != null) {
				out.append(tmp);
				// include the line feed here, sheesh!!!!
				out.append("\n");
			}

			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
