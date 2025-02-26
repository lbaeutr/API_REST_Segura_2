# PRUEBAS GESTIÓN DE TAREAS

Este documento contiene las pruebas realizadas para los **endpoints de gestión de tareas** en la API REST. Incluye los casos de uso, ejemplos de peticiones, respuestas esperadas y códigos de estado.

---


## 1. Endpoints de gestión de tareas (USER)

### 1.1. Crear tarea

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
      "titulo": "Comprar pan",
      "descripcion": "Tengo que comprar pan para la BBQ"
    }
    ```
  
- **Ejemplo de respuesta:**

    ```json
    {
      "_id": "7",
      "titulo": "Comprar pan",
      "descripcion": "Tengo que comprar pan para la BBQ",
      "estado": false,
      "usuarioId": "diego"
    }
    ```
  


- **Captura de pantalla:**
    - **Código de respuesta esperado:** `201 Created`

![Tarea creada](../../screenshot/task/users/00_crearTareaUser.png "Tarea creada")

---

#### Crear tarea sin autenticación

- **Endpoint:** `POST /tareas`
- **Descripción:** Un usuario sin auntenticar no puede crear una tarea.

- **Ejemplo de petición:**

    ```json
    {
       "titulo": "Comprar pan",
       "descripcion": "Tengo que comprar pan para la BBQ"
    }
    ```



- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea no creada](../../screenshot/task/users/01_crearTareaSinToken.png "Tarea no creada")

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

![Tarea campos vacios](../../screenshot/task/users/03_crearTareasCamposVacios.png "Tarea campos vacios")

---


### 1.2. Listar tareas


- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario autenticado puede listar sus tareas por id.

- **Ejemplo de respuesta:**

    ```json
    {
      "_id": "7",
      "titulo": "Comprar pan",
      "descripcion": "Tengo que comprar pan para la BBQ",
      "estado": false,
      "usuarioId": "diego"
    }
    ```


- **Captura de pantalla:**
  - **Código de respuesta esperado:** `200 OK`

![Tarea listada](../../screenshot/task/users/04_listarTareaAunteti.png "Tarea listada")

---

#### Listar tareas que pertenecen a otro usuario

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario intenta listar las tareas de otro usuario.

- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`

![Captura pantalla BBDD](../../screenshot/task/users/06_capturaBBDD.png "Captura BBDD")


![Tarea listada otro usuario](../../screenshot/task/users/07_ListarTareasOtherUsers.png  "Tarea listada otro usuario")

---

#### Listar tareas sin autenticación

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no autenticado no puede listar sus tareas.

- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea listada sin aunte](../../screenshot/task/users/05_ListarTareaSinAunte.png "Tarea listada sin aunte")

---

### 1.3. Actualizar tarea


- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario autenticado puede actualizar una tarea por id.



- **Captura de pantalla antes de la actualización**

![captura](../../screenshot/task/users/00_crearTareaUser.png "captura")

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

![Tarea listada](../../screenshot/task/users/08_actualizarTarea.png "Tarea listada")

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

![Tarea listada no auntenticada](../../screenshot/task/users/09_ActualizarTareasinAuto.png "Tarea listada no auntenticada")

---

#### Actualizar tarea de otro usuario

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no puede actualizar una tarea de otro usuario.


![Captura pantalla BBDD](../../screenshot/task/users/010_capturaPantalla.png "Captura BBDD")

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

![actualizar tarea otro usuario](../../screenshot/task/users/011_ActualizarTareaOtroUsuario.png "actualizar tarea otro usuario")


---

#### Actualizar tarea con datos vacíos

- **Endpoint:** `Get /tareas/{id}`
- **Descripción:** Un usuario no puede actualizar una tarea con datos vacíos.


Este caso de prueba verifica que no se pueda actualizar una tarea con datos vacíos, ya sea con un solo campo o con ambos.


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


![actualizar tarea con campos vacios](../../screenshot/task/users/012_ActualizarTareaCamposVacios.png "actualizar tarea con campos vacios")

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

- **Captura de pantalla:**
  - **Código de respuesta esperado:** `400 Bad Request`

![actualizar tarea con id no existente](../../screenshot/task/users/013_TareaNoExiste.png "actualizar tarea con id no existente")

---

### 1.4. Eliminar tarea

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario autenticado puede eliminar una tarea por id.

- **Captura de pantalla antes de la actualización**

![Captura BBDD](../../screenshot/task/users/014_capturaBBDD.png "Captura BBDD")

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `204 No Content`

![Tarea eliminada](../../screenshot/task/users/015_eliminarTarea.png "Tarea eliminada")

---

#### Eliminar tarea sin autenticación

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario no autenticado no puede eliminar una tarea.
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea eliminada sin autenticación](../../screenshot/task/users/016_eliminarTareaSinToken.png "Tarea eliminada sin autenticación")

---

#### Eliminar tarea de otro usuario

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario no puede eliminar una tarea de otro usuario.


![Captura BBDD](../../screenshot/task/users/017_capturaBBDD.png "Captura BBDD")

- **Captura de pantalla:**
    - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea eliminada de otro usuario](../../screenshot/task/users/018_EliminarTareaDeOtroUsuario.png "Tarea eliminada de otro usuario")

---

#### Eliminar tarea con id no existente

- **Endpoint:** `DELETE /tareas/{id}`
- **Descripción:** Un usuario no puede eliminar una tarea con un id que no existe.
- **Captura de pantalla:**
    - **Código de respuesta esperado:** `400 Bad Request`

![Tarea eliminada con id no existente](../../screenshot/task/users/019_tareaNoExiste.png "Tarea eliminada con id no existente")

---


## 2. Endpoints de gestión de tareas (ADMIN)

### 2.1. Listar tareas

- **Endpoint:** `Get /tareas/all`
- **Descripción:** Un usuario autenticado con rol de administrador puede listar todas las tareas.
- **Código de respuesta esperado:**

    - **200 OK** - La lista de tareas se ha obtenido correctamente.
    - **401 UNAUTHORIZED** - El usuario no está autenticado.
    - **403 FORBIDDEN** - El usuario no tiene permisos para listar las tareas.


Para este caso de prueba se ha utilizado un usuario con rol de administrador(lbaeutr).

![admin](../../screenshot/task/admin/00_UsuarioAdmin.png "admin")


- **Captura de pantalla BBDD con los usuarios existentes**

![Captura BBDD](../../screenshot/task/users/017_capturaBBDD.png "Captura BBDD")


- **Ejemplo de respuesta:**

    ```json
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
		"titulo": "Mi nueva tarea 2 update",
		"descripcion": "Descripción de la tarea",
		"estado": false,
		"usuarioId": "lbaeutr"
	},
	{
		"_id": "4",
		"titulo": "Segunda tarea",
		"descripcion": "Descripción de la tarea",
		"estado": true,
		"usuarioId": "diego"
}
]
    ```
  

- **Captura de pantalla:**
  - **Código de respuesta esperado:** `200 OK`

![Tarea listada admin](../../screenshot/task/admin/01_ListarTareaAdmin.png "Tarea listada admin")

---

#### Listar tareas sin autenticación


- **Endpoint:** `Get /tareas/all`
- **Descripción:** Un usuario no autenticado no puede listar todas las tareas.

- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea listada sin autenticación](../../screenshot/task/admin/02_ListarSinAunt.png "Tarea listada sin autenticación")

---

#### Listar tareas con rol USER

Para este ejemplo se ha utilizado un usuario con rol de usuario (diego).

![user](../../screenshot/task/admin/03_CapturaUser.png "user")

- **Endpoint:** `Get /tareas/all`
- **Descripción:** Un usuario con rol de usuario no puede listar todas las tareas.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `403 Forbidden`

![Tarea listada con rol USER](../../screenshot/task/admin/04_ListarRolUser.png "Tarea listada con rol USER")

---


### 2.2. Eliminar cualquier tarea con rol de administrador

- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario autenticado con rol de administrador puede eliminar cualquier tarea por id.

Para este caso de prueba se ha utilizado un usuario con rol de administrador(lbaeutr) y se ha eliminado la tarea con id 5.

![admin](../../screenshot/task/admin/00_UsuarioAdmin.png "admin")


- **Captura de pantalla BBDD con las tareas existentes**

![Captura BBDD](../../screenshot/task/admin/05_CapturaTareasExistentes.png "Captura BBDD")


- **Captura de pantalla:**
  - **Código de respuesta esperado:** `204 No Content`

![Tarea eliminada admin](../../screenshot/task/admin/06_EliminarTareaRolAdmin.png "Tarea eliminada admin")

