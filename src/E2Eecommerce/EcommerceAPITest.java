package E2Eecommerce;
import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import POJO.LoginRequest;
import POJO.LoginResponse;
import POJO.OrderDetails;
import POJO.Orders;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class EcommerceAPITest {

	public static void main(String[] args) {

		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").setContentType(ContentType.JSON).build();
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("postman13111@gmail.com");
		loginRequest.setUserPassword("Rahulshetty@1");
		
		// SSL bypass certification 
		
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
		
		LoginResponse loginRespose = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response().as(LoginResponse.class);
		String token = loginRespose.getToken();
		String userID = loginRespose.getUserId();
		
		//AddProduct or Create product
		
		RequestSpecification addproduct = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("Authorization", token)
				.build();
		
		RequestSpecification addproductreq = given().log().all().spec(addproduct).param("productName", "qwerty")
		.param("productAddedBy", userID).param("productCategory", "fashion").param("productSubCategory", "shirts")
		.param("productPrice", "11500").param("productDescription", "Addias Originals").param("productFor", "women")
		.multiPart("productImage",new File("C:/Users/Rohit/Downloads/image.png"));
		
		String addproductres = addproductreq.when().post("/api/ecom/product/add-product").then()
				.log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(addproductres);
		
		String productId = js.get("productId");
		
		//Create order
		
		RequestSpecification createOrderreq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("Authorization", token).setContentType(ContentType.JSON)
				.build();
		OrderDetails orderDetail = new OrderDetails();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		
		List<OrderDetails> o = new ArrayList<OrderDetails>();
		o.add(orderDetail);
		
		Orders orders = new Orders();
		orders.setOrders(o);
		
		RequestSpecification createorderreq = given().spec(createOrderreq).body(orders);
		
		String resaddorder = createorderreq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
		
		System.out.println(resaddorder);
		
		//Delete product 
		
		RequestSpecification deleteprodbasereq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("Authorization", token).setContentType(ContentType.JSON)
				.build();
		
		RequestSpecification deleteprodreq = given().log().all().spec(deleteprodbasereq).pathParam("productId", productId);
		
		String deleteProductResponse = deleteprodreq.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all().extract().response().asString();
		
		JsonPath js1 = new JsonPath(deleteProductResponse);
		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
		
		
		
		
	}

}
