package edu.upc.dsa;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    private Map<String, Objeto> objetos;
    private Map<String, Usuario> usuarios;
    private Map<String, Usuario> usuariosm;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    public GameManagerImpl() {
        this.usuarios = new HashMap<>();
        this.usuariosm = new HashMap<>();
        this.objetos = new HashMap<>();
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManagerImpl();
        return instance;
    }

    public Usuario obtenerUsuario(String id) throws CredencialesIncorrectasException {
        Usuario u = null;
        if (usuarios.containsKey(id)) {
            u = usuarios.get(id);
        } else if (usuariosm.containsKey(id)) {
            u = usuariosm.get(id);
        }
        return u;
    }

    @Override
    public void addUsuario(String id, String name, String ape,String contra, String mail, String q, String a) throws UsuarioYaRegistradoException {
        logger.info("Registrando nuevo usuario: " + id + " / " + mail);

        if (usuarios.containsKey(id)) {
            throw new UsuarioYaRegistradoException("El USER ya está registrado");
        }

        if (usuariosm.containsKey(mail)) {
            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
        }

        Usuario nuevo = new Usuario(id, name, ape ,contra, mail,q,a);
        this.usuarios.put(id, nuevo);
        this.usuariosm.put(mail, nuevo);
        logger.info("Usuario registrado exitosamente");
    }

//    public void addUsuarioTest(String id, String name, String ape,String contra, String mail, String q, String a, int tarros, int flores, int mejor) throws UsuarioYaRegistradoException {
//        logger.info("Registrando nuevo usuario: " + id + " / " + mail);
//
//        if (usuarios.containsKey(id)) {
//            throw new UsuarioYaRegistradoException("El USER ya está registrado");
//        }
//
//        if (usuariosm.containsKey(mail)) {
//            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
//        }
//
//        Usuario nuevo = new Usuario(id, name, ape ,contra, mail,q,a, tarros, flores,mejor);
//        this.usuarios.put(id, nuevo);
//        this.usuariosm.put(mail, nuevo);
//        logger.info("Usuario registrado exitosamente");
//    }

    public void addUsuarioTest(String id, String name, String ape, String contra, String mail, String q, String a, int tarros, int flores, int mejor, int floreGold) throws UsuarioYaRegistradoException {
        logger.info("Registrando nuevo usuario: " + id + " / " + mail);

        if (usuarios.containsKey(id)) {
            throw new UsuarioYaRegistradoException("El USER ya está registrado");
        }

        if (usuariosm.containsKey(mail)) {
            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
        }

        Usuario nuevo = new Usuario(id, name, ape, contra, mail, q, a, tarros, flores, mejor);
        nuevo.setFloreGold(floreGold); // Inicializar floreGold
        this.usuarios.put(id, nuevo);
        this.usuariosm.put(mail, nuevo);
        logger.info("Usuario registrado exitosamente");
    }

    @Override
    public UsuarioEnviar login(String mailOId, String pswd) throws CredencialesIncorrectasException {
       logger.info("Iniciando login");
        Usuario u = obtenerUsuario(mailOId);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        if (!u.getPswd().equals(pswd)) {
            throw new CredencialesIncorrectasException("Contraseña incorrecta");
        }
        UsuarioEnviar usu = new UsuarioEnviar(u.getId(), u.getName(), u.getPswd(), u.getMail(), u.getPregunta(), u.getRespuesta(), u.getTarrosMiel(), u.getFlor(), u.getMejorPuntuacion(), u.getNumPartidas(), u.getFloreGold());
        return usu;
    }
    @Override
    public void addObjeto(Objeto objeto) {
        logger.info("Iniciando objeto");
        this.objetos.put(objeto.getNombre(), objeto);
    }

    @Override
    public Objeto findObjeto(String id) {
        return this.objetos.get(id);
    }

    @Override
    public DevolverCompra Comprar (Compra compra) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException { // va a ser un @PUT
        //Algo de que si idUsuari, mande una excepcion de que falta iniciar session, de quiero saber si
        // se ha registrado, mi idea esq en la web arriba a la derecha tengas un parametro con tu
        //id para almacenar la variable y poderla mandar en cada JSON
        Usuario u = this.usuarios.get(compra.getUsuarioId());
        if(u == null) {
            throw new UsuarioNoAutenticadoException("Usuario no autenticado o no encontrado. Debe iniciar sesión.");
        }
        Objeto o = objetos.get(compra.getObjeto());
        if(o.getPrecio() > u.getTarrosMiel()){
            throw new NoSuficientesTarrosException("No tienes suficiente Miel");
        }
            u.setTarrosMiel(u.getTarrosMiel() - o.getPrecio());
            if(o.getTipo() == 1){ //arma
                u.UpdateArmas(o);
            }
            else if(o.getTipo() == 2){ //skin
                u.UpdateSkin(o);
            }
            DevolverCompra y = new DevolverCompra(u.getTarrosMiel());
            System.out.println(y.getTarrosMiel());
            return y;
    }
