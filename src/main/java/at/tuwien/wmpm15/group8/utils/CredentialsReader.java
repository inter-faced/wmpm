package at.tuwien.wmpm15.group8.utils;

import at.tuwien.wmpm15.group8.beans.MongoDbBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CredentialsReader {
	
	
	public static Properties read()
	{
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "credentials.properties";
			input = MongoDbBean.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
				System.out.println("Sorry, unable to find " + filename);
				return null;
			}

			prop.load(input);


			System.out.println("dbName: "+ prop.getProperty("mongodb.webdbName"));
			System.out.println("userName: "+prop.getProperty("mongodb.userName"));
			System.out.println("password: "+prop.getProperty("mongodb.password"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} 
		}
		return prop;
	}

}
