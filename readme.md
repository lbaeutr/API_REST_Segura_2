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
- `POST /auth/register` ➝ Registrar usuario.  
- `POST /auth/login` ➝ Iniciar sesión y obtener token.  

### Usuarios  
- `GET /usuarios/{id}` ➝ Ver un usuario (solo él o ADMIN).  
- `PUT /usuarios/{id}` ➝ Editar usuario (solo él o ADMIN).  
- `DELETE /usuarios/{id}` ➝ Borrar usuario (solo ADMIN).  

### Tareas  
- `GET /tareas` ➝ Ver todas las tareas (solo ADMIN).  
- `GET /tareas/mis-tareas` ➝ Ver mis tareas.  
- `POST /tareas` ➝ Crear una tarea.  
- `PUT /tareas/{id}` ➝ Editar tarea (solo dueño o ADMIN).  
- `DELETE /tareas/{id}` ➝ Borrar tarea (solo dueño o ADMIN).  

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
- `401` → No autorizado (token inválido).  
- `403` → No tienes permisos.  
- `404` → No encontrado.  
- `500` → Error del servidor.  

---