//    @Override
//    public void initTestUsers() throws UsuarioYaRegistradoException {
//        try {
//            this.addUsuarioTest("carlos2004", "Carlos","Gonzalez", "123", "carlos@gmail.com","Tu comida favorita?","Arroz", 1000, 106, 250 );
//            this.addUsuarioTest("MSC78", "Marc", "Lopez","321", "marc@gmail.com","Como se llamaba tu escuela de Primaria?" ,"Dali", 1500, 120, 500 );
//            this.addUsuarioTest("Test", "Dani", "Buenosdias","147", "dani@gmail.com","El nombre de tu familiar mas mayor?" ,"Teresa",2500, 151, 190 );
//            this.addObjeto(new Objeto("1", "Palo",200 ,1, "Un paaaaaaaaaaaaaalo","palo1.png"));
//            this.addObjeto(new Objeto("2", "Hacha",700, 1,"Un hacha asequible para todos pero mortal como ninguna, su especialidad: las telarañas" ,"hacha1.png"));
//            this.addObjeto(new Objeto("3", "Gorro Pirata", 1000, 2, "Para surcar los mares","gorropirata.png"));
//            this.addObjeto(new Objeto("4", "Gorro Patito", 1000, 2, "Para nadar mucho","gorropatito.png"));
//            this.addObjeto(new Objeto("5", "Mister Potato", 1000, 2, "Para ser feliz","misterpotato.png"));
//            this.addObjeto(new Objeto("7", "Espada",1150 ,1, "Un corte profundo que hiere a las arañas más poderosas","espada1.png"));
//            this.addObjeto(new Objeto("8", "Espada Real",1350 ,1, "De su corte se entera hasta la mismisima Anansi","espada2.png"));
//        } catch (UsuarioYaRegistradoException e) {
//            logger.warn("Usuario de prueba ya estaba registrado");
//        }
//    }

    @Override
    public void initTestUsers() throws UsuarioYaRegistradoException {
        try {
            // Crear usuarios de prueba
            this.addUsuarioTest("carlos2004", "Carlos", "Gonzalez", "123", "carlos@gmail.com", "Tu comida favorita?", "Arroz", 1000, 106, 250, 5);
            this.addUsuarioTest("MSC78", "Marc", "Lopez", "321", "marc@gmail.com", "Como se llamaba tu escuela de Primaria?", "Dali", 1500, 120, 500, 10);
            this.addUsuarioTest("Test", "Dani", "Buenosdias", "147", "dani@gmail.com", "El nombre de tu familiar mas mayor?", "Teresa", 2500, 151, 190, 15);

            // Crear objetos de prueba
            this.addObjeto(new Objeto("1", "Palo", 200, 1, "Un paaaaaaaaaaaaaalo", "palo1.png"));
            this.addObjeto(new Objeto("2", "Hacha", 700, 1, "Un hacha asequible para todos pero mortal como ninguna, su especialidad: las telarañas", "hacha1.png"));
            this.addObjeto(new Objeto("3", "Gorro Pirata", 1000, 2, "Para surcar los mares", "gorropirata.png"));
            this.addObjeto(new Objeto("4", "Gorro Patito", 1000, 2, "Para nadar mucho", "gorropatito.png"));
            this.addObjeto(new Objeto("5", "Mister Potato", 1000, 2, "Para ser feliz", "misterpotato.png"));
            this.addObjeto(new Objeto("7", "Espada", 1150, 1, "Un corte profundo que hiere a las arañas más poderosas", "espada1.png"));
            this.addObjeto(new Objeto("8", "Espada Real", 1350, 1, "De su corte se entera hasta la mismisima Anansi", "espada2.png"));

//            // Asignar objetos a usuarios (solo el caso de ejemplo para 'Test')
//            Usuario usuarioTest = this.usuarios.get("Test");
//            Objeto espada = this.objetos.get("7");
//            if (usuarioTest != null && espada != null) {
//                usuarioTest.UpdateArmas(espada);
//            }
//
//            System.out.println(objetos.get(1).getNombre());
//            System.out.println(objetos.get(7).getNombre());
//
//            // Ahora, asegúrate de que el arma está guardada en el HashMap de armas del usuario 'Test'
//            if (usuarioTest != null && !usuarioTest.getArmas().isEmpty()) {
//                // Obtener el primer objeto del HashMap de armas
//                Objeto objetoGuardado = usuarioTest.getArmas().values().iterator().next();
//                logger.info("El usuario 'Test' tiene guardado el objeto (desde el HashMap): " + objetoGuardado.getNombre());
//            } else {
//                logger.info("El usuario 'Test' no tiene armas guardadas o no existe.");
//            }
//
//            logger.info("Usuarios de prueba inicializados.");
        } catch (UsuarioYaRegistradoException e) {
            logger.warn("Usuario de prueba ya estaba registrado");
        }
    }


    @Override
    public ConsultaTienda findArmas() {

        HashMap<String, Objeto> armas = new HashMap<>();
        for (Objeto obj : this.objetos.values()) {
            if (obj.getTipo() == 1) { // tipo 1 = arma
                armas.put(obj.getId(), obj);
            }
        }
        List<Objeto> listaItems = new ArrayList<Objeto>(armas.values());
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }
    @Override
    public ConsultaTienda findSkins() {

        HashMap<String, Objeto> skins = new HashMap<>();
        for (Objeto obj : this.objetos.values()) {
            if (obj.getTipo() == 2) { // tipo 2 = arma
                skins.put(obj.getId(), obj);
            }
        }
        List<Objeto> listaItems = new ArrayList<Objeto>(skins.values());
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException {

        logger.info("Obteniendo pregunta");
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        logger.info(u.getPregunta());
        return u.getPregunta();
    }

    @Override
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException {
        Usuario u = obtenerUsuario(id);

        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        if (!u.getRespuesta().equals(respuesta)) {
            throw new CredencialesIncorrectasException("Respuesta incorrecta");
        }
        return u;


    }

    @Override
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        u.setRespuesta(contra);

    }

    @Override
    public ConsultaTienda skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        List<Objeto> listaItems = new ArrayList<>(u.getSkins().values());

        if (listaItems.isEmpty()) {
            throw new NoHayObjetos("No tienes skins");
        }

        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public ConsultaTienda armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        List<Objeto> listaItems = new ArrayList<>(u.getArmas().values());

        if (listaItems.isEmpty()) {
            throw new NoHayObjetos("No tienes armas");
        }
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public void deleteUsuario(String id) throws UsuarioNoEncontradoException {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }
        String email = usuario.getMail();
        usuarios.remove(id);
        usuariosm.remove(email);
        logger.info("Usuario eliminado: " + id);
    }

    @Override
    public Intercambio intercambio (String usuario) throws CredencialesIncorrectasException, NoHayFlores {
        Usuario u = obtenerUsuario(usuario);
        System.out.println(u.getTarrosMiel() + "+" + u.getFlor()+ "+" + u.getFloreGold());
        int Tarros = 0;
        int FloresSobrantes = u.getFlor();
        if (u == null) {
            throw new CredencialesIncorrectasException("No esta registrado");
        }
        if((u.getFlor() < 30) && (u.getFloreGold() == 0)) {
            throw new NoHayFlores("No hay nada a convertir en Tarros, juega mas para conseguir mas!!");
        }
        while(FloresSobrantes >= 30){ // Va ahciendo restas de 30 n 30 i ba sumando Tarros pq cada 30 flores normales
            Tarros++;                // equivalen a 1 solo Tarro de Miel
            FloresSobrantes = FloresSobrantes - 30;
        }
        Tarros = Tarros + (u.getFloreGold()*50); // 1 Dorada = 50 Tarros
        u.setFlor(FloresSobrantes);
        u.setFloreGold(0);
        u.setTarrosMiel(u.getTarrosMiel() + Tarros);
        Intercambio i = new Intercambio(u.getTarrosMiel(), FloresSobrantes);
        System.out.println(u.getTarrosMiel() + "+" + u.getFlor()+ "+" + u.getFloreGold());
        return i;
    }
    @Override
    public List<Info> informcion() {
        List<Info> info = new ArrayList<>();

        for (Usuario u : usuarios.values()) {
            info.add(new Info(u.getId(), u.getMejorPuntuacion(), u.getNumPartidas()));
        }

        // Ordenar la lista por mejor puntuación de mayor a menor
        info.sort((i1, i2) -> Integer.compare(i2.getMejorPuntuacion(), i1.getMejorPuntuacion()));

        // Devolver solo los 5 primeros (o menos si hay menos usuarios)
        return info.subList(0, Math.min(5, info.size()));
    }
}
