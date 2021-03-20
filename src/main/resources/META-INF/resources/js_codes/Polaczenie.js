
async function pobierz_json(){

    const res = await fetch(
        "http://localhost:8080/hello-resteasy/nowy_sql_json",
        {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }
    )
        .then(response => response.json());

    console.log(res);

    document.getElementById("pole_na_dane").innerHTML = "Sprawdź konsolę !!!";
}

async function pobierz_text(){

    const res = await fetch(
        "http://localhost:8080/hello-resteasy/nowy_sql_text",
        {
            method: 'GET',
            headers: {
                'Content-Type': 'text/plain'
            }
        }
    ).then(response => response.text());

    var r = (eval("[" + res.toString() + "]"));

    document.getElementById("pole_na_dane").innerHTML = r[0].username;
}