CREATE TABLE "person" (
  "id" bigserial PRIMARY KEY,
  "tg_user_id" bigserial,
  "first_name" varchar,
  "last_name" varchar,
  "user_name" varchar,
  "is_bot" boolean,
  "is_article" boolean,
  "is_delete_article" boolean
);

CREATE TABLE "message" (
  "id" bigserial PRIMARY KEY,
  "message_text" varchar,
  "date" bigserial,
  "person_id" bigserial,
  "request_details_id" bigserial
);

CREATE TABLE "product" (
  "id" bigserial PRIMARY KEY,
  "article" varchar UNIQUE,
  "product_name" varchar,
  "category" varchar,
  "price" double precision
);

CREATE TABLE "product_person" (
  "person_id" bigserial,
  "product_id" bigserial
);

CREATE TABLE "request_details" (
  "id" bigserial PRIMARY KEY,
  "start_price" double precision,
  "product_id" bigserial,
  "expected_price" double precision,
  "current_price" double precision
);

ALTER TABLE "product_person" ADD FOREIGN KEY ("person_id") REFERENCES "person" ("id");

ALTER TABLE "product_person" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "message" ADD FOREIGN KEY ("person_id") REFERENCES "person" ("id");

ALTER TABLE "message" ADD FOREIGN KEY ("request_details_id") REFERENCES "request_details" ("id");
