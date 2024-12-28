# UrlShortener

## Proyecto Spring Boot diseñado para acortar URLs. 

Consta de una API REST más una BD Redis que permite:

- Acortar URLs
- Redirigir desde URL cortas a su direción destino
- Habilitar y deshabilitar URLs
- Editar componentes de URL largas
- Obtener estadísticas de acceso a URLs cortas (de momento solo cantidad de accesos)


### Siendo que en términos de rendimiento se busca contar con: 

- Una alta disponibilidad con picos de tráfico de hasta 1 M RPM con un tiempo de actividad de hasta el 99,98%
- Una resolución de URLs con baja latencia
- Estadísticas en tiempo casi real

  se optó por utilizar **Redis** por tratarse de una base de datos que opera en memoria y que por ende ofrece  tiemṕos de respuesta en el reango de milisegundos. Por otra parte Redis es muy eficiente al momento de almacenar pares de clave-valor (los mapeos entre *shortURL* y *longURL*). A su vez el soporte de estadísticas en tiempo real es una caracteristica de la base de datos elegida.

  Se descartó el uso de una BD relacional por ofrecer un rendimiento inferior en altas cargas de datos y por ser tecnologías convenientes para el manejo de relaciones complejas entre tablas, algo innecesario en el contexto del proyecto.


  ## Componentes que integran la solución

- API REST que acorta, redirige, administra y obtiene estadísticas de las URLS. Se optó por una API por ofrecer una interfaz sencilla para que clientes externos interactúen con el sistema.
- Base de Datos Redis que almacena pares clave-valor, sirve datos para resolución rápida de URLs y gestiona estadísticas en tiempo real.

  ![diagrama](https://github.com/user-attachments/assets/6d4125f4-6f02-4b5a-8059-45d0dd6bb8d4)


  ### Como probar la aplicación

  1) Descargate el proyecto
  2) El proyecto usa Java 17, Srping Boot 3.4.2 y Gradle 8.11.1
  3) Parate en la carpeta raíz del proyecto y ejecutá ./gradlew bootRun
  4) Con la aplicación andando, ingresá a http://localhost:8080/swagger-ui/index.html para probar los distintos endpoints (si querés ejecutarlos desde otra terminal con CURL más abajo te dejo los comandos)
  6) Por último, podés probar las métricas del proyecto parándote en /urlshortener/src/load-tests y corriendo el comando k6 run load-test.js


### Comandos cURL para probar la API

- Acortar URL:
```
curl -X 'POST' \
  'http://localhost:8080/shorten' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "longUrl": "https://manodemandioca.blogspot.com/2013/09/chacarera-para-jimi-hendrix.html"
}'
```

- Redireccionar de una URL corta a una larga:
```
curl -X 'GET' \
  'http://localhost:8080/redirect/39d98096' \
  -H 'accept: */*'
```

- Estadísticas (cantidad de accesos a una url corta)
```
curl -X 'GET' \
  'http://localhost:8080/stats/39d98096' \
  -H 'accept: */*'
```

- Habilitar o deshbilitar una url:
```
curl -X 'PUT' \
  'http://localhost:8080/enable/39d98096?enable=false' \
  -H 'accept: */*'
```

- Actualizar una URL larga:
```
curl -X 'PUT' \
  'http://localhost:8080/update/39d98096' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "longUrl": "https://manodemandioca.blogspot.com/"
}'
```
