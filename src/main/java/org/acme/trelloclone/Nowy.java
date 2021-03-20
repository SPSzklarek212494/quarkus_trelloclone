package org.acme.trelloclone;


import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Path("/hello-resteasy")
public class Nowy {

    @Inject
    MySQLPool client;

    @GET
    @Path("/nowy")
    @Produces("text/plain")
    public String nowy() { //podstawowa funkcja zwracająca napis - niepodłączona
        return "i pobrałeś";
    }


    @GET
    @Path("/nowy_sql_json")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<User1> nowy_sql_json() throws InterruptedException {

        Set<User1> wynik = Collections.newSetFromMap(
                Collections.synchronizedMap(new LinkedHashMap<>()));

        AtomicReference<Boolean> downloadingIsEnded = new AtomicReference<>(false);

            client.query("SELECT * FROM quarkus_server.users;").execute(
            (ar) -> {

                if (ar.succeeded()) {
                    RowSet<Row> result = ar.result();

                    if (result.size() > 0) {
                        for (Row i : result) {
                            System.out.println("id_user:  " + i.getValue("id_user")
                                    + "     username:  " + i.getValue("username")
                                    + "     password:  " + i.getValue("password"));

                            // JSON w wersji application/json
                            wynik.add(new User1(
                                    Long.parseLong(i.getValue("id_user").toString()),
                                    i.getValue("username").toString(),
                                    i.getValue("password").toString()
                            ));
                        }
                    }else{
                        System.out.println("-----PUSTO----");
                    }

                } else {
                    System.out.println("Failure: " + ar.cause().getMessage());
                }
                downloadingIsEnded.set(true);
            });

        while(downloadingIsEnded.get() == false){
            // własny i "tani" sposób na synchronizację w javie ( "async" i "await" )
            Thread.sleep(50);
        };

        return wynik;
    }


    @GET
    @Path("/nowy_sql_text")
    @Produces(MediaType.TEXT_PLAIN)
    public String nowy_sql_text() throws InterruptedException {

        AtomicReference<String> wynik = new AtomicReference<>("");

        AtomicReference<Boolean> downloadingIsEnded = new AtomicReference<>(false);

        client.query("SELECT * FROM quarkus_server.users;").execute(
                (ar) -> {

                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();

                        if (result.size() > 0) {
                            for (Row i : result) {
                                System.out.println("id_user:  " + i.getValue("id_user")
                                        + "     username:  " + i.getValue("username")
                                        + "     password:  " + i.getValue("password"));

                                // Json w postaci tekstowej
                                wynik.set(wynik.get() + "{ \"id_user\":" + i.getValue("id_user") + "," +
                                        "\"username\":\"" + i.getValue("username") + "\"," +
                                        "\"password\":\"" + i.getValue("password") + "\" },");
                            }
                        }else{
                            System.out.println("-----PUSTO----");
                        }

                    } else {
                        System.out.println("Failure: " + ar.cause().getMessage());
                    }
                    downloadingIsEnded.set(true);
                });

        while(downloadingIsEnded.get() == false){
            // własny i "tani" sposób na synchronizację w javie ( "async" i "await" )
            Thread.sleep(50);
        };

        return wynik.get().substring(0,wynik.get().length()-1);
    }
}
