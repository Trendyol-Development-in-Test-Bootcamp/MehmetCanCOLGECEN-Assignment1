import com.google.common.io.Resources;
import extensions.RetryAnalyzer;
import io.restassured.RestAssured;
import io.restassured.internal.http.HttpResponseException;
import org.json.JSONObject;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Timestamp;


import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;

public class ApiTests {
    private void createProduct() throws IOException{
        URL file = Resources.getResource("product.json");
        String myJson = Resources.toString(file, Charset.defaultCharset());
        JSONObject json = new JSONObject( myJson );
        given()
            .contentType("application/json; charset=UTF-8")
            .body(json.toString())
        .when()
            .post("/products")
        .then()
            .statusCode(200);
    }



    private void updateProduct(String id) throws  IOException{
        URL userFile = Resources.getResource("product.json");
        String productJson = Resources.toString(userFile, Charset.defaultCharset());
        JSONObject productObject = new JSONObject( productJson );
        String productId = id;

        productObject.put("title","sapka" );
        productObject.put("price", "15$");
        productObject.put("description", "Bu bir sapkadır");
                given() //update user
            .contentType("application/json; charset=UTF-8")
            .body(productObject.toString())
        .when()
            .put("/products/{productId}",productId)
        .then()
            .statusCode(200);
    }
    private void deleteProduct(String productId) throws  IOException{
        given() //delete user
            .contentType("application/json; charset=UTF-8")
        .when()
            .delete("/products/{productId}", productId)
        .then()
            .statusCode(200);
    }



    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void orderJourney() throws IOException {
        RestAssured.baseURI = "https://fakestoreapi.com";
        createProduct();
        updateProduct("1");
        deleteProduct("1");
    }
}
