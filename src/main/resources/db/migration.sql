-- Esquema de Protec. Ejecutar con: --migrate

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category_id BIGINT,
    price NUMERIC(12, 2) NOT NULL,
    specs VARCHAR(2000),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS quotations (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(200) NOT NULL,
    customer_phone VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS quotation_items (
    id BIGSERIAL PRIMARY KEY,
    quotation_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL
);

ALTER TABLE products ADD COLUMN IF NOT EXISTS category_id BIGINT;
ALTER TABLE products DROP COLUMN IF EXISTS category;

ALTER TABLE products DROP CONSTRAINT IF EXISTS fk_products_category;
ALTER TABLE products ADD CONSTRAINT fk_products_category
    FOREIGN KEY (category_id) REFERENCES categories (id);
ALTER TABLE products ALTER COLUMN category_id SET NOT NULL;

ALTER TABLE quotation_items DROP CONSTRAINT IF EXISTS fk_quotation_items_quotation;
ALTER TABLE quotation_items ADD CONSTRAINT fk_quotation_items_quotation
    FOREIGN KEY (quotation_id) REFERENCES quotations (id) ON DELETE CASCADE;

ALTER TABLE quotation_items DROP CONSTRAINT IF EXISTS fk_quotation_items_product;
ALTER TABLE quotation_items ADD CONSTRAINT fk_quotation_items_product
    FOREIGN KEY (product_id) REFERENCES products (id);
