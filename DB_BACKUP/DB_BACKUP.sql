PGDMP         ,                x            lll    13beta3    13beta3 J    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16394    lll    DATABASE     g   CREATE DATABASE lll WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';
    DROP DATABASE lll;
                postgres    false            �            1259    16734    admin    TABLE     �  CREATE TABLE public.admin (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    admin_code character varying(255) NOT NULL,
    create_by character varying(255) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    date_of_birth timestamp without time zone NOT NULL,
    email character varying(255) NOT NULL,
    fullname character varying(255) NOT NULL,
    gender smallint NOT NULL,
    identity_document character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    password character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    delete_status smallint NOT NULL
);
    DROP TABLE public.admin;
       public         heap    postgres    false            �            1259    16742    author    TABLE     �  CREATE TABLE public.author (
    id integer NOT NULL,
    author_code character varying(255) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    description character varying(255),
    modified_date timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    delete_status smallint NOT NULL
);
    DROP TABLE public.author;
       public         heap    postgres    false            �            1259    16750    author_book    TABLE     �   CREATE TABLE public.author_book (
    id integer NOT NULL,
    author_code character varying(255) NOT NULL,
    isbn integer NOT NULL
);
    DROP TABLE public.author_book;
       public         heap    postgres    false            �            1259    16755    book    TABLE     �  CREATE TABLE public.book (
    id bigint NOT NULL,
    barcode character varying(255),
    create_date timestamp without time zone NOT NULL,
    description text NOT NULL,
    edition integer NOT NULL,
    isbn bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    page_number integer NOT NULL,
    price real NOT NULL,
    quantity integer NOT NULL,
    rate bigint NOT NULL,
    rate_count integer NOT NULL,
    title character varying(255) NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    publisher_code character varying(255) NOT NULL,
    delete_status smallint NOT NULL
);
    DROP TABLE public.book;
       public         heap    postgres    false            �            1259    16763    book_detail    TABLE     �  CREATE TABLE public.book_detail (
    id character varying(255) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    isbn integer NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    status_book_code character varying(255) NOT NULL,
    storage_code character varying(255),
    delete_status smallint NOT NULL
);
    DROP TABLE public.book_detail;
       public         heap    postgres    false            �            1259    16771    category    TABLE     �  CREATE TABLE public.category (
    id bigint NOT NULL,
    category character varying(255) NOT NULL,
    category_code character varying(255) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    delete_status smallint NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL
);
    DROP TABLE public.category;
       public         heap    postgres    false            �            1259    16779    category_book    TABLE     �   CREATE TABLE public.category_book (
    id bigint NOT NULL,
    isbn integer NOT NULL,
    category_code character varying(255) NOT NULL
);
 !   DROP TABLE public.category_book;
       public         heap    postgres    false            �            1259    16784    group    TABLE     �  CREATE TABLE public."group" (
    id bigint NOT NULL,
    create_date timestamp without time zone NOT NULL,
    description text,
    group_code character varying(255) NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    delete_status smallint NOT NULL
);
    DROP TABLE public."group";
       public         heap    postgres    false            �            1259    16792    group_admin    TABLE     �   CREATE TABLE public.group_admin (
    id bigint NOT NULL,
    admin_code character varying(255),
    group_code character varying(255)
);
    DROP TABLE public.group_admin;
       public         heap    postgres    false            �            1259    16800    group_permission    TABLE     �   CREATE TABLE public.group_permission (
    id bigint NOT NULL,
    group_code character varying(255) NOT NULL,
    permission_code character varying(255) NOT NULL
);
 $   DROP TABLE public.group_permission;
       public         heap    postgres    false            �            1259    16732    hibernate_sequence    SEQUENCE     {   CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.hibernate_sequence;
       public          postgres    false            �            1259    16808 
   permission    TABLE     
  CREATE TABLE public.permission (
    id bigint NOT NULL,
    api character varying(255) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    description character varying(255),
    method character varying(255) NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    permission_code character varying(255) NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    delete_status smallint NOT NULL
);
    DROP TABLE public.permission;
       public         heap    postgres    false            �            1259    16816    permission_admin    TABLE     �   CREATE TABLE public.permission_admin (
    id character varying(255) NOT NULL,
    admin_code character varying(255) NOT NULL,
    permission_code character varying(255) NOT NULL
);
 $   DROP TABLE public.permission_admin;
       public         heap    postgres    false            �            1259    16824 	   publisher    TABLE     �  CREATE TABLE public.publisher (
    id bigint NOT NULL,
    create_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    publisher_code character varying(255) NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    delete_status smallint NOT NULL
);
    DROP TABLE public.publisher;
       public         heap    postgres    false            �            1259    16832    reader    TABLE     -  CREATE TABLE public.reader (
    id integer NOT NULL,
    address character varying(255),
    create_date timestamp without time zone,
    date_of_birth timestamp without time zone,
    email character varying(255),
    fullname character varying(255),
    gender boolean,
    identity_document character varying(255),
    modified_date timestamp without time zone,
    password character varying(255),
    phone character varying(255),
    reader_code character varying(255),
    create_by character varying(255),
    modified_by character varying(255)
);
    DROP TABLE public.reader;
       public         heap    postgres    false            �            1259    16840    role    TABLE     c  CREATE TABLE public.role (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    description character varying(255),
    modified_date timestamp without time zone,
    name character varying(255),
    role_code character varying(255),
    create_by character varying(255),
    group_id bigint,
    modified_by character varying(255)
);
    DROP TABLE public.role;
       public         heap    postgres    false            �            1259    16848    status_book_detail    TABLE     �  CREATE TABLE public.status_book_detail (
    id bigint NOT NULL,
    create_date timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    status_book_code character varying(255) NOT NULL,
    status_book_message character varying(255) NOT NULL,
    create_by character varying(255) NOT NULL,
    modified_by character varying(255) NOT NULL,
    delete_status smallint NOT NULL
);
 &   DROP TABLE public.status_book_detail;
       public         heap    postgres    false            �            1259    16856    storage    TABLE     �  CREATE TABLE public.storage (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    description character varying(255),
    modified_date timestamp without time zone,
    parent_storage character varying(255),
    storage_code character varying(255),
    create_by character varying(255),
    modified_by character varying(255),
    storage_category_code character varying(255)
);
    DROP TABLE public.storage;
       public         heap    postgres    false            �            1259    16864    storage_category    TABLE     r  CREATE TABLE public.storage_category (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    description character varying(255),
    modified_date timestamp without time zone,
    storage_category character varying(255),
    storage_category_code character varying(255),
    create_by character varying(255),
    modified_by character varying(255)
);
 $   DROP TABLE public.storage_category;
       public         heap    postgres    false            �          0    16734    admin 
   TABLE DATA           �   COPY public.admin (id, address, admin_code, create_by, create_date, date_of_birth, email, fullname, gender, identity_document, modified_by, modified_date, password, phone, delete_status) FROM stdin;
    public          postgres    false    201   Uh       �          0    16742    author 
   TABLE DATA           �   COPY public.author (id, author_code, create_date, description, modified_date, name, create_by, modified_by, delete_status) FROM stdin;
    public          postgres    false    202   ej       �          0    16750    author_book 
   TABLE DATA           <   COPY public.author_book (id, author_code, isbn) FROM stdin;
    public          postgres    false    203   �j       �          0    16755    book 
   TABLE DATA           �   COPY public.book (id, barcode, create_date, description, edition, isbn, modified_date, page_number, price, quantity, rate, rate_count, title, create_by, modified_by, publisher_code, delete_status) FROM stdin;
    public          postgres    false    204   �j       �          0    16763    book_detail 
   TABLE DATA           �   COPY public.book_detail (id, create_date, description, modified_date, isbn, create_by, modified_by, status_book_code, storage_code, delete_status) FROM stdin;
    public          postgres    false    205   Lk       �          0    16771    category 
   TABLE DATA           �   COPY public.category (id, category, category_code, create_date, modified_date, delete_status, create_by, modified_by) FROM stdin;
    public          postgres    false    206   ik       �          0    16779    category_book 
   TABLE DATA           @   COPY public.category_book (id, isbn, category_code) FROM stdin;
    public          postgres    false    207   �k       �          0    16784    group 
   TABLE DATA           �   COPY public."group" (id, create_date, description, group_code, modified_date, name, create_by, modified_by, delete_status) FROM stdin;
    public          postgres    false    208   l       �          0    16792    group_admin 
   TABLE DATA           A   COPY public.group_admin (id, admin_code, group_code) FROM stdin;
    public          postgres    false    209   l       �          0    16800    group_permission 
   TABLE DATA           K   COPY public.group_permission (id, group_code, permission_code) FROM stdin;
    public          postgres    false    210   ;l       �          0    16808 
   permission 
   TABLE DATA           �   COPY public.permission (id, api, create_date, description, method, modified_date, name, permission_code, create_by, modified_by, delete_status) FROM stdin;
    public          postgres    false    211   Xl       �          0    16816    permission_admin 
   TABLE DATA           K   COPY public.permission_admin (id, admin_code, permission_code) FROM stdin;
    public          postgres    false    212   �l       �          0    16824 	   publisher 
   TABLE DATA           �   COPY public.publisher (id, create_date, modified_date, name, publisher_code, create_by, modified_by, delete_status) FROM stdin;
    public          postgres    false    213   �m       �          0    16832    reader 
   TABLE DATA           �   COPY public.reader (id, address, create_date, date_of_birth, email, fullname, gender, identity_document, modified_date, password, phone, reader_code, create_by, modified_by) FROM stdin;
    public          postgres    false    214   %n       �          0    16840    role 
   TABLE DATA           ~   COPY public.role (id, create_date, description, modified_date, name, role_code, create_by, group_id, modified_by) FROM stdin;
    public          postgres    false    215   Bn       �          0    16848    status_book_detail 
   TABLE DATA           �   COPY public.status_book_detail (id, create_date, description, modified_date, status_book_code, status_book_message, create_by, modified_by, delete_status) FROM stdin;
    public          postgres    false    216   _n       �          0    16856    storage 
   TABLE DATA           �   COPY public.storage (id, create_date, description, modified_date, parent_storage, storage_code, create_by, modified_by, storage_category_code) FROM stdin;
    public          postgres    false    217   |n       �          0    16864    storage_category 
   TABLE DATA           �   COPY public.storage_category (id, create_date, description, modified_date, storage_category, storage_category_code, create_by, modified_by) FROM stdin;
    public          postgres    false    218   �n       �           0    0    hibernate_sequence    SEQUENCE SET     A   SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);
          public          postgres    false    200            �
           2606    17121    admin admin_admin_code_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.admin
    ADD CONSTRAINT admin_admin_code_key UNIQUE (admin_code);
 D   ALTER TABLE ONLY public.admin DROP CONSTRAINT admin_admin_code_key;
       public            postgres    false    201            �
           2606    17123    admin admin_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.admin
    ADD CONSTRAINT admin_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.admin DROP CONSTRAINT admin_email_key;
       public            postgres    false    201            �
           2606    17125 !   admin admin_identity_document_key 
   CONSTRAINT     i   ALTER TABLE ONLY public.admin
    ADD CONSTRAINT admin_identity_document_key UNIQUE (identity_document);
 K   ALTER TABLE ONLY public.admin DROP CONSTRAINT admin_identity_document_key;
       public            postgres    false    201            �
           2606    17127    admin admin_phone_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.admin
    ADD CONSTRAINT admin_phone_key UNIQUE (phone);
 ?   ALTER TABLE ONLY public.admin DROP CONSTRAINT admin_phone_key;
       public            postgres    false    201            �
           2606    16741    admin admin_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.admin
    ADD CONSTRAINT admin_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.admin DROP CONSTRAINT admin_pkey;
       public            postgres    false    201            �
           2606    16754    author_book author_book_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.author_book
    ADD CONSTRAINT author_book_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.author_book DROP CONSTRAINT author_book_pkey;
       public            postgres    false    203            �
           2606    16749    author author_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.author DROP CONSTRAINT author_pkey;
       public            postgres    false    202                        2606    16770    book_detail book_detail_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.book_detail
    ADD CONSTRAINT book_detail_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.book_detail DROP CONSTRAINT book_detail_pkey;
       public            postgres    false    205            �
           2606    16762    book book_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.book DROP CONSTRAINT book_pkey;
       public            postgres    false    204                       2606    16783     category_book category_book_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.category_book
    ADD CONSTRAINT category_book_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.category_book DROP CONSTRAINT category_book_pkey;
       public            postgres    false    207                       2606    16778    category category_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.category DROP CONSTRAINT category_pkey;
       public            postgres    false    206                       2606    16799    group_admin group_admin_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.group_admin
    ADD CONSTRAINT group_admin_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.group_admin DROP CONSTRAINT group_admin_pkey;
       public            postgres    false    209                       2606    16807 &   group_permission group_permission_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.group_permission
    ADD CONSTRAINT group_permission_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.group_permission DROP CONSTRAINT group_permission_pkey;
       public            postgres    false    210                       2606    16791    group group_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public."group"
    ADD CONSTRAINT group_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public."group" DROP CONSTRAINT group_pkey;
       public            postgres    false    208                       2606    16823 &   permission_admin permission_admin_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.permission_admin
    ADD CONSTRAINT permission_admin_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.permission_admin DROP CONSTRAINT permission_admin_pkey;
       public            postgres    false    212                       2606    16887 )   permission permission_permission_code_key 
   CONSTRAINT     o   ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_permission_code_key UNIQUE (permission_code);
 S   ALTER TABLE ONLY public.permission DROP CONSTRAINT permission_permission_code_key;
       public            postgres    false    211                       2606    16815    permission permission_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.permission DROP CONSTRAINT permission_pkey;
       public            postgres    false    211                       2606    16831    publisher publisher_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.publisher DROP CONSTRAINT publisher_pkey;
       public            postgres    false    213                       2606    16839    reader reader_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.reader
    ADD CONSTRAINT reader_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.reader DROP CONSTRAINT reader_pkey;
       public            postgres    false    214                        2606    16847    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public            postgres    false    215            "           2606    16855 *   status_book_detail status_book_detail_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.status_book_detail
    ADD CONSTRAINT status_book_detail_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.status_book_detail DROP CONSTRAINT status_book_detail_pkey;
       public            postgres    false    216            *           2606    16871 &   storage_category storage_category_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.storage_category
    ADD CONSTRAINT storage_category_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.storage_category DROP CONSTRAINT storage_category_pkey;
       public            postgres    false    218            &           2606    16863    storage storage_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.storage
    ADD CONSTRAINT storage_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.storage DROP CONSTRAINT storage_pkey;
       public            postgres    false    217            
           2606    16885 "   group uk_a5qy1vaxou1t8cte83owcsusb 
   CONSTRAINT     e   ALTER TABLE ONLY public."group"
    ADD CONSTRAINT uk_a5qy1vaxou1t8cte83owcsusb UNIQUE (group_code);
 N   ALTER TABLE ONLY public."group" DROP CONSTRAINT uk_a5qy1vaxou1t8cte83owcsusb;
       public            postgres    false    208            (           2606    16897 $   storage uk_d9nl3bb5im221ugcwdyrvqee3 
   CONSTRAINT     g   ALTER TABLE ONLY public.storage
    ADD CONSTRAINT uk_d9nl3bb5im221ugcwdyrvqee3 UNIQUE (storage_code);
 N   ALTER TABLE ONLY public.storage DROP CONSTRAINT uk_d9nl3bb5im221ugcwdyrvqee3;
       public            postgres    false    217            �
           2606    25968 !   book uk_ehpdfjpu1jm3hijhj4mm0hx9h 
   CONSTRAINT     \   ALTER TABLE ONLY public.book
    ADD CONSTRAINT uk_ehpdfjpu1jm3hijhj4mm0hx9h UNIQUE (isbn);
 K   ALTER TABLE ONLY public.book DROP CONSTRAINT uk_ehpdfjpu1jm3hijhj4mm0hx9h;
       public            postgres    false    204                       2606    16889 %   publisher uk_gy9o4l39mhc0jtbs2omvy96w 
   CONSTRAINT     j   ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT uk_gy9o4l39mhc0jtbs2omvy96w UNIQUE (publisher_code);
 O   ALTER TABLE ONLY public.publisher DROP CONSTRAINT uk_gy9o4l39mhc0jtbs2omvy96w;
       public            postgres    false    213            $           2606    16895 /   status_book_detail uk_ile1gb0dbk4jty4nv9nbk1cba 
   CONSTRAINT     v   ALTER TABLE ONLY public.status_book_detail
    ADD CONSTRAINT uk_ile1gb0dbk4jty4nv9nbk1cba UNIQUE (status_book_code);
 Y   ALTER TABLE ONLY public.status_book_detail DROP CONSTRAINT uk_ile1gb0dbk4jty4nv9nbk1cba;
       public            postgres    false    216                       2606    16891 #   reader uk_jxmq31v04a0eah4k5i38d2gxb 
   CONSTRAINT     _   ALTER TABLE ONLY public.reader
    ADD CONSTRAINT uk_jxmq31v04a0eah4k5i38d2gxb UNIQUE (email);
 M   ALTER TABLE ONLY public.reader DROP CONSTRAINT uk_jxmq31v04a0eah4k5i38d2gxb;
       public            postgres    false    214                       2606    16883 %   category uk_oglj99tro9covt7h4fli0k969 
   CONSTRAINT     i   ALTER TABLE ONLY public.category
    ADD CONSTRAINT uk_oglj99tro9covt7h4fli0k969 UNIQUE (category_code);
 O   ALTER TABLE ONLY public.category DROP CONSTRAINT uk_oglj99tro9covt7h4fli0k969;
       public            postgres    false    206                       2606    16893 #   reader uk_p1i52g1v6swr02kfjnfe4rf37 
   CONSTRAINT     k   ALTER TABLE ONLY public.reader
    ADD CONSTRAINT uk_p1i52g1v6swr02kfjnfe4rf37 UNIQUE (identity_document);
 M   ALTER TABLE ONLY public.reader DROP CONSTRAINT uk_p1i52g1v6swr02kfjnfe4rf37;
       public            postgres    false    214            ,           2606    16899 -   storage_category uk_sukgrtsj3pfg8uc17wwv5wv4t 
   CONSTRAINT     y   ALTER TABLE ONLY public.storage_category
    ADD CONSTRAINT uk_sukgrtsj3pfg8uc17wwv5wv4t UNIQUE (storage_category_code);
 W   ALTER TABLE ONLY public.storage_category DROP CONSTRAINT uk_sukgrtsj3pfg8uc17wwv5wv4t;
       public            postgres    false    218            �      x����j�@�ϫ�������V��MS|H|�(�j��R�R�z,�
ŗSJ{h|T�{�M:Z9N�����a>~3��%0��ä�[V��!ɧ���)���F���Ѧ��A�$��� bj8��mjA��x0:K�����Y��T���R���Q/���&
 �{�-%��ȩG۰-��s�L,�VN�n������r�R�_��n�*���5�	�	����>��ݭ~�ɴ��;��K�M����e�>�����wM�&N��x��A>�,�F鿭/�������>R����`�:�,CtPq��TF�;����J��������/�N����K��C>�F����p�lڝMެ8ۂ���&�6LJ���z���Y/�U����u���T�_׋ϱ`��-`Tޫ�v��ש���M $Ƌ^)����׺�(�;�H7w�réŰA��A����+I�M�Ht�WQQ��{���c�.U�@9�ܘ�x9n�������ɾH�$�E�      �   8   x�3�����s��4200�50"+0���*읙��������\1z\\\ [Ku      �      x�3�����s��44����� %�u      �   e   x�3�4�4200�50"+0�v�a��q�pB`��-�
%w-�TH��S�=�"_!/�� �KW�8�9��/��C<�<�=�9�b���� x�!�      �      x������ � �      �   R   x�3��M�KO��u�sw�4200�50"+0�*f����\F�%�w7�*�d�V>ܵ��3��54>�#4�5��b���� �2"�      �   &   x�3�44��u�sw�21C<]C�C<B#]C�b���� sJ�      �      x������ � �      �      x������ � �      �      x������ � �      �   �   x�u�1�0���+���]�K���"ƥ8	��CE,���-�����}L���`�ĩ`��d��m�[`dԘ4�
)S�!��90����ki� M��e@(�v	*�$E������~���<�}���gv�~���UiŞ�R���3�      �   �   x�]��NCAD�ݏA����K��&4�
)�E$��G��g��� ������p��EEe;������6��/o���0h��˽ݮ�����0��*������?.�v7�z�� u-Q��C�~K
�вi����}_g:'hz�O��QSI�"c$g�v�ؿnWY,�l��^R�̒�����Ks      �   U   x�3�4200�50"+0�*杙�pd��ݓ��9�=}�]���9��؀ˈh�B2�2<oN��p����wD2(F��� -�"�      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �     