Sistema de Subastas RMI

Proyecto académico desarrollado en Java RMI que implementa un Sistema de Subastas distribuido con arquitectura cliente-servidor y persistencia en base de datos PostgreSQL.

Este repositorio está preparado para que cualquier persona pueda clonar, configurar y ejecutar el proyecto correctamente, entendiendo cómo funciona la comunicación remota (RMI) entre los componentes.

🧰 Tecnologías utilizadas
🔹 Lenguaje y entorno

Java OpenJDK 25.0.1 LTS (Temurin)

OpenJDK Runtime Environment Temurin-25.0.1+8

OpenJDK 64-Bit Server VM

🔹 IDE

Apache NetBeans 28

🔹 Base de datos

PostgreSQL 18.1 (última versión)

🔹 Control de versiones

Git

📥 Clonar el repositorio
git clone https://github.com/MarioCamayo/Sistema-Subastas-RMI-v1.git
cd SistemaSubastasRMI

Abrir el proyecto en NetBeans:

File → Open Project

Seleccionar la carpeta SistemaSubastasRMI

Abrir

📡 Arquitectura y funcionamiento Java RMI

Este sistema utiliza Java RMI (Remote Method Invocation) para permitir la comunicación entre clientes y un servidor remoto como si los métodos se invocaran localmente.

🧠 Idea general

El cliente invoca métodos remotos definidos en una interfaz

El servidor implementa esa interfaz y expone los métodos

La comunicación se realiza a través del RMI Registry

El servidor persiste la información en PostgreSQL

🏗️ Arquitectura general
Cliente RMI
   │
   ▼
Interfaz Remota (common)
   │
   ▼
Servidor RMI
   │
   ▼
Base de Datos PostgreSQL
🔌 Interfaces remotas (paquete common)

El paquete common contiene los contratos RMI compartidos entre cliente y servidor.

🔹 AuctionInterface

Interfaz remota principal del sistema

Define las operaciones de la subasta (crear, ofertar, consultar, etc.)

Extiende Remote

Todos los métodos lanzan RemoteException

🔹 ClientCallback

Interfaz remota para comunicación servidor → cliente

Permite notificar eventos como:

Nuevas pujas

Cambios de estado

Ganador de la subasta

🔹 Objetos compartidos

Product, Bid, WinnerInfo

Implementan Serializable

Se envían entre cliente y servidor

SubastaEstado (enum)

Representa los estados de la subasta

🖥️ Servidor RMI (paquete server)
🔹 AuctionServer

Clase principal del servidor

Inicia el RMI Registry

Publica el objeto remoto usando bind

🔹 AuctionServerImpl

Implementa AuctionInterface

Contiene la lógica real del sistema de subastas

Gestiona clientes conectados

Ejecuta callbacks hacia los clientes

Se comunica con la base de datos mediante DAO

🔹 DBConnection

Maneja la conexión a PostgreSQL

Lee las credenciales desde db.properties

💾 Capa de persistencia (paquete server.dao)

Implementa el patrón DAO (Data Access Object):

UserDAO

ProductDAO

BidDAO

Funciones:

Separar la lógica de negocio del acceso a datos

Ejecutar consultas SQL

Mantener una arquitectura limpia

👤 Cliente RMI (paquete client)
🔹 AuctionClient

Cliente principal del sistema

Obtiene el objeto remoto mediante Naming.lookup

Invoca métodos del servidor como si fueran locales

🔹 ClientCallbackImpl

Implementa ClientCallback

Permite que el servidor envíe notificaciones al cliente

🔹 UserStats

Manejo de información y estadísticas del usuario

🔄 Flujo de ejecución del sistema

Iniciar el RMI Registry

Ejecutar AuctionServer

El servidor publica el objeto remoto

El cliente ejecuta AuctionClient

El cliente obtiene el stub remoto

El cliente invoca métodos de subasta

El servidor procesa la lógica

La información se guarda en PostgreSQL

El servidor notifica a los clientes mediante callbacks

🗄️ Configuración de la base de datos
Crear la base de datos
CREATE DATABASE subastas_rmi;
Script SQL

El proyecto incluye el archivo:

database/subastas_rmi.sql

Este archivo contiene la creación de las tablas necesarias.

🔐 Configuración de conexión (db.properties)

Por seguridad, las credenciales NO se suben al repositorio.

Cada usuario debe crear el archivo:

db.properties

Con el contenido:

db.url=jdbc:postgresql://localhost:5432/subastas_rmi
db.user=postgres
db.password=TU_PASSWORD

Existe un archivo de ejemplo:

db.properties.example.properties
▶️ Ejecución del proyecto

Ejecutar primero:

AuctionServer

Luego ejecutar:

AuctionClient

📁 Estructura del proyecto
SistemaSubastasRMI/
├── src/
│   ├── client/
│   ├── common/
│   ├── server/
│   └── server/dao/
├── lib/
├── database/
│   └── subastas_rmi.sql
├── nbproject/
├── db.properties.example.properties
├── .gitignore
├── build.xml
└── README.md
👥 Trabajo en equipo

Cada integrante debe:

Clonar el repositorio

Crear su propio db.properties

Configurar PostgreSQL localmente

Ejecutar servidor y cliente

No es necesario usar npm install, ya que es un proyecto Java.

📌 Notas finales

El JDK no se versiona

Las credenciales nunca deben subirse a Git

El proyecto sigue buenas prácticas de sistemas distribuidos

📄 Licencia

Desarrollado por Mario Camayo.

Proyecto académico con fines educativos.
