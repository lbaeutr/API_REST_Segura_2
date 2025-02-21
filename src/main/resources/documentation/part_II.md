# Documentación: Gestión de Usuarios

## 1. Introducción

Este documento presenta la implementación del sistema de gestión de usuarios en la aplicación, abarcando el registro, inicio de sesión y autorización mediante tokens JWT.

Se describen los endpoints de la API, su integración con MongoDB y las pruebas realizadas para garantizar su correcto funcionamiento.

---

## 2. Configuración de la Base de Datos (MongoDB)

Para conectar la aplicación con MongoDB, se configuraron las dependencias necesarias, entre ellas:

```kts

dependencies {
    //Soporte de Spring Data para MongoDB, que facilita la interacción con la base de datos y otras herramientas.
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    //Driver de MongoDB para Kotlin, que permite interactuar con la base de datos de forma síncrona.
    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.3.0")
    // Otras dependencias...
}
```

Se creó un archivo de configuración `application.properties` para establecer la conexión con la base de datos

Además, se añadio un archivo `env.properties` en el directorio `src/main/resources` con la siguiente configuración:

```properties
# uri de la base de datos en el que se indica el usuario y la contraseña
spring.data.mongodb.uri=mongodb+srv://....
# Clave de la api de geolocalización
API_KEY=<API_KEY de la api de geolocalización>
```

```properties
# Configuración de la base de datos MongoDB
# Carga la clave privada y la clave pública en el contexto
rsa.private-key=classpath:certs/private.pem
rsa.public-key=classpath:certs/public.pem
# LA URL DE CONEXIÓN A MONGODB
spring.config.import=env.properties
spring.data.mongodb.uri=${MONGO_URI}
spring.data.mongodb.auto-index-creation=true

```

---

## 3. Endpoints Implementados

### 3.1 Registro de Usuarios

- **Endpoint:** `POST /usuarios/register`
- **Descripción:** Permite registrar un nuevo usuario.
- **Código de respuesta esperado:** 

  - `201 Created` : Registro exitoso. 
  - `400 Bad Request` : Uno o más campos vacíos.
  - `409 Conflict` : Conflicto con la BBDD.

- **Ejemplo de petición:**

```json
{
  "username": "prueba",
  "email": "prueba@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```

- **Ejemplo de respuesta exitosa:**

```json
{
  "username": "prueba",
  "email": "prueba@email.com",
  "rol": "USER"
}
```

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `201 Created` 

![Registro existoso](../../screenshot/register/01_registro.png "Registro existoso")

---


### **Petición con error "nickname en uso" (nickname ya registrado en MongoDB):**

En caso de que el nickname ya esté registrado, se enviará nuevamente la misma petición que en el ejemplo anterior.

- **Ejemplo de petición:**

```json
{
  "username": "prueba",
  "email": "prueba@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```
- **Ejemplo de respuesta erronea**

```json
{
  "message": "Conflict (409): Usuario prueba ya está registrado",
  "uri": "/usuarios/register"
}
```

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `409 Conflict`

![Registro errone nick](../../screenshot/register/02_nicknameRepetido.png "Registro errone nick")

---

### **Petición con error "nickname vacío":**

En caso de que el nickname esté vacío, se enviará la siguiente petición.

- **Ejemplo de petición:**

```json
{
  "username": "",
  "email": "prueba@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```

- **Ejemplo de respuesta erronea**

```json
{
  "message": "Bad Request Exception (400). Uno o más campos vacíos",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`


![Registro errone nick](../../screenshot/register/03_nicknameVacio.png "Registro nick vacio")

---

### **Petición con error "correo electrónico en uso" (usuario con correo electrónico ya registrado en MongoDB):**

En caso de que el correo electrónico ya esté registrado, se enviará la petición nuevamente, modificando el **"nickname"** para evitar conflictos con los casos anteriores.

- **Ejemplo de petición:**

```json
{
  "username": "prueba_correo",
  "email": "prueba@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```

- **Ejemplo de respuesta erronea**

