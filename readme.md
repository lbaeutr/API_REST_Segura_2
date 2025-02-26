# API_REST_Segura_2 - Gestión de Tareas del Hogar

## ¿De qué va este proyecto?
Esta API REST permite gestionar tareas del hogar de manera segura. Los usuarios pueden registrarse, iniciar sesión y manejar sus propias tareas. Además, hay un rol de administrador que tiene más permisos para gestionar las tareas de todos.

## ¿Cómo organizamos los datos?
Estamos usando **MongoDB** para almacenar la información, con las siguientes colecciones:

### Usuarios (`Usuario`)
Cada usuario tiene sus datos de acceso y su dirección almacenada dentro de su propio documento.  
- `_id` ➝ Identificador único del usuario.  
- `username` ➝ Nombre de usuario único.  
- `password` ➝ Contraseña cifrada para mayor seguridad.  
- `email` ➝ Correo electrónico único.  
- `roles` ➝ Rol del usuario (`USER` por defecto, puede ser `ADMIN`).  
- `direccion` ➝ Objeto embebido con la dirección del usuario:  
  - `calle` ➝ Nombre de la calle.  
  - `num` ➝ Número de la vivienda.  
  - `municipio` ➝ Municipio de residencia.  
  - `provincia` ➝ Provincia donde vive el usuario.  
  - `cp` ➝ Código postal.  

### Tareas (`Tarea`)
Cada usuario tiene su propia lista de tareas almacenadas en esta colección.  
- `_id` ➝ Identificador único de la tarea.  
- `titulo` ➝ Nombre de la tarea.  
- `descripcion` ➝ Un poco más de info sobre la tarea.  
- `estado` ➝ Indica si la tarea está completada (`true` o `false`).  
- `usuarioId` ➝ ID del usuario dueño de la tarea.  


---

## **Endpoints de la API**
###  Autenticación  
- `POST /usuarios/register` ➝ Registrar usuario.  
- `POST /usuarios/login` ➝ Iniciar sesión y obtener token.  

### Usuarios  
- `GET /usuarios/{id}` ➝ Ver un usuario (solo él o ADMIN).  
- `PUT /usuarios/{id}` ➝ Editar usuario (solo él o ADMIN).  
- `DELETE /usuarios/{id}` ➝ Borrar usuario.  

### Tareas  
- `GET /tareas/mis-tareas` ➝ Ver mis tareas (solo USER).  
- `POST /tareas` ➝ Crear una tarea.  
- `PUT /tareas/{id}` ➝ Editar tarea (solo USER).  
- `DELETE /tareas/{id}` ➝ Borrar tarea (solo USER).  
- `GET /tareas/all` ➝ Ver mis tareas (solo ADMIN).
- `DELETE /tareas/admin/{id}` ➝ Borrar tarea (solo ADMIN).
- `POST /tareas/admin?usuarioId={usuarioId}` ➝ Crear una tarea (solo ADMIN).




---

## **Reglas de la API**
- Los **usuarios normales** solo ven y manejan sus tareas.  
- Los **ADMIN** pueden ver, editar y eliminar cualquier usuario o tarea.  
- La API usa **Spring Security y JWT** para proteger los datos.  

---


## **Errores y respuestas**
- `200` → Todo bien.  
- `201` → Creado con éxito.  
- `400` → Datos incorrectos.  
- `401` → No autorizado .  
- `403` → No tienes permisos.  
- `404` → No encontrado.  
- `409` → Conflicto (EJ:usuario existente en BBDD).
- `500` → Error del servidor.  

---

# **Documentación:**

* ## **[Documentación - PARTE II: Gestión de Usuarios](src/main/resources/documentation/part_II.md)**

* ## **[Documentación - PARTE III: Gestión de Tareas](src/main/resources/documentation/part_III.md)**


