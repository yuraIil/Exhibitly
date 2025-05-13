


# Exhibitly — каталогізатор музейних експонатів

Мета проєкту — створити JavaFX-застосунок для керування музейною колекцією з можливістю перегляду, додавання, редагування та видалення експонатів.

---

## Основні вимоги

- Java 17+
- JavaFX UI (FXML, CSS)
- Тришарова архітектура:
  - **Presentation Layer** — JavaFX форми (`form`, `windows`)
  - **Business Layer** — сервіси (репозиторії/логіка)
  - **Data Access Layer** — DAO через JDBC
- Підтримка PostgreSQL / MySQL / SQLite
- Мінімум 5 сутностей:
  - `Exhibit`
  - `Category`
  - `Multimedia`
  - `Users`
  - `Report`

---

## Початок роботи

### 1. Клонування репозиторію

```bash
git clone git@github.com:yuraIil/exhibit-catalog.git
cd exhibit-catalog
````

### 2. Конфігурація бази даних

Файл `src/main/resources/application.properties`:

```
db.url=jdbc:postgresql://localhost:5432/exhibitly
db.username=postgres
db.password=yourpassword
db.pool.size=5
```


---

## Збірка та запуск

### Maven

```bash
mvn clean install
   mvn javafx:run
```



---

## Структура проєкту

```
src/
├── main/
│   ├── java/
│   │   └── com/yuralil/
│   │       ├── application/
│   │       │   ├── form/             # JavaFX UI форми
│   │       │   └── windows/          # Вікна (логін, меню, інтро)
│   │       ├── components/           # Компоненти (ConfirmDialog)
│   │       ├── domain/
│   │       │   ├── dao/              # DAO-класи
│   │       │   └── entities/         # Сутності: Exhibit, Category, Users тощо
│   │       └── infrastructure/
│   │           ├── util/             # ConnectionPool, Holder, Init
│   │           └── repository/       # (за потреби — додаткові сервіси)
│   └── resources/
│       ├── db/init.sql               # SQL-структура БД
│       ├── fxml/                     # FXML-файли для JavaFX
│       ├── style/                    # CSS стилі
│       └── application.properties    # Налаштування БД
```

---



## Автор

**YuraIil**
GitHub: [github.com/yuraIil/exhibit-catalog](https://github.com/yuraIil/exhibit-catalog)

