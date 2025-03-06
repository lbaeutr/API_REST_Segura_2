# Parte IV - Despliegue y Pruebas en Render

## Introducción
En esta última fase de la entrega, llevaremos a cabo el **despliegue de nuestra API REST Segura en Render**, con el objetivo de validar su correcto funcionamiento en un entorno de producción. Render nos proporciona una infraestructura en la nube donde podemos ejecutar nuestra API de manera accesible desde cualquier lugar.

A lo largo de esta entrega, realizaremos pruebas en la API ya desplegada, verificando el comportamiento de los diferentes endpoints y asegurando que las medidas de seguridad implementadas se mantienen en producción.


## Indice de Contenidos:

1. **Despliegue en Render**
    - Creación del archivo Dockerfile.
    - Creación del servicio en Render.
    - Verificación del despliegue.
    - Pruebas iniciales de despliegue.

2. **Login y registro**
3. **Gestión de Tareas**
4. **TODO**

## Despliegue en Render
Para desplegar la API en Render, seguimos los siguientes pasos:

1. **Creación del archivo Dockerfile:**
    - Para contenerizar nuestra aplicación, hemos creado un archivo `Dockerfile` con la siguiente configuración:
      ```Dockerfile
        FROM azul/zulu-openjdk:17-latest
        VOLUME /tmp
        COPY build/libs/*.war app.war
        CMD ["java", "-jar", "/app.war"]
      ```

