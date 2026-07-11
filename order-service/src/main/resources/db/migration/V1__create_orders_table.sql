CREATE TABLE orders
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(64)    NOT NULL,
    product     VARCHAR(255)   NOT NULL,
    quantity    INTEGER        NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    status      VARCHAR(32)    NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL,
    updated_at  TIMESTAMPTZ    NOT NULL
);

-- FULL replica identity so Debezium emits the complete "before" image on UPDATE/DELETE
ALTER TABLE orders REPLICA IDENTITY FULL;
