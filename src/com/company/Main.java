package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static void insertPlant (Connection conn , String type , String subtype) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSER INTO plants VALUES (NULL , ? , ?");
        stmt.setString(1, type);
        stmt.setString(2, subtype);
        stmt.execute();
    }

    static void deletePlant ( Connection conn , int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM plants WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    static ArrayList<Plant> selectPlants (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM plants");
        ArrayList<Plant> plants = new ArrayList();
        while (results.next()){
            Plant plant = new Plant();
            plant.id = results.getInt("id");
            plant.type = results.getString("type");
            plant.subtype = results.getString("subtype");
            plants.add(plant);
        }
        return plants;
    }

    static void updatePlant (Connection conn, int selectNum , String type, String subtype) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE plants SET type = ? , subtype = ? WHERE id = ?");
        stmt.setString(1, type);
        stmt.setString(2, subtype);
        stmt.setInt(3, selectNum);
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt. execute("CREATE TABLE IF NOT EXISTS beers(id IDENTITY , name VARCHAR , type VARCHAR)");


        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    ArrayList<Plant> plants = selectPlants(conn);
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
                    //plant.id = plants.size() + 1;
                    plant.type = request.queryParams("planttype");
                    plant.subtype = request.queryParams("plantsubtype");
                    //String m = request.queryParams("maturity");
                    //int mat = Integer.valueOf(m);
                    //plant.maturity= request.queryParams("maturity");
                    insertPlant(conn , plant.type , plant.subtype);
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
                        deletePlant(conn , idNum);
                        //plants.remove(idNum - 1);
                        //for (int i = 0; i < plants.size(); i++) {
                            //plants.get(i).id = i + 1;
                    } catch (Exception e){
                    }
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/edit-plant",
                ((request, response) -> {
                    String id = request.queryParams("plantid");
                    String type = request.queryParams("planttype");
                    String subtype = request.queryParams("plantsubtype");
                    try {
                        int idNum = Integer.valueOf(id);
                        updatePlant(conn, idNum, type, subtype);
                    } catch (Exception e){

                    }
                    response.redirect("/");
                    return "";
                })
        );
    }
}
