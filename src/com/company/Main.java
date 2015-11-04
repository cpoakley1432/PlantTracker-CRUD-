package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Plant> plants = new ArrayList();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        return new ModelAndView(new HashMap(), "not-logged-in.html");
                    }
                    HashMap m = new HashMap();
                    m.put("username", username);
                    m.put("plants", plants);
                    return new ModelAndView(m, "logged-in.html");
                }),
                new MustacheTemplateEngine()
        );
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

        Spark.post(
                "/create-plant",
                ((request, response) -> {
                    Plant plant = new Plant();
                    plant.id = plants.size() + 1;
                    plant.type = request.queryParams("planttype");
                    plant.subtype = request.queryParams("plantsubtype");
                    //String m = request.queryParams("maturity");
                    //int mat = Integer.valueOf(m);
                    //plant.maturity= request.queryParams("maturity");
                    plants.add(plant);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete-plant",
                ((request, response) -> {
                    String id = request.queryParams("plantid");
                    try {
                        int idNum = Integer.valueOf(id);
                        plants.remove(idNum - 1);
                        for (int i = 0; i < plants.size(); i++) {
                            plants.get(i).id = i + 1;
                        }
                    } catch (Exception e){
                    }
                    response.redirect("/");
                    return "";
                })
        );
    }
}
