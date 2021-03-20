package org.acme.trelloclone;

import io.vertx.mutiny.sqlclient.Row;

public class User1 {
    private long id_user;
    private String username;
    private String password;

    User1(long id_user, String username, String password){
        this.id_user = id_user;
        this.username = username;
        this.password = password;
    }
}
