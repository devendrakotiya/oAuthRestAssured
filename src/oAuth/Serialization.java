package oAuth;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import POJO.AddPlace;
import POJO.Location;

public class Serialization {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";  

AddPlace p = new AddPlace();
		
		p.setAccuracy(50);
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		p.setWebsite("https://rahulshettyacademy.com");
		p.setLanguage("French-IN");
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		p.setLocation(l);
		
		List<String> Ltype = new ArrayList<String>();
		Ltype.add("shoe park");
		Ltype.add("shop");
		p.setTypes(Ltype);
		
		Response res = given().queryParam("key", "qaclick123")
		.body(p)
		.when().post("/maps/api/place/add/json").then()
		.assertThat().statusCode(200).extract().response();
		
		String responseString = res.asString();
		System.out.println(responseString);
	}

}
