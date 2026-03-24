-- 1️⃣ Create DB owned by postgres
CREATE DATABASE lumina_book OWNER postgres ENCODING 'UTF8' LC_COLLATE 'en_US.UTF-8' LC_CTYPE 'en_US.UTF-8' TEMPLATE template0;

-- 2️⃣ Create a dedicated login role (user) with explicit privileges
CREATE ROLE lumina_book LOGIN PASSWORD '123' 
   NOSUPERUSER NOCREATEROLE NOCREATEDB NOREPLICATION INHERIT CONNECTION LIMIT 100;

-- ALTER USER lumina_book WITH PASSWORD 'NEW_STRONG_PASSWORD';

-- ensures consistent locale setup
-- 3️⃣ Restrict default public access to the DB
REVOKE CONNECT ON DATABASE lumina_book FROM PUBLIC;
GRANT CONNECT ON DATABASE lumina_book TO lumina_book;
GRANT CONNECT ON DATABASE lumina_book TO postgres;

-- 4) Optional hardening (careful: may affect tooling)
REVOKE ALL ON DATABASE postgres FROM PUBLIC;