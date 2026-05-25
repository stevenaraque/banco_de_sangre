# 🩸 Blood Bank API

API REST para la gestión integral de un Banco de Sangre, desarrollada con **Spring Boot 3.5.14** y **Java 21**. Permite registrar donantes, controlar donaciones, administrar el inventario de sangre y validar requisitos médicos mediante un sistema de validaciones extensible basado en principios SOLID.

---

## 📋 Características Principales

| Módulo | Descripción |
|--------|-------------|
| **Donantes** | CRUD completo con paginación, filtros por tipo de sangre y subida de firmas digitales |
| **Donaciones** | Registro con código único (`DON-XXXXXXXX`), historial y exportación a PDF |
| **Inventario** | Control automático de unidades y mililitros por tipo de sangre (8 grupos) |
| **Validaciones Médicas** | 5 reglas SOLID: edad, peso, tiempo entre donaciones, consentimiento y firma |
| **Consentimientos** | Endpoint independiente para gestión de firmas y aceptación informada |
| **Documentación** | Swagger/OpenAPI 3.1.0 integrado |

---

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.5.14** (Web, Data JPA, Validation)
- **MySQL 8.0** + **Hibernate 6**
- **Lombok 1.18.38**
- **MapStruct 1.6.0**
- **OpenPDF 2.0.2** (generación de PDFs)
- **Swagger/OpenAPI 3.1.0** (springdoc)
- **JUnit 5 + Mockito** (tests unitarios)

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura por capas:
src/main/java/blood/bank/api/
├── config/           # Configuraciones y DataLoader
├── controller/       # Controladores REST
├── domain/entity/    # Entidades JPA
├── dto/              # Request y Response (DTOs)
├── enums/            # Tipos de sangre y catálogos
├── exception/        # Excepciones personalizadas y @RestControllerAdvice
├── mapper/           # MapStruct (Entity ↔ DTO)
├── repository/       # Spring Data JPA
├── service/          # Interfaces de servicio
│   └── impl/         # Implementaciones
├── util/             # Utilidades (FileStorage, etc.)
│   └── validacion/   # Validaciones médicas (SOLID)
└── BloodBankApplication.java
plain
Copy

---

## 🚀 Instalación y Configuración

### 1. Requisitos previos
- Java 21
- Maven 3.9+
- MySQL 8.0+

### 2. Base de datos
Crear la base de datos en MySQL:
```sql
CREATE DATABASE blood_bank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
3. Configurar conexión
Editar src/main/resources/application.properties:
properties
Copy
spring.datasource.url=jdbc:mysql://localhost:3306/blood_bank_db?createDatabaseIfNotExist=true
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
4. Compilar y ejecutar
bash
mvn clean install
mvn spring-boot:run
La aplicación se ejecuta en: http://localhost:8080
📖 Documentación API
Una vez ejecutando, accede a Swagger UI:
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
Endpoints principales
Table
Método	Endpoint	Descripción
| Método   | Endpoint                                    | Descripción                                 |
| -------- | ------------------------------------------- | ------------------------------------------- |
| `POST`   | `/api/donantes`                             | Registrar donante                           |
| `GET`    | `/api/donantes`                             | Listar paginado (filtro por tipo de sangre) |
| `GET`    | `/api/donantes/todos`                       | Listar todos                                |
| `GET`    | `/api/donantes/{id}`                        | Buscar por ID                               |
| `PUT`    | `/api/donantes/{id}`                        | Actualizar donante                          |
| `DELETE` | `/api/donantes/{id}`                        | Eliminar donante                            |
| `POST`   | `/api/donantes/{id}/firma`                  | Subir firma como imagen (multipart)         |
| `POST`   | `/api/donaciones`                           | Registrar donación                          |
| `GET`    | `/api/donaciones`                           | Listar donaciones                           |
| `GET`    | `/api/donaciones/{id}`                      | Buscar donación por ID                      |
| `GET`    | `/api/donaciones/codigo/{codigo}`           | Buscar por código único                     |
| `GET`    | `/api/donaciones/historial/{donanteId}/pdf` | Descargar historial en PDF                  |
| `GET`    | `/api/inventario`                           | Consultar inventario completo               |
| `GET`    | `/api/inventario/{tipoSangre}`              | Consultar por tipo de sangre                |
| `POST`   | `/api/consentimientos`                      | Crear consentimiento                        |
| `GET`    | `/api/consentimientos/{donanteId}`          | Consultar consentimiento                    |

🏥 Validaciones Médicas (Principio SOLID)
Las validaciones siguen el principio Open/Closed: puedes agregar nuevas reglas sin modificar el código existente.
Table
| # | Validación              | Clase                      | Regla                         |
| - | ----------------------- | -------------------------- | ----------------------------- |
| 1 | Edad mínima             | `ValidacionEdadDonante`    | Mayor de 18 años              |
| 2 | Peso mínimo             | `ValidacionPesoDonante`    | Mínimo 50 kg                  |
| 3 | Tiempo entre donaciones | `ValidacionTiempoDonacion` | Mínimo 3 meses                |
| 4 | Consentimiento aceptado | `ValidacionConsentimiento` | `aceptaConsentimiento = true` |
| 5 | Firma registrada        | `ValidacionFirma`          | Firma válida y existente      |

Flujo de la firma (3 niveles de validación)
La firma puede registrarse en 3 formatos según el contexto del taller:
Table
| Formato        | Ejemplo                            | Validación                           |
| -------------- | ---------------------------------- | ------------------------------------ |
| **Base64**     | `data:image/png;base64,iVBORw0...` | Estructura y decodificación correcta |
| **URL**        | `https://servidor.com/firma.png`   | Prefijo `http://` o `https://`       |
| **Ruta local** | `uploads/firmas/firma_1_...jpeg`   | Archivo existe físicamente en disco  |

Flujo real:
Se registra el donante (la firma puede ser null inicialmente)
Se sube la firma mediante POST /api/donantes/{id}/firma (multipart)
Al momento de donar, el sistema valida que la firma exista y sea válida
🧪 Tests Unitarios
12 tests implementados con Mockito, todos pasando:
Table

| Clase de Test                  | Tests | Cobertura                |
| ------------------------------ | ----- | ------------------------ |
| `DonanteServiceImplTest`       | 5     | CRUD y reglas de negocio |
| `ValidacionEdadDonanteTest`    | 2     | Límite de edad           |
| `ValidacionPesoDonanteTest`    | 2     | Límite de peso           |
| `ValidacionConsentimientoTest` | 2     | Consentimiento informado |
| `BloodBankApplicationTests`    | 1     | Contexto de Spring       |


Ejecutar tests:

bash

mvn test

📁 Estructura de Carpetas
blood-bank/
├── src/
│   ├── main/
│   │   ├── java/blood/bank/api/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── domain/entity/
│   │   │   ├── dto/request/
│   │   │   ├── dto/response/
│   │   │   ├── enums/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   │   └── impl/
│   │   │   └── util/
│   │   │       └── validacion/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/blood/bank/api/
├── uploads/firmas/          # Almacenamiento de firmas subidas
├── pom.xml
└── README.md
👤 Autor
Steven Alejandro Araque Castro
