--user 
Insert into users(nomuser , prenomuser , mail , nee , pwd)
    values('Ains' , 'Andy' , 'andyrakotonavalona@gmail.com','2005-05-27','cloud') ;
Insert into users(nomuser , prenomuser , mail , nee , pwd)
    values('ITU' , 'developer' , 'developerrakotonavalona@gmail.com','2005-05-27','cloud') ;

-- models 
Insert into marque(nommarque) values 
    ('FERRARI') , ('PORSCHE'),('MERCEDES'),('BMW'),('FORD') ;
-- categories
Insert into categorie(nomcategorie) values 
    ('FAMILLIALE') , ('COURSE') , ('CITADINS') , ('OFF ROAD') ; 
-- carburant
Insert into carburant(nomcarburant) values
    ('Diesel') , ('ESSENCE') , ('HYBRIDE');
-- models
Insert into models(nommodel , idmarque , transmission , anneefab , vitesse , idcarburant)
    values('Ferrari furieux' , 1 , 1 , 2015 , 320 , 1 ) ;

Insert into models(nommodel , idmarque , transmission , anneefab , vitesse , idcarburant)
    values('BMW IRIS' , 4 , 1 , 2015 , 320 , 1 ) ;

Insert into models(nommodel , idmarque , transmission , anneefab , vitesse , idcarburant)
    values('FORD RAPTORS' , 5 , 1 , 2015 , 320 , 1 ) ;
-- models_categorie
insert into modelcategorie (idmodel , idcategorie) values
    (1 , 1) , (1,2) , (2,1),(2,2),(2,3) , (3,1),(3,2),(3,3),(3,4)  ;
-- voitureinfo
    Insert into voitureinfo (nomvoiture , nombreplace , kilometrage ,  iduser ,  idmodel)
        values('BMW IRIS 404' , 7 , 145000 , 1 , 2) ;

    Insert into voitureinfo (nomvoiture , nombreplace , kilometrage ,  iduser ,  idmodel)
        values('FERRARI 101' , 4 , 145000 , 1 , 1) ;
        
    Insert into voitureinfo (nomvoiture , nombreplace , kilometrage ,  iduser ,  idmodel)
        values('FORD RAPTOR 101' , 2 , 145000 , 1 , 3) ;
    
    Insert into voitureinfo (nomvoiture , nombreplace , kilometrage ,  iduser ,  idmodel)
        values('FERRARI EXTREME' , 4 , 145000 , 1 , 1) ;


-- lieu
Insert into Lieu (nomlieu)
    values ('MAHAJANGA') , ('ANTANANARIVO') , ('DIEGO') , ('TULEAR') , ('TOAMASINA'),('FINARANTSOA');

-- annonce
Insert into annonce(prixvente , descriptions , statusvente , etat , idlieu , idvoitureinfo , dateannonce)
    values(5000000 , 'tsara b io' , 0 , 10 , 1 , 1 , now()) ;

Insert into annonce(prixvente , descriptions , statusvente , etat , idlieu , idvoitureinfo , dateannonce)
    values(5000000 , 'tsara b io' , 0 , 10 , 1 , 2 , now()) ;

Insert into annonce(prixvente , descriptions , statusvente , etat , idlieu , idvoitureinfo , dateannonce)
    values(5000000 , 'tsara b io' , 0 , 10 , 1 , 3 , now()) ;

Insert into annonce(prixvente , descriptions , statusvente , etat , idlieu , idvoitureinfo , dateannonce)
    values(5000000 , 'tsara b io' , 0 , 10 , 1 , 4 , now()) ;

-- vendu

Insert into vendu( datevente , datemodifstatus , idannonce )
    values('2024-01-25' , '2024-01-25',1);

Insert into vendu( datevente , datemodifstatus , idannonce )
    values('2024-01-25' , '2024-01-25',2);

Insert into vendu( datevente , datemodifstatus , idannonce )
    values('2024-01-25' , '2024-01-25',3);

Insert into vendu( datevente , datemodifstatus , idannonce )
    values('2024-01-25' , '2024-01-25',4);

-- solde site 
    Insert into soldesite(solde,dateupdate) values(9000 , current_date);