- **Captura de pantalla BBDD con las tareas existentes después de eliminación**

![Captura BBDD post](../../screenshot/task/admin/07_CapturaBBDDpost.png "Captura BBDD post")


---

#### Eliminar cualquier tarea sin autenticación 

- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario no autenticado no puede eliminar cualquier tarea por id.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`
  
![Tarea eliminada sin autenticación](../../screenshot/task/admin/08_EliminarTareaSinAunte.png "Tarea eliminada sin autenticación")

---

#### Eliminar cualquier tarea con rol USER

Para este ejemplo se ha utilizado un usuario con rol de usuario (diego).

![user](../../screenshot/task/admin/03_CapturaUser.png "user")


- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario con rol de usuario no puede eliminar cualquier tarea por id.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `403 Forbidden`

![Tarea eliminada con rol USER](../../screenshot/task/admin/09_EliminarTareaRolUser.png "Tarea eliminada con rol USER")

---

#### Eliminar cualquier tarea con id no existente

- **Endpoint:** `DELETE /tareas/admin/{id}`
- **Descripción:** Un usuario con rol de administrador no puede eliminar una tarea con un id que no existe.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `400 Bad Request`

![Tarea eliminada con id no existente](../../screenshot/task/admin/010_EliminarTareaNoexistente.png "Tarea eliminada con id no existente")

---


### 2.3. Dar alta a cualquier tarea con rol de administrador para un usuario

- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario autenticado con rol de administrador puede dar de alta una tarea para cualquier usuario.
- **Código de respuesta esperado:**

    - **201 CREATED** - La tarea se ha creado correctamente.
    - **400 BAD REQUEST** - La petición no es válida.
    - **401 UNAUTHORIZED** - El usuario no está autenticado.
    - **403 FORBIDDEN** - El usuario no tiene permisos para crear la tarea.
    - **404 NOT FOUND** - El usuario no existe.


Para este caso de prueba se ha utilizado un usuario con rol de administrador(lbaeutr) y se ha creado una tarea para el usuario (diego).


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
	  "_id": "3",
	  "titulo": "Tarea para diego",
	  "descripcion": "Descripción de la tarea",
	  "estado": false,
	  "usuarioId": "diego"
    }
    ```

- **Captura de pantalla:**
  - **Código de respuesta esperado:** `201 Created`

![Tarea creada admin](../../screenshot/task/admin/011_DarAltaUserSiendoAdmin.png "Tarea creada admin")

---

#### Dar alta a cualquier tarea sin autenticación

- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario no autenticado no puede dar de alta una tarea para cualquier usuario.


- **Captura de pantalla:**
  - **Código de respuesta esperado:** `401 Unauthorized`

![Tarea creada admin no aut](../../screenshot/task/admin/012_DarAltaAdminNoAuth.png "Tarea creada admin no aut")

---


#### Dar alta a cualquier tarea con rol USER

Para este ejemplo se ha utilizado un usuario con rol de usuario (diego) y creado una tarea para el usuario (lbaeutr).


- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario con rol de usuario no puede dar de alta una tarea para cualquier usuario.
- **Captura de pantalla:**
  - **Código de respuesta esperado:** `403 Forbidden`

![Tarea creada con rol USER](../../screenshot/task/admin/013_DarAltaAdminSiendoUser.png "Tarea creada con rol USER")

---


#### Dar alta a cualquier tarea con datos vacíos

- **Endpoint:** `POST /tareas/admin?usuarioId={usuarioId}`
- **Descripción:** Un usuario con rol de administrador no puede dar de alta una tarea con datos vacíos, ya sea con un solo campo o con ambos.

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

![Tarea creada con campos vacíos](../../screenshot/task/admin/014_DarAltaAdminCamposVacios.png "Tarea creada con campos vacíos")

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



![Tarea creada con usuario no existente](../../screenshot/task/admin/015_DarAltaAdminUserNoExiste.png "Tarea creada con usuario no existente")

---



## Documentación extra

### **Vídeo funcionamiento interfaz**



<a href="https://www.canva.com/design/DAGgN8_A-Uc/E1m-NTSSqsAjB3VeGuzeLA/watch?utm_content=DAGgN8_A-Uc&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h100348045b"><img src="../../screenshot/icon/icono_play.png" alt="icono" width="200" height="200"></a>

