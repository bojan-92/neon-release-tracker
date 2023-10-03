package com.neon.controller;

import com.neon.controller.dto.ReleaseDTO;
import com.neon.entity.Release;
import com.neon.entity.Status;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;

public class ReleaseTrackerControllerTest extends Base {

    @Test
    public void get_release_by_name_request_should_return_200() {
        repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1efg")
                .name("ReleaseV1")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1ghd")
                .name("ReleaseV2")
                .description("This is the Release version 3")
                .status(Status.ON_DEV)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addParam("name", "ReleaseV1");

        RestAssured
                .given()
                .spec(builder.build())
                .when()
                .get(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .body("size()", is(2))
                .statusCode(200);
    }

    @Test
    public void get_release_by_name_description_status_request_should_return_200() {
        repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1efg")
                .name("ReleaseV1")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1ghd")
                .name("ReleaseV2")
                .description("This is the Release version 3")
                .status(Status.ON_DEV)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addParam("name", "ReleaseV1");
        builder.addParam("description", "This is the Release version 2");
        builder.addParam("status", Status.DONE.name());

        RestAssured
                .given()
                .spec(builder.build())
                .when()
                .get(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .body("size()", is(1))
                .statusCode(200);
    }

    @Test
    public void get_release_by_releaseDateFrom_releaseDateTo_request_should_return_200() {
        repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 8, 10))
                .build());

        repository.save(Release.builder()
                .id("1efg")
                .name("ReleaseV1")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 9, 15))
                .build());

        repository.save(Release.builder()
                .id("1ghd")
                .name("ReleaseV2")
                .description("This is the Release version 3")
                .status(Status.ON_DEV)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addParam("releaseDateFrom", "2023-08-30");
        builder.addParam("releaseDateTo", "2023-09-26");

        RestAssured
                .given()
                .spec(builder.build())
                .when()
                .get(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .body("size()", is(1))
                .statusCode(200);
    }

    @Test
    public void get_release_by_releaseDateFrom_releaseDateTo_request_should_return_empty_list_200() {
        repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 8, 10))
                .build());

        repository.save(Release.builder()
                .id("1efg")
                .name("ReleaseV1")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 9, 15))
                .build());

        repository.save(Release.builder()
                .id("1ghd")
                .name("ReleaseV2")
                .description("This is the Release version 3")
                .status(Status.ON_DEV)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addParam("releaseDateFrom", "2023-08-16");
        builder.addParam("releaseDateTo", "2023-09-14");

        RestAssured
                .given()
                .spec(builder.build())
                .when()
                .get(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .body("size()", is(0))
                .statusCode(200);
    }

    @Test
    public void get_release_by_id_request_should_return_200() {
        Release release = repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        RestAssured
                .given()
                .when()
                .get(BASIC_ENDPOINT + "/{id}", release.getId())
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody();
    }

    @Test
    public void get_release_by_id_request_should_return_404() {
        RestAssured
                .given()
                .when()
                .get(BASIC_ENDPOINT + "/{id}", "efg2")
                .prettyPeek()
                .then()
                .body("message", is("Release with id: efg2 not found"))
                .statusCode(404);
    }

    @Test
    public void delete_release_by_id_request_should_return_204() {
        Release release = repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.ON_PROD)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        RestAssured
                .given()
                .when()
                .delete(BASIC_ENDPOINT + "/{id}", release.getId())
                .prettyPeek()
                .then()
                .statusCode(204)
                .extract()
                .response()
                .getBody();
    }

    @Test
    public void delete_release_by_id_request_should_return_404() {
        RestAssured
                .given()
                .when()
                .delete(BASIC_ENDPOINT + "/{id}", "randId123")
                .prettyPeek()
                .then()
                .statusCode(404)
                .extract()
                .response()
                .getBody();
    }

    @Test
    public void create_release_request_should_return_201() {
        ReleaseDTO releaseDTO = ReleaseDTO.builder()
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status("CREATED")
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(releaseDTO))
                .when()
                .post(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .statusCode(201)
                .extract()
                .response()
                .getBody();
    }

    @Test
    public void create_release_request_wrong_status_value_should_return_400() {
        ReleaseDTO releaseDTO = ReleaseDTO.builder()
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status("CREATE")
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(releaseDTO))
                .when()
                .post(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .body("message", is("Please provide a valid status"))
                .statusCode(400);
    }

    @Test
    public void create_release_request_null_name_value_should_return_400() {
        ReleaseDTO releaseDTO = ReleaseDTO.builder()
                .description("This is the Release version 1")
                .status("CREATED")
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(releaseDTO))
                .when()
                .post(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .statusCode(400)
                .extract()
                .response()
                .getBody();
    }

    @Test
    public void create_release_request_null_description_value_should_return_400() {
        ReleaseDTO releaseDTO = ReleaseDTO.builder()
                .name("ReleaseV1")
                .status("CREATED")
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(releaseDTO))
                .when()
                .post(BASIC_ENDPOINT)
                .prettyPeek()
                .then()
                .statusCode(400)
                .extract()
                .response()
                .getBody();
    }

    @Test
    public void update_existing_release_request_should_return_200() {
        Release release = repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.ON_PROD)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        ReleaseDTO releaseDTO = ReleaseDTO.builder()
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status("DONE")
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(releaseDTO))
                .when()
                .put(BASIC_ENDPOINT + "/{id}", release.getId())
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody();
    }

    @Test
    public void update_non_existing_release_request_should_return_404() {
        ReleaseDTO releaseDTO = ReleaseDTO.builder()
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status("DONE")
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(releaseDTO))
                .when()
                .put(BASIC_ENDPOINT + "/{id}", "efg2")
                .prettyPeek()
                .then()
                .body("message", is("Release with id: efg2 not found"))
                .statusCode(404);
    }

    @Test
    public void update_existing_release_null_name_request_should_return_400() {
        Release release = repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.ON_PROD)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        ReleaseDTO releaseDTO = ReleaseDTO.builder()
                .description("This is the Release version 2")
                .status("DONE")
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(getRequestBody(releaseDTO))
                .when()
                .put(BASIC_ENDPOINT + "/{id}", release.getId())
                .prettyPeek()
                .then()
                .statusCode(400)
                .extract()
                .response()
                .getBody();
    }
}
