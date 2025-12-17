-- ================================
-- Base de datos: subastas_rmi
-- Sistema de Subastas RMI
-- ================================

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE productos (
    id_producto SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio_inicial NUMERIC(10,2) NOT NULL,
    puja_actual NUMERIC(10,2) NOT NULL,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVA'
);

CREATE TABLE bids (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    amount NUMERIC(10,2) NOT NULL CHECK (amount > 0),
    bid_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bid_product
        FOREIGN KEY (product_id)
        REFERENCES productos(id_producto),

    CONSTRAINT fk_bid_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
