package com.company;

import spark.Session;
import spark.Spark;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Plant> plants = new ArrayList();

        Spark.post(
                "/login",
                ((request, response) -> {
                    String username = request.queryParams("username");
                    Session session = request.session();
                    session.attribute("username", username);
                    response.redirect("/");
                    return "";
                })
        );

    }
}