```json
{
  "message": "Conflict (409): Email prueba@email.com ya está registrado",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `409 Conflict`

![Registro error email](../../screenshot/register/04_email_existenteBBDD.png "Registro email existente en la base de datos")

---

### **Petición con error "correo electrónico vacío" (el usuario no proporciona un correo electrónico):**

Para el caso de que el correo electrónico este vacio, se enviara la petición, pero con la modificación del **"nickname"**, para que no reconozca los casos anteriores del **"nickname"**.

- **Ejemplo de petición:**
```json
{
  "username": "prueba_correo",
  "email": "",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```

- **Ejemplo de respuesta erronea**

```json
{
	"message": "Bad Request Exception (400). Uno o más campos vacíos",
	"uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Registro error email](../../screenshot/register/05_email_vacio.png "Registro email vacio")

---

### **Petición con error "contraseña inválida"**

En caso de que la contraseña esté vacía, esta prueba se aplica tanto a **"password"** como a **"passwordRepeat"**, así como a cada opción de forma independiente.

- **Ejemplo de petición:**

```json
{
  "username": "prueba_contrasena",
  "email": "pruebaContrasena@email.com",
  "password": "",
  "passwordRepeat": "",
  "rol": "USER",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```
- **Ejemplo de respuesta erronea**


```json
{
  "message": "Bad Request Exception (400). Uno o más campos vacíos",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Registro error contrasena](../../screenshot/register/06_pass_vacias.png "Pass vacias en el registro")

---

### **Petición con "Rol" administrador:**

Para el caso del rol de administrador, se enviará la siguiente petición para verificar que el rol asignado sea correcto y corresponda a un administrador.

- **Cuerpo JSON**

```json
{
  "username": "prueba_rol",
  "email": "pruebaRol@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "ADMIN",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```
- **Ejemplo de respuesta erronea**

```json
{
  "username": "prueba_rol",
  "email": "pruebaRol@email.com",
  "rol": "ADMIN"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `201 Created`

![Registro admin](../../screenshot/register/07_registro_admin.png "Registro admin")

---

### **Petición con error en "Rol":**

En caso de que el rol esté vacío o no se haya asignado ningún tipo de rol.

- **Cuerpo JSON**

```json
{
  "username": "prueba_rol_vacio",
  "email": "pruebaRolVacio@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```
- **Ejemplo de respuesta erronea**


```json
{
  "message": "Bad Request Exception (400). ROL:  incorrecto",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request` 

![Registro admin vacio](../../screenshot/register/08_rolVacio.png "Registro admin vacio")

---

### **Petición con error en "Rol" (sin datos):**

En caso de que el rol no tenga ningún identificador, ya sea "admin" o "user".

- **Cuerpo JSON**

```json
{
  "username": "prueba_rol_perro",
  "email": "pruebaRolPerro@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "perro",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```

- **Ejemplo de respuesta erronea**

```json
{
  "message": "Bad Request Exception (400). ROL: perro incorrecto",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Registro admin erroneo](../../screenshot/register/09_rolErroneo.png "Registro admin erroneo")

---

### **Petición con error en "Provincia"**

En caso de que la provincia esté vacía y no se haya especificado ningún valor.

- **Cuerpo JSON**

```json
{
  "username": "municipio_provincia",
  "email": "municipio_provincia@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "",
    "municipio": "Cádiz",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```
- **Ejemplo de respuesta erronea**

```json
{
  "message": "Bad Request Exception (400). Provincia  no encontrada",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Registro provincia vacia](../../screenshot/register/010_provincia_vacia.png "Registro provincia vacia")

---

### **Petición con error en "Provincia" debido a un municipio incorrecto:**

En caso de que la provincia contenga un municipio incorrecto.

- **Cuerpo JSON**

```json
{
  "username": "municipio_provincia",
  "email": "municipio_provincia@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "Barcelona",
    "municipio": "Barbate",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```
- **Ejemplo de respuesta erronea**


```json
{
  "message": "Bad Request Exception (400). Municipio Barbate incorrecto",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Registro provincia municipio incorrecto](../../screenshot/register/011_provincia_municipio_incorrecto.png "Registro provincia municipio incorrecto")

---

### **Petición con error en "Municipio":**

En caso de que el municipio esté vacío y no se haya especificado ningún valor.

- **Cuerpo JSON**

```json
{
  "username": "municipio_provincia",
  "email": "municipio_provincia@email.com",
  "password": "Segura123!",
  "passwordRepeat": "Segura123!",
  "rol": "USER",
  "direccion": {
    "provincia": "Cádiz",
    "municipio": "",
    "calle": "calle acacias",
    "num": "1",
    "cp": "11007"
  }
}
```

- **Ejemplo de respuesta erronea**

```json
{
  "message": "Bad Request Exception (400). Municipio  incorrecto",
  "uri": "/usuarios/register"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Registro provincia municipio vacio](../../screenshot/register/012_municipio_vacio.png "Registro provincia municipio vacio")

---

He decidido no aplicar validaciones estrictas para los campos **calle**, **número** y **código postal**, ya que su verificación exacta puede resultar compleja. 
Factores como la ausencia de numeración en algunas calles, la existencia de nombres de calles repetidos en distintas localidades o las variaciones en los códigos postales dificultan una validación precisa.

### 3.2 Inicio de Sesión

- **Endpoint:** `POST /usuarios/login`
- **Descripción:** Permite a un usuario iniciar sesión.
- **Código de respuesta esperado:** 
  - `200 OK` (Éxito) 
  - `401 Unauthorized` (Credenciales incorrectas)
  - `400 Bad Request` (Uno o más campos vacíos)

### **Petición login correcto**

```json
{
  "username": "angelito",
  "password": "Segura123!"
}
```

- **Ejemplo de respuesta exitosa**

Esta respuesta incluye el token JWT que se utilizará para autorizar las peticiones.

```json
{
	"token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYW5nZWxpdG8iLCJleHAiOjE3NDAwNjk3MTMsImlhdCI6MTc0MDA2NjExMywicm9sZXMiOiJST0xFX1VTRVIifQ.oa-79mi0syyaOsVcSx-ksUmFtbqZ9njl8L9pbSYCphC_3G1805WHxuB9JNja42fbabyM72zAza8UXEChH_bVKS9sAZu1cTBCackA-Yz0-3VGv5a-oXrYKclX6NjyVmMQCncAPonFTNw-utcK7MADorgvcl30SYB12clhDaMW6U7SqnuDL5KlgPdQGYoJvG9n9jEz4cI_k25Sg9t1J1iqpkZb0wrJWpz58hbYYA0xKpumz737qcWphOC-rx1P4GIMz4gKukqWaVZskt33FkGBGVv913z4dBtOziBM1A5DZb7yfFKjSU_q1bISB7UiadX0Z9w-7Gd4UHq9upg-dftCVQ"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `token`

![Inicio sesion correcto](../../screenshot/register/013_inicio_sesion_correcto.png "Inicio sesion correcto")

---

### **Petición login incorrecto**

```json
{
  "username": "angelito",
  "password": "incorrecto"
}
```

- **Ejemplo de respuesta erronea**

```json
{
  "message": "Not authorized exception (401). Credenciales incorrectas",
  "uri": "/usuarios/login"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`


![Inicio sesion incorrecto](../../screenshot/register/014_inicio_sesion_incorrecto.png "Inicio sesion incorrecto")

---

### **Petición inicio de sesion con campos vacíos**

Esta validación se aplica tanto al campo **"username"** como al campo **"password"** y actúa en caso de que alguno de los campos esté vacío o ambos lo estén.

```json
{
  "username": "",
  "password": ""
}
```

- **Ejemplo de respuesta erronea**

```json
{
  "message": "Bad Request Exception (400). El usuario o la contraseña no pueden estar vacíos",
  "uri": "/usuarios/login"
}
```
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Inicio sesion vacio](../../screenshot/register/015_inicio_sesion_vacio.png "Inicio sesion vacio")


---

## 4. Interfaz Gráfica
Se ha desarrollado una interfaz gráfica donde los usuarios pueden registrarse e iniciar sesión. Esta interfaz interactúa con los endpoints mencionados anteriormente para gestionar el acceso de los usuarios.

**interfaz gráfica**

|                                    **Interfaz de login**                                    |                                    **Interfaz de registro**                                    |
|:-------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| ![Inicio sesion vacio](../../screenshot/login/016_pantalla_login.png "Inicio sesion vacio") | ![Inicio sesion vacio](../../screenshot/login/017_pantalla_registro.png "Inicio sesion vacio") |




### 4.1 Pruebas de la Interfaz Gráfica

#### **Login usuario**

- **Caso de éxito:** Se ha logeado un usuario correctamente.

![Registro existoso](../../screenshot/login/018_interfaz_login_correcto.png "Registro existoso")

---


- **Caso de error:** El usuario intenta logearse con datos invalidos.

![Usuario se logea con datos invalidos](../../screenshot/login/019_interfaz_login_incorrecto.png "Usuario datos invalidos")

---

- **Caso de error:** El usuario no introduce datos en el login.

![Datos invalidos](../../screenshot/login/020_LoginDatosInvalidos.png "Datos invalidos")

---


#### **Registro usuario**

- **Caso de éxito:** El usuario se ha registrado correctamente.

![Registro existoso](../../screenshot/login/021_registroExito.png "Registro existoso")

---

- **Caso de error:** El usuario intenta registrarse con un correo ya registrado.

![Correo ya registrado](../../screenshot/login/024_Verificacion%20usuarioRegistradoCorreoElectronico.png "Correo ya registrado")

---

- **Caso de error:** El usuario intenta registrarse con un nickname ya registrado.

![Nickname ya registrado](../../screenshot/login/023_Verificacion%20usuarioRegistradoNickname.png "Nickname ya registrado")

---

- **Caso de error:** El usuario intenta registrarse con un campo vacío.

![Campo vacio](../../screenshot/login/022_VerificacionCamposAPP.png "Campo vacio")

---


## 5. Verificacion de datos en MongoDB


### **Usuarios registrados en MongoDB**

Se han registrado varios usuarios en la base de datos para comprobar el correcto funcionamiento de los endpoints, adjunto una captura de pantalla con los usuarios registrados en MongoDB y el cuerpo JSON de los mismos.


- **Captura de pantalla página MongoDB:**

![Captura verificacion MONGODB](../../screenshot/register/025_verificacionPersistenciaMongo.png "Captura verificacion MONGODB")

- **Usuarios registrados(JSON):**


```json
[
  {
    "_id": {"$oid": "67b6e16fc2d38613b46714ae"},
    "_class": "com.es.API_REST_Segura_2.model.Usuario",
    "direccion": {
      "calle": "acacias",
      "num": "1",
      "municipio": "Cádiz",
      "provincia": "Cádiz",
      "cp": "11007"
    },
    "email": "luis@admin.com",
    "password": "$2a$10$RL3o6ID65LQVkheWseY/b.Y1.E0cCpvcIDz0TIvpnvJg0StAs.2DC",
    "roles": "USER",
    "username": "lbaeutr"
  },
  {
    "_id": {"$oid": "67b7148e6395e7155e370ac8"},
    "_class": "com.es.API_REST_Segura_2.model.Usuario",
    "direccion": {
      "calle": "real",
      "num": "1",
      "municipio": "san fernando",
      "provincia": "cádiz",
      "cp": "11100"
    },
    "email": "angel@gmail.com",
    "password": "$2a$10$Z04K3uQWXSykUkOoFbDJAO1aji51W711MzrItQEQxA.x.IhfT346W",
    "roles": "USER",
    "username": "angelito"
  },
  {
    "_id": {"$oid": "67b75123fa876d07bbbccf55"},
    "_class": "com.es.API_REST_Segura_2.model.Usuario",
    "direccion": {
      "calle": "calle acacias",
      "num": "1",
      "municipio": "Cádiz",
      "provincia": "Cádiz",
      "cp": "11007"
    },
    "email": "prueba@email.com",
    "password": "$2a$10$6vEWGLy6TaXmiUN31cq51O6a7wLw8nbDQEPm.QjsDYvkdD7wsOD52",
    "roles": "USER",
    "username": "prueba"
  },
  {
    "_id": {"$oid": "67b75b48fa876d07bbbccf63"},
    "_class": "com.es.API_REST_Segura_2.model.Usuario",
    "direccion": {
      "calle": "calle acacias",
      "num": "1",
      "municipio": "Cádiz",
      "provincia": "Cádiz",
      "cp": "11007"
    },
    "email": "pruebaRol@email.com",
    "password": "$2a$10$EebP62HTz8ehfzFGYNr10.owOb94FiokIP6c9yerZCec2ZrdX6fxW",
    "roles": "ADMIN",
    "username": "prueba_rol"
  },
  {
    "_id": {"$oid": "67b76636fa876d07bbbccf6e"},
    "_class": "com.es.API_REST_Segura_2.model.Usuario",
    "direccion": {
      "calle": "veticuatro de julio",
      "num": "1",
      "municipio": "Cádiz",
      "provincia": "Cádiz",
      "cp": "11007"
    },
    "email": "lbaeutr888@gmail.com",
    "password": "$2a$10$qZ7Q5CKesulQYM6pxMF1ROA99ldmGXxkC.PyunullfvXB3uQ849Xa",
    "roles": "USER",
    "username": "luisito"
  },
  {
    "_id": {"$oid": "67b780f848d3ca7c716e1b34"},
    "_class": "com.es.API_REST_Segura_2.model.Usuario",
    "direccion": {
      "calle": "gas",
      "num": "12",
      "municipio": "Cádiz",
      "provincia": "Cádiz",
      "cp": "11008"
    },
    "email": "diego@gmail.com",
    "password": "$2a$10$ejq8BjVJtCfCWEDLsyJIoeRoQSrF5I0YjOoTPENIGxNS59f3ECMd6",
    "roles": "USER",
    "username": "diego"
  }
]

  ```

