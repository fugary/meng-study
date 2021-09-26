CREATE TABLE t_simple_sub_transaction
(
    sub_tx_id     varchar(32) NOT NULL,
    tx_id         varchar(32) NOT NULL,
    tx_key        varchar(1024),
    tx_name       varchar(1024),
    idx_no        int(10),
    cancel_method varchar(255),
    start_date    timestamp NULL,
    end_date      timestamp NULL,
    cancel_date   timestamp NULL,
    status        int(10),
    message       varchar(1024),
    service_name  varchar(1024),
    service_class varchar(1024),
    param_clazz   varchar(81920),
    param_data    varchar(81920),
    PRIMARY KEY (sub_tx_id)
);
CREATE TABLE t_simple_transaction
(
    tx_id       varchar(32) NOT NULL,
    tx_key      varchar(1024),
    tx_name     varchar(1024),
    start_date  timestamp NULL,
    end_date    timestamp NULL,
    cancel_date timestamp NULL,
    status      int(10),
    message     varchar(1024),
    tx_time     bigint(20),
    lock_status int(10),
    lock_date   timestamp NULL,
    retry_times int(10),
    PRIMARY KEY (tx_id)
);
CREATE TABLE t_simple_transaction_log
(
    id                  bigint(20) NOT NULL,
    tx_id               varchar(32),
    tx_key              varchar(1024),
    tx_name             varchar(1024),
    start_date          timestamp NULL,
    end_date            timestamp NULL,
    cancel_date         timestamp NULL,
    status              int(10),
    message             varchar(1024),
    tx_time             bigint(20),
    lock_status         int(10),
    lock_date           timestamp NULL,
    retry_times         int(10),
    sub_transaction_log varchar(81920),
    PRIMARY KEY (id)
);
ALTER TABLE t_simple_sub_transaction
    ADD CONSTRAINT fk_simple_sub_transaction FOREIGN KEY (tx_id) REFERENCES t_simple_transaction (tx_id);
