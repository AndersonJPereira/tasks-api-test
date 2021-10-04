package tasks.apitest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;

public class TasksAPITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:9554/tasks-backend";
		RestAssured.basePath = "/todo";
		
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.setContentType(ContentType.JSON);
		requestSpecBuilder.log(LogDetail.ALL);
		RestAssured.requestSpecification = requestSpecBuilder.build();
		
		ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
		responseSpecBuilder.log(LogDetail.ALL);
		RestAssured.responseSpecification = responseSpecBuilder.build();
		
	}
	
	
	@Test
	public void deveInserirTasks() {
		LocalDate data = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
		RestAssured
			.given()
				.body("{\"task\": \"Estudar Devops\",\"dueDate\":\""+data.format(formatter)+"\"}")
			.when()
				.post()
			.then()
				.statusCode(201)
				.log().all()
				.body("task", Matchers.is("Estudar Devops"))
			;
	}
	
	
	@Test
	public void naoDeveInserirTaskDataPassada() {
		RestAssured
		.given()
			.body("{\"task\": \"Estudar Devops\",\"dueDate\": \"2021-09-21\"}")
		.when()
			.post()
		.then()
			.statusCode(400)
			.log().all()
			.body("error", Matchers.is("Bad Request"))
			.body("message", Matchers.is("Due date must not be in past"))
		;
		
	}
	
	@Ignore
	public void deveDeletarTasks() {
		RestAssured
			.given()
				.pathParam("id", "2")
			.when()
				.delete("/{id}")
			.then()
				.statusCode(200)
				.log().all();
			;
	}
	
	
	@Test
	public void deveConsultarTasks() {
		RestAssured
			.given()
			.when()
				.get()
			.then()
				.statusCode(200)
				.log().all();
			;
	}

}
