-- 5️⃣ Lock down public schema from PUBLIC
REVOKE ALL ON SCHEMA public FROM PUBLIC;

-- 6️⃣ Ensure schema owner is lumina_book (should already be, but enforce)
ALTER SCHEMA public OWNER TO lumina_book;

-- Remove CREATE permission so the app cannot change tables, only data
REVOKE CREATE ON SCHEMA public FROM lumina_book;
GRANT USAGE ON SCHEMA public TO lumina_book;

-- Explicitly grant DML permissions
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO lumina_book;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO lumina_book;

-- 8️⃣ Default privileges for future objects created by lumina_book
ALTER DEFAULT PRIVILEGES IN SCHEMA public REVOKE ALL ON TABLES FROM PUBLIC;
ALTER DEFAULT PRIVILEGES IN SCHEMA public REVOKE ALL ON SEQUENCES FROM PUBLIC;
ALTER DEFAULT PRIVILEGES IN SCHEMA public REVOKE ALL ON FUNCTIONS FROM PUBLIC;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO lumina_book;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO lumina_book;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO lumina_book;