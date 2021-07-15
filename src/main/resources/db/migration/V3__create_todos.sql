create table todos
(
    id          bigint auto_increment,
    description varchar(255),
    create_time timestamp ,
    update_time timestamp ,
    is_done bit(1),
    category_id bigint,
    primary key (id),
    foreign key (category_id) references categories (id)
);