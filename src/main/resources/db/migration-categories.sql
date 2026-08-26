-- Ejecutar manualmente en PostgreSQL (ddl-auto=none)

CREATE TABLE IF NOT EXISTS categories (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

ALTER TABLE products ADD COLUMN IF NOT EXISTS category_id BIGINT;

-- Migrar datos existentes (si products.category aún existe como texto)
-- INSERT INTO categories (name)
-- SELECT DISTINCT category FROM products WHERE category IS NOT NULL
-- ON CONFLICT (name) DO NOTHING;
--
-- UPDATE products p
-- SET category_id = c.id
-- FROM categories c
-- WHERE p.category = c.name;

ALTER TABLE products DROP COLUMN IF EXISTS category;

ALTER TABLE products
    ALTER COLUMN category_id SET NOT NULL;

ALTER TABLE products
    ADD CONSTRAINT fk_products_category
    FOREIGN KEY (category_id) REFERENCES categories (id);
