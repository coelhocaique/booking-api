CREATE TABLE property (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    guest_info VARCHAR(255) NOT NULL,
    CONSTRAINT fk_property_booking FOREIGN KEY (property_id) REFERENCES property(id)
);

CREATE TABLE property_block (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255),
    CONSTRAINT fk_property_property_block FOREIGN KEY (property_id) REFERENCES property(id)
);

