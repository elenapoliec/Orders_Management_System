--
-- PostgreSQL database dump
--

\restrict nANdF8GiWJqs8cN8ImfCiXuFkUqI6Hj0anZXWgt2gCWzt0XO4lfGvRHNcyZbjNR

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    address character varying(255),
    email character varying(150)
);


ALTER TABLE public.client OWNER TO postgres;

--
-- Name: client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.client_id_seq OWNER TO postgres;

--
-- Name: client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_id_seq OWNED BY public.client.id;


--
-- Name: log_bill; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log_bill (
    id integer NOT NULL,
    orderid integer NOT NULL,
    clientname character varying(150) NOT NULL,
    productname character varying(150) NOT NULL,
    quantity integer NOT NULL,
    totalprice numeric(10,2) NOT NULL
);


ALTER TABLE public.log_bill OWNER TO postgres;

--
-- Name: log_bill_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.log_bill_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.log_bill_id_seq OWNER TO postgres;

--
-- Name: log_bill_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.log_bill_id_seq OWNED BY public.log_bill.id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id integer NOT NULL,
    clientid integer NOT NULL,
    productid integer NOT NULL,
    quantity integer NOT NULL
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_id_seq OWNER TO postgres;

--
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    stock integer NOT NULL,
    price numeric(10,2) NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_id_seq OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- Name: client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client ALTER COLUMN id SET DEFAULT nextval('public.client_id_seq'::regclass);


--
-- Name: log_bill id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_bill ALTER COLUMN id SET DEFAULT nextval('public.log_bill_id_seq'::regclass);


--
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client (id, name, address, email) FROM stdin;
1	Popescu Ion	Str. Florilor 15, Cluj-Napoca	ion.popescu@email.com
2	Ionescu Maria	Bld. Eroilor 2, Turda	maria.ionescu@email.com
3	Andrei Vasile	Str. Avram Iancu 45, Floresti	andrei.vasile@yahoo.com
6	Georgescu Ana	Str. Fabricii 22, Gherla	ana.georgescu@outlook.com
8	Brana Daniel	Str. Observatorului, 17	david.darius@gmail.com
9	Crasnean Aurel	Str. Primaverii, 17	Aurelc@gmail.com
10	Pop Davidd	Str. Vesela, 27	d.pop@gmail.com
5	Radu Mihnea	Aleea Florii, 34	mihnea.radu@gmail.com
\.


--
-- Data for Name: log_bill; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.log_bill (id, orderid, clientname, productname, quantity, totalprice) FROM stdin;
1	1	Popescu Ion	Laptop ASUS Rog	1	4500.00
2	2	Ionescu Maria	Mouse Wireless Logitech	2	301.00
3	3	Andrei Vasile	Monitor Gaming Dell 27	1	1250.99
4	4	Andrei Vasile	Tastatură Mecanică HyperX	1	320.00
5	5	Radu Mihalcea	Webcam Logitech Brio	1	750.00
6	6	Georgescu Ana	Căști Audio Sony WH-1000	1	1100.00
7	7	Popescu Ion	Mouse Wireless Logitech	2	301.00
8	8	Andrei Vasile	Căști Audio Sony WH-1000	3	3300.00
9	9	Georgescu Ana	Mouse gaming	5	339.45
10	10	Pop Davidd	Monitor Gaming Dell 27	3	3752.97
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (id, clientid, productid, quantity) FROM stdin;
1	1	1	1
3	3	4	1
4	3	3	1
5	5	6	1
6	6	5	1
8	3	5	3
9	6	10	5
10	10	4	3
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, name, stock, price) FROM stdin;
3	Tastatură Mecanică HyperX	25	320.00
6	Webcam Logitech Brio	2	750.00
9	Laptop 	19	3450.00
5	Căști Audio Sony WH-1000	0	1100.00
10	Mouse gaming	41	67.89
1	Laptop ASUS	20	4999.99
4	Monitor Gaming Dell 27	12	1250.99
\.


--
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.client_id_seq', 11, true);


--
-- Name: log_bill_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.log_bill_id_seq', 10, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 10, true);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_id_seq', 11, true);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: log_bill log_bill_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_bill
    ADD CONSTRAINT log_bill_pkey PRIMARY KEY (id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: orders fk_client; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_client FOREIGN KEY (clientid) REFERENCES public.client(id) ON DELETE CASCADE;


--
-- Name: orders fk_product; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_product FOREIGN KEY (productid) REFERENCES public.product(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict nANdF8GiWJqs8cN8ImfCiXuFkUqI6Hj0anZXWgt2gCWzt0XO4lfGvRHNcyZbjNR

