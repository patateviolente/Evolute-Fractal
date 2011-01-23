package fract.test;

import java.util.Locale;
import java.util.ResourceBundle;

public class TestTraduction {

	public static void main(String[] args) {
		Locale locale_US = new Locale("en", "");
		Locale locale_FR = new Locale("fr", "");
		
		Locale.setDefault(new Locale("frr", ""));

		System.out.println(Messages.getString("TestTraduction.0"));
	}

}
