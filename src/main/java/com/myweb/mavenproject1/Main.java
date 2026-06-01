package com.myweb.mavenproject1;

import com.myweb.mavenproject1.dao.DocumentoDAO;
import com.myweb.mavenproject1.dao.LoginUsuarioDAO;
import com.myweb.mavenproject1.entidades.Documento;
import com.myweb.mavenproject1.entidades.LoginUsuario;


import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
        System.out.println("MAIN MODIFICADO 31 MAYO");
        }).start(7000);
        
        
        // PRUEBA SERVIDOR
        app.get("/", ctx -> {
            ctx.result("Servidor DocuFlow funcionando");
        });

        // PRUEBA DOCUMENTOS
       app.get("/documentos", ctx -> {

        DocumentoDAO dao = new DocumentoDAO();

        ctx.json(dao.listar());

    });
       
       app.get("/documentos/{radicado}", ctx -> {

     System.out.println("RADICADO BUSCADO: "
            + ctx.pathParam("radicado"));
           
    DocumentoDAO dao = new DocumentoDAO();

    Documento documento =
            dao.buscarPorRadicado(
                    ctx.pathParam("radicado")
            );

    if (documento != null) {

        ctx.json(documento);

    } else {

        ctx.status(404);
        ctx.result("Documento no encontrado");
    }
   });
       
        // LOGIN
        app.post("/login", ctx -> {

        System.out.println("=================================");
        System.out.println("ENTRO AL ENDPOINT /login");
        System.out.println("BODY RECIBIDO: " + ctx.body());
        System.out.println("=================================");

    try {

        LoginRequest request =
                ctx.bodyAsClass(LoginRequest.class);

        System.out.println("Usuario recibido: " + request.getNombre());

        LoginUsuarioDAO dao =
                new LoginUsuarioDAO();

        LoginUsuario usuario =
                dao.validarLogin(
                        request.getNombre(),
                        request.getContrasena()
                );

        if (usuario != null) {

            System.out.println("LOGIN CORRECTO");

            ctx.result("LOGIN CORRECTO");

        } else {

            System.out.println("USUARIO O CONTRASEÑA INCORRECTOS");

            ctx.status(401);
            ctx.result("Usuario o contraseña incorrectos");
        }

    } catch (Exception e) {

        System.out.println("ERROR EN LOGIN");
        e.printStackTrace();

        ctx.status(500);
        ctx.result("ERROR INTERNO");
    }

});
        // RADICAR DOCUMENTO
        app.post("/documentos", ctx -> {
            
            
            System.out.println("================================");
            System.out.println("ENTRO A RADICACION");
            System.out.println(ctx.body());
            System.out.println("================================");

            try {

                Documento documento =
                        ctx.bodyAsClass(Documento.class);

                DocumentoDAO dao =
                        new DocumentoDAO();

                dao.guardar(documento);

                ctx.status(201);
                ctx.result("Documento radicado correctamente");
                
                
            } catch (Exception e) {

                e.printStackTrace();
                ctx.status(500);
                ctx.result("Error al guardar documento");
            }

        });

    }
}