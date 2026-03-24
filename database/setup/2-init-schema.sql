BEGIN;


CREATE TABLE public.accounts (
	id serial4 NOT NULL,
	title varchar NOT NULL,
	email varchar NOT NULL,
	created timestamp(6) NOT NULL,
	"firstName" varchar NULL,
	"lastName" varchar NULL,
	CONSTRAINT accounts_pk PRIMARY KEY (id)
);


COMMIT;