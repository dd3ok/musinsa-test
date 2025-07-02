DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS brand;

CREATE TABLE brand (
                       id BIGINT AUTO_INCREMENT NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       CONSTRAINT pk_brand PRIMARY KEY (id)
);

ALTER TABLE brand ADD CONSTRAINT uc_brand_name UNIQUE (name);

CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT NOT NULL,
                         price DECIMAL(19, 2) NOT NULL,
                         category VARCHAR(255) NOT NULL,
                         brand_id BIGINT NOT NULL,
                         CONSTRAINT pk_product PRIMARY KEY (id)
);

ALTER TABLE product ADD CONSTRAINT FK_PRODUCT_ON_BRAND FOREIGN KEY (brand_id) REFERENCES brand (id);

CREATE INDEX idx_product_category ON product (category);
CREATE INDEX idx_product_brand_id ON product (brand_id);
