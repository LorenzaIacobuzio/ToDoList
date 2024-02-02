create table activities (
    "id" uuid not null,
    "userId" uuid not null,
    "title" varchar(255) not null,
    "activityGroup" varchar(255) null,
    "dueDate" timestamp not null,
    "priority" varchar(255) default 'MEDIUM',
    "description" varchar(255) null,
    "rescheduledToDate" timestamp null,
    "frequency" varchar(255) not null
);

create table users (
    "userId" uuid not null,
    "username" varchar(255) not null,
    "password" varchar(255) not null
);