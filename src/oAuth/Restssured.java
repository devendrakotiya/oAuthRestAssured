package oAuth;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import POJO.Api;
import POJO.GetCourse;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class Restssured {

	public static void main(String[] args) {
		
		// Generating access token
		String response = given()
		.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.formParam("grant_type", "client_credentials")
		.formParam("scope", "trust")
		.when().log().all()
		.post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();
		
		System.out.println(response);
		JsonPath js = new JsonPath(response);
		String accessToken = js.getString("access_token");
		
		// retriving courses using access token

		GetCourse resp = given()
				.queryParam("access_token", accessToken)
				.when().log().all()
				.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourse.class);
		
		System.out.println("LinkedIn URL = "+ resp.getLinkedIn());
		System.out.println("InstructoR = "+ resp.getInstructor());
		
		List<Api> ap = resp.getCourses().getApi();
		for(int i=0;i<ap.size();i++)
		{
			if(ap.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println(ap.get(i).getPrice());
			}
		}
		
		//Get the courses name of webautomation
		ArrayList<String> a = new ArrayList<String>();
		
		List<POJO.WebAutomation>w = resp.getCourses().getWebAutomation();
		
		for(int i=0;i<w.size();i++)
		{
			a.add(w.get(i).getCourseTitle());
		}
		
		// verifying the courses
		String[] courseTitle = {"Selenium Webdriver Java","Cypress","Protractor"};
		//now convert this string array into list because list to list we can compare we cannot compare list to Array
		List<String> expectedCorses = Arrays.asList(courseTitle);
		
		Assert.assertTrue(a.equals(expectedCorses));
		
		
	}

}