2. **Creación del servicio en Render:**
    - Accedemos a [Render](https://render.com/).
    - Creamos un nuevo **Web Service**.
    - Conectamos el servicio con nuestro repositorio en **GitHub**.
    - Seleccionamos el entorno de ejecución **Docker**.
    - Configuramos el **Dockerfile** como base para la construcción de la imagen.
    - Configuramos las **variables de entorno**.
      
3. **Verificación del despliegue:**
    - Hemos realizado la verificación del despliegue con una prueba de conexión a la API.
    - Captura de pantalla del despliegue exitoso:
   
   
![Captura de pantalla del despliegue en Render](../../screenshot/render/00_despliegueRender.png)

4. **Pruebas iniciales de despliegue:**
    - Verificamos que el servicio se inicia correctamente en la URL generada por Render: https://api-rest-segura-2.onrender.com/





---

## 2. Login y Registro de Usuarios

### 2.1 Registro de Usuarios

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

![Registro existoso](../../screenshot/render/register_render/00_registroexito.png "Registro existoso")

---
  
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

![Registro errone nick](../../screenshot/render/register_render/01_errorNick.png "Registro errone nick")

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

![Registro errone nick](../../screenshot/render/register_render/02_nickvacio.png "Registro nick vacio")

---

### **Petición con error "correo electrónico en uso" (usuario con correo electrónico ya registrado en MongoDB):**

En caso de que el correo electrónico ya esté registrado, se enviará la petición nuevamente, modificando el **"nickname"
** para evitar conflictos con los casos anteriores.

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

![Registro error email](../../screenshot/render/register_render/03_correoEnuso.png "Registro email existente en la base de datos")

---

### **Petición con error "correo electrónico vacío" (el usuario no proporciona un correo electrónico):**

Para el caso de que el correo electrónico este vacio, se enviara la petición, pero con la modificación del **"nickname"
**, para que no reconozca los casos anteriores del **"nickname"**.

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

![Registro error email](../../screenshot/render/register_render/04_emailVacio.png "Registro email vacio")

---

### **Petición con error "contraseña inválida"**

En caso de que la contraseña esté vacía, esta prueba se aplica tanto a **"password"** como a **"passwordRepeat"**, así
como a cada opción de forma independiente.

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

![Registro error contrasena](../../screenshot/render/register_render/05_contrasenaVacia.png "Pass vacias en el registro")

---

### **Petición con "Rol" administrador:**

Para el caso del rol de administrador, se enviará la siguiente petición para verificar que el rol asignado sea correcto
y corresponda a un administrador.

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

![Registro admin](../../screenshot/render/register_render/06_registroAdmin.png "Registro admin")

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

![Registro admin vacio](../../screenshot/render/register_render/07_regitroAdminVacio.png "Registro admin vacio")

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

![Registro admin erroneo](../../screenshot/render/register_render/08_registroRolErroneo.png "Registro admin erroneo")

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

![Registro provincia vacia](../../screenshot/render/register_render/09_provinciaVacia.png "Registro provincia vacia")

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

![Registro provincia municipio incorrecto](../../screenshot/render/register_render/10_municipioIncorrecto.png "Registro provincia municipio incorrecto")

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

![Registro provincia municipio vacio](../../screenshot/render/register_render/011_municipioVacio.png "Registro provincia municipio vacio")

---

He decidido no aplicar validaciones estrictas para los campos **calle**, **número** y **código postal**, ya que su
verificación exacta puede resultar compleja.
Factores como la ausencia de numeración en algunas calles, la existencia de nombres de calles repetidos en distintas
localidades o las variaciones en los códigos postales dificultan una validación precisa.



### 2.2 Inicio de Sesión

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
  "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYW5nZWxpdG8iLCJleHAiOjE3NDEyNTQxODQsImlhdCI6MTc0MTI1MDU4NCwicm9sZXMiOiJST0xFX1VTRVIifQ.i_ZVl3GyuWwpDQ06kxZDnzLbyGGpxb6xCo9vEtqr73PDdRq9p-rpwBJ2-LeTieCz43ZmNWkYirKxQx92t8jnGbvHM73PzXe9c1VDdgnIl9pipvF9G7OKpFL7_3qAsCV_Z11L05PiVrzfyQBnHgR_aGdzKKxYH-Q-cN8hCMt9iVdYm3uNdvUuN9t-_gjFmm8JdzFdOHLXKl_EDl7fVSe8tsVR6jRHOEZtlVTGbnWTYuAfhSrzeQiO3lL6RE9SN3x-b7na_cbp4lE7f20YTf1-fcd24gMMtk5WHv_RwIAqQ0Rq0bY8mEVb6ybp0cfbRMS313WuYhJuWK2RSlvTbvIK1w"
}
```

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `token`

![Inicio sesion correcto](../../screenshot/render/login_render/00_loginExito.png "Inicio sesion correcto")

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

![Inicio sesion incorrecto](../../screenshot/render/login_render/01_loginIncorrecto.png "Inicio sesion incorrecto")

---

### **Petición inicio de sesion con campos vacíos**

Esta validación se aplica tanto al campo **"username"** como al campo **"password"** y actúa en caso de que alguno de
los campos esté vacío o ambos lo estén.

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

![Inicio sesion vacio](../../screenshot/render/login_render/02_inicioSesionVacio.png "Inicio sesion vacio")


---
---

## 3. Gestión de Tareas (Usuarios)

### 3.1. Crear tarea


- **Endpoint:** `POST /tareas`
- **Descripción:** Un usuario autenticado puede crear una tarea para sí mismo.
- **Código de respuesta esperado:**

    - **201 CREATED** - La tarea se ha creado correctamente.
    - **400 BAD REQUEST** - La petición no es válida.
    - **401 UNAUTHORIZED** - El usuario no está autenticado.
    - **403 FORBIDDEN** - El usuario no tiene permisos para crear la tarea.
    - **404 NOT FOUND** - El usuario no existe.

- **Ejemplo de petición:**

    ```json
    {
    "titulo": "prueba_render",
    "descripcion": "prueba"
    }
    ```

- **Ejemplo de respuesta:**

    ```json
    {
    "_id": "5",
    "titulo": "prueba_render",
    "descripcion": "prueba",
    "estado": false,
    "usuarioId": "diego"
    }
    ```


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `201 Created`

![Tarea creada](../../screenshot/render/task_render/00_creacionTareaExito.png "Tarea creada")

---

#### Crear tarea sin autenticación

- **Endpoint:** `POST /tareas`
- **Descripción:** Un usuario sin auntenticar no puede crear una tarea.

- **Ejemplo de petición:**

    ```json
    {
    "titulo": "prueba_render",
    "descripcion": "prueba"
      }
    ```


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea no creada](../../screenshot/render/task_render/01_tareaSinAunteticacion.png "Tarea no creada")

---

#### Crear tarea con datos vacios

Este caso de prueba verifica que no se pueda crear una tarea con datos vacíos, ya sea con un solo campo o con ambos.

- **Ejemplo de petición:**

    ```json
    {
      "titulo": "",
      "descripcion": ""
    }
    ```

- **Ejemplo de respuesta (error):**

    ```json
    {
      "message": "Bad Request Exception (400). El título y la descripción no pueden estar vacíos",
      "uri": "/tareas"
    }
    ```


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Tarea campos vacios](../../screenshot/render/task_render/02_tareasVacias.png "Tarea campos vacios")

---

### 3.2. Listar tareas

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario autenticado puede listar sus tareas por id.

- **Ejemplo de respuesta:**

    ```json
    {
	  "_id": "5",
	  "titulo": "prueba_render",
	  "descripcion": "prueba",
	  "estado": false,
	  "usuarioId": "diego"
    }
    ```


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `200 OK`

![Tarea listada](../../screenshot/render/task_render/03_listarTareaAutenticada.png "Tarea listada")

---


#### Listar tareas que pertenecen a otro usuario

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario intenta listar las tareas de otro usuario.

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Captura pantalla BBDD](../../screenshot/render/task_render/04_capturaPantalla.png "Captura BBDD")

![Tarea listada otro usuario](../../screenshot/render/task_render/05_listarTareaOtherUsers.png  "Tarea listada otro usuario")

---

#### Listar tareas sin autenticación

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no autenticado no puede listar sus tareas.

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea listada sin aunte](../../screenshot/render/task_render/06_listarTareaSinAuntenticar.png "Tarea listada sin aunte")

---

### 3.3. Actualizar tarea

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario autenticado puede actualizar una tarea por id.


- **Captura de pantalla antes de la actualización**

![Captura pantalla BBDD](../../screenshot/render/task_render/04_capturaPantalla.png "Captura BBDD")

En este caso se actualiza tanto la descripción como el estado de la tarea.

- **Ejemplo de petición:**

    ```json
    {
      "titulo": "Comprar pan",
      "descripcion": "Tengo que comprar pan para la BBQ y para casa de mi madre",
      "estado": true
    }
    ```


- **Ejemplo de respuesta:**

    ```json
    {
      "_id": "7",
      "titulo": "Comprar pan",
      "descripcion": "Tengo que comprar pan para la BBQ y para casa de mi madre",
      "estado": true,
      "usuarioId": "diego"
    }
    ```


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `200 OK`

![Tarea listada](../../screenshot/render/task_render/07_actualizarTarea.png "Tarea listada")

---

#### Actualizar tarea sin autenticación

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no autenticado no puede actualizar una tarea.


- **Ejemplo de petición:**

    ```json
    {
      "titulo": "Comprar pan",
      "descripcion": "Tengo que comprar pan para la BBQ y para casa de mi madre y resto de personas del instituto",
      "estado": true
    }
    ```


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401  Unauthorized`

