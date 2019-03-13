/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.almaviva.cms.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import it.almaviva.cms.services.secureData.PasswordEncrypter;

public class Util {

	public static <T> List<T> listOf(T value) {
		List<T> list = new ArrayList<>();
		list.add(value);
		return list;
	}

	public static boolean isFilled(Object value) {
		return (value != null);
	}

	public static boolean isFilled(String value) {
		return (value != null && !"".equals(value.trim()));
	}

	public static final boolean isFilled(Collection<?> value) {
		return (value != null && value.size() > 0);
	}

	public static final boolean isFilled(Object[] value) {
		return (value != null && !(value.length < 1));
	}

	public static List<String> readFile(InputStream in) {
		List<String> output = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = br.readLine()) != null) {
				output.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static int len(Object[] value) {
		if (value != null)
			return value.length;

		return 0;
	}

	public static int len(char[] value) {
		if (value != null)
			return value.length;

		return 0;
	}

	@SafeVarargs
	public static final <T> boolean in(T obj, T... values) {
		if (obj == null || !isFilled(values))
			return false;
		for (Object v : values)
			if (obj.equals(v))
				return true;

		return false;
	}

	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String encryptData(String key, String dataToEncript) {

		PasswordEncrypter encripter = new PasswordEncrypter(key);
		return encripter.encrypt(dataToEncript);

	}
	public static int generateRandomInt(int upperRange){
	    Random random = new Random();
	    return random.nextInt(upperRange);
	}

}
