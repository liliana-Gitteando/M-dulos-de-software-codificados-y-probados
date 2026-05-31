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
            ctx.result("Endpoint documentos activo");
        });

        // LOGIN
        app.post("/prueba123", ctx -> {

            try {

                LoginRequest request =
                        ctx.bodyAsClass(LoginRequest.class);

                LoginUsuarioDAO dao =
                        new LoginUsuarioDAO();

                LoginUsuario usuario =
                        dao.validarLogin(
                                request.getNombre(),
                                request.getContrasena()
                        );

                if (usuario != null) {

                    ctx.result("LOGIN CORRECTO");

                } else {

                    ctx.status(401);
                    ctx.result("Usuario o contraseña incorrectos");
                }

            } catch (Exception e) {

                e.printStackTrace();
                ctx.status(500);
                ctx.result("ERROR INTERNO");
            }

        });

        // RADICAR DOCUMENTO
        app.post("/documentos", ctx -> {

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