![Tarea listada no auntenticada](../../screenshot/render/task_render/08_actualizarTareasSinAuten.png "Tarea listada no auntenticada")

---

#### Actualizar tarea de otro usuario

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no puede actualizar una tarea de otro usuario.

![Captura pantalla BBDD](../../screenshot/render/task_render/09_BBDD2.png "Captura BBDD")

- **Ejemplo de petición:**

    ```json
    {
      "titulo": "Comprar pan",
      "descripcion": "Tengo que comprar pan para la BBQ y para casa de mi madre y resto de personas del instituto",
      "estado": true
    }
    ```


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401  Unauthorized`

![actualizar tarea otro usuario](../../screenshot/render/task_render/010_actualizarTareaOtroUsuario.png "actualizar tarea otro usuario")


---

#### Actualizar tarea con datos vacíos

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no puede actualizar una tarea con datos vacíos.

Este caso de prueba verifica que no se pueda actualizar una tarea con datos vacíos, ya sea con un solo campo o con
ambos.

- **Ejemplo de petición:**

    ```json
    {
      "titulo": "",
      "descripcion": "",
      "estado": true
    }
    ```

- **Ejemplo de respuesta (error):**

    ```json
    {
      "message": "Bad Request Exception (400). El título y la descripción no pueden estar vacíos",
      "uri": "/tareas/7"
    }
    ```

- **Captura de pantalla:**
- **Código de respuesta esperado:** `400 Bad Request`

![actualizar tarea con campos vacios](../../screenshot/render/task_render/011_actualizarTareasCamposVacios.png "actualizar tarea con campos vacios")

---

#### Actualizar tarea con id no existente

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no puede actualizar una tarea con un id que no existe.

En este caso se intenta actualizar una tarea con un id que no existe en la base de datos.

-**Ejemplo de peticion:**

```json
{
  "titulo": "Comprar pan",
  "descripcion": "Tengo que comprar pan para la BBQ y para casa de mi madre y resto de personas del instituto",
  "estado": true
}
```

- **Ejemplo de respuesta (error):**

    ```json
    {
	  "message": "Bad Request Exception (400). Tarea no encontrada",
	  "uri": "/tareas/99"
    }
    ```

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![actualizar tarea con id no existente](../../screenshot/render/task_render/012_tareaNoExiste.png "actualizar tarea con id no existente")

---


### 3.4. Eliminar tarea

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario autenticado puede eliminar una tarea por id.

- **Captura de pantalla antes de la actualización**

![Captura BBDD](../../screenshot/task/users/014_capturaBBDD.png "Captura BBDD")

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `204 No Content`

![Tarea eliminada](../../screenshot/render/task_render/013_eliminarTareaId.png "Tarea eliminada")

---

#### Eliminar tarea sin autenticación

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario no autenticado no puede eliminar una tarea.
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea eliminada sin autenticación](../../screenshot/render/task_render/014_eliminarTareaSinAutenti.png "Tarea eliminada sin autenticación")

---

#### Eliminar tarea de otro usuario

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario no puede eliminar una tarea de otro usuario.

![Captura BBDD](../../screenshot/render/task_render/015_capturaBBdd.png "Captura BBDD")

Para este caso el usuario diego intentara eliminar una tarea de angelito.

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea eliminada de otro usuario](../../screenshot/render/task_render/016_eliminarTareaOtroUsuario.png "Tarea eliminada de otro usuario")

---

#### Eliminar tarea con id no existente

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario no puede eliminar una tarea con un id que no existe.
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Tarea eliminada con id no existente](../../screenshot/render/task_render/017_EliminarTareaNOEXIS.png "Tarea eliminada con id no existente")

---


## 4. Gestión de Tareas (Administradores)


### 4.1. Listar tareas

- **Endpoint:** `Get /tareas/all`
- **Descripción:** Un usuario autenticado con rol de administrador puede listar todas las tareas.
- **Código de respuesta esperado:**

    - **200 OK** - La lista de tareas se ha obtenido correctamente.
    - **401 UNAUTHORIZED** - El usuario no está autenticado.
    - **403 FORBIDDEN** - El usuario no tiene permisos para listar las tareas.

Para este caso de prueba se ha utilizado un usuario con rol de administrador(lbaeutr).

![admin](../../screenshot/render/task_render/018_capturaPantallaUsuarios.png  "admin")

- **Captura de pantalla BBDD con los usuarios existentes**

![Captura BBDD](../../screenshot/task/users/017_capturaBBDD.png "Captura BBDD")

- **Ejemplo de respuesta:**

    ```json
   [
        [
        {
            "_id": "1",
            "titulo": "Mi nueva tarea 1",
            "descripcion": "Descripción de la tarea",
            "estado": false,
            "usuarioId": "angelito"
        },
        {
            "_id": "2",
            "titulo": "info bbdd",
            "descripcion": "info bbdd",
            "estado": false,
            "usuarioId": "diego"
        },
        {
            "_id": "3",
            "titulo": "info1",
            "descripcion": "info2 para relleno",
            "estado": false,
            "usuarioId": "lbaeutr"
        },
        {
            "_id": "4",
            "titulo": "info2",
            "descripcion": "info3 para relleno",
            "estado": false,
            "usuarioId": "lbaeutr"
        },
        {
            "_id": "5",
            "titulo": "contenido",
            "descripcion": "Relleno de la BBDD",
            "estado": false,
            "usuarioId": "diego"
    }
    ]
   ]
    ```

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `200 OK`

![Tarea listada admin](../../screenshot/render/task_render/019_listarAdmin.png "Tarea listada admin")

---

---

#### Listar tareas sin autenticación

- **Endpoint:** `Get /tareas/all`
- **Descripción:** Un usuario no autenticado no puede listar todas las tareas.

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea listada sin autenticación](../../screenshot/render/task_render/020_listarSinAunti.png "Tarea listada sin autenticación")

---

#### Listar tareas con rol USER

Para este ejemplo se ha utilizado un usuario con rol de usuario (diego).

![user](../../screenshot/render/task_render/021_userDiego.png "user")

- **Endpoint:** `Get /tareas/all`
- **Descripción:** Un usuario con rol de usuario no puede listar todas las tareas.
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `403 Forbidden`

![Tarea listada con rol USER](../../screenshot/render/task_render/022_listarSiendoUSER.png "Tarea listada con rol USER")

---

### 4.2. Eliminar cualquier tarea con rol de administrador

- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario autenticado con rol de administrador puede eliminar cualquier tarea por id.

Para este caso de prueba se ha utilizado un usuario con rol de administrador(lbaeutr) y se ha eliminado la tarea con id
5.

![admin](../../screenshot/render/task_render/023_adminLba.png "admin")

- **Captura de pantalla BBDD con las tareas existentes**

![Captura BBDD](../../screenshot/render/task_render/024_tareasBBDD.png "Captura BBDD")

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `204 No Content`

![Tarea eliminada admin](../../screenshot/render/task_render/025_eliminarTareasAdmin.png "Tarea eliminada admin")

- **Captura de pantalla BBDD con las tareas existentes después de eliminación**

![Captura BBDD post](../../screenshot/render/task_render/026_BBDD_actualizada.png "Captura BBDD post")


---


#### Eliminar cualquier tarea sin autenticación

- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario no autenticado no puede eliminar cualquier tarea por id.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea eliminada sin autenticación](../../screenshot/render/task_render/027_eliminarSinAunte.png "Tarea eliminada sin autenticación")

---

#### Eliminar cualquier tarea con rol USER

Para este ejemplo se ha utilizado un usuario con rol de usuario (diego).

![user](../../screenshot/render/task_render/028_UserDiego.png "user")

Para este caso de prueba se ha utilizado un usuario con rol de usuario (diego) y se ha intentado eliminar la tarea perteneciente a otro usuario(angelito).

- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario con rol de usuario no puede eliminar cualquier tarea por id.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `403 Forbidden`

![Tarea eliminada con rol USER](../../screenshot/render/task_render/029_eliminarCualquierTareaUser.png  "Tarea eliminada con rol USER")

---

#### Eliminar cualquier tarea con id no existente

- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario con rol de administrador no puede eliminar una tarea con un id que no existe.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `400 Bad Request`

![Tarea eliminada con id no existente](../../screenshot/render/task_render/030_eliminarTareaQueNoexisteSiendoADMIN.png "Tarea eliminada con id no existente")

---


### 4.3. Dar alta a cualquier tarea con rol de administrador para un usuario


- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario autenticado con rol de administrador puede dar de alta una tarea para cualquier usuario.
- **Código de respuesta esperado:**

  - **201 CREATED** - La tarea se ha creado correctamente.
  - **400 BAD REQUEST** - La petición no es válida.
  - **401 UNAUTHORIZED** - El usuario no está autenticado.
  - **403 FORBIDDEN** - El usuario no tiene permisos para crear la tarea.
  - **404 NOT FOUND** - El usuario no existe.

Para este caso de prueba se ha utilizado un usuario con rol de administrador(lbaeutr) y se ha creado una tarea para el
usuario (diego).

- **Ejemplo de petición:**

  ```json
  {
    "titulo": "Tarea para diego",
    "descripcion": "Descripción de la tarea"
  }
  ```

- **Ejemplo de respuesta:**

    ```json
    {
	  "_id": "6",
	  "titulo": "Tarea para diego",
	  "descripcion": "Descripción de la tarea",
	  "estado": false,
	  "usuarioId": "diego"
    }
    ```

- **Captura de pantalla:**
  - **Código de respuesta esperado:** `201 Created`

![Tarea creada admin](../../screenshot/render/task_render/031_crearTareaAdmin.png "Tarea creada admin")

- **Captura de pantalla BBDD con nuevos datos**

![Captura BBDD](../../screenshot/render/task_render/032_capturaBBDD.png "Captura BBDD")


---

#### Dar alta a cualquier tarea sin autenticación

- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario no autenticado no puede dar de alta una tarea para cualquier usuario.


- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea creada admin no aut](../../screenshot/render/task_render/033_altaTareaSinAute.png "Tarea creada admin no aut")

---

#### Dar alta a cualquier tarea con rol USER

Para este ejemplo se ha utilizado un usuario con rol de usuario (diego) y creado una tarea para el usuario (lbaeutr).

- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario con rol de usuario no puede dar de alta una tarea para cualquier usuario.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `403 Forbidden`

![Tarea creada con rol USER](../../screenshot/render/task_render/034_tareaCreadaConRolUser.png "Tarea creada con rol USER")

---


#### Dar alta a cualquier tarea con datos vacíos

- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario con rol de administrador no puede dar de alta una tarea con datos vacíos, ya sea con un
  solo campo o con ambos.

- **Ejemplo de petición:**

  ```json
  {
    "titulo": "",
    "descripcion": ""
  }
  ```

- **Ejemplo de respuesta (error):**

  ```json
  {
    "message": "Bad Request Exception (400). El título y la descripción no pueden estar vacíos",
    "uri": "/tareas/admin"
  }
    ```
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `400 Bad Request`

![Tarea creada con campos vacíos](../../screenshot/render/task_render/035_darAltaConcontenidoVacio.png "Tarea creada con campos vacíos")

---

#### Dar alta a cualquier tarea con usuario no existente

- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario con rol de administrador no puede dar de alta una tarea para un usuario que no existe.
  - **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

- **Ejemplo de respuesta (error):**

  ```json
  {
    "message": "Bad Request Exception (400). Usuario no encontrado en la BBDD",
    "uri": "/tareas/admin"
  }
  ```

![Tarea creada con usuario no existente](../../screenshot/render/task_render/036_darAltaNoExisto.png "Tarea creada con usuario no existente")

---

## 5. Interfaz Gráfica

## Diseño e Interfaz de Usuario
A continuación, presentamos el diseño y las acciones de la interfaz para los diferentes módulos de la aplicación:

### 1. **Interfaz de Login**
- **Diseño:** Formulario con campos para `usuario` y `contraseña`, con botón de `Iniciar sesión` y enlace para `Registrarse`.

![Inicio sesion vacio](../../screenshot/login/016_pantalla_login.png "Inicio sesion vacio") 


- **Acciones:**
  - Generación de token JWT en caso de éxito.

![Registro existoso](../../screenshot/login/018_interfaz_login_correcto.png "Registro existoso")

- Validación de credenciales en la API y manejo de credenciales.

![Datos invalidos](../../screenshot/login/020_LoginDatosInvalidos.png "Datos invalidos")

- Redirección a la pantalla principal en caso de éxito.


### 2. **Interfaz de Registro**
- **Diseño:** Formulario con los campos:
  - `Nombre de usuario`, `Correo electrónico`, `Contraseña`, `Confirmar contraseña`, `Dirección` (provincia, municipio, calle, número, código postal).
  - Botón de `Registrarse` y enlace para `Iniciar sesión` en caso de tener cuenta.

![Inicio sesion vacio](../../screenshot/login/017_pantalla_registro.png "Inicio sesion vacio")





- **Acciones:**
  - Envío de datos a la API.
  - Manejo de respuestas de error (usuario o correo en uso, campos vacíos, etc.).


|                                                      **Manejo error BBDD**                                                      |                                 **Manejo error campos vacios**                                 |
|:-------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------:|
|![Correo ya registrado](../../screenshot/login/024_Verificacion%20usuarioRegistradoCorreoElectronico.png "Correo ya registrado") | ![Campo vacio](../../screenshot/login/022_VerificacionCamposAPP.png "Campo vacio")|


**_Optimización_**: Las verificaciones implementadas en la interfaz evitan enviar peticiones con campos vacíos a la API, reduciendo el tráfico innecesario y mejorando el rendimiento del sistema.


  - Registro exitoso con confirmación visual.

![Registro existoso](../../screenshot/login/021_registroExito.png "Registro existoso")

#### En la Parte II se documentaron pruebas adicionales de autenticación, validación de usuarios y manejo de errores, complementando la verificación del sistema.

* [Documentación - PARTE II: Gestión de Usuarios](./part_II.md)
* Video demostrativo de la documentación de la parte II: [Video](https://www.canva.com/design/DAGfv6VjqS0/QwR7pDb2zHZRVPGTzHXlVA/watch?utm_content=DAGfv6VjqS0&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=hac420c36b9)


### 3. **Gestión de Tareas (Usuario)**
- **Diseño:**
  - Pantalla con lista de tareas asignadas al usuario.
  - icono para `Actulizar` , `Eliminar`.
  - icono para `Desloguearse`.
  - CheckBox para `Completar` tareas.
  - Botón flotante para `Crear nueva tarea`.
  - `Snackbars` para muestra de mensajes de codigo de respuesta.
  

![interfaz tarea](../../screenshot/render/task_render/037_interfazTareas.png "interfaz tarea")


  - Formulario para `Crear nueva tarea`.

![interfaz tarea formulario](../../screenshot/render/task_render/038_interfazCrearTarea.png "interfaz tarea formulario")

- **Acciones:**
  - Cargar tareas desde la API.
  - Permitir al usuario gestionar solo sus propias tareas.
  - Confirmación de acciones críticas (eliminar tareas).

![interfaz tarea formulario](../../screenshot/render/task_render/039_confirmacionEliminacion.png "interfaz tarea formulario")


### 4. **Gestión de Tareas (Admin)**
- **Diseño:**
  - Tabla con todas las tareas registradas en la aplicación de todos los usuarios.
  - Opción para `Eliminar` cualquier tarea.
  - Botón flotante para `Crear nueva tarea` para los usuarios y el mismo.
  - `Snackbars` para muestra de mensajes de codigo de respuesta de la API.

![interfaz tarea ADMIN](../../screenshot/render/task_render/040_listaTareasAdmin.png "interfaz tarea ADMIN")


  - Formulario para `Crear tarea` para cualquier usuario.

![interfaz tarea FORMULARIO](../../screenshot/render/task_render/041_formularioRegistrarTareas.png "interfaz tarea FORMULARIO")


- **Acciones:**
  - Carga de todas las tareas desde la API.
  - Restricción de acciones según el rol del usuario.
  - Confirmación de acciones críticas (eliminación de tareas de otros usuarios).


| **Eliminación Confirmación Admin** |                                             **Snackbar para códigos**                                              |
|:----------------------------------:|:------------------------------------------------------------------------------------------------------------------:|
| ![interfaz tarea FORMULARIO](../../screenshot/render/task_render/042_EliminacionConfirmacionAdmin.png "interfaz tarea FORMULARIO") | ![interfaz tarea FORMULARIO](../../screenshot/render/task_render/043_snackbarCode.png "interfaz tarea FORMULARIO") |

#### En la Parte III se documentaron pruebas adicionales de gestión de tareas y manejo de errores, complementando la verificación del sistema.

* [Documentación - PARTE III: Gestión de Tareas](./part_III.md)
* Video demostrativo de la documentación de la parte III: [Video](https://www.canva.com/design/DAGgN8_A-Uc/E1m-NTSSqsAjB3VeGuzeLA/watch?utm_content=DAGgN8_A-Uc&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h100348045b)



---

## Documentación y Evidencias: Entrega Parte IV.
**Vídeo del funcionamiento de la API en producción:**


<p align="center">

<a href="https://www.canva.com/design/DAGg9s88qtE/rl1ts_QhP_Aj_YvvyhHPFg/watch?utm_content=DAGg9s88qtE&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h0499538f9e">
<img src="../../screenshot/icon/icono_play.png" alt="icono" width="200" height="200">
</a>

</p>


## Conclusión
### Con el despliegue en Render, comprobamos que la API funciona bien en producción. Las pruebas confirman que es segura, estable y lista para usar