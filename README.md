# Arquitectura del mini API-Rest del sistema de gestión talleresAgrim

### Tecnologías usadas
* Java
* Maven
* IntelliJ
* GitHub

## Diseño de entidades
![themes-entities-class-diagram](https://user-images.githubusercontent.com/16058725/48664196-4ac42880-ea9b-11e8-860c-1c8610d95c49.png)

## Arquitectura
![themes-architecture-diagram]()

## API
### POST /clients
#### Parámetros del cuerpo
- `nombreCompleto`: String (**requerido**)
- `horasNevera`: int
#### Respuesta
- 200 OK 
  - `id`: String
- 403 BAD_REQUEST
---
### PUT /clients/{id}
#### Parámetros del cuerpo
- `nombreCompleto`: String (**requerido**)
- `horasNevera`: int
#### Respuesta
- 200 OK 
- 403 BAD_REQUEST
- 404 NOT_FOUND
--- 

##### Autor: Ilya Kabushko.
