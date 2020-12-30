package TestModels;

import java.util.Random;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import Utils.AbstractClass;

import com.relevantcodes.extentreports.LogStatus;

public class TestCapital extends AbstractClass{
	
	String[] capitalInfo = new String[2];
	
	@Test(priority=1)
	public void positiveScenarioTest() {
		test = extentReports.startTest("Test Positive Scenario ");
		getCapitalName();
		RestAssured.baseURI= "https://restcountries.eu/rest/v2/capital/"+capitalInfo[0]+"?fields=name;capital;currencies;latlng;regionalBlocs";
		RequestSpecification ourrRequest = RestAssured.given();
		// get one Capital info
		Response response = ourrRequest.request(Method.GET);
		// get response status code	
		int code = response.getStatusCode();
		System.out.println("Status code is: "+ code);
		// get response body
		ResponseBody body = response.getBody();
		
		// assert status code
		Assert.assertEquals(code, 200);
		
		// validate that  that currency code in the " Capital" API matches the currency code in the " Countries" API.
		JSONArray JSONResponseBody = new   JSONArray(response.body().asString());
		JSONArray CapitalAPICurrencyInfo=new   JSONArray(JSONResponseBody.getJSONObject(0).getJSONArray("currencies").toString());
		Assert.assertEquals(CapitalAPICurrencyInfo.getJSONObject(0).getString("code"), capitalInfo[1]);
		
		// assert response body schema (if there is a schema file in the project)
		//assertThat(response, matchesJsonSchemaInClasspath("CapitalSpecifications.json"));
		test.log(LogStatus.INFO, "Response : " + body.asString());
		extentReports.endTest(test);
	}
	
	@Test(priority=2)
	public void negativeScenarioTest() {
		test = extentReports.startTest("Test Negative Scenario ");
		getCapitalName();
		// to fulfill the negative scenario I call the API with parameter 'currency code' instead of parameter 'Capital name'
		RestAssured.baseURI= "https://restcountries.eu/rest/v2/capital/"+capitalInfo[1]+"?fields=name;capital;currencies;latlng;regionalBlocs";
		RequestSpecification ourrRequest = RestAssured.given();
		// get one Capital info
		Response response = ourrRequest.request(Method.GET);
		// get response status code	
		int code = response.getStatusCode();
		System.out.println("Status code is: "+ code);
		// get response body
		ResponseBody body = response.getBody();
		// assert status code
		Assert.assertEquals(code, 404);
		test.log(LogStatus.INFO, "Response : " + body.asString());
		extentReports.endTest(test);		
	}
	
	public void getCapitalName(){
		RestAssured.baseURI= "https://restcountries.eu/rest/v2/";
		RequestSpecification ourrRequest = RestAssured.given();
		Response response = ourrRequest.request(Method.GET,"all?fields=name;capital;currencies;latlng");
		// get response body
		ResponseBody body = response.getBody();
		JSONArray JSONResponseBody = new   JSONArray(response.body().asString());
		// Fill Capital name value by getting random country info
		Random r = new Random();
		int randomInt = r.nextInt(150) + 1;
		capitalInfo[0]=JSONResponseBody.getJSONObject(randomInt).getString("capital");
		JSONArray CurrencyInfo=new   JSONArray(JSONResponseBody.getJSONObject(randomInt).getJSONArray("currencies").toString());
		// Fill currency code value from Currencies array object
		capitalInfo[1]=CurrencyInfo.getJSONObject(0).getString("code");
		System.out.println("Capital name: "+capitalInfo[0]);
		System.out.println("Currency code: "+capitalInfo[1]);
		
	}
}