-- sole user
    Insert into soldeuser(solde,dateupdate,iduser)
        values(5000000 , current_date , 2) ;
-- motif
    Insert into motif (nommotif , codemotif) values('commission' , 'C000');
-- debitersoldeuser
insert into debitersoldeuser(montantd , dated , idmotif , idsoldeuser) values (8000 , '2024-01-20',1,1);
insert into debitersoldeuser(montantd , dated , idmotif , idsoldeuser) values (1000 , '2023-01-20',1,1);
-- creditersoldersite
    Insert into creditersoldesite(montantc , datec , iddebit , idmotif , Idsoldesite) 
        values(8000,'2024-01-20',1,1,1);
    Insert into creditersoldesite(montantc , datec , iddebit , idmotif , Idsoldesite) 
        values(1000,'2023-01-20',2,1,1);

-- view count users 
    CREATE OR REPLACE VIEW V_count_user as 
        SELECT count(*) as nombre_users FROM users ; 
-- view count voiture
    CREATE OR REPLACE VIEW V_count_voiture as 
        SELECT count(*) as nombre_voiture FROM annonce 
            WHERE  Extract(year from dateannonce) = '2024' ;
-- view somme_prix_site 
    select sum(montantc) as montant_site 
        from creditersoldesite where 
            extract(year from datec) = '2024';

-- view marque le plus vendu
    CREATE OR REPLACE VIEW v_annonce_voiture as 
        SELECT annonce.idannonce ,  voitureinfo.* FROM annonce JOIN voitureinfo 
            ON (annonce.idvoitureinfo = voitureinfo.idvoitureinfo) WHERE annonce.statusvente = 0 ;

    CREATE OR REPLACE VIEW v_voiture_vendu as
        SELECT v_annonce_voiture.* , vendu.datevente FROM v_annonce_voiture 
            JOIN vendu ON (v_annonce_voiture.idannonce = vendu.idannonce) ;

    CREATE OR REPLACE VIEW v_models_vendu as
        SELECT v_voiture_vendu.idannonce,models.*,v_voiture_vendu.datevente FROM v_voiture_vendu JOIN 
            models ON (v_voiture_vendu.idmodel = models.idmodel) ;

    CREATE or REPLACE VIEW v_marque_default as
        SELECT marque.idmarque,0 as vendue FROM marque ;

    -- final statistique
        SELECT idmarque , max(vendue) as vendu FROM (select * from v_marque_default UNION 
        SELECT  marque.idmarque , count(idannonce) AS vendue FROM marque  left join v_models_vendu ON 
            (marque.idmarque = v_models_vendu.idmarque) WHERE Extract( year from datevente) = '2024' GROUP BY marque.idmarque ) as union_result group by idmarque;

-- view province le plus de nombre de vente 
    CREATE or REPLACE VIEW v_lieu_default as
        SELECT lieu.idlieu,0 as vendue FROM lieu ;

    CREATE OR REPLACE VIEW V_annonce_vendu_lieu as  
        SELECT vendu.* , annonce.idlieu FROM annonce  join vendu on (annonce.idannonce = vendu.idannonce) ;

    -- final statistique
    SELECT idlieu , max(vendue) as vendue FROM (
        SELECT * FROM v_lieu_default UNION
        SELECT lieu.idlieu , count(idannonce) AS vendue FROM lieu  LEFT JOIN V_annonce_vendu_lieu 
            ON (lieu.idlieu = V_annonce_vendu_lieu.idlieu) WHERE 
                Extract(YEAR FROM v_annonce_vendu_lieu.datevente) = '2024'  GROUP BY lieu.idlieu 
    ) as union_lieu_result group by idlieu;




CREATE TABLE models(
   idmodel SERIAL,
   nommodel VARCHAR(50) ,
   idmarque int REFERENCES marque (idmarque),
   transmission int REFERENCES transmission(idtransmission),
   anneefab int check(anneefab>0),
   vitesse DOUBLE PRECISION check (vitesse>0),
   idcarburant int REFERENCES carburant (idcarburant),
   PRIMARY KEY(idmodel)
);